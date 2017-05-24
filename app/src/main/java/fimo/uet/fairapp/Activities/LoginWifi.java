package fimo.uet.fairapp.Activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import fimo.uet.fairapp.Progress_Dialog.ConnectFairBox_Progress;
import fimo.uet.fairapp.R;

/**
 * Created by HP on 3/28/2017.
 */

public class LoginWifi extends AlertDialog{
    String ssid;
    TextView SSID;
    EditText PassWord;
    CheckBox ShowPassWord;
    Button KetNoi,Huy;
    Boolean isMCU = null;
    Bundle bundle;
    public LoginWifi(final Activity activity, String ssid, final Bundle bundle) {
        super(activity);
        this.ssid=ssid;
        this.bundle = bundle;
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
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                ConnectFairBox_Progress connectFairBox_progress =
                        new ConnectFairBox_Progress(activity,SSID.getText().toString(),PassWord.getText().toString(), bundle);
                connectFairBox_progress.execute();
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//                activity.finish();

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




     class IsMCU extends AsyncTask<String, String, String>{


         @Override
         protected String doInBackground(String... params) {
             URL url = null;
             try {
                 url = new URL("http://192.168.4.1/scan");
                 try {
                     URLConnection urlConnection = url.openConnection();
                     urlConnection.connect();
                     isMCU = true;
                 } catch (IOException e) {
                     isMCU = false;
                     e.printStackTrace();
                 }
             } catch (MalformedURLException e) {
                 e.printStackTrace();
             }
             return null;
         }

         // create a urlconnection object


    }



    public String formatSSID(String ssid){
        String s="";
        char[]chars = ssid.toCharArray();
        for (int i=0;i<chars.length;i++){
            if(chars[i]!='"'){
                s+=chars[i];
            }
        }
        return s;
    }
}
