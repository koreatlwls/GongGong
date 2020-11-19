package com.example.gonggong;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.internal.PlaceEntity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class DetailInfo extends AppCompatActivity implements OnMapReadyCallback  {
    private static NodeList constoreList;
    private static NodeList welfareList;
    private static NodeList freefoodList;
    private static String name;
    private Double latitude;
    private Double longitude;
    private GoogleMap googleMap=null;
    String ServiceKey = "kHfFtRQnsh8Dm3LJi8a82MF%2F5vsGDD%2BZQHrmRfLqPs%2F6MHeISttv1xd%2Bz%2Bs3ShfRYYBITs6aBtPLgRneYSoPHw%3D%3D";

    private String getTagValue(String tag, Element eElement) {
        try {
            NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
            Node nValue = (Node) nlList.item(0);
            if (nValue == null)
                return null;
            return nValue.getNodeValue();
        } catch(NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_freefood);
        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        int code = intent.getExtras().getInt("code");
        latitude=intent.getExtras().getDouble("latitude");
        longitude=intent.getExtras().getDouble("longitude");
        Button button = null;
        ImageButton buttonBack = null;
        SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map2);
        if(supportMapFragment!=null)
            supportMapFragment.getMapAsync(this);
        button=findViewById(R.id.btnFindWay);
        buttonBack=findViewById(R.id.btnBack);


        switch (code) {
            case NearbyFacility.conStore:
                //편의점
                String constoreurl = "http://api.data.go.kr/openapi/tn_pubr_public_chil_wlfare_mlsv_api?serviceKey=" + ServiceKey + "&pageNo=0&numOfRows=1&type=xml&mrhstNm=" + name;
                OpenApi openapi = new OpenApi(constoreurl);
                openapi.execute();
                try {
                    constoreList = openapi.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                Node nNode = constoreList.item(0);
                showConStoreDetail(nNode);
                break;
            case NearbyFacility.freeFood:
                //무료급식소
                String freefoodurl = "http://api.data.go.kr/openapi/tn_pubr_public_free_mlsv_api?serviceKey=" + ServiceKey + "&pageNo=0&numOfRows=1&type=xml&fcltyNm=" + name;
                OpenApi openapi2 = new OpenApi(freefoodurl);
                openapi2.execute();
                try {
                    freefoodList = openapi2.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                Node nNode2 = freefoodList.item(0);
                showFreeFoodDetail(nNode2);
                break;
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:"+latitude+","+longitude+"?q="+name));
                startActivity(intent);
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void showFreeFoodDetail(Node node){
        TextView classify=findViewById(R.id.textClass);
        Element element;
        TextView[] textViews=new TextView[7];
        textViews[0]=findViewById(R.id.facilityName);
        textViews[1]=findViewById(R.id.address);
        textViews[2]=findViewById(R.id.phoneNumber);
        textViews[3]=findViewById(R.id.foodLocation);
        textViews[4]=findViewById(R.id.forWhom);
        textViews[5]=findViewById(R.id.foodDay);
        textViews[6]=findViewById(R.id.foodTime);
        String[] temp = new String[7];
        classify.setText("무료 급식소");
        if(node.getNodeType()==Node.ELEMENT_NODE) {
            element = (Element) node;
            temp[0] = getTagValue("fcltyNm", element);
            temp[1] = getTagValue("rdnmadr", element);
            temp[2] = getTagValue("phoneNumber", element);
            temp[3] = getTagValue("mlsvPlace", element);
            temp[4] = getTagValue("mlsvTrget", element);
            temp[5] = getTagValue("mlsvDate", element);
            temp[6] = getTagValue("mlsvTime", element);
            for(int i=0;i<7;i++)
                if(temp[i]!=null)
                    textViews[i].setText(temp[i]);

        }
    }
    private void showConStoreDetail(Node node){
        TextView classify=findViewById(R.id.textClass);
        Element element;
        TextView[] textViews=new TextView[7];
        textViews[0]=findViewById(R.id.facilityName);
        textViews[1]=findViewById(R.id.address);
        textViews[2]=findViewById(R.id.phoneNumber);
        textViews[3]=findViewById(R.id.foodLocation);
        textViews[4]=findViewById(R.id.forWhom);
        textViews[5]=findViewById(R.id.foodDay);
        textViews[6]=findViewById(R.id.foodTime);
        Drawable timeImage=this.getDrawable(R.drawable.ic_schedule_24px);
        ImageView imageView1=findViewById(R.id.imgFoodDay);
        ImageView imageView2=findViewById(R.id.imgFoodTime);
        ImageView imageView3=findViewById(R.id.imgForWhom);
        ImageView imageView4=findViewById(R.id.imgFoodLocation);
        imageView1.setImageDrawable(timeImage);
        imageView2.setImageDrawable(timeImage);
        imageView3.setImageDrawable(timeImage);
        imageView4.setImageDrawable(timeImage);
        String[] temp=new String[7];
        classify.setText("급식카드 가맹점");
        if(node.getNodeType()==Node.ELEMENT_NODE){
            element=(Element) node;
            temp[0]=getTagValue("mrhstNm",element);
            temp[1]=getTagValue("rdnmadr",element);
            temp[2]=getTagValue("phoneNumber",element);
            temp[3]="평일 시작 "+getTagValue("weekdayOperOpenHhmm",element)+" 평일 종료 "+getTagValue("weekdayOperColseHhmm",element);
            temp[4]="주말 시작 "+getTagValue("satOperOperOpenHhmm",element)+" 주말 종료 "+getTagValue("satOperCloseHhmm",element);
            temp[5]="공휴일 시작 "+getTagValue("holidayOperOpenHhmm",element)+" 공휴일 종료 "+getTagValue("holidayCloseOpenHhmm",element);
            temp[6]="배달 시작 "+getTagValue("dlvrOperOpenHhmm",element)+" 배달 종료 "+getTagValue("dlvrCloseOpenHhmm",element);
            for(int i=0;i<7;i++)
                if(temp[i]!=null)
                    textViews[i].setText(temp[i]);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
        LatLng latLng=new LatLng(latitude,longitude);
        MarkerOptions markerOptions=new MarkerOptions().position(latLng).title(name);
        this.googleMap.addMarker(markerOptions);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14f));


    }
}


