package com.khang.rssurl.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.khang.rssurl.Adapter.FavoriteNewsAdapter;
import com.khang.rssurl.Adapter.NewsAdapter;
import com.khang.rssurl.Helper.DAONews;
import com.khang.rssurl.Helper.RSSParser;
import com.khang.rssurl.Model.RSSItem;
import com.khang.rssurl.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private ProgressBar pDialog;
    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<>();

    RSSParser rssParser = new RSSParser();

    TextInputEditText textInputEditText;
    Button btRead;
    List<RSSItem> rssItems = new ArrayList<>();
    ListView lv;
    RelativeLayout relativeLayout;
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_DESCRIPTTION = "description";
    SharedPreferences sharedPreferences;
    AutoCompleteTextView completeTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences = getActivity().getSharedPreferences("idUser", MODE_PRIVATE);
//        String link = sharedPreferences.getString("rssLink", "");
//        Log.d("LINK: ", link + " ");
//        if (link.isEmpty() == false) {
//            new LoadRSSFeedItems().execute(link);
//        }
//        textInputEditText = view.findViewById(R.id.etUrl);
        completeTextView = view.findViewById(R.id.comTV);
        ArrayList<String> history = new ArrayList<>();
        history.add("feeds.feedburner.com/tinhte/");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(),android.R.layout.select_dialog_item,history);
        completeTextView.setThreshold(1);//will start working from first character
        completeTextView.setAdapter(adapter);
        btRead = view.findViewById(R.id.btnRss);
        lv = view.findViewById(R.id.listview);
        relativeLayout = view.findViewById(R.id.relativeLayout);
//"https://feeds.feedburner.com/tinhte/"
        btRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (completeTextView.getText().toString().isEmpty() == false) {
                    String rss_link = "https://" + completeTextView.getText().toString().trim();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("rssLink", rss_link);
                    editor.commit();
                    new LoadRSSFeedItems().execute(rss_link);
                    completeTextView.setText("");
                } else {
                    Toast.makeText(getContext(), "Please, input url", Toast.LENGTH_SHORT).show();
                }

            }
        });



        return view;
    }
    public class LoadRSSFeedItems extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            pDialog.setLayoutParams(lp);
            pDialog.setVisibility(View.VISIBLE);
            relativeLayout.addView(pDialog);
        }

        @Override
        protected String doInBackground(String... args) {
            // rss link url
            String rss_url = args[0];

            // list of rss items
            rssItems = rssParser.getRSSFeedItems(rss_url);

            // looping through each item
//            for (RSSItem item : rssItems) {
//                // creating new HashMap
//                if (item.getLink().toString().equals(""))
//                    break;
//                HashMap<String, String> map = new HashMap<String, String>();
//
//                // adding each child node to HashMap key => value
//                String givenDateString = item.getPubdate().trim();
//
//                map.put(TAG_TITLE, item.getTitle());
//                map.put(TAG_LINK, item.getLink());
//                map.put(TAG_PUB_DATE, givenDateString); // If you want parse the date
//                map.put(TAG_DESCRIPTTION, item.getDescription());
//                // adding HashList to ArrayList
//                rssItemList.add(map);
//            }

            // updating UI from Background Thread
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(
                            getContext(),
                            rssItemList, R.layout.row_item_list,
                            new String[]{TAG_LINK, TAG_TITLE, TAG_PUB_DATE, TAG_DESCRIPTTION},
                            new int[]{R.id.page_url, R.id.title, R.id.pub_date, R.id.description});

                    // updating listview
                    NewsAdapter adapter1 = new NewsAdapter(getContext(), R.layout.row_item_list, rssItems);
                    lv.setAdapter(adapter1);

                }
            });
            return null;
        }

        protected void onPostExecute(String args) {
            pDialog.setVisibility(View.GONE);
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("favoriteList", (ArrayList<RSSItem>) rssItems);

        Log.d("Data : ", "1");
        super.onSaveInstanceState(outState);

    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {

        String link = sharedPreferences.getString("rssLink", "");
        if (link.isEmpty() == false) {
            new LoadRSSFeedItems().execute(link);
        }
        super.onViewStateRestored(savedInstanceState);
    }

}