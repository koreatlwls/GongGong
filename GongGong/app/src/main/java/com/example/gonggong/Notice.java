package com.example.gonggong;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Notice extends Fragment {
    ListView listview;
    NoticeViewAdapter adapter;
    Elements titleContents, centerContents,title;
    String[] nums = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private static String[] centers = new String[10];
    private static String[] titles = new String[10];

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_notice,container,false);

        adapter = new NoticeViewAdapter();
        listview = (ListView) v.findViewById(R.id.noticeListView);
        listview.setAdapter(adapter);
        try {
            getData();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int cnt =0;
        for (Element element : titleContents) {
            String lenCheck = element.text()+"\n";
            if(lenCheck.length() > 20) {
                lenCheck = lenCheck.substring(0, 20);
                lenCheck += " ...";
                titles[cnt] = lenCheck;
            }
            else titles[cnt] = element.text()+"\n";
            cnt++;
            if (cnt == 10) // 10개만 파싱
                break;
        }
        for(int i=0;i<10;i++){
            adapter.addItem(nums[i], centers[i], titles[i]);
        }
        listview.clearChoices() ;
        adapter.notifyDataSetChanged();

        return v;
    }
    private void getData() throws ExecutionException, InterruptedException {
        NoticeJsoup jsoupAsyncTask = new NoticeJsoup();
        jsoupAsyncTask.execute();
        title = jsoupAsyncTask.get();
    }
    private class NoticeJsoup extends AsyncTask<Void, Void, Elements> {
        // AsyncTask객체 생성
        @Override
        protected Elements doInBackground(Void... voids) {
            Document doc = null;
            NodeList nlist = null;
            try {
                doc = Jsoup.connect("https://www.ion.or.kr/center/news/notice/1#").get();
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

            return titleContents;
        }
    }
}