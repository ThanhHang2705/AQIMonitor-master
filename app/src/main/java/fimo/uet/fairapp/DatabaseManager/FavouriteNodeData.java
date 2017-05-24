package fimo.uet.fairapp.DatabaseManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import fimo.uet.fairapp.KQNode;

/**
 * Created by HP on 4/22/2017.
 */

public class FavouriteNodeData {
    Context context;
    SQLiteDatabase database;
    public FavouriteNodeData(Context context){
        this.context = context;
        database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);
    }

    public ArrayList<KQNode> GetFavouriteList(){
        ArrayList<KQNode> FavouriteList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM ALL_DATA WHERE Favourite = true",null);
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
            FavouriteList.add(kqNode);
            cursor.moveToNext();
        }
        cursor.close();
        return FavouriteList;
    }

    public void InsertNodeToFavourite(KQNode kqNode){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID",kqNode.getID());
        contentValues.put("NameNode",kqNode.getNameNode());
        contentValues.put("Address",kqNode.getAddress());
        contentValues.put("Latitude",kqNode.getLatLng().latitude);
        contentValues.put("Longtitude",kqNode.getLatLng().longitude);
        contentValues.put("PM",kqNode.getPM());
        contentValues.put("Temperature",kqNode.getTemperature());
        contentValues.put("Humidity",kqNode.getHumidity());
        contentValues.put("Favourite",true);
        database.insert("ALL_DATA",null,contentValues);
    }

    public void RemoveNodeFromFavourite(String ID){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Favourite",false);
        database.update("ALL_DATA", contentValues, "ID = ?", new String[]{ID});
    }
}
