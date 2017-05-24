package fimo.uet.fairapp.DatabaseManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import fimo.uet.fairapp.KQNode;

/**
 * Created by HP on 4/22/2017.
 */

public class User_DB {
    Context context;
    SQLiteDatabase database;
    ArrayList<KQNode>FavouriteList;

    public User_DB(Context context){
        this.context = context;
        database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);
    }

    public void CreateTable(){
//        database.execSQL("DROP TABLE IF EXISTS USER_DB");
        database.execSQL("CREATE TABLE IF NOT EXISTS USER_DB(\n" +
                "   ID          VARCHAR     NOT NULL,\n" +
                "   Conditional INT     NOT NULL,\n" +
                "   FAVOURITE   BOOL     NOT NULL\n" +
                ");");
    }

    public ArrayList<KQNode> GetFavouriteList(){
//        Cursor cursor = database.rawQuery("SELECT Data.ID, Data.NameNode, Data.Address, Data.Latitude, Data.Longtitude Data.PM, " +
//                "Data.Temperature, Data.Humidity, Notification.Conditional " +
//                "FROM Data INNER JOIN Notification ON Data.ID = Notification.ID",null);
        Cursor cursor1 = database.rawQuery("SELECT ID FROM USER_DB WHERE FAVOURITE = 'TRUE'",null);
        int size = cursor1.getCount();
        for (int i = 0; i<size; i++){
            String ID = cursor1.getString(0);
            Cursor cursor2 = database.rawQuery("SELECT * FROM INFO_FAIR_BOX WHERE ID = '"+ID+"'",null);
            String NameNode = cursor1.getString(1);
            String Address = cursor1.getString(2);
            double Latitude = cursor1.getDouble(3);
            double Longtitude = cursor1.getDouble(4);
            Cursor cursor3 = database.rawQuery("SELECT * FROM INDEX_FAIR_BOX WHERE ID = '"+ID+"'",null);
            double PM = cursor3.getDouble(1);
            double Temperature = cursor3.getDouble(2);
            double Humidity = cursor3.getDouble(3);
//            NodeInfo nodeInfo = new NodeInfo(ID,NameNode,Address,Latitude,Longtitude);
//            IndexInfo indexInfo = new IndexInfo(PM,Temperature,Humodity);
//            MyNotificationInfo myNotificationInfo = new MyNotificationInfo(nodeInfo,indexInfo);
            KQNode kqNode = new KQNode(ID,Address,Address,new LatLng(Latitude,Longtitude),PM,Humidity,Temperature);
            FavouriteList.add(kqNode);
        }
        return FavouriteList;
    }
}
