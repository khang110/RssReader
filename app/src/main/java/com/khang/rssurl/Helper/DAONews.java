package com.khang.rssurl.Helper;

import static com.khang.rssurl.Adapter.NewsAdapter.TAG;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khang.rssurl.Model.RSSItem;
import com.khang.rssurl.Model.User;

public class DAONews {
    private DatabaseReference databaseReference;
    public DAONews(String path) {
        FirebaseDatabase db= FirebaseDatabase.getInstance();
        databaseReference = db.getReference(path);
    }

    public Task<Void> addNews(RSSItem rssItem) {
        return  databaseReference.push().setValue(rssItem);
    }
    public Task<Void> addUser(User user) {
        return  databaseReference.push().setValue(user);
    }


}
