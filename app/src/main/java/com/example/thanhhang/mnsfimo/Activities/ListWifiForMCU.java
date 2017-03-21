package com.example.thanhhang.mnsfimo.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
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
        PassWord = (EditText)findViewById(R.id.pass_wifi);
        ListSSID4MCU = new ArrayList<>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getListWifi().execute("http://192.168.4.1/scan");
                try {
                    Thread.sleep(2000);
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
            }
        });
        PassWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String url = "http://192.168.4.1/config?ssid="+SSID+"&pwd="+PassWord.getText().toString();
                            new getListWifi().execute(url);
                        }
                    });
                }

                return false;
            }
        });


    }

    class getListWifi extends AsyncTask<String, String , String> {

        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
//            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            try {
                JSONObject Data = new JSONObject(s);
                JSONArray ListSSID = Data.getJSONArray("ssid");
                for(int i=0;i<ListSSID.length();i++){
                    JSONObject Wifi = ListSSID.getJSONObject(i);
                    String SSID = Wifi.getString("ssid");
                    ListSSID4MCU.add(SSID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


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


}


