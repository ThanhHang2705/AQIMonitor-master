package fimo.uet.fairapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
import java.util.Calendar;
import java.util.TimeZone;

import fimo.uet.fairapp.KQNode;

/**
 * Created by HP on 5/15/2017.
 */

public class UpdateDataForMap {
    String JsonResult=null;
    int ResponseCode = 0;
    Context context;
    KQNode kqNode;
    int current_timestamp;
    String ArrayID;
    public UpdateDataForMap(Context context, String ArrayID){
        this.context = context;
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        current_timestamp = (int)(now / 1000);
        this.ArrayID = ArrayID;
    }

    public String CreateBody(){
        String array_id = "";
        String [] Array_ID = ArrayID.split("\n");
        int length = Array_ID.length;
        if (Array_ID.length>0){
            for (int i=0; i<Array_ID.length;i++){
                if (i==(Array_ID.length-1)){
                    array_id+="\""+Array_ID[i]+"\"";
                }else{
                    array_id+="\""+Array_ID[i]+"\""+", ";
                }
            }
        }
        String Body = "{\n" +
                "\t\"pID\": ["+array_id+"],\n" +
                "\t\"current\": true\n" +
                "}";
        return Body;
    }

    public int getData(){

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

        String Body =CreateBody();
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
            SQLiteDatabase database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);
            Cursor cursor = database.rawQuery("SELECT * FROM INDEX_FAIR_BOX",null);
            JSONArray All_Data = new JSONArray(JsonResult);
            int size = All_Data.length();
            for (int i=0;i<size;i++){
                if (All_Data.getJSONObject(i).has("values")){
                    JSONObject detail = All_Data.getJSONObject(i).getJSONObject("details");
                    JSONObject values = All_Data.getJSONObject(i).getJSONObject("values");
                    String timestamp = values.getString("timestamp");
                    int time_zone = TimeZone.getDefault().getOffset(Long.parseLong(timestamp));
                    int int_timestamp = (int) ((Long.valueOf(timestamp))/1000);
                    String pID = detail.getString("pID");
                    String property = detail.getString("op");
                    double value = 0;
                    value = values.getDouble("value");
                    InsertToDB(pID,property,value, int_timestamp);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void InsertToDB(String pID, String property, double value, int TimeStamp){
        SQLiteDatabase database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery("SELECT * FROM INDEX_FAIR_BOX",null);
        int size = cursor.getCount();
        ContentValues contentValues = new ContentValues();
        if(property.equals("PM2.5")){
            contentValues.put("PM25",value);
            contentValues.put("PMTimeStamp",TimeStamp);
            database.update("INDEX_FAIR_BOX", contentValues,"ID=?",new String[]{pID});
        }else if(property.equals("air_temperature")){
            contentValues.put("Temperature",value);
            contentValues.put("TemperatureTimeStamp",TimeStamp);
            database.update("INDEX_FAIR_BOX", contentValues,"ID=?",new String[]{pID});
        }else if (property.equals("air_humidity")){
            contentValues.put("Humidity",value);
            contentValues.put("HumidityTimeStamp",TimeStamp);
            database.update("INDEX_FAIR_BOX", contentValues,"ID=?",new String[]{pID});
        }
    }
}
