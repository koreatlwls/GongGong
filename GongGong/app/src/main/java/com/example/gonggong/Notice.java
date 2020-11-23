package com.example.gonggong;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Notice extends Fragment implements Runnable {
    ListView listview;
    NoticeViewAdapter adapter;
    Elements titleContents, centerContents, dateContents, title;
    String[] nums = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private static String[] centers = new String[10];
    private static String[] titles = new String[10];
    private static String[] dates = new String[10];
    private static String[] links = new String[10];
    private static com.example.gonggong.ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Thread th=new Thread(Notice.this);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        th.start();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_notice,container,false);

        adapter = new NoticeViewAdapter();
        listview = (ListView) v.findViewById(R.id.noticeListView);
        listview.setAdapter(adapter);

        return v;
    }
    private void getData() throws ExecutionException, InterruptedException {
        NoticeJsoup jsoupAsyncTask = new NoticeJsoup();
        jsoupAsyncTask.execute();
        title = jsoupAsyncTask.get();
    }

    @Override
    public void run() {
        try {
            getData();
            for(int i=0;i<10;i++){
                adapter.addItem(nums[i], centers[i], titles[i], dates[i], links[i]); }
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private class NoticeJsoup extends AsyncTask<Void, Void, Elements> {
        // AsyncTask객체 생성
        @Override
        protected void onPreExecute(){

        }
        @Override
        protected Elements doInBackground(Void... voids) {
            Document doc = null;
            NodeList nlist = null;
            try {
                doc = Jsoup.connect("https://www.ion.or.kr/center/news/notice/1#").get();
                titleContents = doc.select(".left a"); // 셀렉터로 class가 left인 값 중 a태그의 값을 불러옴
                centerContents = doc.select(".center a");
                dateContents = doc.select(".listTable td").next().next().next().next();
                int cnt = 0;
                for (Element element : titleContents) {
                    links[cnt] = "https://www.ion.or.kr"+element.attr("onclick").substring(12).replace("');","");
                    Log.i("title parse end",links[cnt]);
                    titles[cnt++] = element.text();
                    if (cnt == 10) // 10개만 파싱
                        break;
                }

                cnt = 0;
                for(Element element : centerContents) {
                    centers[cnt++] = element.text();
                    if(cnt == 10) break;
                }

                cnt = 0;
                for (Element element : dateContents) {
                    dates[cnt++] = element.text();
                    if (cnt == 10) // 10개만 파싱
                        break;
                    Log.i("dateparse end",dates[cnt-1]);
                }
                return titleContents;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}