/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitcoin.bitcoinanalyzer;
   
import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
//import com.json.parsers.JSONParser;
//import com.json.parsers.JsonParserFactory;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amoghantarkar
 */
public class Bitcoin {

 
    public static void main(String[] args) throws JSONException {
                
        Runnable runnable = new Runnable() {
            public void run() {
                Bitcoin bitcoin = new Bitcoin();
                bitcoin.run();
            }
        };

        
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnable, 0, 2, TimeUnit.MINUTES);
        
        
        
    }
    
    
    public  void run(){
        
        
        ClientResponse response;
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource("https://api.coindesk.com/v1/bpi/currentprice.json");
        response = service.get(ClientResponse.class);
        String jsonString = response.getEntity(String.class);
      
      
        
        Map<String,Object> data = JsonUtil.parseJson(jsonString);
        List<String>result = new ArrayList<>();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        LocalDate localDate = LocalDate.now();
        String dateFormat  = dtf.format(localDate);
        System.out.println("localDate:"+localDate);
        
        FileWriter fw;
    try {
            BufferedWriter bw = null;
            String finalResult = writeLogLine(data,0,"",result);

             File file = new File("/Users/amoghantarkar/Documents/bitcoin_companies/logs/"+dateFormat+"_btc_price.txt");

            if (file.exists()) {
                fw = new FileWriter(file, true);//if file exists append to file. Works fine.
            } else {
                fw = new FileWriter(file);// If file does not exist. Create it. This throws a FileNotFoundException. Why? 
            }
            // append to end of file
    //            FileWriter fw;
        String finalEvent = "";
        for (String event : result) {

            event = event+ "|";
            finalEvent += event;
        }
        
        try{
            Map<String,String> solrKeyValues = new HashMap<>();
            String[] pairs = finalEvent.split("\\|");
            for(String pair: pairs){
                String[] keyValue = pair.split("=");
                solrKeyValues.put(keyValue[0],keyValue[1]);
                
            }
            solrKeyValues.remove("disclaimer");
            SolrUtil.postSolr(jsonString,solrKeyValues);
        }catch(Exception ex){
            
        }
        fw = new FileWriter(file, true);
        bw = new BufferedWriter(fw);
        bw.write(finalEvent + "\n");
//        System.out.println("finalEvent:"+finalEvent);


            if (bw != null) {
                bw.close();
            }

            if (fw != null) {
                fw.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }                 
//      System.out.println("finalResult:"+result.toString());
    }
    
    
    private String writeLogLine(final Map<String,Object> map,int index, String prefix, final List<String> events){
        String logString="";
        String key;
        Object value;
        for(Map.Entry<String,Object> entry: map.entrySet()){
            key = entry.getKey();
            value = entry.getValue();
            try{
                if(value instanceof java.util.LinkedHashMap){
                    
                }
            }catch(Exception ex){
                
                
            }
            if(value!= null){
                if(!logString.isEmpty()){
                    logString +="|";
                }
                if(value instanceof Map){
                    logString +=writeLogLine((Map)value,index+1,index>0?key+".":"",events);
                }else{
                    logString +=(prefix+key+"="+value.toString().replaceAll("^\"|\"$", ""));
                }
            }
            if(index ==0){             
                events.add(logString);
                logString ="";
            }

        }
        return logString;
    }
 
 

}
