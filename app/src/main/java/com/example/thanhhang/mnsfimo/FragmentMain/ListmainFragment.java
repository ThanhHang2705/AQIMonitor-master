package com.example.thanhhang.mnsfimo.FragmentMain;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.thanhhang.mnsfimo.Activities.Detail;
import com.example.thanhhang.mnsfimo.Adapters.ListLoveAdapter;
import com.example.thanhhang.mnsfimo.Data.Database;
import com.example.thanhhang.mnsfimo.KQNode;
import com.example.thanhhang.mnsfimo.Love;
import com.example.thanhhang.mnsfimo.MainActivity;
import com.example.thanhhang.mnsfimo.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListmainFragment extends Fragment {
    ListView list;
    ListLoveAdapter adapter;
    private ArrayList<KQNode> listLove;
//    static ArrayList<Love> listLove= new ArrayList<>();
    static KQNode kqNode;
    static Love love;

    public ListmainFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_listmain, null);
        list = (ListView) view.findViewById(R.id.listLove);

        LayoutInflater factory = LayoutInflater.from(getContext());
        UpdateListLove();
        int size = listLove.size();
        //list.setAdapter(null);
        size = list.getCount();
        adapter = new ListLoveAdapter(listLove,getContext());
        list.setAdapter(adapter);
        size = list.getCount();
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
                        TextView PM = (TextView) list.getChildAt(position).findViewById(R.id.txt_pm);
                        String pm = PM.getText().toString();
                        int pm_int = Integer.parseInt(pm);
                        bundle.putInt("PM", Integer.parseInt(pm));
                        TextView diadiem = (TextView) list.getChildAt(positionToRemove).findViewById(R.id.txt_diadiem);
                        String NameNode = diadiem.getText().toString();
                        bundle.putString("Address",NameNode);
                        Intent intent = new Intent(getContext(), Detail.class);
                        intent.putExtra("TapTin", bundle);
                        startActivity(intent);
                    }
                });
                adb.setPositiveButton("Delete", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        view.setVisibility(View.GONE);
//                        listLove.remove(positionToRemove);
//                        adapter = new ListLoveAdapter(listLove,getContext());
//                        list.setAdapter(adapter);
                        TextView diadiem = (TextView) list.getChildAt(positionToRemove).findViewById(R.id.txt_diadiem);
                        String NameNode = diadiem.getText().toString();
                        ArrayList<KQNode> temp = ((MainActivity)getActivity()).getDataFromSQLite();
                        for (int i=0 ;i<temp.size();i++){
                            String nameNode = temp.get(i).getNameNode();
                            if(NameNode.equals(nameNode)){
//                                Toast.makeText(getContext(),temp.get(i).getID(),Toast.LENGTH_LONG).show();
                                deleteDataInFavourite(temp.get(i).getID());
                                break;
                            }
                        }
                        listLove.remove(positionToRemove);
                        UpdateListLove();
                        int size = listLove.size();
                        adapter = new ListLoveAdapter(listLove,getContext());
                        list.setAdapter(adapter);
                        ArrayList<Fragment> al = (ArrayList<Fragment>) getFragmentManager().getFragments();

                        Fragment currentFragment = al.get(1);
                        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                        fragTransaction.detach(currentFragment);
                        fragTransaction.attach(currentFragment);
                        fragTransaction.commit();


                    }});
                adb.show();


            }
        });
        return view;
    }

    public ArrayList<KQNode> getListLove(){
        return listLove;
    }

    public void UpdateListLove(){

        listLove = ((MainActivity)getActivity()).getFavouriteList();
    }

    public void UpdateAdapter(){
        adapter = new ListLoveAdapter(listLove,getContext());
        list.setAdapter(adapter);
    }

    public void deleteDataInFavourite(int ID){
        SQLiteDatabase database = Database.initDatabase(getActivity(),"List_Favourite.sqlite");
        Cursor cursor = database.rawQuery("SELECT * FROM Favourite", null);
        int size = cursor.getCount();
        database.delete("Favourite","ID = ?", new String[]{String.valueOf(ID)});
        cursor = database.rawQuery("SELECT * FROM Favourite", null);
        size = cursor.getCount();
        int i = 0;
    }
}
