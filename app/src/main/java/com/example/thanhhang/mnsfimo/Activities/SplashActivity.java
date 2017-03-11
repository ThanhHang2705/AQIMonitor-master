package com.example.thanhhang.mnsfimo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.thanhhang.mnsfimo.MainActivity;
import com.example.thanhhang.mnsfimo.R;

public class SplashActivity extends AppCompatActivity {
    ProgressBar progressBar;
    private int status = 0;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(10);
        progressBar.setMax(100);
        getSupportActionBar().hide();
        final Thread myThread = new Thread(){
            @Override
            public void run() {
                while(status<100) {
                    try {
                        status +=10;
                        Thread.sleep(1000);
                        progressBar.setProgress(status);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    super.run();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }


        };

        myThread.start();
    }
    }