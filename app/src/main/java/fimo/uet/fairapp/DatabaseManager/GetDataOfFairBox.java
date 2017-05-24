package fimo.uet.fairapp.DatabaseManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

import fimo.uet.fairapp.KQNode;

/**
 * Created by HP on 5/5/2017.
 */

public class GetDataOfFairBox {
    Context context;
    SQLiteDatabase database;
    ArrayList<KQNode> AllData;
    int current_timestamp;
    public GetDataOfFairBox(Context context){
        this.context = context;
        database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);
        AllData = new ArrayList<KQNode>();
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        current_timestamp = (int)(now / 1000);
    }

    public ArrayList<KQNode> getData(){
        Cursor cursor1 = database.rawQuery("SELECT * FROM INFO_FAIR_BOX",null);
        int size = cursor1.getCount();
        cursor1.moveToFirst();
        for(int i=0;i<size;i++){
            String ID = cursor1.getString(0);
            String Address = cursor1.getString(1);
            double Latitude = cursor1.getDouble(2);
            double Longtitude = cursor1.getDouble(3);
            double Altitude = cursor1.getDouble(4);
            Cursor cursor2 = database.rawQuery("SELECT * FROM INDEX_FAIR_BOX WHERE ID = '"+ID+"'",null);
            if (cursor2.getCount()>0){
                cursor2.moveToFirst();
                double PM25 = -1;
                double Temperature = -1;
                double Humidity = -1;
                int PMTimeStamp = cursor2.getInt(4);
                int TemperatureTimeStamp = cursor2.getInt(5);
                int HumidityTimeStamp = cursor2.getInt(6);
                int a = current_timestamp-PMTimeStamp;
                a= current_timestamp-TemperatureTimeStamp;
                a=current_timestamp-HumidityTimeStamp;
                if((current_timestamp-PMTimeStamp)/3600<1){
                    PM25 = cursor2.getDouble(1);
                }
                if((current_timestamp-TemperatureTimeStamp)/3600<1){
                    Temperature = cursor2.getDouble(2);
                }
                if((current_timestamp-HumidityTimeStamp)/3600<1){
                    Humidity = cursor2.getDouble(3);
                }
                KQNode kqNode = new KQNode(ID,Address,Address,new LatLng(Latitude,Longtitude),PM25,Temperature,Humidity);
                AllData.add(kqNode);
            }
            cursor1.moveToNext();
        }

        return AllData;
    }

    public ArrayList<KQNode> getFavouriteData(){
        AllData.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM USER_DB" , null);
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            for(int i=0;i<cursor.getCount();i++) {
                String ID = cursor.getString(0);
                Cursor cursor1 = database.rawQuery("SELECT Address, Latitude, Longtitude FROM INFO_FAIR_BOX WHERE ID = '" + ID + "'", null);
                Cursor cursor2 = database.rawQuery("SELECT * FROM INDEX_FAIR_BOX WHERE ID = '" + ID + "'", null);
                if (cursor1.getCount()>0 && cursor2.getCount()>0){
                    cursor1.moveToFirst();
                    cursor2.moveToFirst();
                    String Address = cursor1.getString(0);
                    double Latitude = cursor1.getDouble(1);
                    double Longtitude = cursor1.getDouble(2);
                    int PMTimeStamp = cursor2.getInt(4);
                    int TemperatureTimeStamp = cursor2.getInt(5);
                    int HumidityTimeStamp = cursor2.getInt(6);
                    double PM25 = -1;
                    double Temperature = -1;
                    double Humidity = -1;
                    int a = current_timestamp-PMTimeStamp;
                    a= current_timestamp-TemperatureTimeStamp;
                    a=current_timestamp-HumidityTimeStamp;
                    if((current_timestamp-PMTimeStamp)/3600<1){
                        PM25 = cursor2.getDouble(1);
                    }
                    if((current_timestamp-TemperatureTimeStamp)/3600<1){
                        Temperature = cursor2.getDouble(2);
                    }
                    if((current_timestamp-HumidityTimeStamp)/3600<1){
                        Humidity = cursor2.getDouble(3);
                    }
                    KQNode kqNode = new KQNode(ID, Address, Address, new LatLng(Latitude, Longtitude), PM25, Temperature, Humidity);
                    double d = kqNode.getPM();
                    AllData.add(kqNode);
                }
                cursor.moveToNext();
            }
        }
        return AllData;
    }

}
