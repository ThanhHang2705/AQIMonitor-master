package com.example.thanhhang.mnsfimo.FragmentMain;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thanhhang.mnsfimo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroPMFragment extends Fragment {

    public IntroPMFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro_pm, container, false);
    }

}
