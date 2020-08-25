package com.example.demo.component;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class HttpServiceCustom {

    public Map doPost(String url, Object o) throws IOException {
        Gson gson = new Gson();
        boolean flag = false;
        int count = 0;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        String json =gson.toJson(o);
        StringEntity postingString = new StringEntity(json, "UTF-8");
        httpPost.setEntity(postingString);
        httpPost.setHeader("Content-Type", "application/json");

        log.info("url :: " + url);
        log.info("body ::" + json);
        HttpResponse response = httpclient.execute(httpPost);
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            log.info("Satisfactory response doesn't need retries. Request: "+url +" with body "+json);
            flag = true;
        }
        while (!flag){
            log.info("Retry number "+count+1+ ". Request: "+url +" with body "+json);
            response = httpclient.execute(httpPost);
            if(count==2){
                flag = true;
            }
            count++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error("Couldn't sleep");
            }
        }

        Map map = new HashMap();
        try {

            HttpEntity entity2 = response.getEntity();
            map = gson.fromJson(EntityUtils.toString(entity2), Map.class);
            log.info("Request: "+url + " with response "+map.toString());

        } catch (Exception e) {
            
            map.put("error", e.getMessage());
            log.info("error :: " +  e.getMessage());
        }

        return map;
    }
    public String doGet(String url) throws IOException {

        final String USER_AGENT = "Mozilla/5.0";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println("Response message : "+response.toString());

        return response.toString();
    }
}