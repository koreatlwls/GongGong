package com.example.gonggong;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/*
주변 시설 프래그먼트
 */
public class NearbyFacility extends Fragment {
    private Context mContext;
    private ExtendedFloatingActionButton fab_main,fab_sub1,fab_sub2,fab_sub3;
    private boolean isFabOpen=false; //Floating ActionButton의 상태
    private static final int freeFood=3,welfare=2,conStore=1;
    private static int showWhat=freeFood; //현재 표시하고 있는 시설
    private static int colorOrange;
    private static int colorLightSkyBlue;

    public NearbyFacility() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
        colorOrange=getResources().getColor(R.color.colorOrange);
        colorLightSkyBlue=getResources().getColor(R.color.colorLightSkyBlue);
        try {
            setUpMap();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void setUpMap() throws ExecutionException, InterruptedException {
        String ServiceKey = "kHfFtRQnsh8Dm3LJi8a82MF%2F5vsGDD%2BZQHrmRfLqPs%2F6MHeISttv1xd%2Bz%2Bs3ShfRYYBITs6aBtPLgRneYSoPHw%3D%3D";
        String url = "http://api.data.go.kr/openapi/tn_pubr_public_chil_wlfare_mlsv_api?serviceKey="+ServiceKey+"&pageNo=0&numOfRows=100&type=xml";
        mealcardApi meal = new mealcardApi(url);
        meal.execute();
        NodeList nList = meal.get();
        for (int temp = 0; temp <5; temp++){
            Node nNode = nList.item(temp);
            if(nNode.getNodeType()==Node.ELEMENT_NODE){
                Element eElement = (Element) nNode;
                Log.d("태그","가맹정명:"+getTagValue("mrhstNm",eElement));
            }
        }
    }
    private String getTagValue(String tag, Element eElement){
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if(nValue == null)
            return null;
        return nValue.getNodeValue();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_nearby_facility,container,false);
        fab_main=v.findViewById(R.id.fab_Main);
        fab_sub1=v.findViewById(R.id.fab_Sub1);
        fab_sub2=v.findViewById(R.id.fab_Sub2);
        fab_sub3=v.findViewById(R.id.fab_Sub3);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab_main.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });
        fab_sub1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               onFabClicked(R.id.fab_Sub1);
            }
        });
        fab_sub2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onFabClicked(R.id.fab_Sub2);
            }
        });
        fab_sub3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onFabClicked(R.id.fab_Sub3);
            }
        });
    }
    //Floating Action Button을 터치 시 서브 버튼들을 보여주거나 다시 숨긴다.
    private void toggleFab(){
        if(isFabOpen){
            ObjectAnimator.ofFloat(fab_sub1,"translationY",0f).start();
            ObjectAnimator.ofFloat(fab_sub2,"translationY",0f).start();
            ObjectAnimator.ofFloat(fab_sub3,"translationY",0f).start();
            fab_sub1.setClickable(false);
            fab_sub2.setClickable(false);
            isFabOpen = false;
        }
        else{
            ObjectAnimator.ofFloat(fab_sub1, "translationY", +210f).start();
            ObjectAnimator.ofFloat(fab_sub2, "translationY", +420f).start();
            ObjectAnimator.ofFloat(fab_sub3,"translationY",+630f).start();
            fab_sub1.setClickable(true);
            fab_sub2.setClickable(true);
            isFabOpen = true;
        }
    }
    private void onFabClicked(int id){
        fab_main.setBackgroundColor(colorOrange);
        switch(id){
            case R.id.fab_Sub1:
                if(showWhat!=conStore) {
                    fab_main.setText("편의점");
                    fab_main.setIcon(mContext.getDrawable(R.drawable.ic_store_white_24dp));
                    showWhat=conStore;
                    fab_sub1.setBackgroundColor(colorOrange);
                    fab_sub2.setBackgroundColor(colorLightSkyBlue);
                    fab_sub3.setBackgroundColor(colorLightSkyBlue);
                }
                break;
            case R.id.fab_Sub2:
                if(showWhat!=welfare) {
                    fab_main.setText("복지 센터");
                    fab_main.setIcon(mContext.getDrawable(R.drawable.ic_accessible_24px));
                    showWhat=welfare;
                    fab_sub1.setBackgroundColor(colorLightSkyBlue);
                    fab_sub2.setBackgroundColor(colorOrange);
                    fab_sub3.setBackgroundColor(colorLightSkyBlue);
                }
                break;
            case R.id.fab_Sub3:
                if(showWhat!=freeFood) {
                    fab_main.setText("무료급식소");
                    fab_main.setIcon(mContext.getDrawable(R.drawable.ic_local_dining_24px));
                    showWhat=freeFood;
                    fab_sub1.setBackgroundColor(colorLightSkyBlue);
                    fab_sub2.setBackgroundColor(colorLightSkyBlue);
                    fab_sub3.setBackgroundColor(colorOrange);
                }
                break;
        }
    }

}