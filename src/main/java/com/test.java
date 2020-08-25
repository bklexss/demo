package com;

import com.example.demo.component.HttpServiceCustom;
import com.example.demo.dto.Data;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {

    public static void main(String[] args) {

        String url = "https://alb-prod-yaab.ikeasistencia.com/record";
        List<Data> fechas = new ArrayList<Data>();

        fechas.add(new Data(1,"2020-08-12 04:35:08"));
        fechas.add(new Data(2,"2020-08-12 04:35:08"));
        fechas.add(new Data(3,"2020-08-12 11:58:45"));

        Map status = new HashMap();
        status.put("clProvider", 370);
        status.put("country", "CO");
        status.put("folio",6394897);
        status.put("observations","");

        fechas.stream().forEach(data -> {
            if(data.getType()==1){
                status.put("code", 23);
                status.put("description","Proveedor Llega a la Ubicación");
                status.put("timestamp",getTS(data.getDate(), String.valueOf(status.get("country"))));
            }else if(data.getType()==2){
                status.put("code", 5);
                status.put("description","Nuestro Usuario Contactado");
                status.put("timestamp",getTS(data.getDate(), String.valueOf(status.get("country"))));
            }else if(data.getType()==3){
                status.put("code", 20);
                status.put("description","Proveedor Concluido");
                status.put("observations","");
                status.put("timestamp",getTS(data.getDate(), String.valueOf(status.get("country"))));
            }

            Gson gson = new Gson();
            String json =gson.toJson(status);

            Map finalStatus = new HashMap();
            finalStatus.put("entityType","Status");
            finalStatus.put("entity", status);


            try {
                doPost(url,finalStatus);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public static String getTS(String date, String country){
        String zoneId = "";
        if(country.equals("CO")){
            zoneId = "America/Bogota";
        }else{
            zoneId = "America/Mexico_City";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of(zoneId));
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        System.out.println("dateTime = " + dateTime);
        Timestamp ts = Timestamp.valueOf(dateTime);
        Instant instant = Instant.ofEpochMilli(ts.getTime());
        System.out.println("inst = " + instant.toEpochMilli());
        System.out.println("instant.getEpochSecond() = " + instant.getEpochSecond());
        return String.valueOf(ts.getTime());
    }

    public static Map doPost(String url, Object o) throws IOException {
        Gson gson = new Gson();
        boolean flag = false;
        int count = 0;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        String json =gson.toJson(o);
        StringEntity postingString = new StringEntity(json, "UTF-8");
        httpPost.setEntity(postingString);
        httpPost.setHeader("Content-Type", "application/json");

        System.out.println("url :: " + url);
        System.out.println("body ::" + json);
        HttpResponse response = httpclient.execute(httpPost);
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            System.out.println("Satisfactory response doesn't need retries. Request: "+url +" with body "+json);
            flag = true;
        }
        while (!flag){
            System.out.println("Retry number "+count+1+ ". Request: "+url +" with body "+json);
            response = httpclient.execute(httpPost);
            if(count==2){
                flag = true;
            }
            count++;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Couldn't sleep");
            }
        }

        Map map = new HashMap();
        try {

            HttpEntity entity2 = response.getEntity();
            map = gson.fromJson(EntityUtils.toString(entity2), Map.class);
            System.out.println("Request: "+url + " with response "+map.toString());

        } catch (Exception e) {

            map.put("error", e.getMessage());
            System.out.println("error :: " +  e.getMessage());
        }

        return map;
    }

}
