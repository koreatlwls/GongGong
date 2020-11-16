package com.example.gonggong;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
/*
공지사항 프래그먼트
 */

public class Notice extends Fragment {

    private Button getBtn;
    private TextView result;
    Elements contents;
    Document doc = null;
    String Top10;


    public Notice() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notice, container, false);

        result = (TextView) view.findViewById(R.id.result);
        getBtn = (Button)view.findViewById(R.id.getBtn);


        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask() { // AsyncTask객체 생성
                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                            doc = Jsoup.connect("https://www.ion.or.kr/center/news/notice/1#").get(); // 페이지를 불러옴
                            contents = doc.select(".left a"); // 셀렉터로 class가 left인 값 중 a태그의 값을 불러옴
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Top10 = "";
                        int cnt = 0;
                        for(Element element: contents) {
                            cnt++;
                            Top10 += cnt+". "+element.text() + "\n";
                            if(cnt == 10) // 10개만 파싱
                                break;
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        Log.i("TAG",""+Top10);
                        result.setText(Top10);
                    }
                }.execute();
            }
        });
        return view;
    }
}