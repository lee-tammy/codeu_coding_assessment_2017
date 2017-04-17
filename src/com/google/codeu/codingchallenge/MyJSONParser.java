// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.codingchallenge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.*;
import java.util.Stack;

final class MyJSONParser implements JSONParser{

  @Override
  public JSON parse(String in) throws IOException{
    String emptyPat = "\\{?\"?[ ]*\"?\\}?";
    Pattern pattern0 = Pattern.compile(emptyPat);

    MyJSON json = new MyJSON();
    in = in.trim(); 
    // Check if the parentheses and quotes are balanced
    if(!isBalanced(in)){
      throw new IOException("Parentheses/or quotes are unbalanced");
    } 
    in = removeSpaces(in);     
    if(pattern0.matcher(in).matches()){
      // Nothing happens
    }else if(stringMatch(in)){
      String[] values = separatePairs(in);
      json.setString(values[0], values[1]);

    }else if(in.charAt(0) == '{' && in.charAt(in.length() - 1) == '}'){
      String inside = in.substring(in.indexOf('{') + 1, in.length() - 1);
      inside = inside.trim();
      ArrayList<String> nestedObj = splitJSON(inside);

      // breaks case - " ":" ", " ":{"":""}
      if(validMultiPairs(inside)){
        if(nestedObj.size() >= 1){
          for(int i = 0; i < nestedObj.size(); i++){
            if(stringMatch(nestedObj.get(i))){
              String values[] = separatePairs(nestedObj.get(i));
              json.setString(values[0], values[1]);
            }else if(objectMatch(nestedObj.get(i))){
              String values[] = separatePairs(nestedObj.get(i));
              json.setObject(values[0], parse(values[1]));
            }
          }
        }
      // one key and value pair
      }else{
        if(stringMatch(inside)){ 
          String[] pair = separatePairs(inside);
          json.setString(pair[0], pair[1]);
        }else if(objectMatch(inside)){
          String[] pair = separatePairs(inside);
          json.setObject(pair[0], parse(pair[1]));
        }
      }
    } 
    return json;
  }


  private boolean stringMatch(String in){
    String strstrPat = "[ ]*\"[\\\\\" a-zA-Z0-9]+\":\"[\\\\\" a-zA-Z0-9]+\"";
    Pattern pattern1 = Pattern.compile(strstrPat);
    return pattern1.matcher(in).matches();
  }

  private boolean objectMatch(String in){
    String strObjPat="\"[\\\\\" a-zA-Z0-9]+\":\\{[\\}\\{a-zA-Z0-9:,\\\\\" ]+" + 
        "\\}";

    Pattern pattern2 = Pattern.compile(strObjPat);
    return pattern2.matcher(in).matches();  
  }

  private String[] separatePairs(String in) throws IOException{
    String compressed = in.replaceAll(" ", "");
    if(compressed.length() > 2){
      StringBuilder sb = new StringBuilder();
    
      // removes wrong quotes when there are objects
      sb.append(removeQuotes(in.substring(0, in.indexOf(":"))));
      sb.append(removeQuotes(in.substring(in.indexOf(":"))));
      if(!checkEscapeChars(in)){
        throw new IOException("Invalid escape characters");
      } 
      return sb.toString().split(":", 2);
    }
    String[] st = new String[0];
    return st;
  }

  private boolean checkEscapeChars(String in){ 
    for(int i = 0; i < in.length() - 1; i++){
      char c = in.charAt(i);
      if(c == '\\'){
        char nextChar = in.charAt(i + 1);
        if(nextChar != 't' && nextChar != 'b' && nextChar != 'n' && 
            nextChar != 'r' && nextChar != 'f' && nextChar != 's'){
          return false;
        }       
      }
    }
    return true;
  }

  private boolean validMultiPairs(String in){
    int count = 0;
    for(int i = 0; i < in.length(); i++){ 
      char c = in.charAt(i);
      if(c == '{'){
        count++;
      }else if(c == '}'){
        count--;
      }
      if(c == ',' && count == 0){
        return true;
      }
    }
    return false;
  }

  private String removeSpaces(String in){
    int quotes = 0;
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < in.length(); i++){
      char c = in.charAt(i);
      if(c == '\"'){
        quotes++;
      }
      if(c == ' ' &&  quotes % 2 == 0){
        
      }else{
        sb.append(in.charAt(i));
      }
    }
    return sb.toString();
  }

  private String removeQuotes(String in){
    StringBuilder sb = new StringBuilder();
    int inBetween = 0;
    for(int i = 0; i < in.length(); i++){
      char c = in.charAt(i);
      if(c == '{'){
        inBetween++;
      }else if(c == '}'){
        inBetween--;
      }
      if(c == '\"' && inBetween == 0){
        i++;
      }
      if(i < in.length()){
        sb.append(in.charAt(i));
      }
    }
    return sb.toString();
  }

  private ArrayList<String> splitJSON(String in){
    ArrayList<String> list = new ArrayList<String>();
    StringBuilder sb = new StringBuilder();
    int inBetween = 0;
    int index = 0;
    for(int i = 0; i < in.length(); i++){
      char c = in.charAt(i);
      if(c == '{'){
        inBetween++;
      }else if(c == '}'){
        inBetween--;
      }
      if(c == ',' && inBetween == 0){
        list.add(sb.toString());
        sb = new StringBuilder();
        index = i + 1;
      }else{
        if(i == in.length() - 1){
          list.add(in.substring(index));
        }else{
          sb.append(in.charAt(i));
        }
      }
    }
    return list;
  }

  String replaceLast(String string, String substring, String replacement){
    int index = string.lastIndexOf(substring);
    
    if (index == -1){
      return string;
    }
    return string.substring(0, index) + replacement
          + string.substring(index+substring.length());
  }
  
  private boolean isBalanced(String str){
    Stack<Character> bracketStack = new Stack<Character>();
    int quoteCount = 0;
    for(int i = 0; i < str.length(); i++){
      char c = str.charAt(i);
      if(c == '}' && bracketStack.isEmpty()){
        return false;
      }else if(!bracketStack.isEmpty() && c == '}'){
        bracketStack.pop();
      }else if(c == '{'){
        bracketStack.push('{');
      }
      if(c == '"'){
        quoteCount++;
      }
    }
    
    return bracketStack.isEmpty() && quoteCount % 2 == 0;
  }
}
