package com.khang.rssurl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.khang.rssurl.Helper.DAONews;
import com.khang.rssurl.Model.RSSItem;
import com.khang.rssurl.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    MaterialButton materialButton;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    DAONews daoNews = new DAONews(User.class.getSimpleName());
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        updateWithToken(AccessToken.getCurrentAccessToken());
        sharedPreferences = getSharedPreferences("idUser", MODE_PRIVATE);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, loginResultFacebookCallback());
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        materialButton = findViewById(R.id.loginCustomButton);

        // Callback registration

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();

            }
        });
    }

    private void updateWithToken(AccessToken currentAccessToken) {
        if (currentAccessToken != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private FacebookCallback<LoginResult> loginResultFacebookCallback() {
        FacebookCallback<LoginResult> loginResult = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("Are u ok>", loginResult.toString());
                GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {

                        Log.d("onCompleted: ", jsonObject.toString());
                        try {
                            String pic = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                            String name = jsonObject.getString("name");
//                            String gender = jsonObject.getString("gender");
//                            String hometown = jsonObject.getJSONObject("hometown").getString("name");
//                            String email = jsonObject.getString("email");
                            String id = jsonObject.getString("id");
                            String token = loginResult.getAccessToken().getToken();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("id", id);
                            editor.commit();

                            User user = new User(id, name, "email", "Male", "Hồ Chí Minh", pic, token);
                            daoNews.addUser(user).addOnSuccessListener(suc-> {
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                            }).addOnFailureListener(er->{
                                Log.d("ERR: ", er.toString());
                                Toast.makeText(LoginActivity.this, er.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("LỖI: ", e.toString());
                        }

                    }
                });
                Bundle bundle = new Bundle();
                bundle.putString("fields", " id, name, gender, picture.width(150).height(150), email, hometown");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                // App codeLog.d("Are u ok>", "Ok");
                Log.d("CANCEL", "YES");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("HUY", "ROI: " +  exception);
            }
        };
        return loginResult;
    };
}