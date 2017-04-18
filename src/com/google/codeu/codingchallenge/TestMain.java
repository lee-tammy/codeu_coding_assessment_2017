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
import java.util.Collection;
import java.util.HashSet;

final class TestMain {

  // testing purposes
  public static void main(String[] args) {

    final Tester tests = new Tester();

    tests.add("Empty Object", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {
        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{ }");

        final Collection<String> strings = new HashSet<>();
        obj.getStrings(strings);

        Asserts.isEqual(strings.size(), 0);

        final Collection<String> objects = new HashSet<>();
        obj.getObjects(objects);

        Asserts.isEqual(objects.size(), 0);
      }
    });

    tests.add("String Value", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {
        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{ \"name\":\"sam doe\" }");

        Asserts.isEqual("sam doe", obj.getString("name"));
     }
    });

    tests.add("String value with escape characters", new Test(){
      public void run(JSONFactory factory) throws Exception{
        final JSONParser parser1 = factory.parser();
        final JSON obj1 = parser1.parse("{\"name\":\"sa\\nm\\tdoe\"}");

        Asserts.isEqual("sa\\nm\\tdoe", obj1.getString("name"));      
  
        try{
          final JSON obj = parser.parse("{\"\\g\":\"sam\"}");
        }catch(IOException e){
          //normal 
        }       
       }
    }); 

    tests.add("Multiple String keys and String values", new Test(){
      public void run(JSONFactory factory) throws Exception{
        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{\"first\":\"sam\",\"last\":\"doe\"}");

        final Collection<String> strings = new HashSet<>();
        obj.getStrings(strings);

        Asserts.isEqual(strings.size(), 2);
        Asserts.isTrue(!strings.contains("sam"), "should not contain sam");
        Asserts.isTrue(!strings.contains("doe"), "should not contain doe");
        Asserts.isTrue(strings.contains("last"), "should contain" + " last"); 
      }
    });

    tests.add("Object Value", new Test() {
      @Override
      public void run(JSONFactory factory) throws Exception {

        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{ \"name\":{\"first\":\"sam\", \"last\""
            + ":\"doe\" } }");
        final JSON nameObj = obj.getObject("name");

        Asserts.isNotNull(nameObj);
        Asserts.isEqual("sam", nameObj.getString("first"));
        Asserts.isEqual("doe", nameObj.getString("last"));
      }
    });
    
    tests.add("Nested Obj", new Test(){
      public void run(JSONFactory factory) throws Exception{
        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{\"school\":{\"name\":{\"first\":\"sam"
            +"\"}}}");
        final JSON nameObj1 = obj.getObject("school");
        final JSON nameObj2 = nameObj1.getObject("name");
        
        Asserts.isNotNull(nameObj1);      
        Asserts.isNotNull(nameObj2);
        Asserts.isEqual("sam", nameObj2.getString("first"));
        
      }
    });

    tests.add("Excess space", new Test(){
      public void run(JSONFactory factory) throws Exception{
        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{\" first   \": \"sa m\", \" last" + 
            " \":\"doe\"}");
              
        Asserts.isEqual("sa m", obj.getString(" first   "));
        Asserts.isEqual("doe", obj.getString(" last "));
      }
    }); 

    tests.add("Random mix of objects and strings", new Test(){
      public void run(JSONFactory factory) throws Exception{
        final JSONParser parser = factory.parser();
        final JSON obj = parser.parse("{\"office\":{\"first\":\"sam\"," +
            "\"last\":\"doe\"},\"school\":{\"student\":{\"first\":" + 
            "\"john\", \"last\":\"apple\" }}, \"friend\":\"lily\\nlin\"}");

        final Collection<String> objects = new HashSet<>();
        obj.getObjects(objects);
        Asserts.isEqual(objects.size(), 2);

        final JSON nameObj0 = obj.getObject("office");
        final JSON nameObj1 = obj.getObject("school");
        final JSON nameObj2 = nameObj1.getObject("student");

        Asserts.isNotNull(nameObj0);
        Asserts.isNotNull(nameObj1);
        Asserts.isNotNull(nameObj2);
       
        Asserts.isEqual("sam", nameObj0.getString("first"));
        Asserts.isEqual("doe", nameObj0.getString("last"));


        Asserts.isEqual("john", nameObj2.getString("first"));
        Asserts.isEqual("apple", nameObj2.getString("last"));   
      
        Asserts.isEqual("lily\\nlin", obj.getString("friend"));           

      }
    });
    

    tests.run(new JSONFactory(){
      @Override
      public JSONParser parser() {
        return new MyJSONParser();
      }

      @Override
      public JSON object() {
        return new MyJSON();
      }
    });
  }
}
