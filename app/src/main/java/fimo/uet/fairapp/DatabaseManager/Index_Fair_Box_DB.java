package fimo.uet.fairapp.DatabaseManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import fimo.uet.fairapp.Data.UpdateIndexFairBox;

/**
 * Created by HP on 4/29/2017.
 */

public class Index_Fair_Box_DB {
    Context context;
    SQLiteDatabase database;
    int current_timestamp;
    public Index_Fair_Box_DB(Context context){
        this.context = context;
        database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);

    }

    public void CreateTable(){
        database.execSQL("DROP TABLE IF EXISTS INDEX_FAIR_BOX");
        database.execSQL("CREATE TABLE IF NOT EXISTS INDEX_FAIR_BOX(\n" +
                "   ID             VARCHAR     NOT NULL,\n" +
                "   PM25           DOUBLE    NOT NULL,\n" +
                "   Temperature    DOUBLE    NOT NULL,\n" +
                "   Humidity       DOUBLE    NOT NULL,\n" +
                "   PMTimeStamp      INTEGER        NOT NULL,\n" +
                "   TemperatureTimeStamp      INTEGER        NOT NULL,\n" +
                "   HumidityTimeStamp      INTEGER        NOT NULL\n" +
                ");");
    }

    public void RemoveAllData(){
        database.rawQuery("DELETE FROM INDEX_FAIR_BOX",null);
    }

    public void ReCreateTable(){
        database.execSQL("DROP TABLE IF EXISTS INDEX_FAIR_BOX");
        CreateTable();
    }

    public int UpdateDatabase(){
//        RemoveAllData();
        int ResponseCode = 0;
        UpdateIndexFairBox updateIndexFairBoxData = new UpdateIndexFairBox(context);
        ResponseCode = updateIndexFairBoxData.getData();
        return ResponseCode;
    }

    public void UpdateIndexFromID(String pID, double PM25, double Temperature, double Humidity){
        ContentValues contentValues = new ContentValues();
        contentValues.put("PM25", PM25);
        contentValues.put("Temperature", Temperature);
        contentValues.put("Humidity", Humidity);
        database.update("INDEX_FAIR_BOX", contentValues, "ID=?",new String[]{pID});
    }
}
