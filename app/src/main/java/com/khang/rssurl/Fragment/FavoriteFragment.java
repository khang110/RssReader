package com.khang.rssurl.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.khang.rssurl.Adapter.FavoriteNewsAdapter;
import com.khang.rssurl.Helper.DAONews;
import com.khang.rssurl.Model.RSSItem;
import com.khang.rssurl.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    ListView listView;
    FavoriteNewsAdapter favoriteNewsAdapter;
    List<RSSItem> rssItems;
    private static String TAG = "DatabaseRealtime";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    FirebaseDatabase db= FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    ArrayList<RSSItem> cityList;
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        sharedPreferences = getActivity().getSharedPreferences("idUser", MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "");
        listView = view.findViewById(R.id.lvFavorite);
        rssItems = new ArrayList<>();
        favoriteNewsAdapter = new FavoriteNewsAdapter(getContext(), R.layout.row_item_favorite, rssItems);
        listView.setAdapter(favoriteNewsAdapter);

        getData(id);

        return view;
    }
    private void getData(String id) {
        Query query = db.getReference().child("RSSItem").orderByChild("idUser").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cityList = new ArrayList<>();
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    RSSItem user1 = userSnapshot.getValue(RSSItem.class);
                    rssItems.add(user1);
                }
                favoriteNewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("Data change: ", "yêu bông");
    }


}