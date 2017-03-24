package com.example.thanhhang.mnsfimo.FragmentMain;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.thanhhang.mnsfimo.Activities.RangeSeekBar;
import com.example.thanhhang.mnsfimo.Activities.ResultActivity;
import com.example.thanhhang.mnsfimo.KQNode;
import com.example.thanhhang.mnsfimo.MainActivity;
import com.example.thanhhang.mnsfimo.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    String NameOfAddress;
    String[] AddressToFind;
    List<Address> address;
    LatLng CurrentLatLng;
    Spinner choose_pm,choose_time;
    private Drawable thumb;
    public SearchFragment() {
        // Required empty public constructor
    }
    EditText Address;
    ListView ListAddress;
    ArrayAdapter arrarAdapter;
    ArrayList<KQNode> ListNode;
    ArrayList<String> ListResult;
    int max_temperature, min_temperature, max_humidity, min_humidity, min_pm, max_pm;
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

        min_pm =0;
        min_temperature=0;
        max_temperature=50;
        min_humidity =0;
        max_humidity=100;
        ListNode = new ArrayList<>();
        UpdateListNode();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.pm25, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        choose_pm.setAdapter(adapter);
        choose_pm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==1){
                    min_pm=0;

                }else if(position==2){
                    min_pm=35;

                }else if(position==3){
                    min_pm=75;

                }else if(position==4){
                    min_pm=115;

                }else if(position==5){
                    min_pm=150;

                }else if(position==6){
                    min_pm=250;

                }else if(position==7){
                    min_pm=350;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
        temperature.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                max_temperature= maxValue;
                min_temperature = minValue;
//                Toast.makeText(getContext(),min_temperature+" "+max_temperature,Toast.LENGTH_SHORT).show();
            }
        });


        humidity.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                max_humidity= maxValue;
                min_humidity = minValue;
            }
        });
        Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address.setText("");
                findPlace();

            }
        });



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
            MyFindPlace();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("Result", ListResult);
            Intent intent = new Intent(getActivity(),ResultActivity.class);
            intent.putExtra("TapTin", bundle);
            startActivity(intent);
//            GoogleMap googleMap = null;
//            String s = googleMap.getProjection().getVisibleRegion().toString();
//            startActivity(new Intent(getActivity(), ResultActivity.class));
            getCompleteAddressString(ListNode.get(0).getLatLng().latitude, ListNode.get(0).getLatLng().longitude);
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

                List<Integer> typePlace;
                Address.setText(place.getAddress());
                NameOfAddress = place.getName().toString();
                AddressToFind = place.getAddress().toString().split(", ");
                CurrentLatLng = place.getLatLng();
//                getCompleteAddressString(latLng.latitude,latLng.longitude);

//                String address = place.get

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = null;
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
//                    strAdd.add(returnedAddress.getAddressLine(i));
                }
                strAdd=strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    public void UpdateListNode(){
        ListNode = new ArrayList<>();
        ListNode = ((MainActivity)getActivity()).getDataFromSQLite();
    }

    public void MyFindPlace(){
        ListResult = new ArrayList<>();
        if(CurrentLatLng!=null){
            double latitude = CurrentLatLng.latitude;
            double longTitude = CurrentLatLng.longitude;
            Location currentLocation = new Location("A");

            currentLocation.setLongitude(longTitude);
            currentLocation.setLatitude(latitude);
            for(int i=0;i<ListNode.size();i++){
                String s=null;
                Location AnotherLocation = new Location("B");
                latitude=ListNode.get(i).getLatLng().latitude;
                longTitude=ListNode.get(i).getLatLng().longitude;
                int PM = (int) Math.round(Double.parseDouble(ListNode.get(i).getPM()));
                String Node=getCompleteAddressString(latitude,longTitude);
                int temperature = ListNode.get(i).getTemperature();
                int humidity = ListNode.get(i).getHumidity();
                for(int j=0;j<2;j++){
                    String a = AddressToFind[j];
                    if(Node.contains(a)){
                        s = ListNode.get(i).getID()+"\n"+ListNode.get(i).getNameNode()+"\n"+
                                ListNode.get(i).getAddress()+"\n"+PM+"\n"+humidity+"\n"+
                                temperature+"\n"+ListNode.get(i).getLatLng().latitude+"\n"+
                                ListNode.get(i).getLatLng().longitude;
                        ListResult.add(s);
                        break;
                    }
                }
                if(s==null){
                    AnotherLocation.setLatitude(latitude);
                    AnotherLocation.setLongitude(longTitude);
                    double distance = currentLocation.distanceTo(AnotherLocation);
                    if(distance<=3000){
                        PM = (int) Math.round(Double.parseDouble(ListNode.get(i).getPM()));
                        if ( PM>min_pm){
                            if(temperature<=max_temperature && temperature>=min_temperature){
                                if(humidity<=max_humidity && humidity>=min_humidity){
                                    s = ListNode.get(i).getID()+"\n"+ListNode.get(i).getNameNode()+"\n"+
                                            ListNode.get(i).getAddress()+"\n"+PM+"\n"+humidity+"\n"+
                                            temperature+"\n"+ListNode.get(i).getLatLng().latitude+"\n"+
                                            ListNode.get(i).getLatLng().longitude;

                                    ListResult.add(s);
                                }
                            }
                        }
                    }
                }
            }

        }else{
            for(int i=0;i<ListNode.size();i++){

                int PM = (int) Math.round(Double.parseDouble(ListNode.get(i).getPM()));
                if ( PM>min_pm){
                    int temperature = ListNode.get(i).getTemperature();
                    if(temperature<=max_temperature && temperature>=min_temperature){
                        int humidity = ListNode.get(i).getHumidity();
                        if(humidity<=max_humidity && humidity>=min_humidity){
                            String s = ListNode.get(i).getID()+"\n"+ListNode.get(i).getNameNode()+"\n"+
                                    ListNode.get(i).getAddress()+"\n"+PM+"\n"+humidity+"\n"+
                                    temperature+"\n"+ListNode.get(i).getLatLng().latitude+"\n"+
                                    ListNode.get(i).getLatLng().longitude;
                            ListResult.add(s);
                        }
                    }
                }
            }

        }

    }

    public void RemoveNameFromAddress(){
//        String[] s = AddressToFind.split(NameOfAddress);
//        AddressToFind = s[1];
//        NameOfAddress = s[0];
    }
}
