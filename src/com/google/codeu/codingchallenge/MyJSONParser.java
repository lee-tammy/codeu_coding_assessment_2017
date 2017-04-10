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

final class MyJSONParser implements JSONParser {
  HashMap<String, MyJSON> map = new HashMap<String, MyJSON>();
  private final String INVALID_START = "The JSON string should not begin that" 
      + " way.";
  private final String STRING_DNE = "The first string does not exist.";

  @Override
  public JSON parse(String in) throws IOException {
    String str = in.trim();
    String key = "";
    MyJSON value = new MyJSON();
    int i = 0; 
    while(i < str.length()){
      if(i == 0){
        char firstChar = validFirst(str.charAt(i));
        if(firstChar == ','){
          throw new IOException(INVALID_START);
        }
        int stringLen = matchClosing(str, firstChar, i);
        if(stringLen < 0){
          throw new IOException(STRING_DNE);
        }
        key += str.substring(0, stringLen);
        i = stringLen;
      }else if(i == str.length() - 1){
      }else{
        
      }
      i++;
    }
    
    return new MyJSON();
  }

  private char validFirst(char c){
    if(c == '\"' || c == '{'){
      return c;
    }else{
      return ','; // dummy char that denotes invalid starting character
    }
  }

  private int matchClosing(String str, char firstChar, int index){
    int i = index + 1;
    while(i < str.length()){
      if(str.charAt(i) == firstChar){
        return i;
      }
    }
    return -1;
  }

}
