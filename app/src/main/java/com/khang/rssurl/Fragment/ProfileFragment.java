package com.khang.rssurl.Fragment;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khang.rssurl.LoginActivity;
import com.khang.rssurl.MainActivity;
import com.khang.rssurl.Model.RSSItem;
import com.khang.rssurl.Model.User;
import com.khang.rssurl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    MaterialButton materialButton;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    CircleImageView circleImageView;
    private  static  String token;
    TextView name, gender, email, hometown;

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

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        FacebookSdk.sdkInitialize(getApplicationContext());
        name = view.findViewById(R.id.name);
        gender = view.findViewById(R.id.gender);
        email = view.findViewById(R.id.email);
        hometown = view.findViewById(R.id.hometown);
        circleImageView = view.findViewById(R.id.profile_image);
        callbackManager = CallbackManager.Factory.create();
        materialButton = view.findViewById(R.id.loginCustomButton);

//        printKeyHash();
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        // If using in a fragment
        loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, loginResultFacebookCallback());
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                loginButton.performClick();
                SharedPreferences preferences = getActivity().getSharedPreferences("idUser", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("rssLink");
                editor.remove("id");
                editor.commit();
                LoginManager.getInstance().logOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();


            }
        });

        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {

                Log.d("onCompleted: ", jsonObject.toString());
                try {
                    String pic = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                    String UserName = jsonObject.getString("name");
//                    String Gender = jsonObject.getString("gender");
//                    String Hometown = jsonObject.getJSONObject("hometown").getString("name");
//                    String Email = jsonObject.getString("email");
                    String Id = jsonObject.getString("id");
                    String token = AccessToken.getCurrentAccessToken().getToken();
//                            Log.d("TOKEN NE: ", token);
//                            Log.d("INFOR USER", name + ", " + gender+ ", " +hometown+ ", " +email+ ", " +id );


                    name.setText(UserName);
                    gender.setText("Male");
                    email.setText("example@gmail.com");
                    hometown.setText("Hồ Chí Minh");

                    Glide.with(getApplicationContext())
                            .load("https://graph.facebook.com/" + Id+ "/picture?type=large")
                            .into(circleImageView);
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("fields", " id, name, picture.width(150).height(150), email, gender, hometown");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();



        return view;
    }

    private FacebookCallback<LoginResult> loginResultFacebookCallback() {
        FacebookCallback<LoginResult> result = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

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
        return result;
    }


    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo("com.khang.rssurl", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }
}