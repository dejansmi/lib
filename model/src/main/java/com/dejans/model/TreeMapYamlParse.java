package com.dejans.model;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;

public class TreeMapYamlParse {

    private TreeMap<String, String> treeMap = new TreeMap<String, String>();
    private int arrayLevel = 0;

    public String toString() {
        String pom = new String();
        for (Map.Entry elem : treeMap.entrySet()) {
            pom += elem.getKey() + ": " + elem.getValue() + "\n";
        }

        pom += "\n\nSize: " + Integer.toString(treeMap.size());
        return pom;
    }

    private String lastObjectArray(String lKey, String lObjectArray) {

        int ind = lKey.lastIndexOf(lObjectArray);
        if (ind == 0) {
            lKey = "";
        } else if (ind == -1) {
            // not find lObjectArray so doing nothing
        } else {
            int minus = ((lObjectArray == ".") ? 0 : 1);
            lKey = lKey.substring(0, ind - minus);
        }

        return lKey;
    }

    public TreeMap<String, String> getTreeMap() {
        return treeMap;
    }

    private String valueToken(String lKey, String lField, String lValue) {
        String pomArr = new String();
        String pKey = new String();
        String lastOfKey = new String();
        int ind = lKey.indexOf("..");
        if (ind < 0) {
            pomArr = "";
            pKey = lKey;
            ind = lKey.lastIndexOf(".");
            if (ind > -1 && lKey.length() > ind) {
                lastOfKey = lKey.substring(ind + 1);
            }
        } else {
            pomArr = lKey.substring(ind);
            lKey = lKey.substring(0, ind);
            int i = Integer.parseInt(pomArr.substring(2)) + 1;
            pomArr = ".." + Integer.toString(i);
            ind = lKey.lastIndexOf(".");
            if (ind > -1 && lKey.length() > ind) {
                lastOfKey = lKey.substring(ind + 1);
            }
            pKey = lKey + pomArr;
        }

        if (lastOfKey.equals("x-domain")) {
            pKey = lastObjectArray(pKey, "{.x-domain");
            lValue = "#/Domains/" + lValue + "/microservices";
            findRefAndExtractOne(pKey, lValue);
        } else if (lastOfKey.equals("domain")) {
            pKey = lastObjectArray(pKey, "{.domain");
            lValue = "#/Domains/" + lValue;
            findRefAndExtractOne(pKey, lValue);
        } else {
            treeMap.put(pKey, lValue);
        }

        if (lField != null) {
            return lastObjectArray(lKey, ".") + pomArr;
        } else {
            return lKey + pomArr;
        }
    }

    private void findRefAndExtractOne(String key, String value) {
        String keyAddTo = "";
        if (value.length() > 0 && value.substring(0, 1).equals("#")) {
            // referencing the same file
            Map<String, String> lMap;
            String valueExt = value.substring(1).replaceAll("/", ".{.");
            keyAddTo = lastObjectArray(key, "{.$ref");
            String pomKey = "";
            lMap = new TreeMap<String, String>();
            pomKey = treeMap.ceilingKey(valueExt);
            while (pomKey != null) {
                if (pomKey.length() > valueExt.length()
                        && (pomKey.substring(0, valueExt.length() + 1).equals(valueExt + "."))
                        || pomKey.equals(valueExt)) {
                    String valueOf = new String();
                    valueOf = treeMap.get(pomKey);
                    String fullKey = keyAddTo + pomKey.substring(valueExt.length());
                    if (!treeMap.containsKey(fullKey)) {
                        lMap.put(fullKey, valueOf);
                    }
                    pomKey = treeMap.higherKey(pomKey);
                } else {
                    break;
                }
            }
            treeMap.putAll(lMap);
            treeMap.remove(key);

        }
    }

    private void findRefAndExtracts() {
        String key = "";
        String value = "";
        for (key = ""; key != null; key = treeMap.higherKey(key)) {
            int ind = key.indexOf("$ref");
            if (ind > 0) {
                // found $ref so now we should extract it
                value = treeMap.get(key);
                findRefAndExtractOne(key, value);
            }

        }
    }

