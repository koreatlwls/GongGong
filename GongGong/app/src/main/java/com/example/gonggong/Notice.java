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

import com.google.android.gms.common.util.Strings;

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

    Elements titleContents, centerContents;
    Document doc = null;
    String[] nums = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[] centers = new String[10];
    String[] titles = new String[10];

    ListView listView;
    NoticeViewAdapter adapter;


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

        adapter = new NoticeViewAdapter();
        listView = (ListView) view.findViewById(R.id.noticeListView);
        listView.setAdapter(adapter);

        new AsyncTask() { // AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    doc = Jsoup.connect("https://www.ion.or.kr/center/news/notice/1#").get(); // 페이지를 불러옴
                    titleContents = doc.select(".left a"); // 셀렉터로 class가 left인 값 중 a태그의 값을 불러옴
                    centerContents = doc.select(".center a");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int cnt = 0;
                for (Element element : titleContents) {
                    titles[cnt] = element.text()+"\n";
                    cnt++;
                    if (cnt == 10) // 10개만 파싱
                        break;
                }
                cnt = 0;
                for(Element element : centerContents) {
                    centers[cnt] = element.text()+"\n";
                    cnt++;
                    if(cnt == 10) break;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Log.i("TAG", "" + titles[0]);
            }
        }.execute();


        for(int i = 0; i < 10; i++) {
            adapter.addItem(nums[i], centers[i], titles[i]);
            //Log.i("TAG", "" + titles[i]);
        }

        listView.clearChoices() ;
        adapter.notifyDataSetChanged();

        return view;
    }

}