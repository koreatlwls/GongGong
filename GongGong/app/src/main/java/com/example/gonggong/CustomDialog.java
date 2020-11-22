package com.example.gonggong;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class CustomDialog {
    int position;
    private Double latitude;
    private Double longitude;
    private String name;
    private String address;
    private int code;
    private Context context;
    private DbOpenHelper mDbOpenHelper;
    private ListViewAdapter listViewAdapter;
    public CustomDialog(Context context, String name, String address, Double latitude, Double longitude, int code, int pos, ListViewAdapter listViewAdapter) {
        this.context = context;
        this.name=name;
        this.address=address;
        this.latitude=latitude;
        this.longitude=longitude;
        this.code=code;
        position=pos;
        this.listViewAdapter=listViewAdapter;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.

        final Button Button1 = (Button) dlg.findViewById(R.id.btnShowDetailDialog);
        final Button Button2 = (Button) dlg.findViewById(R.id.btnDeleteDialog);
        final Button Button3 = (Button) dlg.findViewById(R.id.btnCloseDialog);
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(code!=NearbyFacility.welfare) {
                    Intent intent = new Intent(context, DetailInfo.class);
                    intent.putExtra("name", name);
                    intent.putExtra("address", address);
                    intent.putExtra("code", code);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    context.startActivity(intent);
                }
                else
                    Toast.makeText(context,"복지관은 상세정보를 조회할 수 없습니다.",Toast.LENGTH_LONG).show();
                dlg.dismiss();
            }
        });
        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbOpenHelper=new DbOpenHelper(context);
                mDbOpenHelper.open();
                mDbOpenHelper.create();
                mDbOpenHelper.deleteColumn(address);
                listViewAdapter.removeItem(position);
                listViewAdapter.notifyDataSetChanged();
                dlg.dismiss();
            }
        });
        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
}

