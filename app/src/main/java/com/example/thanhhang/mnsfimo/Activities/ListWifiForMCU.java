package com.example.thanhhang.mnsfimo.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.example.thanhhang.mnsfimo.Adapters.AddNodeAdapter;
import com.example.thanhhang.mnsfimo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static com.example.thanhhang.mnsfimo.R.id.lv_NewNode;

/**
 * Created by HP on 3/20/2017.
 */

public class ListWifiForMCU extends AppCompatActivity {

    ListView lv;
    ArrayList<String>ListSSID4MCU;
    EditText PassWord;
    String SSID;
    private AddNodeAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_wifi_4_mcu);
        lv = (ListView)findViewById(lv_NewNode);
        ListSSID4MCU = new ArrayList<>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getListWifi(ListWifiForMCU.this).execute("http://192.168.4.1/scan");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        adapter = new AddNodeAdapter(ListSSID4MCU,this);
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

    class getListWifi extends AsyncTask<String, String , String> {
        ProgressDialog progressDialog;
        Activity activity;
        public getListWifi(Activity activity){
            this.activity = activity;
            progressDialog = new ProgressDialog(activity);
        }
        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
//            String s = "{\"ssid\":[{\"id\":0,\"ssid\":\"FIMO_TRUONGSA\",\"rssi\":-54,\"isOpen\":false}," +
//                    "{\"id\":1,\"ssid\":\"PCNMang\",\"rssi\":-93,\"isOpen\":false}," +
//                    "{\"id\":2,\"ssid\":\"TP-LINK_627C\",\"rssi\":-83,\"isOpen\":false}," +
//                    "{\"id\":3,\"ssid\":\"FIMO_HOANGSA_518\",\"rssi\":-51,\"isOpen\":false}," +
//                    "{\"id\":4,\"ssid\":\"TTKDCLGD\",\"rssi\":-90,\"isOpen\":false}," +
//                    "{\"id\":5,\"ssid\":\"FIMO_HOANGSA_408\",\"rssi\":-79,\"isOpen\":false}," +
//                    "{\"id\":6,\"ssid\":\"PM208-G2\",\"rssi\":-92,\"isOpen\":true}]}";
//            return s;

        }

        @Override
        protected void onPreExecute() {
            progressDialog.setTitle("Progress start");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null){
                progressDialog.dismiss();
            }
//            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            try {
                JSONObject Data = new JSONObject(s);
                JSONArray ListSSID = Data.getJSONArray("ssid");
                for(int i=0;i<ListSSID.length();i++){
                    JSONObject Wifi = ListSSID.getJSONObject(i);
                    String SSID = Wifi.getString("ssid");
                    ListSSID4MCU.add(SSID);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();

        }

    }


    private static String docNoiDung_Tu_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }

    class loginDialog extends AlertDialog{
        String ssid;
        TextView SSID;
        EditText PassWord;
        CheckBox ShowPassWord;
        Button KetNoi,Huy;
        protected loginDialog(@NonNull Context context, String ssid) {
            super(context);
            this.ssid=ssid;
//        WindowManager.LayoutParams params = getContext().getWindow().getAttributes();
//        params.gravity = Gravity.TOP;
            final AlertDialog.Builder alertadd = new AlertDialog.Builder(getContext());
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
                    String url = "http://192.168.4.1/config?ssid="+SSID.getText().toString()+"&pwd="+PassWord.getText().toString();
                    new getListWifi(ListWifiForMCU.this).execute(url);
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
}


