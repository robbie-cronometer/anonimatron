package com.rolfje.anonimatron.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private String name;
    private Integer fetchSize;
    private List<Column> columns;
    private List<Discriminator> discriminators;
    private boolean whiteListEmails;
    private String whitelistColumnName;
    private String whichWhitelist;

    // Used for progress monitoring
    private long numberOfRows;


    public String getWhitelistColumnName() {
        return this.whitelistColumnName;
    }

    public boolean hasWhitelist() {
        return whitelistColumnName != null && !whitelistColumnName.isEmpty();
    }

    public void setWhitelistColumnName(String whitelistColumnName) {
        this.whitelistColumnName = whitelistColumnName;
    }

    public String getWhichWhitelist() {
        return this.whichWhitelist;
    }

    public void setWhichWhitelist(String whichWhitelist) {
        this.whichWhitelist = whichWhitelist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(Integer fetchSize) {
        this.fetchSize = fetchSize;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public static Map<String, Column> getColumnsAsMap(List<Column> columns) {
        Map<String, Column> columnMap = new HashMap<>();
		if (columns != null) {
			for (Column column : columns) {
				columnMap.put(column.getName(), column);
			}
		}
        return columnMap;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Discriminator> getDiscriminators() {
        return discriminators;
    }

    public void setDiscriminators(List<Discriminator> discriminators) {
        this.discriminators = discriminators;
    }

    public long getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(long numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public boolean getWhiteListEmails() {
        return this.whiteListEmails;
    }

    public void setWhiteListEmails(boolean whiteListEmails) {
        this.whiteListEmails = whiteListEmails;
    }



}
