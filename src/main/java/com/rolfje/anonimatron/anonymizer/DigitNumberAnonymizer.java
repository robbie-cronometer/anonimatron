package com.rolfje.anonimatron.anonymizer;

import com.rolfje.anonimatron.synonyms.*;
import org.apache.log4j.Logger;

import java.util.*;

import static org.apache.log4j.Logger.getLogger;

public class DigitNumberAnonymizer implements Anonymizer {

   private final        Random random = new Random();

   public static final String TYPE = "RANDOMNUMBER";

   @Override
   public String getType() {
      return TYPE;
   }

   @Override
   public Synonym anonymize(Object from, int size, boolean shortlived, Map<String, String> parameters) {
      return anonymize(from, size, shortlived);
   }

   @Override
   public Synonym anonymize(Object from, int size, boolean shortlived) {

      if (from == null) {
         return new NumberSynonym(
               getType(),
               null,
               null,
               shortlived
         );
      }

      return new NumberSynonym(
            getType(),
            (Number) from,
            random.nextInt(Integer.MAX_VALUE - 1),
            shortlived
      );
   }


}
