package com.example.gonggong;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

public class BottomSheetDialog extends BottomSheetDialogFragment  {
    private ImageButton btnShowDetail;
    private ImageButton btnClose;
    private ImageView classifyImage;
    private TextView bottomSheetClass;
    private TextView bottomSheetName;
    private TextView textAddress;
    private String name;
    private String address;
    private int code;
    public BottomSheetDialog(String name,String address,int code){
        this.name=name;
        this.code=code;
        this.address=address;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet, container, false);
        btnShowDetail = v.findViewById(R.id.btnShowDetail);
        btnClose = v.findViewById(R.id.btnCloseBottomSheet);
        classifyImage=v.findViewById(R.id.bottomSheetImg);
        bottomSheetClass=v.findViewById(R.id.bottomSheetClass);
        bottomSheetName=v.findViewById(R.id.bottomSheetName);
        textAddress=v.findViewById(R.id.address);
        bottomSheetName.setText(name);
        textAddress.setText(address);
        if(code==NearbyFacility.conStore) {
            bottomSheetClass.setText("급식카드 가맹점");
            classifyImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_store_white_24dp));
        }
        else if(code==NearbyFacility.freeFood) {
            bottomSheetClass.setText("무료 급식소");
            classifyImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_local_dining_24px));
        }
        else {
            bottomSheetClass.setText("복지센터");
            classifyImage.setImageDrawable(getContext().getDrawable(R.drawable.ic_accessible_24px));
        };

        btnShowDetail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),DetailInfo.class);
                intent.putExtra("name",name);
                intent.putExtra("code",code);
                startActivity(intent);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }

}
