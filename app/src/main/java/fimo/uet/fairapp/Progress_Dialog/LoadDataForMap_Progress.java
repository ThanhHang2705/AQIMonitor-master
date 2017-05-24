package fimo.uet.fairapp.Progress_Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Calendar;

import fimo.uet.fairapp.Data.UpdateDataForMap;
import fimo.uet.fairapp.R;

/**
 * Created by HP on 5/15/2017.
 */

public class LoadDataForMap_Progress extends AsyncTask <Integer,Integer,Integer>{
    Context context;
    ProgressDialog dialog;
    String ArrayID;
    public LoadDataForMap_Progress(Context context, String ArrayID){
        this.context = context;
        dialog = new ProgressDialog(context);
        this.ArrayID = ArrayID;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        final int[] ResponseCode = {0};
        new Thread(new Runnable() {
            @Override
            public void run() {
                UpdateDataForMap updateDataForMap = new UpdateDataForMap(context, ArrayID);
                ResponseCode[0] = updateDataForMap.getData();
            }
        }).start();
        int interval = 0;
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        long timestamp = now / 1000;
        while (interval<10){
            calendar = Calendar.getInstance();
            now = calendar.getTimeInMillis();
            long current_timestamp = now / 1000;
            interval = (int) (current_timestamp-timestamp);
        }
        return ResponseCode[0];
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage(context.getString(R.string.device_connecting));
        this.dialog.setCancelable(false);
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        dialog.dismiss();
        if(integer>=200 && integer<300){
            Toast.makeText(context,"Đã cập nhật dữ liệu", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context,"Cập nhật dữ liệu không thành công", Toast.LENGTH_LONG).show();
        }
    }
}
