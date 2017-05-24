package fimo.uet.fairapp.Progress_Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import fimo.uet.fairapp.R;

import fimo.uet.fairapp.DatabaseManager.Index_Fair_Box_DB;
import fimo.uet.fairapp.DatabaseManager.Info_Fair_Box_DB;

/**
 * Created by HP on 5/10/2017.
 */

public class UpdateAllDB_Progress extends AsyncTask <Integer,Integer,Integer>{
    Context context;
    ProgressDialog dialog;
    public UpdateAllDB_Progress(Context context){
        this.context = context;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        Info_Fair_Box_DB info_fair_box_db = new Info_Fair_Box_DB(context);
        info_fair_box_db.CreateTable();
        info_fair_box_db.RemoveAllData();
        Index_Fair_Box_DB index_fair_box_db = new Index_Fair_Box_DB(context);
        index_fair_box_db.CreateTable();
        index_fair_box_db.RemoveAllData();
        info_fair_box_db.UpdateDatabase();
        index_fair_box_db.UpdateDatabase();
        return 1;
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage(context.getString(R.string.device_connecting));
        this.dialog.setCancelable(false);
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (integer==1){
            dialog.dismiss();
            Toast.makeText(context,"Quá trình cập nhật thành công", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"Quá trình cập nhật không thành công", Toast.LENGTH_LONG).show();
        }
    }
}
