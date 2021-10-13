package com.khang.rssurl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.khang.rssurl.Helper.DAONews;
import com.khang.rssurl.Model.RSSItem;
import com.khang.rssurl.R;

import java.util.List;

public class FavoriteNewsAdapter extends BaseAdapter {
    public FavoriteNewsAdapter(Context myContext, int myLayout, List<RSSItem> listRss) {
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(myLayout, null);

        RSSItem item = listRss.get(i);

        TextView name = view.findViewById(R.id.title);
        TextView pubDate = view.findViewById(R.id.pub_date);
        TextView link = view.findViewById(R.id.page_url);
        TextView des = view.findViewById(R.id.description);

        name.setText(item.getTitle());
        pubDate.setText(item.getPubdate());
        link.setText(item.getLink());
        des.setText(item.getDescription());

        return view;
    }
}
