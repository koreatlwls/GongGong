package com.example.gonggong;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.concurrent.ExecutionException;

public class DetailInfo extends AppCompatActivity {
    private static NodeList constoreList;
    private static NodeList welfareList;
    private static NodeList freefoodList;
    String ServiceKey = "kHfFtRQnsh8Dm3LJi8a82MF%2F5vsGDD%2BZQHrmRfLqPs%2F6MHeISttv1xd%2Bz%2Bs3ShfRYYBITs6aBtPLgRneYSoPHw%3D%3D";

    private String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null)
            return null;
        return nValue.getNodeValue();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_freefood);
        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        int code = intent.getExtras().getInt("code");
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
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                }
                break;
            case NearbyFacility.welfare:
                //복지
                String welfareurl = "http://api.data.go.kr/openapi/tn_pubr_public_oldnddspsnprt_carea_api?serviceKey=" + ServiceKey + "&pageNo=0&numOfRows=1&type=xml&trgetFcltyNm="+name;
                OpenApi openapi1 = new OpenApi(welfareurl);
                openapi1.execute();
                try {
                    welfareList = openapi1.get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                Node nNode1 = welfareList.item(0);
                if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode1;
                }
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
            temp[1]= getTagValue("rdnmadr",element);
            temp[2]=getTagValue("phoneNumber",element);
            temp[3]=getTagValue("mlsvPlace",element);
            temp[4]=getTagValue("mlsvTrget",element);
            temp[5]= getTagValue("mlsvDate",element);
            temp[6]=getTagValue("mlsvTime",element);
            for(int i=0;i<7;i++)
                if(temp[i]!=null)
                    textViews[i].setText(temp[i]);

        }
    }
}


