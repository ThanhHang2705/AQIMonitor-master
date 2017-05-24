package fimo.uet.fairapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP on 3/5/2017.
 */

public class DataNode extends SQLiteOpenHelper {
    private String DATABASE_NAME ;
    private String TABLE_NAME ;
    private int ID ;
    private String NAME_NODE ;
    private double PM25 ;
    private double HUMIDITY ;
    private double TEMPERATURE;

    public DataNode(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
