package fimo.uet.fairapp.DatabaseManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by HP on 4/29/2017.
 */

public class CurrentLocation_DB {
    Context context;
    SQLiteDatabase database;
    public CurrentLocation_DB(Context context){
        this.context = context;
        database =context.openOrCreateDatabase("FeatureOfInterest.sqlite", context.MODE_PRIVATE,null);
    }

    public void CreateTable(){
        database.execSQL("CREATE TABLE IF NOT EXISTS INFO_FAIR_BOX(\n" +
                "   ID       VARCHAR     NOT NULL     PRIMARY KEY,\n" +
                "   Address    VARCHAR    NOT NULL,\n" +
                "   Latitude    DOUBLE    NOT NULL,\n" +
                "   Longtitude  DOUBLE    NOT NULL,\n" +
                "   Altitude    DOUBLE    NOT NULL\n" +
                ");");
    }

    public void RemoveAllData(){
        database.rawQuery("DELETE FROM Data",null);
    }
}