    public TreeMapYamlParse(File fileName) throws IOException {
        YamlParserToTree(fileName);
    }

    public void addConfiguration(File fileName) throws IOException {
        YamlParserToTree(fileName);
    }

    private void YamlParserToTree(File fileName) throws IOException {

        YAMLFactory factory = new YAMLFactory();
        YAMLParser parser = factory.createParser(fileName);
        String key = new String();

        while (parser.nextToken() != null) {
            if (parser.currentToken() == JsonToken.END_ARRAY) {
                arrayLevel--;
                int ind = key.indexOf("..");
                String pomArr = new String();
                pomArr = key.substring(ind);
                key = key.substring(0, ind);
                if (arrayLevel == 0) {
                    pomArr = "";
                }

                key = lastObjectArray(key, "[");
                key = lastObjectArray(key, ".");
                key += pomArr;
            } else if (parser.currentToken() == JsonToken.END_OBJECT) {
                String pomArr = new String();
                int ind = key.indexOf("..");
                if (ind < 0) {
                    pomArr = "";
                } else {
                    pomArr = key.substring(ind);
                    key = key.substring(0, ind);

                }
                String endObject = new String();
                endObject = ((parser.getCurrentName() == null) ? "{" : parser.getCurrentName());
                key = lastObjectArray(key, endObject);
                key += pomArr;
            } else if (parser.currentToken() == JsonToken.FIELD_NAME) {
                String pomArr = new String();
                int ind = key.indexOf("..");
                if (ind < 0) {
                    pomArr = "";
                    key += "." + parser.getText() + pomArr;
                } else {
                    pomArr = key.substring(ind);
                    key = key.substring(0, ind) + "." + parser.getText() + pomArr;
                }
            } else if (parser.currentToken() == JsonToken.NOT_AVAILABLE) {
                // TODO: da li je ovde potrebno prekinuti sa greskom ili je potrebno nesto drugo uraditi
            } else if (parser.currentToken() == JsonToken.START_ARRAY) {
                arrayLevel++;
                if (arrayLevel == 1) {
                    key += "." + parser.getText() + "..0";
                } else {
                    int ind = key.indexOf("..");
                    String pomArr = new String();
                    pomArr = key.substring(ind);
                    key = key.substring(0, ind) + "." + parser.getText() + pomArr;

                }
            } else if (parser.currentToken() == JsonToken.START_OBJECT) {
                int ind = key.indexOf("..");
                if (ind < 0) {
                    key += "." + parser.getText();
                } else {
                    String pomArr = new String();
                    pomArr = key.substring(ind);
                    key = key.substring(0, ind) + "." + parser.getText() + pomArr;
                    //pomArr = ".." + Integer.toString((Integer.parseInt(pomArr.substring(2)+1)));

                }
            } else if (parser.currentToken() == JsonToken.VALUE_EMBEDDED_OBJECT) {
                key = valueToken(key, parser.getCurrentName(), parser.getText());
            } else if (parser.currentToken() == JsonToken.VALUE_FALSE) {
                key = valueToken(key, parser.getCurrentName(), parser.getText());
            } else if (parser.currentToken() == JsonToken.VALUE_NULL) {
                key = valueToken(key, parser.getCurrentName(), parser.getText());
            } else if (parser.currentToken() == JsonToken.VALUE_NUMBER_FLOAT) {
                key = valueToken(key, parser.getCurrentName(), parser.getText());
            } else if (parser.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                key = valueToken(key, parser.getCurrentName(), parser.getText());
            } else if (parser.currentToken() == JsonToken.VALUE_STRING) {
                key = valueToken(key, parser.getCurrentName(), parser.getText());
            } else if (parser.currentToken() == JsonToken.VALUE_TRUE) {
                key = valueToken(key, parser.getCurrentName(), parser.getText());
            }

        }
        findRefAndExtracts();
    }

}