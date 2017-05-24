package fimo.uet.fairapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fimo.uet.fairapp.KQNode;

/**
 * Created by HP on 4/29/2017.
 */

public class UpdateInfoFairBoxData {
    String JsonResult=null;
    int ResponseCode = 0;
    Context context;
    KQNode kqNode;
    public UpdateInfoFairBoxData(Context context){
        this.context = context;
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

    public boolean UpdateAllData(){
        boolean isDone = false;
        RequestToServer();
        while(JsonResult==null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(ResponseCode>=200 && ResponseCode<300){
            DeleteAllData();
            parseJsonResult(JsonResult);
            isDone = true;
        }
        return isDone;
    }

    public void RequestToServer() {

        final String url = "118.70.72.15:5902/API/v1/procedures";
        final OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                        .addHeader("Authorization","Basic !fimo@54321")
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

            JSONArray FullData = new JSONArray(JsonResult);
            int size = FullData.length();
            for (int i=0;i<size;i++){
                JSONObject procedure = FullData.getJSONObject(i);
                String pID = procedure.getString("pID");
                String NameNode = procedure.getString("offering");
                JSONObject foi = procedure.getJSONObject("foi");
                String address = foi.getString("address");
                double latitude = foi.getDouble("lat");
                double longtitude = foi.getDouble("lng");
                double altitude = 0;
                InsertData(pID, NameNode, address, latitude, longtitude, altitude);
            }
//
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void InsertData(String pID, String NameNode, String address, double latitude, double longtitude, double altitude){
        SQLiteDatabase database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery("SELECT * FROM INFO_FAIR_BOX WHERE ID = '"+pID+"'",null);
        if (cursor.getCount()==0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID",pID);
            contentValues.put("Address",address);
            contentValues.put("Latitude",latitude);
            contentValues.put("Longtitude", longtitude);
            contentValues.put("Altitude",altitude);
            database.insert("INFO_FAIR_BOX", null, contentValues);
            contentValues.clear();
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("ID", pID);
            contentValues2.put("PM25",0);
            contentValues2.put("Temperature",0);
            contentValues2.put("Humidity",0);
            contentValues2.put("PMTimeStamp",0);
            contentValues2.put("TemperatureTimeStamp",0);
            contentValues2.put("HumidityTimeStamp",0);
            database.insert("INDEX_FAIR_BOX",null, contentValues2);
            Cursor c1 = database.rawQuery("SELECT * FROM INFO_FAIR_BOX",null);
            int size = c1.getCount();
            c1.close();
        } else{
            ContentValues contentValues = new ContentValues();
            contentValues.put("Address",address);
            contentValues.put("Latitude",latitude);
            contentValues.put("Longtitude", longtitude);
            contentValues.put("Altitude",altitude);
            database.update("INFO_FAIR_BOX", contentValues, "ID = ?", new String[]{pID});
        }
    }

    public void DeleteAllData(){
        SQLiteDatabase database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);
        database.execSQL("DROP FROM INFO_FAIR_BOX");
        database.close();
    }

}
