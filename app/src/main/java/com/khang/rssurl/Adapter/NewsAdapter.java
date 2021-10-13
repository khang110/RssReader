package com.khang.rssurl.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.khang.rssurl.Helper.DAONews;
import com.khang.rssurl.Model.RSSItem;
import com.khang.rssurl.R;


import java.util.List;

public class NewsAdapter extends BaseAdapter {

    public NewsAdapter(Context myContext, int myLayout, List<RSSItem> listRss) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.listRss = listRss;
    }

    Context myContext;
    int myLayout;
    List<RSSItem> listRss;

    @Override
    public int getCount() {
        return listRss.size();
    }

    @Override
    public Object getItem(int i) {
        return listRss.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    DAONews daoNews = new DAONews(RSSItem.class.getSimpleName());
    public static String TAG = "Vừa mới click: ";
    boolean isPlay = false;
    SharedPreferences sharedPreferences;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(myLayout, null);
        sharedPreferences = myContext.getSharedPreferences("idUser", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        RSSItem item = listRss.get(i);
        item.setIdUser(id);
        TextView name = view.findViewById(R.id.title);
        TextView pubDate = view.findViewById(R.id.pub_date);
        TextView link = view.findViewById(R.id.page_url);
        TextView des = view.findViewById(R.id.description);
        ImageView like = view.findViewById(R.id.favorite);

        name.setText(item.getTitle());
        pubDate.setText(item.getPubdate());
        link.setText(item.getLink());
        des.setText(item.getDescription());


        like.setImageResource(R.drawable.ic_outline_favorite_border_24);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlay) {
                    like.setImageResource(R.drawable.ic_outline_favorite_border_24);
                } else {
                    like.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
                isPlay = !isPlay;
                daoNews.addNews(item).addOnSuccessListener(suc-> {
                    Toast.makeText(myContext, "Saved", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er->{
                    Toast.makeText(myContext, er.getMessage(), Toast.LENGTH_SHORT).show();
                });

            }
        });



        return view;
    }

}
