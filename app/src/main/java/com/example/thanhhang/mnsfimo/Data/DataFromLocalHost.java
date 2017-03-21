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
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DataFromLocalHost extends AsyncTask<String, Integer, String>{

    public String Data = "";
    String result, current_time, before_time;
    Date Time;

    ArrayList<Long> Temperature, PM25, Humidity,  AllData;

    @Override
    protected String doInBackground(String... params) {
        int time = 100;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
//
        try {
            Time = df.parse(df.format(c.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milliSeconds = Time.getTime() - 24*3600*1000;

        c.setTimeInMillis(milliSeconds);
        Date date = new Date(milliSeconds);
        current_time = FormatTime(Time);
        before_time = FormatTime(date);
        int dem = 0;
        if(isConnectedToServer("http://118.70.72.15:8080/sos-bundle/service",1000)){
            getsData();
            while(Data==""|| dem==10){
                try {
//
                    Thread.sleep(time);
                    time+=100;
                    dem += 1;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return Data;
    }

    @Override
    protected void onPostExecute(String s) {


    }

    public void getsData(){
        final String[] data = new String[1];

        Thread t = new Thread(new Runnable() {


            @Override
            public void run() {

                Looper.prepare(); //For Preparing Message Pool for the childThread
                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 1000); //Timeout Limit
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
                            "        \""+before_time+"\",\n" +
                            "        \""+current_time+"\"\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  }\n" +
                            "}";
                    StringEntity se = new StringEntity(json);
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);
                    response.toString();
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

    public String FormatTime(String time){
        String formattedDate1 = "";
        for(int i=0;i<19;i++){
            if(i != 10 ) {
                formattedDate1 += time.charAt(i);
            }
        }

        return formattedDate1;
    }

    public String FormatTime(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        String time = df.format(date)+"+07:00";
        String temp = "";
        for (int i=0;i<time.length();i++){
            temp += time.charAt(i);
            if(i==9){
                temp +="T";
            }
        }
        return temp;
    }

    public Date FormatResultTime(String result_time){
//        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
//        String temp = "";
//        for (int i=0;i<19;i++){
//            if(i != 10) temp += result_time.charAt(i);
//        }
        String temp ="";
        for (int i=0;i<19;i++){
            if(i!=10){
                temp+=result_time.charAt(i);
            }
        }
        Date date = null;
        try {
            date = df.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public ArrayList<Long>getAllData(){
        return AllData;
    }

    public void ParseJsonData(String JSON_Data, ArrayList<Long> PM25, ArrayList<Long> Temperature, ArrayList<Long> Humidity){
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
//
            try {
                Time = df.parse(df.format(c.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }



//            Temperature = new ArrayList<>();
//            PM25 = new ArrayList<>();
//            Humidity = new ArrayList<>();
////            AllData = new ArrayList<>();
            for (int i =0;i<24;i++){
                Temperature.add((long) 0);
                PM25.add((long) 0);
                Humidity.add((long) 0);
            }
            JSONObject JsonData = new JSONObject(JSON_Data);
            JSONArray Observations = JsonData.getJSONArray("observations");
            String s1 = Observations.toString();
            int size = Observations.length();

            for (int i=0; i<size; i++){
                String ResultTime = Observations.getJSONObject(i).getString("resultTime");

                double subtract_curTime_resultTime = (Time.getTime()- FormatResultTime(ResultTime).getTime())/3600000;
                int RoundSubtract = (int) Math.round(subtract_curTime_resultTime);
                if((24-RoundSubtract)>=0){
                    String observableProperty = Observations.getJSONObject(i).getString("observableProperty");
                    if(observableProperty.equals("temperature")){
                        JSONObject result = Observations.getJSONObject(i).getJSONObject("result");
                        String temperature = result.getString("value");
                        Temperature.set(24-RoundSubtract,Math.round(Double.parseDouble(temperature)));
//                    Temperature.add(24-RoundSubtract-1, Math.round(Double.parseDouble(temperature)));
                    } else if(observableProperty.equals("Humidity")){
                        JSONObject result = Observations.getJSONObject(i).getJSONObject("result");
                        String humidity = result.getString("value");
                        Humidity.set(24-RoundSubtract,Math.round(Double.parseDouble(humidity)));
                    } else if(observableProperty.equals("PM2.5")){
                        JSONObject result = Observations.getJSONObject(i).getJSONObject("result");
                        String pm25 = result.getString("value");
                        PM25.set(24-RoundSubtract,Math.round(Double.parseDouble(pm25)));
                    }
//
                }
            }

//

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}


//
