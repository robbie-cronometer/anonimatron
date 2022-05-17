package com.rolfje.anonimatron.anonymizer;

import com.rolfje.anonimatron.synonyms.Synonym;

import java.sql.Date;
import java.util.Map;

public interface MultiFieldAnonymiser {

   /**
    * @return The ID or name of this anonymizer, as used in the XML
    * configuration file. This is generally something along the lines
    * of "LASTNAME" or "UNEVENNUMBER". Please see the output of the
    * -configexample command line option when running Anonimatron.
    */
   String getType();


   /**
    * Anonymizes the given data into a non-tracable, non-reversible synonym,
    * and does it consistently, so that A always translates to B.
    *
    * @param from       the data to be anonymized, usually passed in as a
    *                   {@link String}, {@link Integer}, {@link Date} or other classes
    *                   which can be stored in a single JDBC database column.
    * @param size       the optional maximum size of the generated value
    * @param shortlived indicates that the generated synonym must have the
    *                   {@link Synonym#isShortLived()} boolean set
    * @param parameters
    * @return a {@link Synonym}
    */
   Synonym anonymize(Map<String, Anonymizer> anonymizerMap, SynonymCache synonymCache, Object from, int size, boolean shortlived,
         Map<String, String> parameters);

}
