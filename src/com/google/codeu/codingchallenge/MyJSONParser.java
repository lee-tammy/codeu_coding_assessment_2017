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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

final class MyJSONParser implements JSONParser{
  //private MyJSON json = new MyJSON();

  @Override
  public JSON parse(String in) throws IOException{
    in = in.trim(); 
    //putInHash(in); 
    // Check if the parentheses and quotes are balanced
    /*if(!isBalanced(in)){
      return false;//throw new IOException("Brackets /or quotes are not balanced");
    }*/
    
    // Check if it matches pattern for JSON
    
    // Group together
    return putInHash(in);
  }

  private MyJSON putInHash(String in) throws IOException{
    MyJSON json = new MyJSON();
    String emptyPat = "\\{?\"?[ ]*\"?\\}?";
    
    //String strObjPat = "\"[ a-zA-Z0-9]+\":{[ a-zA-Z0-1]+}";
      

    Pattern pattern0 = Pattern.compile(emptyPat);
    

    if(pattern0.matcher(in).matches()){
      // Nothing happens
    }else if(stringMatch(in)){
      //System.out.println("Matched pattern1"); 
      //put in hash value
              //System.out.println("hello" + values[0] + " " + values[1]);
      String[] values = separatePairs(in);
      System.out.println("value 1: " + values[0] + " value 2: " + values[1]);
      json.setString(values[0], values[1]);

    }else if(in.charAt(0) == '{' && in.charAt(in.length() - 1) == '}'){
      String inside = in.substring(in.indexOf('{') + 1, in.length() - 1);
      inside = inside.trim();
      System.out.println(inside);
      String[] nestedObj = inside.split(",");
      if(inside.contains(",")){
        if(nestedObj.length >= 1){
          for(int i = 0; i < nestedObj.length; i++){
            System.out.println(nestedObj[i]);
            if(stringMatch(nestedObj[i])){
              String values[] = separatePairs(nestedObj[i]);
              json.setString(values[0], values[1]);
            }else if(objectMatch(nestedObj[i])){
              String values[] = separatePairs(nestedObj[i]);
              System.out.println("hello" + values[1]);
              json.setObject(values[0], putInHash(values[1]));
            }
          }
        }
      // one key and value pair
      }else{
        System.out.println("HI");
        System.out.println(inside);
        System.out.println(objectMatch(inside));
        if(stringMatch(inside)){ 
          String[] pair = separatePairs(inside);
          json.setString(pair[0], pair[1]);
        }else if(objectMatch(inside)){
          String[] pair = separatePairs(inside);
          System.out.println("pair1: " + pair[0] + " pair2: " + pair[1]);
          json.setObject(pair[0], putInHash(pair[1]));
        }
      }
    } 
    return json;
  }


  private boolean stringMatch(String in){
    String strstrPat = "\"[\\\\\" a-zA-Z0-9]+\":\"[\\\\\" a-zA-Z0-9]+\"";
    Pattern pattern1 = Pattern.compile(strstrPat);
    System.out.println(in);
    return pattern1.matcher(in).matches();
  }

  private boolean objectMatch(String in){
    String strObjPat = "\"[\\\\\" a-zA-Z0-9]+\":\\{[a-zA-Z0-9:,\\\\\" ]+\\}";
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
            nextChar != 'r' && nextChar != 'f' && nextChar != '"' && 
            nextChar != 's'){
          return false;
        }       
      }
    }
    return true;
  }

  private String removeQuotes(String in){
    in = replaceLast(in, "\"", ""); 
    in = in.replaceFirst("\"", "");
    return in;
  }

  String replaceLast(String string, String substring, String replacement){
    int index = string.lastIndexOf(substring);
    
    if (index == -1){
      return string;
    }
    return string.substring(0, index) + replacement
          + string.substring(index+substring.length());
  }
  
  /*private boolean isBalanced(String str){
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
  }*/
}
