package com.dejans.HttpUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

import com.dejans.model.TreeMapYamlParse;

import org.springframework.web.client.RestTemplate;

import javax.json.JsonReader;
import javax.json.Json;
import javax.json.JsonObject;

public class SetPortOfMicroservice {

    private static JsonObject jsonFromString(String jsonObjectStr) {

        JsonReader jsonReader = Json.createReader(new StringReader(jsonObjectStr));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

        return object;
    }

    public static void setPort(String clientName) {
        TreeMap<String, String> model;
        String uri = new String();

        File fileName = new File("../eureka.yml");
        TreeMapYamlParse tMYP = null;
        try {
            tMYP = new TreeMapYamlParse(fileName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        model = tMYP.getTreeMap();

        String scheme = model.get(".{.eureka.{.server.{.scheme");
        String host = model.get(".{.eureka.{.server.{.host");
        String portEureka = model.get(".{.eureka.{.server.{.port");
        String pathPortSet = model.get(".{.eureka.{.server.{.path.{.portset");

        String port = new String();
        RestTemplate restTemplate = new RestTemplate();
        long pid = java.lang.ProcessHandle.current().pid();
        //long pid = 1002; 
        HttpUtils uriClass = new HttpUtils();
        try {
            uri = uriClass.setScheme(scheme).setHost(host).setPort(portEureka).setPath(pathPortSet)
                    .addParamToQuery("clientName", clientName).addParamToQuery("pid", pid).getUrl();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("URI " + uri);
        String jsonAnswer = restTemplate.getForObject(uri, String.class);
        JsonObject jobj = jsonFromString(jsonAnswer);
        int portInt = jobj.getInt("port");
        port = Integer.toString(portInt);
        System.out.println("TEXTIC");
        System.out.println(jsonAnswer);

        System.out.println("TEXTIC");

        System.setProperty("server.port", port);

    }
}