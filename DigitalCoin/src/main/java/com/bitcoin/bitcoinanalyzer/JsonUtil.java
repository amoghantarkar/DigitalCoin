/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitcoin.bitcoinanalyzer;
import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;

/**
 *
 * @author amoghantarkar
 */
public class JsonUtil {

    
    
    
    public static Map<String,Object> parseJson(final String jsonString){
        JsonReader jsonReader = Json.createReader( new ByteArrayInputStream(jsonString.getBytes()));
        final JsonStructure js = jsonReader.read();
        
        jsonReader = Json.createReader(new ByteArrayInputStream(jsonString.getBytes()));
        
        return js.getValueType() == JsonValue.ValueType.ARRAY ? parseJson(jsonReader.readArray(),0):parseJson(jsonReader.readObject(),0);
    }
    
    public static Map<String,Object> parseJson(final JsonArray jsonArray, int counter){
        JsonObject jsonChildArray;
        String jsonString;
        JsonReader jsonReader;
        JsonObject tempObject;
        Map<String,Object> arrayMap = new LinkedHashMap<>();
        final Map<String,Object> map = new LinkedHashMap<>();
        
        for(int i = 0, size = jsonArray.size(); i<size;i++){
        
            if(jsonArray.get(i).getClass().getSimpleName().equals("JsonStringImpl")){
                continue;
            }
            jsonChildArray = jsonArray.getJsonObject(i);
            for(Map.Entry<String,JsonValue> childEntry : jsonChildArray.entrySet()){
                if (childEntry.getValue().getValueType() == JsonValue.ValueType.ARRAY) {
                    jsonString = childEntry.getValue().toString();
                    jsonReader = Json.createReader(new ByteArrayInputStream(jsonString.getBytes()));

                    JsonArray ja = jsonReader.readArray();
                    for (int k = 0, kSize = ja.size(); k < kSize; k++) {
                        if (ja.get(k).getClass().getSimpleName().equals("JsonStringImpl")) {
                            continue;
                        }

                        try {
                            final Map<String, Object> subMap = parseJson(ja.getJsonObject(k), counter + 1);
                            arrayMap.put(childEntry.getKey() + "-" + (k + 1), subMap);
                        } catch (Exception ex) {
                            System.out.println("");
                        }
                    }

                } else if(childEntry.getValue().getValueType() == JsonValue.ValueType.OBJECT){
                    jsonString = childEntry.getValue().toString();
                    jsonReader = Json.createReader(new ByteArrayInputStream(jsonString.getBytes()));
                    tempObject = jsonReader.readObject();
                    final Map<String,Object> subMap = parseJson(tempObject,counter+1);
                    arrayMap.put(childEntry.getKey(),subMap);
                }else{
                    arrayMap.put(childEntry.getKey(),childEntry.getValue().toString());
                }
        
            }
                map.put(""+(i+1), arrayMap);
                arrayMap = new LinkedHashMap<>();
             
    }    
           return map;
    }
    
    public static Map<String,Object> parseJson(final JsonObject jsonObject, int counter){
        final Map<String,Object> map = new LinkedHashMap<>();
        JsonArray jsonArray;
        for(Map.Entry<String,JsonValue> entry: jsonObject.entrySet()){
            if(entry.getValue().getValueType() == JsonValue.ValueType.ARRAY){
                jsonArray = jsonObject.getJsonArray(entry.getKey());
                final Map<String,Object> arrayMap = parseJson(jsonArray,counter+1);
                map.put(entry.getKey(),arrayMap);
            }else{
                if(entry.getValue().getValueType() == JsonValue.ValueType.OBJECT){
                    final Map<String,Object> subMap = parseJson((JsonObject)entry.getValue(),counter+1);
                    map.put(entry.getKey(), subMap);
                }else{
                    map.put(entry.getKey(), entry.getValue().toString());
                }
            }                        
        }
        return map;
    }
    
    public static void printJsonMap(final Map<String,Object>map,final int indent){
        Object value;
        for(Entry<String,Object> entry: map.entrySet()){
            value = entry.getValue();
            if(value instanceof Map){
                String space= "";
                for(int i = 0;i<indent;i++){
                    space+=" ";
                }
                printJsonMap((Map<String,Object>) value,indent+2);
            }else{
                String space ="";
                for(int i = 0;i<indent;i++){
                    space+=" ";
                }
            }
        }
    }
    
    public static String getKeyValue(final Map<String,Object> map, int index, final String prefix){
        String logString="";
        String key;
        Object value;
        for(Map.Entry<String,Object> entry: map.entrySet()){
            key = entry.getKey();
            value = entry.getValue();
            if(value !=null){
                if(!logString.isEmpty()){
                    logString +=",";
                }
                if(value instanceof Map){
                    logString +=getKeyValue((Map) value, index+1, index>1? key+".":"");
                }else{
                    logString +=(prefix+key+"="+value.toString());
                }
            }
            
        }
        return logString.replaceAll("\\r\\n|\\r|\\n|\\t|\\\\n|\\\\t", " ").trim();
    }
}