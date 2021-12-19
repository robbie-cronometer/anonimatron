package com.rolfje.anonimatron.configuration;

import com.rolfje.anonimatron.anonymizer.AnonymizerService;
import com.rolfje.anonimatron.anonymizer.CharacterStringAnonymizer;
import com.rolfje.anonimatron.anonymizer.StringAnonymizer;
import com.rolfje.anonimatron.file.CsvFileReader;
import com.rolfje.anonimatron.file.CsvFileWriter;
import org.apache.log4j.Logger;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Whitelist {
	
	private String name;
	private List<String> values;

	private HashSet<String> valuesSet;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public boolean isWhitelisted(Object object) {
		if (valuesSet == null) {
			valuesSet = new HashSet<>();
			valuesSet.addAll(values);
		}
		return valuesSet.contains(object.toString());
	}


}