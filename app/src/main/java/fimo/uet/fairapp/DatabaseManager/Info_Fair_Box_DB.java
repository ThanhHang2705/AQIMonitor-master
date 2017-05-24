package fimo.uet.fairapp.DatabaseManager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fimo.uet.fairapp.Data.UpdateInfoFairBoxData;

/**
 * Created by HP on 4/29/2017.
 */

public class Info_Fair_Box_DB {
    Context context;
    SQLiteDatabase database;
    public Info_Fair_Box_DB(Context context){
        this.context = context;
        database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);
    }

    public void CreateTable(){
        database.execSQL("DROP TABLE IF EXISTS INFO_FAIR_BOX");
        database.execSQL("CREATE TABLE IF NOT EXISTS INFO_FAIR_BOX(\n" +
                "   ID       VARCHAR     NOT NULL,\n" +
                "   Address    VARCHAR    NOT NULL,\n" +
                "   Latitude    DOUBLE    NOT NULL,\n" +
                "   Longtitude  DOUBLE    NOT NULL,\n" +
                "   Altitude    DOUBLE    NOT NULL\n" +
                ");");
        Cursor cursor = database.rawQuery("SELECT * FROM INFO_FAIR_BOX",null);
        int size = cursor.getCount();
        int i =0;
    }

    public void RemoveAllData(){
            database.rawQuery("DELETE FROM INFO_FAIR_BOX",null);
    }

    public int UpdateDatabase(){
//        RemoveAllData();
        int ResponeCode = 0;
        UpdateInfoFairBoxData updateInfoFairBoxData = new UpdateInfoFairBoxData(context);
        ResponeCode = updateInfoFairBoxData.getData();
        return ResponeCode;
    }

    public String GetAddressFromID(String pID){
        String address = null;
        Cursor cursor = database.rawQuery("SELECT Address FROM INFO_FAIR_BOX WHERE ID = '"+pID+"'",null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            address = cursor.getString(0);
        }
        return address;
    }
}
