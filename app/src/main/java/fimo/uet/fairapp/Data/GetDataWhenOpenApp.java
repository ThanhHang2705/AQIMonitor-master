package fimo.uet.fairapp.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Locale;

import fimo.uet.fairapp.KQNode;

/**
 * Created by HP on 4/19/2017.
 */

public class GetDataWhenOpenApp {
    String ID;
    String JsonResult=null;
    Context context;
    KQNode kqNode;

    public GetDataWhenOpenApp(Context context, String id){
        this.ID = id;
        this.context = context;
        try {
            JsonResult = getJsonFromServer();
            while(JsonResult==null){
                Thread.sleep(100);
            }
            parseJsonResult(JsonResult);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getJsonFromServer() throws IOException {
        String url= "https://api.waqi.info/feed/@"+ID+"/?token=1b4e3291a2257461524a84a5b6e7b7097f2203fa";
        BufferedReader inputStream = null;

        URL jsonUrl = new URL(url);
        URLConnection dc = jsonUrl.openConnection();

        dc.setConnectTimeout(5000);
        dc.setReadTimeout(5000);

        inputStream = new BufferedReader(new InputStreamReader(
                dc.getInputStream()));

        // read the JSON results into a string
        JsonResult = inputStream.readLine();
        return JsonResult;
    }

    public void parseJsonResult(String JsonResult){
        try {
            JSONObject FullData = new JSONObject(JsonResult);
            JSONObject Data = FullData.getJSONObject("data");
            JSONObject city = Data.getJSONObject("city");
            String name_node = city.getString("name");
            JSONObject iaqi = Data.getJSONObject("iaqi");
            int pm25 = iaqi.getJSONObject("pm25").getInt("v");
            int temperature = (int)Math.round(iaqi.getJSONObject("t").getDouble("v"));
            int humidity = iaqi.getJSONObject("h").getInt("v");
            JSONArray jsonLatLong =  city.getJSONArray("geo");
            double latitude = jsonLatLong.getDouble(0);
            double longtitude = jsonLatLong.getDouble(1);

            String address = getCompleteAddressString(latitude,longtitude);
            kqNode = new KQNode(ID,name_node,address,new LatLng(latitude,longtitude),pm25,temperature,humidity);
            saveData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
//                    strAdd.add(returnedAddress.getAddressLine(i));
                }
                strAdd=strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    public void saveData(){
        SQLiteDatabase database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);
        ContentValues contentValues = new ContentValues();
//        contentValues.put("NAMENODE",kqNode.GetAddress());
//        contentValues.put("Address",kqNode.getAddress());
        contentValues.put("PM",kqNode.getPM());
        contentValues.put("Temperature", kqNode.getTemperature());
        contentValues.put("Humidity",kqNode.getHumidity());
        database.update("Data", contentValues,"ID=?",new String[]{String.valueOf(ID)});
    }

}
