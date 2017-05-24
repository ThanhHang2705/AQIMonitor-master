package fimo.uet.fairapp.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fimo.uet.fairapp.Adapters.AddNodeAdapter;
import fimo.uet.fairapp.Data.GetIDFromFairBox;
import fimo.uet.fairapp.Data.ListWifiForFairBox;
import fimo.uet.fairapp.Progress_Dialog.Config_Update_FairBox_Progress;
import fimo.uet.fairapp.R;

import static fimo.uet.fairapp.R.id.lv_NewNode;

/**
 * Created by HP on 3/20/2017.
 */

public class ListWifiForMCU extends AppCompatActivity {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    ListView lv;
    ArrayList<String>ListSSID4MCU;
    EditText PassWord;
    String SSID;
    String ID;
    double latitude,longtitude;
    String body;
    String Address;

    private AddNodeAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_wifi_4_mcu);
        ID=null;
        lv = (ListView)findViewById(lv_NewNode);
        ListSSID4MCU = new ArrayList<>();
//        GetListWifi();
//        GetIdOfNode();
        new ProgressTask(ListWifiForMCU.this).execute();
        getLatLng();
    }

    public void getLatLng(){
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("LatLng");
        if(bundle!=null){
            latitude = bundle.getDouble("Latitude");
            longtitude = bundle.getDouble("Longtitude");
            Address = bundle.getString("Address");
        }
    }

    public void GetIdOfNode() {
        GetIDFromFairBox getIDFromFairBox = new GetIDFromFairBox();
        ID = getIDFromFairBox.GetID();

    }

    public String BodyForRequest(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("pID", this.ID);
            JSONObject obj2 = new JSONObject();
            obj2.put("lat", latitude);
            obj2.put("lng", longtitude);
            obj2.put("alt", "");
            obj2.put("address", Address);
            obj.put("foi", obj2);
            body = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
       return body;
    }

    public void GetListWifi(){
        ListWifiForFairBox listWifiForFairBox = new ListWifiForFairBox();
        listWifiForFairBox.GetListWifi(ListSSID4MCU);
    }

    class loginDialog extends AlertDialog{
        String ssid;
        TextView SSID;
        EditText PassWord;
        CheckBox ShowPassWord;
        Button KetNoi,Huy;
        protected loginDialog(@NonNull final Context context, String ssid) {
            super(context);
            this.ssid=ssid;
//        WindowManager.LayoutParams params = getContext().getWindow().getAttributes();
//        params.gravity = Gravity.TOP;
            final Builder alertadd = new Builder(getContext());
            LayoutInflater factory = LayoutInflater.from(getContext());
            final AlertDialog alertDialog = alertadd.create();
            final View view = factory.inflate(R.layout.login_dialog, null);
            SSID = (TextView)view.findViewById(R.id.ssid);
            PassWord = (EditText)view.findViewById(R.id.pass_word);
            ShowPassWord = (CheckBox)view.findViewById(R.id.show_pass_word);
            KetNoi = (Button)view.findViewById(R.id.btx_ket_noi);
            Huy = (Button)view.findViewById(R.id.btx_huy);
            SSID.setText(ssid);
            PassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            KetNoi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    Config_Update_FairBox_Progress config_update_fairBox_progress =
                            new Config_Update_FairBox_Progress(ListWifiForMCU.this, SSID.getText().toString(),PassWord.getText().toString(),body);
                    config_update_fairBox_progress.execute();

                }
            });
            Huy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            PassWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                    }

                }
            });
            ShowPassWord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        PassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        PassWord.setSelection(PassWord.getText().length());
                    }else{
                        PassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        PassWord.setSelection(PassWord.getText().length());
                    }
                }
            });
            alertDialog.setView(view);
            alertDialog.show();
        }
    }
    public class ProgressTask extends AsyncTask<Integer,Integer,Integer>{
        private ProgressDialog dialog;

        private Context context;
        public ProgressTask(Context context) {
            this.context = context;
            dialog = new ProgressDialog(context);

        }
        @Override
        protected Integer doInBackground(Integer... params) {
            GetListWifi();
            GetIdOfNode();
            body = BodyForRequest();
            return 1;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage(context.getString(R.string.device_connecting));
            dialog.show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer==1){
                dialog.dismiss();
                adapter = new AddNodeAdapter(ListSSID4MCU,context);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView NameNode = (TextView) lv.getChildAt(position).findViewById(R.id.txt_NewNode);
                        SSID = NameNode.getText().toString();
                        loginDialog lgDialog = new loginDialog(ListWifiForMCU.this,SSID);


                    }
                });
            }
        }
    }

}


