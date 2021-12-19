package com.rolfje.anonimatron.anonymizer;

import com.rolfje.anonimatron.synonyms.StringSynonym;
import com.rolfje.anonimatron.synonyms.Synonym;

import java.util.Map;
import java.util.Random;

public class IpAnonymizer extends AbstractElevenProofAnonymizer {

    @Override
    public String getType() {
        return "IP_ADDRESS";
    }

    private static final Random r = new Random();

    @Override
    public Synonym anonymize(Object from, int size, boolean shortlived, Map<String, String> parameters) {
        return anonymize(from, size, shortlived);
    }

    @Override
    public Synonym anonymize(Object from, int size, boolean shortlived) {
        if (from == null) {
            return new StringSynonym(
                    getType(),
                    null,
                    null,
                    shortlived
            );
        }
        //The blocks 192.0.2.0/24 (TEST-NET-1)... are reserved for documentation and are unroutable, https://www.rfc-editor.org/rfc/rfc5737
        String to =  "192.0.2." + r.nextInt(256);

        return new StringSynonym(
                getType(),
                from.toString(),
                to,
                shortlived
        );
    }

}