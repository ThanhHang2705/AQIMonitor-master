package com.example.thanhhang.mnsfimo.FragmentMain;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Spinner;

import com.example.thanhhang.mnsfimo.Activities.RangeSeekBar;
import com.example.thanhhang.mnsfimo.Activities.ResultActivity;
import com.example.thanhhang.mnsfimo.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;


import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private GoogleApiClient mGoogleApiClient;
    Button btx_timNangCao, btx_reset;
    //CrystalRangeSeekbar  humidity;
    RangeSeekBar<Integer>  humidity,temperature;

    Spinner choose_pm,choose_time;
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
        btx_reset = (Button) view.findViewById(R.id.btx_reset);
        temperature = (RangeSeekBar<Integer>) view.findViewById(R.id.temperature);
        humidity = (RangeSeekBar<Integer>) view.findViewById(R.id.humidity);

        choose_pm = (Spinner)view.findViewById(R.id.choose_pm);
        choose_time = (Spinner)view.findViewById(R.id.choose_time);
        temperature.setRangeValues(0,50);
        humidity.setRangeValues(0,100);
        temperature.setColorFilter(Color.BLACK);




        temperature.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
                public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {

            }
        });

        Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address.setText("");
                findPlace();

            }
        });

// set final value listener


//        humidity.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number minValue, Number maxValue) {
//                humidity_min.setText(String.valueOf(minValue));
//                humidity_max.setText(String.valueOf(maxValue));
////                tvMin.setText(String.valueOf(minValue));
////                tvMax.setText(String.valueOf(maxValue));
//            }
//
//        });



// set final value listener
//        humidity.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
//            @Override
//            public void finalValue(Number minValue, Number maxValue) {
//                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
//            }
//        });
//        arrarAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,mGoogleApiClient);

//
        btx_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address.setText("");
                choose_time.setSelection(0);
                choose_pm.setSelection(0);
                temperature.resetSelectedValues();
                humidity.resetSelectedValues();
                humidity.setRight(100);
            }
        });
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

    public void findPlace() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getActivity());
            startActivityForResult(intent, 1);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());


                Address.setText(place.getAddress());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

}
