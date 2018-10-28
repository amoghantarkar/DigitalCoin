/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bitcoin.bitcoinanalyzer;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.apache.http.impl.client.DefaultHttpClient;
/**
 *
 * @author amoghantarkar
 */
public class SolrUtil {
    
    public static void main(String[]args){
        SolrUtil btcSolr = new SolrUtil();
//        btcSolrpostSolr();
    }
    public static void postSolr(String json,Map<String,String> solrKeyValues) {
try{
//   DefaultHttpClient httpClient = new DefaultHttpClient();
////    HttpPost post = new HttpPost("http://localhost:8983/solr/bitcoincore/update/json?wt=json&commit=true");
//    HttpPost post = new HttpPost("http://localhost:8983/solr/testbtccore/update/json?wt=json&commit=true");
//    String addJson = "{\"add\": "+json+"}";
//    System.out.println(addJson);
////    addJson ="{\"add\": {\"doc\":{\"userName\": \"Bill\", \"stars\": 4, \"review_id\": \"-TsVN230RCkLYKBeLsuz7A\", \"type\": \"review\", \"business_name\": \"Eric Goldberg, MD\", \"user_id\": \"zvJCcrpm2yOZrxKffwGQLA\", \"text\": \"Dr. Goldberg has been my doctor for years and I like him.  I've found his office to be fairly efficient.  Today I actually got to see the doctor a few minutes early!  \\n\\nHe seems very engaged with his patients and his demeanor is friendly, yet authoritative.    \\n\\nI'm glad to have Dr. Goldberg as my doctor.\"}}}";
////    System.out.println(addJson);
//    StringEntity entity  = new StringEntity(addJson, "UTF-8");
//    entity.setContentType("application/json");
//    post.setEntity(entity);                
//    HttpResponse response = httpClient.execute(post);
//    HttpEntity httpEntity = response.getEntity();
//    InputStream in = httpEntity.getContent();
//
//    String encoding = httpEntity.getContentEncoding() == null ? "UTF-8" : httpEntity.getContentEncoding().getName();
//    encoding = encoding == null ? "UTF-8" : encoding;
//    String responseText = IOUtils.toString(in, encoding);
//    System.out.println("response Text is " + responseText);
//    
    
    
    SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/bitcoinindexcore").build();
//    for(int i=0;i<1000;++i) {
      SolrInputDocument doc = new SolrInputDocument();
      for(Map.Entry<String,String> entry: solrKeyValues.entrySet()){
          doc.addField(entry.getKey(), entry.getValue());
      }
      client.add(doc);
//      System.out.println("doc:"+doc.toString());
//      if(i%100==0) client.commit();  // periodically flush
//    }
    client.commit(); 
} catch (UnsupportedEncodingException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
} catch (Exception e) {
    e.printStackTrace();
}
}
//{"add": {"time":{"updated":"Dec 4, 2017 08:12:00 UTC","updatedISO":"2017-12-04T08:12:00+00:00","updateduk":"Dec 4, 2017 at 08:12 GMT"},"disclaimer":"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org","chartName":"Bitcoin","bpi":{"USD":{"code":"USD","symbol":"&#36;","rate":"11,442.0038","description":"United States Dollar","rate_float":11442.0038},"GBP":{"code":"GBP","symbol":"&pound;","rate":"8,513.6517","description":"British Pound Sterling","rate_float":8513.6517},"EUR":{"code":"EUR","symbol":"&euro;","rate":"9,655.9527","description":"Euro","rate_float":9655.9527}}} }
//{"add": {"doc":{"userName": "Bill", "stars": 4, "review_id": "-TsVN230RCkLYKBeLsuz7A", "type": "review", "business_name": "Eric Goldberg, MD", "user_id": "zvJCcrpm2yOZrxKffwGQLA", "text": "Dr. Goldberg has been my doctor for years and I like him.  I've found his office to be fairly efficient.  Today I actually got to see the doctor a few minutes early!  \n\nHe seems very engaged with his patients and his demeanor is friendly, yet authoritative.    \n\nI'm glad to have Dr. Goldberg as my doctor."}}}

}
