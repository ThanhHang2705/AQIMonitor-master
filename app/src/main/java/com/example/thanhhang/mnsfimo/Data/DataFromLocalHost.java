package com.example.thanhhang.mnsfimo.Data;

import android.os.AsyncTask;
import android.os.Looper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DataFromLocalHost extends AsyncTask<String, Integer, ArrayList>{

    public String Data = "";
    String result;

    ArrayList<Long> Temperature, PM25, Humidity,  AllData;

    @Override
    protected ArrayList doInBackground(String... params) {

        String s = null;
        String result1 ="";
        try {
            int time = 1000;
            getsData();
            while(Data==""){
                try {
//                    GetDataJson getDataJson = new GetDataJson();
//                    getDataJson.t.start();
//                    Data = getsData();
                    Thread.sleep(time);
                    time+=1000;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Temperature = new ArrayList<>();
            PM25 = new ArrayList<>();
            Humidity = new ArrayList<>();
            AllData = new ArrayList<>();
            JSONObject JsonData = new JSONObject(Data);
            JSONArray Observations = JsonData.getJSONArray("observations");
            String s1 = Observations.toString();
            int size = Observations.length();

            for (int i=0;i<Observations.length();i++){
                if(i<20){
                    JSONObject result = Observations.getJSONObject(i).getJSONObject("result");
                    String temperature = result.getString("value");
                    AllData.add(Math.round(Double.parseDouble(temperature)));

                }else if(i>=20 && i<40){
                    JSONObject result = Observations.getJSONObject(i).getJSONObject("result");
                    String PM_25 = result.getString("value");
                    AllData.add(Math.round(Double.parseDouble(PM_25)));
                }else{
                    JSONObject result = Observations.getJSONObject(i).getJSONObject("result");
                    String humidity = result.getString("value");
                    AllData.add(Math.round(Double.parseDouble(humidity)));
                }
            }

//            for (int i =0; i<Temperature.size();i++){
//                result1 += "Temperature: "+Temperature.get(i)+"\n";
////
//            }
//
//            for (int i =0; i<PM25.size();i++){
//                result1 += "PM2.5: "+PM25.get(i)+"\n";
//
//            }
//
//            for (int i =0; i<Humidity.size();i++){
//                result1 += "Humidity: "+Humidity.get(i)+"\n";
//            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return AllData;
    }

    @Override
    protected void onPostExecute(ArrayList s) {


    }

    public void getsData(){
        final String[] data = new String[1];

        Thread t = new Thread(new Runnable() {


            @Override
            public void run() {
                Looper.prepare(); //For Preparing Message Pool for the childThread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 2000); //Timeout Limit
                HttpResponse response;
//                JSONObject json = new JSONObject();

                try {
                    HttpPost post = new HttpPost("http://118.70.72.15:8080/sos-bundle/service");
//                    json.put("play", play);
//                    json.put("Properties", prop);
                    String json = "{\n" +
                            "  \"request\": \"GetObservation\",\n" +
                            "  \"service\": \"SOS\",\n" +
                            "  \"version\": \"2.0.0\",\n" +
                            "  \"procedure\": \"procedure_aqi8641\", \n" +
                            "  \"observedProperty\": [\"PM2.5\",\"Humidity\",\"temperature\"],\n" +
                            "  \"temporalFilter\": {\n" +
                            "    \"during\": {\n" +
                            "      \"ref\": \"om:phenomenonTime\",\n" +
                            "      \"value\": [\n" +
                            "        \"2017-03-06T13:00:00+07:00\",\n" +
                            "        \"2017-03-07T13:00:00+07:00\"\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  }\n" +
                            "}";
                    StringEntity se = new StringEntity(json);
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);

                /*Checking response */
                    if (response != null) {
                        InputStream in = response.getEntity().getContent(); //Get the data in the entity
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder str = new StringBuilder();
                        String line = null;
                        try {
                            while ((line = bufferedReader.readLine()) != null) {
                                str.append(line + "\n");
                            }
                            data[0] = str.toString();
                            Data = data[0];
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } finally {
                            try {
                                in.close();
                            } catch (IOException e) {
                                //tough luck...
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Looper.loop(); //Loop in the message queue
            }
        });
        t.start();
//        return data[0];
    }

}


//    class GetDataJson {
//        final String[] data = new String[1];
//
//       Thread t = new Thread(new Runnable() {
//
//
//           @Override
//           public void run() {
//               Looper.prepare(); //For Preparing Message Pool for the childThread
//               HttpClient client = new DefaultHttpClient();
//               HttpConnectionParams.setConnectionTimeout(client.getParams(), 2000); //Timeout Limit
//               HttpResponse response;
////                JSONObject json = new JSONObject();
//
//               try {
//                   HttpPost post = new HttpPost("http://118.70.72.15:8080/sos-bundle/service");
////                    json.put("play", play);
////                    json.put("Properties", prop);
//                   String json = "{\n" +
//                           "  \"request\": \"GetObservation\",\n" +
//                           "  \"service\": \"SOS\",\n" +
//                           "  \"version\": \"2.0.0\",\n" +
//                           "  \"procedure\": \"procedure_aqi8641\", \n" +
//                           "  \"observedProperty\": [\"PM2.5\",\"Humidity\",\"temperature\"],\n" +
//                           "  \"temporalFilter\": {\n" +
//                           "    \"during\": {\n" +
//                           "      \"ref\": \"om:phenomenonTime\",\n" +
//                           "      \"value\": [\n" +
//                           "        \"2017-03-06T13:00:00+07:00\",\n" +
//                           "        \"2017-03-07T13:00:00+07:00\"\n" +
//                           "      ]\n" +
//                           "    }\n" +
//                           "  }\n" +
//                           "}";
//                   StringEntity se = new StringEntity(json);
//                   se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//                   post.setEntity(se);
//                   response = client.execute(post);
//
//                /*Checking response */
//                   if (response != null) {
//                       InputStream in = response.getEntity().getContent(); //Get the data in the entity
//                       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//                       StringBuilder str = new StringBuilder();
//                       String line = null;
//                       try {
//                           while ((line = bufferedReader.readLine()) != null) {
//                               str.append(line + "\n");
//                           }
//                           data[0] = str.toString();
//                           Data = data[0];
//                       } catch (IOException e) {
//                           throw new RuntimeException(e);
//                       } finally {
//                           try {
//                               in.close();
//                           } catch (IOException e) {
//                               //tough luck...
//                           }
//                       }
//                   }
//
//               } catch (Exception e) {
//                   e.printStackTrace();
//               }
//
//               Looper.loop(); //Loop in the message queue
//           }
//       });
//
////        Data =data[0];
//
//
//    }
//
//
//
//
