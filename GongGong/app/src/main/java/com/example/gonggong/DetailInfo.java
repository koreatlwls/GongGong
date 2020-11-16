package com.example.gonggong;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
        setContentView(R.layout.detail);
        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        int code = intent.getExtras().getInt("code");
        Log.d("태그",name);
        switch (code) {
            case 1:
                //편의점
                String constoreurl = "http://api.data.go.kr/openapi/tn_pubr_public_chil_wlfare_mlsv_api?serviceKey=" + ServiceKey + "&pageNo=0&numOfRows=1&type=xml&mrhstNm=" + name;
                OpenApi openapi = new OpenApi(constoreurl);
                openapi.execute();
                try {
                    constoreList = openapi.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Node nNode = constoreList.item(0);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    Log.d("태그", "가맹정명:" + getTagValue("mrhstNm", eElement));
                }
                break;
            case 2:
                //복지
                String welfareurl = "http://api.data.go.kr/openapi/tn_pubr_public_oldnddspsnprt_carea_api?serviceKey=" + ServiceKey + "&pageNo=0&numOfRows=1&type=xml&trgetFcltyNm="+name;
                OpenApi openapi1 = new OpenApi(welfareurl);
                openapi1.execute();
                try {
                    welfareList = openapi1.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Node nNode1 = welfareList.item(0);
                if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode1;
                    Log.d("태그", "가맹정명:" + getTagValue("trgetFcltyNm", eElement));
                }
                break;
            case 3:
                //무료급식소
                String freefoodurl = "http://api.data.go.kr/openapi/tn_pubr_public_free_mlsv_api?serviceKey=" + ServiceKey + "&pageNo=0&numOfRows=1&type=xml&fcltyNm=" + name;
                OpenApi openapi2 = new OpenApi(freefoodurl);
                openapi2.execute();
                try {
                    freefoodList = openapi2.get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Node nNode2 = freefoodList.item(0);
                if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode2;
                    Log.d("태그", "가맹정명:" + getTagValue("fcltyNm", eElement));
                }
                break;
        }
    }
}


