package com.example.gonggong;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.ArrayList;


public class  Settings extends Fragment {
    ListView listView;
    ListViewAdapter listViewAdapter;
    private DbOpenHelper mDbOpenHelper;
    public Settings() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbOpenHelper = new DbOpenHelper(getContext());
        mDbOpenHelper.open();
        mDbOpenHelper.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_settings,container,false);
        listViewAdapter=new ListViewAdapter();
        listView=(ListView)v.findViewById(R.id.listView);
        listView.setAdapter(listViewAdapter);
        showList(listViewAdapter);

        return v;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                ListViewItem item=(ListViewItem) parent.getItemAtPosition(i);
                CustomDialog customDialog=new CustomDialog(getContext(),item.getName()
                ,item.getAddress(),item.getLatitude(),item.getLongitude(),item.getCode(),i,listViewAdapter);
                customDialog.callFunction();
            }
        });
    }

        private void showList(ListViewAdapter listViewAdapter){
        Cursor iCursor=mDbOpenHelper.sortColumn("_id");
        Drawable Icon;
        while(iCursor.moveToNext()){
            String tempName=iCursor.getString(iCursor.getColumnIndex("facname"));
            String tempAddr=iCursor.getString(iCursor.getColumnIndex("facaddr"));
            int tempCode=iCursor.getInt(iCursor.getColumnIndex("code"));
            double tempLatitude=iCursor.getDouble(iCursor.getColumnIndex("latitude"));
            double tempLongitude=iCursor.getDouble(iCursor.getColumnIndex("longitude"));
            int code= tempCode=iCursor.getInt(iCursor.getColumnIndex("code"));
            switch(tempCode) {
                case NearbyFacility.conStore:
                    Icon= ContextCompat.getDrawable(getContext(),R.drawable.ic_store_white_24dp);
                    Icon.setTint(ContextCompat.getColor(getContext(),R.color.colorOrange));
                    listViewAdapter.addItem(Icon,tempName,tempAddr,tempLatitude,tempLongitude,code);
                    break;
                    case NearbyFacility.freeFood:
                        Icon=ContextCompat.getDrawable(getContext(),R.drawable.ic_local_dining_24px);
                        Icon.setTint(ContextCompat.getColor(getContext(),R.color.colorOrange));
                        listViewAdapter.addItem(Icon,tempName,tempAddr,tempLatitude,tempLongitude,code);
                        break;
                        case NearbyFacility.welfare:
                            Icon=ContextCompat.getDrawable(getContext(),R.drawable.ic_accessible_24px);
                            Icon.setTint(ContextCompat.getColor(getContext(),R.color.colorOrange));
                            listViewAdapter.addItem(Icon,tempName,tempAddr,tempLatitude,tempLongitude,code);
                            break;
            }
        }
    }
}