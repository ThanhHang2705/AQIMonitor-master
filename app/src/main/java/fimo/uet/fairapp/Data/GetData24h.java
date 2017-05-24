package fimo.uet.fairapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import fimo.uet.fairapp.KQNode;

/**
 * Created by HP on 5/7/2017.
 */

public class GetData24h {

    String JsonResult=null;
    int ResponseCode = 0;
    Context context;
    KQNode kqNode;
    String pID;
    ArrayList<Double> pm25;
    ArrayList<Double> temperature;
    ArrayList<Double> humidity;
    int current_timestamp;
    public GetData24h(Context context,String pID){
        this.context = context;
        this.pID = pID;
        pm25 = new ArrayList<>();
        temperature = new ArrayList<>();
        humidity = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        current_timestamp = (int)(now / 1000);
    }

    public int getData(){
        for (int i=0; i<24;i++){
            this.pm25.add(Double.valueOf(0));
            this.temperature.add(Double.valueOf(0));
            this.humidity.add(Double.valueOf(0));
        }
        RequestToServer();
        while(JsonResult==null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(ResponseCode>=200 && ResponseCode<300){
            parseJsonResult(JsonResult);
        }
        return ResponseCode;

    }
    public void RequestToServer() {

        final String url = "http://118.70.72.15:5902/API/v1/observations/getData";
        final OkHttpClient client = new OkHttpClient();
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        String Body = "{\n" +
                "\t\"pID\" : \""+pID+"\",\n" +
                "\t\"current\":false\n" +
                "}";
        RequestBody body = RequestBody.create(JSON, Body);
        Request request = new Request.Builder()
                .addHeader("Authorization","Basic !fimo@54321")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            JsonResult = response.body().string();
            ResponseCode = response.code();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseJsonResult(String JsonResult){
        try {
            JSONArray All_Data = new JSONArray(JsonResult);
            int size = All_Data.length();
            for (int i=0;i<size;i++){
                JSONObject detail = All_Data.getJSONObject(i).getJSONObject("details");
                JSONArray values = All_Data.getJSONObject(i).getJSONArray("values");
                String pID = detail.getString("pID");
                String property = detail.getString("op");
                if(property.equals("PM2.5")){
                    InsertToArray("PM25", values);
                }else if(property.equals("air_temperature")){
                    InsertToArray("Temperature", values);
                }else if (property.equals("air_humidity")){
                    InsertToArray("Humidity", values);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void InsertToArray(String property, JSONArray values){
        int size = values.length();
        for (int i=0;i<size;i++){
            try {
                String timestamp = values.getJSONObject(i).getString("timestamp");
                int time_zone = TimeZone.getDefault().getOffset(Long.parseLong(timestamp));
                int int_timestamp = (int) ((Long.valueOf(timestamp))/1000);
                int index = 24-(current_timestamp-int_timestamp)/3600;
                if(index<24 && index>=0){
                    double value = values.getJSONObject(i).getDouble("value");
                    if (index==0){
                        InsertToDB(property, value);
                    }
                    if(property.equals("PM25")){
                        pm25.set(index, value);
                    }else if(property.equals("Temperature")){
                        temperature.set(index, value);
                    }else if (property.equals("Humidity")){
                        humidity.set(index, value);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void InsertToDB(String property, double value){
        SQLiteDatabase database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);
        ContentValues contentValues =new ContentValues();
        contentValues.put(property,value);
        database.update("INDEX_FAIR_BOX", contentValues,"ID=?",new String[]{pID});
        database.close();
    }

    public int ConvertTimestamp(String timestamp){
        DateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar pacific = Calendar.getInstance(TimeZone.getDefault());
        int after_timestamp = 0;
        try {
            Date formatedDate = newFormat.parse(timestamp);
            after_timestamp = (int) formatedDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return after_timestamp/1000;
    }


    public ArrayList<Double> GetPM25(){
        return pm25;
    }

    public ArrayList<Double> GetTemperature(){
        return temperature;
    }

    public ArrayList<Double> GetHumidity(){
        return humidity;
    }

}
