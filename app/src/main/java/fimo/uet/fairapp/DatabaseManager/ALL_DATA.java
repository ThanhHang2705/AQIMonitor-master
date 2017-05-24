package fimo.uet.fairapp.DatabaseManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import fimo.uet.fairapp.KQNode;

/**
 * Created by HP on 4/18/2017.
 */

public class ALL_DATA {
    Context context;
    SQLiteDatabase database;
    public ALL_DATA(Context context){
        this.context = context;
        database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);
    }

    public void CreateTable(){
        database.execSQL("CREATE TABLE IF NOT EXISTS ALL_DATA(\n" +
                "   ID VARCHAR     NOT NULL,\n" +
                "   NameNode  VARCHAR    NOT NULL,\n" +
                "   Address  VARCHAR    NOT NULL,\n" +
                "   Latitude  DOUBLE    NOT NULL,\n" +
                "   Longtitude  DOUBLE    NOT NULL,\n" +
                "   PM  DOUBLE    NOT NULL,\n" +
                "   Temperature  DOUBLE    NOT NULL,\n" +
                "   Humidity  DOUBLE    NOT NULL,\n" +
                "   Favourite  BOOL    NOT NULL,\n" +
                ");");
    }


    public ArrayList<KQNode> ReadAllData(){
        ArrayList<KQNode> AllData = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Data",null);
        int size = cursor.getCount();
        cursor.moveToFirst();
        for(int i=0;i<size;i++){
            String ID = cursor.getString(0);
            String NameNode = cursor.getString(1);
            String Address = cursor.getString(2);
            double Latitue = cursor.getDouble(3);
            double Longtitude = cursor.getDouble(4);
            double PM = cursor.getDouble(5);
            double Temperature = cursor.getDouble(6);
            double Humidity = cursor.getDouble(7);
            KQNode kqNode = new KQNode(ID, NameNode, Address, new LatLng(Latitue,Longtitude), PM, Temperature, Humidity);
            AllData.add(kqNode);
            cursor.moveToNext();
        }
        cursor.close();
        return AllData;
    }



    public void UpdateAllData(ArrayList<KQNode> AllData){
        DeleteAllData();
        for (int i=0;i<AllData.size();i++){
            KQNode kqNode = AllData.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID",kqNode.getID());
            contentValues.put("NameNode",kqNode.getNameNode());
            contentValues.put("Address",kqNode.getAddress());
            contentValues.put("Latitude",kqNode.getLatLng().latitude);
            contentValues.put("Longtitude",kqNode.getLatLng().longitude);
            contentValues.put("PM",kqNode.getPM());
            contentValues.put("Temperature",kqNode.getTemperature());
            contentValues.put("Humidity",kqNode.getHumidity());
            database.insert("Data",null,contentValues);
        }
    }

    public void UpdateAllDataFromID(KQNode kqNode){
        ContentValues contentValues = new ContentValues();
        contentValues.put("NameNode",kqNode.getNameNode());
        contentValues.put("Address",kqNode.getAddress());
        contentValues.put("Latitude",kqNode.getLatLng().latitude);
        contentValues.put("Longtitude",kqNode.getLatLng().longitude);
        contentValues.put("PM",kqNode.getPM());
        contentValues.put("Temperature",kqNode.getTemperature());
        contentValues.put("Humidity",kqNode.getHumidity());
        database.update("Data",contentValues,"ID=?", new String[]{String.valueOf(kqNode.getID())});
    }

    public void UpdateNameNode(int ID, String NameNode){
        ContentValues contentValues = new ContentValues();
        contentValues.put("NameNode",NameNode);
        database.update("Data",contentValues,"ID=?", new String[]{String.valueOf(ID)});
    }

    public void UpdateAddress(int ID, String Address){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Address",Address);
        database.update("Data",contentValues,"ID=?", new String[]{String.valueOf(ID)});
    }


    public void InsertNode(KQNode kqNode){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID",kqNode.getID());
        contentValues.put("NameNode",kqNode.getNameNode());
        contentValues.put("Address",kqNode.getAddress());
        contentValues.put("Latitude",kqNode.getLatLng().latitude);
        contentValues.put("Longtitude",kqNode.getLatLng().longitude);
        contentValues.put("PM",kqNode.getPM());
        contentValues.put("Temperature",kqNode.getTemperature());
        contentValues.put("Humidity",kqNode.getHumidity());
        contentValues.put("Favourite",false);
        database.insert("ALL_DATA",null,contentValues);
    }
    public void UpdateIndexFromID(){

    }



    public void DeleteAllData(){
        database.rawQuery("DELETE FROM Data",null);
    }
}
