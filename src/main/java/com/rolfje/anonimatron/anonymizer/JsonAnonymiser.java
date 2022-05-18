package com.rolfje.anonimatron.anonymizer;

import com.rolfje.anonimatron.synonyms.*;
import org.apache.log4j.Logger;
import org.json.*;

import java.util.*;

import static org.apache.log4j.Logger.getLogger;


//example


// <table name="professional" whichWhitelist="userid" whitelistColumnName="userId">
// <column name="meta" type="JSON" >
// <parameter id="whitelist">
// <value>plan,proManager</value>
// </parameter>
// </column>
// </table>


public class JsonAnonymiser implements MultiFieldAnonymiser {


   public static final String type = "JSON";

   @Override
   public String getType() {
      return type;
   }

   private static final Logger logger = getLogger(JsonAnonymiser.class);

   @Override
   public Synonym anonymize(Map<String, Anonymizer> anonymizerMap, SynonymCache synonymCache, Object from, int size, boolean shortlived,
         Map<String, String> parameters) {
      if (from != null && !(from instanceof String)) {
         logger.error("Unable to read value as json. Returning {} for value " + from);
         return new StringSynonym(StringAnonymizer.TYPE, (String) from, "{}", shortlived);
      }

      Set<String> whiteListLowerCase = new HashSet<>();
      if (parameters != null && parameters.containsKey("whitelist")) {
         String[] whitelistedFields = parameters.get("whitelist").split(",");
         for (String field : whitelistedFields) {
            whiteListLowerCase.add(field.trim());
         }
      }

      JSONObject jo;
      try {
         jo = new JSONObject((String) from);
      } catch (JSONException je) {
         logger.error("Unable to read value as json. Returning {} for value " + from);
         return new StringSynonym(StringAnonymizer.TYPE, (String) from, "{}", shortlived);
      }

      return new StringSynonym(StringAnonymizer.TYPE, (String) from,
            anonymizeJsonObject(anonymizerMap, synonymCache, jo, size, shortlived, whiteListLowerCase).toString(), shortlived);
   }

   public JSONObject anonymizeJsonObject(Map<String, Anonymizer> anonymizerMap, SynonymCache synonymCache, JSONObject fromJo, int size,
         boolean shortlived, Set<String> whiteListLowerCase) {
      if (fromJo == null) {
         return null;
      }

      try {
         JSONObject to = new JSONObject();

         Iterator<String> keys = fromJo.keys();
         while (keys.hasNext()) {
            String key = keys.next();
            Object theObj = fromJo.get(key);

            if (whiteListLowerCase.contains(key.toLowerCase())) {
               to.put(key, theObj);
               continue;
            }

            if (theObj == null || theObj == JSONObject.NULL) {
               to.putOpt(key, JSONObject.NULL);
            } else if (theObj instanceof String) {
               to.put(key, anonymizeString(anonymizerMap, synonymCache, (String) theObj, size, shortlived));
            } else if (theObj instanceof JSONObject) {
               to.put(key, anonymizeJsonObject(anonymizerMap, synonymCache, (JSONObject) theObj, size, shortlived, whiteListLowerCase));
            } else if (theObj instanceof JSONArray) {
               to.put(key, anonymizeJsonArray(anonymizerMap, synonymCache, (JSONArray) theObj, size, shortlived, whiteListLowerCase));
            } else if (theObj instanceof Number) {
               to.put(key, anonymiseNumber(anonymizerMap, synonymCache, (Number) theObj, size, shortlived));
            } else {
               logger.error("Unable to determine type of json value " + theObj);
            }

         }
         return to;

      } catch (Exception e) {
         logger.error("Exception anonymising json. Returning an empty json object for " + fromJo, e);
         // we're not failing silently, look out for these logs but the default failure condition should be to just return a valid json of some sort if we can't fully anonymise
         return new JSONObject("{}");
      }

   }

   private Number anonymiseNumber(Map<String, Anonymizer> anonymizerMap, SynonymCache synonymCache, Number value, int size, boolean shortlived) {
      Synonym synonym = synonymCache.get(DigitNumberAnonymizer.TYPE, value);
      if (synonym == null) {
         synonym = anonymizerMap.get(DigitNumberAnonymizer.TYPE).anonymize(value, size, shortlived);
         synonymCache.put(synonym);
      }
      return (Number) synonym.getTo();
   }

   private String anonymizeString(Map<String, Anonymizer> anonymizerMap, SynonymCache synonymCache, String value, int size,
         boolean shortlived) {
      Synonym synonym = synonymCache.get(StringAnonymizer.TYPE, value);
      if (synonym == null) {
         synonym = anonymizerMap.get(StringAnonymizer.TYPE).anonymize(value, size, shortlived);
         synonymCache.put(synonym);
      }
      return (String) synonym.getTo();
   }

   private JSONArray anonymizeJsonArray(Map<String, Anonymizer> anonymizerMap, SynonymCache synonymCache, JSONArray innerArray, int size,
         boolean shortlived, Set<String> whiteListLowerCase) {
      JSONArray toArray = new JSONArray();
      for (int i = 0; i < innerArray.length(); i++) {
         Object value = innerArray.get(i);
         if (value instanceof Number) {
            toArray.put(anonymiseNumber(anonymizerMap, synonymCache, (Number) value, size, shortlived));
         } else if (value instanceof String) {
            toArray.put(anonymizeString(anonymizerMap, synonymCache, (String) value, size, shortlived));
         } else if (value instanceof JSONArray) {
            toArray.put(anonymizeJsonArray(anonymizerMap, synonymCache, (JSONArray) value, size, shortlived, whiteListLowerCase));
         } else if (value instanceof JSONObject) {
            toArray.put(anonymizeJsonObject(anonymizerMap, synonymCache, (JSONObject) value, size, shortlived, whiteListLowerCase));
         } else {
            logger.error("Unable to determine synonym for value in json array " + value);
         }
      }
      return toArray;
   }
}
