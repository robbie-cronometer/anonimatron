package com.rolfje.anonimatron.anonymizer;

import com.rolfje.anonimatron.synonyms.Synonym;
import org.json.*;
import org.junit.*;

import java.util.*;

public class JsonAnonymizerTest
{

   String testJson = "{\n" + "   \"potato1\":\"Potato\",\n" + "   \"tomato1\":\"Tomato\",\n" + "   \"potato2\":\"Potato\",\n"
         + "   \"numbertest\": 123,\n" + "   \"numbertest2\": 456,\n" + "    \"numbertest1copy\": 123,\n" + "   \"wholeObject\" : {\n"
         + "       \"innerpotato\": Potato,\n" + "       \"innerList\": [\"ham\", 123, {\"innerObjectArray\": [99]}]\n" + "    },\n"
         + "  \"array\" : [\"hello\", [\"I'm nested\"]],\n" + "  \"whitelistme\" : \"preserveme\",\n"
         + "  \"alsowhitelisted\" : 69420\n" + "}";

   @Test
   public void testAnonymisation() {
      JSONObject in = new JSONObject(testJson);
      MultiFieldAnonymiser anonymiser = new JsonAnonymiser();
      DigitNumberAnonymizer da = new DigitNumberAnonymizer();
      StringAnonymizer sa = new StringAnonymizer();
      Map<String, Anonymizer> anonymizerMap = new HashMap<>();
      anonymizerMap.put(da.getType(), da);
      anonymizerMap.put(sa.getType(), sa);
      SynonymCache sc = new SynonymCache();

      Map<String, String> parameters = new HashMap<>();
      parameters.put("whitelist", "whitelistme,alsowhitelisted");
      Synonym synonym = anonymiser.anonymize(anonymizerMap, sc, testJson, Integer.MAX_VALUE, false, parameters);
      System.out.println(synonym.getTo().toString());
      JSONObject out = new JSONObject((String) synonym.getTo());
      for (String key: in.keySet()) {
         Assert.assertTrue(out.has(key));
      }
      for (String key: out.keySet()) {
         Assert.assertTrue(in.has(key));
      }

      // checkwhitelist values
      Assert.assertEquals(in.getString("whitelistme"), out.getString("whitelistme"));
      Assert.assertEquals(in.getNumber("alsowhitelisted"), out.getNumber("alsowhitelisted"));

      Assert.assertTrue(out.get("potato1") instanceof String);
      Assert.assertFalse(((String) out.get("potato1")).isEmpty());
      Assert.assertFalse(((String) out.get("tomato1")).isEmpty());
      Assert.assertEquals(out.get("potato1"), out.get("potato2"));
      Assert.assertNotEquals(out.get("potato1"), out.get("tomato1"));

      Assert.assertNotEquals(in.get("potato1"), out.get("potato1"));
      Assert.assertNotEquals(in.get("potato2"), out.get("potato2"));
      Assert.assertNotEquals(in.get("tomato1"), out.get("tomato1"));

      Assert.assertTrue(out.get("numbertest") instanceof Number);
      Assert.assertTrue(out.get("numbertest1copy") instanceof Number);
      Assert.assertTrue(out.get("numbertest2") instanceof Number);
      Assert.assertEquals(out.get("numbertest"), out.get("numbertest1copy"));
      Assert.assertNotEquals(out.get("numbertest"), out.get("numbertest2"));

      Assert.assertNotEquals(in.get("numbertest"), out.get("numbertest"));
      Assert.assertNotEquals(in.get("numbertest1copy"), out.get("numbertest1copy"));
      Assert.assertNotEquals(in.get("numbertest2"), out.get("numbertest2"));

      Assert.assertTrue(out.get("array") instanceof JSONArray);
      Assert.assertTrue(((JSONArray) out.get("array")).length() == 2);
      Assert.assertNotEquals(((JSONArray) in.get("array")).getString(0), ((JSONArray) out.get("array")).getString(0));
      Assert.assertTrue(((JSONArray) out.get("array")).getJSONArray(1).length() == 1);
      Assert.assertNotEquals(((JSONArray) in.get("array")).getJSONArray(1).getString(0), ((JSONArray) out.get("array")).getJSONArray(1).getString(0));
      Assert.assertTrue(((JSONArray) out.get("array")).getJSONArray(1).length() == 1);

      Assert.assertNotEquals(((JSONObject) in.get("wholeObject")).getJSONArray("innerList").getString(0),
            ((JSONObject) out.get("wholeObject")).getJSONArray("innerList").getString(0));

      Assert.assertNotEquals(((JSONObject) in.get("wholeObject")).getJSONArray("innerList").getNumber(1),
            ((JSONObject) out.get("wholeObject")).getJSONArray("innerList").getNumber(1));

      Assert.assertEquals(((JSONObject) out.get("wholeObject"))
                  .getString("innerpotato")
                  , out.getString("potato1"));

      Assert.assertEquals(((JSONObject) out.get("wholeObject"))
                  .getJSONArray("innerList")
                  .getJSONObject(2)
                  .getJSONArray("innerObjectArray").length(),
            1);

      Assert.assertNotNull(((JSONObject) out.get("wholeObject"))
                  .getJSONArray("innerList")
                  .getJSONObject(2)
                  .getJSONArray("innerObjectArray").get(0));

      Assert.assertNotEquals(((JSONObject) in.get("wholeObject"))
                  .getJSONArray("innerList")
                  .getJSONObject(2)
                  .getJSONArray("innerObjectArray").getNumber(0),
            ((JSONObject) out.get("wholeObject"))
                  .getJSONArray("innerList")
                  .getJSONObject(2)
                  .getJSONArray("innerObjectArray").getNumber(0));

   }



}
