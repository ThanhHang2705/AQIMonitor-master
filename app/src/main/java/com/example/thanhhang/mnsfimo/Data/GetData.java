package com.example.thanhhang.mnsfimo.Data;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.example.thanhhang.mnsfimo.R;

/**
 * Created by HP on 3/6/2017.
 */

public class GetData  extends ActionBarActivity {

    //Button bt;
    TextView txt;

    // Response
    String responseServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_from_localhost);

        txt = (TextView) findViewById(R.id.textView6);
//        bt = (Button) findViewById(R.id.sendData);
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AsyncT asyncT = new AsyncT();
//                asyncT.execute();
//            }
//        });
    }

    /* Inner class to get response */


}
