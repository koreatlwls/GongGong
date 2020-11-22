package com.example.gonggong;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class NoticeViewAdapter extends BaseAdapter {
    private ArrayList<NoticeViewItem> noticeViewItemList = new ArrayList<>() ;

    public NoticeViewAdapter(){

    }
    @Override
    public int getCount() {
        return noticeViewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return noticeViewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.noticeview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView numView = (TextView)convertView.findViewById(R.id.num);
        TextView centerView = (TextView)convertView.findViewById(R.id.center);
        TextView titleView = (TextView)convertView.findViewById(R.id.title);
        TextView dateView = (TextView)convertView.findViewById(R.id.date);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final NoticeViewItem noticeViewItem = noticeViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        numView.setText(noticeViewItem.getNum());
        titleView.setText(noticeViewItem.getTitle());
        centerView.setText(noticeViewItem.getCenter());
        dateView.setText(noticeViewItem.getDate());

        LinearLayout noticeitem = (LinearLayout)convertView.findViewById(R.id.noticeitem);
        noticeitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(noticeViewItem.getLink()));
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    public void addItem(String num, String center, String title, String date, String link) {
        NoticeViewItem item = new NoticeViewItem();
        item.setNum(num);
        item.setCenter(center);
        item.setTitle(title);
        item.setDate(date);
        item.setLink(link);
        noticeViewItemList.add(item);
    }


}
