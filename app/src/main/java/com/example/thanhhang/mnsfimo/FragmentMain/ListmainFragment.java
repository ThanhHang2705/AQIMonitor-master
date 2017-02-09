package com.example.thanhhang.mnsfimo.FragmentMain;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thanhhang.mnsfimo.Activities.Detail;
import com.example.thanhhang.mnsfimo.Adapters.ListLoveAdapter;
import com.example.thanhhang.mnsfimo.Love;
import com.example.thanhhang.mnsfimo.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListmainFragment extends Fragment {
    ListView list;
    ListLoveAdapter adapter;
    static ArrayList<Love> listLove= new ArrayList<>();
    static Love love;
    public ListmainFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ListmainFragment(Love love) {
        // Required empty public constructor
        this.love = love;
        if(love!=null){
            listLove.add(love);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       /* LayoutInflater factory = LayoutInflater.from(getContext());*/
        final View view = inflater.inflate(R.layout.fragment_listmain, null);
        /*View view =inflater.inflate(R.layout.fragment_listmain, container, false);*/
        /*listLove.add(new Love("Công viên cầu giấy",90,60,27));*/

        list = (ListView) view.findViewById(R.id.listLove);
        LayoutInflater factory = LayoutInflater.from(getContext());

        adapter = new ListLoveAdapter(listLove,getContext());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final int position1 = position;
                AlertDialog.Builder adb=new AlertDialog.Builder(getContext());
                adb.setTitle("Detail Or Delete");
                final int positionToRemove = position;
                adb.setNegativeButton("Detail", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("PM", love.getAqi());
                        Intent intent = new Intent(getContext(), Detail.class);
                        intent.putExtra("TapTin", bundle);
                        startActivity(intent);
                    }
                });
                adb.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        view.setVisibility(View.GONE);
                        listLove.remove(positionToRemove);
                    }});
                adb.show();


            }
        });
        return view;
    }

}
