package com.rolfje.anonimatron.anonymizer;

import com.rolfje.anonimatron.synonyms.Synonym;
import org.json.JSONObject;
import org.junit.Test;

import java.util.*;

public class JsonAnonymizerTest
{

   String testJson = "{\n" + "   \"potato1\":\"Potato\",\n" + "   \"tomato1\":\"Tomato\",\n" + "   \"potato2\":\"Potato\",\n"
         + "   \"numbertest\": 123,\n" + "   \"numbertest2\": 456,\n" + "    \"numbertest1copy\": 123,\n" + "   \"wholeObject\" : {\n"
         + "       \"innerpotato\": Potato,\n" + "       \"innerList\": [\"ham\", 123, {\"innerObjectArray\": [99]}]\n" + "    },\n"
         + "  \"array\" : [\"hello\", [\"I'm nested\"]]\n" + "}";

   @Test
   public void testAnonymisation() {
      JSONObject input = new JSONObject(testJson);
      MultiFieldAnonymiser anonymiser = new JsonAnonymiser();
      DigitNumberAnonymizer da = new DigitNumberAnonymizer();
      StringAnonymizer sa = new StringAnonymizer();
      Map<String, Anonymizer> anonymizerMap = new HashMap<>();
      anonymizerMap.put(da.getType(), da);
      anonymizerMap.put(sa.getType(), sa);
      SynonymCache sc = new SynonymCache();


      Synonym synonym = anonymiser.anonymize(anonymizerMap, sc, testJson, Integer.MAX_VALUE, false);
      System.out.println(synonym.getTo().toString());



   }



}
