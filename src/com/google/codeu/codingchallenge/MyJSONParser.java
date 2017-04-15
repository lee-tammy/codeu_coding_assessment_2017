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
  private MyJSON json = new MyJSON();

  @Override
  public JSON parse(String in) throws IOException{
    in = in.trim(); 
    putInHash(in); 
    // Check if the parentheses and quotes are balanced
    /*if(!isBalanced(in)){
      return false;//throw new IOException("Brackets /or quotes are not balanced");
    }*/
    
    // Check if it matches pattern for JSON
    
    // Group together
    return json;
  }

  private void putInHash(String in){
    String emptyPat = "\\{?\"?[ ]*\"?\\}?";
    String strstrPat = "\"[ a-zA-Z0-9]+\":\"[ a-zA-Z0-9]+\"";

    Pattern pattern1 = Pattern.compile(strstrPat);
    Pattern pattern0 = Pattern.compile(emptyPat);

    if(pattern0.matcher(in).matches()){
      System.out.println("hi");
    }else if(pattern1.matcher(in).matches()){

      //put in hash value
      String compressed = in.replaceAll(" ", "");
      System.out.println(compressed);
      if(compressed.length() > 2){
        in = in.replaceAll("\\\"",""); 
        String values[] = in.split(":");
        json.setString(values[0], values[1]);
      }

    }else if(in.charAt(0) == '{' && in.charAt(in.length() - 1) == '}'){
      String inside = in.substring(in.indexOf('{') + 1, in.length() - 1);
      inside = inside.trim();
      String[] nestedObj = inside.split(",");
      if(nestedObj.length >= 1){
        for(int i = 0; i < nestedObj.length; i++){
          putInHash(nestedObj[i]);
        }
      }else{
      }

    }
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
