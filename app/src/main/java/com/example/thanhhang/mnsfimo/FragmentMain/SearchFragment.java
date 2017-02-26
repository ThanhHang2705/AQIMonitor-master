package com.example.thanhhang.mnsfimo.FragmentMain;


import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.thanhhang.mnsfimo.Activities.ResultActivity;
import com.example.thanhhang.mnsfimo.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private GoogleApiClient mGoogleApiClient;
    Button btx_timNangCao;
    CrystalRangeSeekbar temperature, humidity;
    TextView temperature_min, temperature_max, humidity_min, humidity_max;
    private Drawable thumb;
    public SearchFragment() {
        // Required empty public constructor
    }
    EditText Address;
    ListView ListAddress;
    ArrayAdapter arrarAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Address = (EditText)view.findViewById(R.id.search_address);
        ListAddress = (ListView)view.findViewById(R.id.result_4_basic_search);
        btx_timNangCao = (Button) view.findViewById(R.id.btx_timNangCao);
        btx_timNangCao.setOnClickListener(On_Click_nangcao);
        temperature = (CrystalRangeSeekbar) view.findViewById(R.id.temperature);
        humidity = (CrystalRangeSeekbar) view.findViewById(R.id.humidity);
        temperature_min = (TextView)view.findViewById(R.id.temperature_min);
        temperature_max = (TextView)view.findViewById(R.id.temperature_max);
        humidity_min = (TextView)view.findViewById(R.id.humidity_min);
        humidity_max = (TextView)view.findViewById(R.id.humidity_max);
        temperature.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                temperature_min.setText(String.valueOf(minValue));
                temperature_max.setText(String.valueOf(maxValue));
//                tvMin.setText(String.valueOf(minValue));
//                tvMax.setText(String.valueOf(maxValue));
            }

        });

// set final value listener
        temperature.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });

        humidity.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                humidity_min.setText(String.valueOf(minValue));
                humidity_max.setText(String.valueOf(maxValue));
//                tvMin.setText(String.valueOf(minValue));
//                tvMax.setText(String.valueOf(maxValue));
            }

        });

// set final value listener
        humidity.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });
//        arrarAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,mGoogleApiClient);

//        temperature.setMax(50);
//        humidity.setMax(100);
//        temperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int progress_value;
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                progress_value = progress;
//                final Toast toast = Toast.makeText(getContext(),String.valueOf(progress_value),Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 500);
//                toast.show();
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        toast.cancel();
//                    }
//                }, 0);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                final Toast toast = Toast.makeText(getContext(),String.valueOf(progress_value),Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 500);
//                toast.show();
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        toast.cancel();
//                    }
//                }, 100);
//            }
//        });

//        humidity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int progress_value;
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                progress_value = progress;
//                final Toast toast = Toast.makeText(getContext(),String.valueOf(progress_value),Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 600);
//                toast.show();
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        toast.cancel();
//                    }
//                }, 00);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                final Toast toast = Toast.makeText(getContext(),String.valueOf(progress_value),Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 600);
//                toast.show();
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        toast.cancel();
//                    }
//                }, 100);
//            }
//        });
        return view;
    }

    public View.OnClickListener On_Click_nangcao = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getActivity(), ResultActivity.class));
        }
    };


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
