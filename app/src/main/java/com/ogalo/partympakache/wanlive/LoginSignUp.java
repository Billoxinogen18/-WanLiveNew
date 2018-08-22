package com.ogalo.partympakache.wanlive;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ogalo.partympakache.wanlive.app.AppController;
import com.ogalo.partympakache.wanlive.data.WanItem;
import com.ogalo.partympakache.wanlive.groupchannel.GroupChannelActivity;
import com.ogalo.partympakache.wanlive.utils.PreferenceUtils;
import com.ogalo.partympakache.wanlive.utils.PushUtils;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LoginSignUp extends AppCompatActivity {
    private String URL_FEED = "http://www.wayawaya.co.ke/wayawaya.co.ke/bill/wanlive/wanlive_thebalanceofdestiny.json";



    private static final String TAG = WanMaps.class.getSimpleName();

    private List<WanItem> markerItems=new ArrayList<WanItem>();;

    private DatabaseReference mDatabase;
    private RelativeLayout mLoginLayout;
    private EditText mDisplayName, mEmail;
    private Button mConnectButton;
    private Button login;
    private ContentLoadingProgressBar mProgressBar;
    private String user_id;

    private Button mCreateBtn;
    private Button loginActiv;
    private Button lognActivity;
    private EditText mPassword;


    private Toolbar mToolbar;
    private String userId;
    String userNickname;
    private ProgressDialog mRegProgress;





    private SweetAlertDialog pDialog;
    //ProgressDialog


    //Firebase Auth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);
















        FirebaseMessaging.getInstance().setAutoInitEnabled(true);





        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(ConstantsClass.CHANNEL_ID, ConstantsClass.CHANNEL_NAME, importance);
            mChannel.setDescription(ConstantsClass.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }


        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);




        mRegProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mLoginLayout = (RelativeLayout) findViewById(R.id.layout_logina);
        login=(Button)findViewById(R.id.logina);
       mPassword = (EditText) findViewById(R.id.pass);
       mPassword.setText("");
        mDisplayName = (EditText) findViewById(R.id.username);
        mEmail = (EditText) findViewById(R.id.city);


        mPassword.setText(PreferenceUtils.getUserId());
        mEmail.setText(PreferenceUtils.getNickname());


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        mConnectButton = (Button) findViewById(R.id.login_button);
        mConnectButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {









                String display_name = mDisplayName.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){

//                    mRegProgress.setTitle("Registering User");
//                    mRegProgress.setMessage("Creating Profile");
//                    mRegProgress.setCanceledOnTouchOutside(false);
//                    mRegProgress.show();

                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#B21AAC"));
                    pDialog.setTitleText("Registering User");
                    pDialog.setContentText("Creating Profile");
                    pDialog.setCancelable(false);
                    pDialog.show();
                    register_user(display_name, email, password);


//                userId = mPassword.getText().toString();

                    String useridnew=mEmail.getText().toString()+mPassword.getText().toString()+user_id;



//                userId = mLoginPassword.getText().toString();
                    userId=useridnew;

                    userNickname = mEmail.getText().toString();
                    // Remove all spaces from userID
                    userId = userId.replaceAll("\\s", "");







                }
                else
                {

                    mDisplayName.setHint("Please Enter Your Username!");
                    mEmail.setHint("Input a Valid e-mail");
                    mPassword.setHint("Input a password");
                }

            }
        });

        mDisplayName.setSelectAllOnFocus(true);
        mEmail.setSelectAllOnFocus(true);

        // A loading indicator
        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar_login);

        // Display current SendBird and app versions in a TextView


//        WanNotificationManager.getInstance(this).displayNotification("WanLive", "WanLive Update");


    }

    @Override
    protected void onStart() {
        super.onStart();

        mPassword.setText("");
        mEmail.setText("");




        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(PreferenceUtils.getConnected()&&currentUser!=null) {
//            mRegProgress.setTitle("Retrieving State");
//            mRegProgress.setMessage("Loading Profile");
//            mRegProgress.setCanceledOnTouchOutside(false);
//            mRegProgress.show();
//            SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#B21AAC"));
            pDialog.setTitleText("Retrieving State");
            pDialog.setCancelable(false);
            pDialog.show();

            connectToSendBird(PreferenceUtils.getUserId(), PreferenceUtils.getNickname());
        }
    }

    /**
     * Attempts to connect a user to SendBird.
     * @param userId    The unique ID of the user.
     * @param userNickname  The user's nickname, which will be displayed in chats.
     */
    private void connectToSendBird(final String userId, final String userNickname) {
        // Show the loading indicator
        showProgressBar(true);
        mConnectButton.setEnabled(false);

        ConnectionManager.login(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                // Callback received; hide the progress bar.
                showProgressBar(false);

                if (e != null) {
                    // Error!
                    Toast.makeText(
                            getApplicationContext(), "" + e.getCode() + ": " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show login failure snackbar
//                    mRegProgress.dismiss();
                    showSnackbar("Login to WanLive failed");
                    mConnectButton.setEnabled(true);
                    PreferenceUtils.setConnected(false);
                    return;
                }

                PreferenceUtils.setNickname(user.getNickname());
                PreferenceUtils.setProfileUrl(user.getProfileUrl());
                PreferenceUtils.setConnected(true);

                // Update the user's nickname
                updateCurrentUserInfo(userNickname);
                updateCurrentUserPushToken();

                // Proceed to MainActivity
                WanItem wanItem=new WanItem();

                Intent intent = new Intent(getApplicationContext(), WanMaps.class);
                intent.putExtra("latitude",wanItem.getLatitude());
                intent.putExtra("longitude", wanItem.getLongitude());


                startActivity(intent);
                pDialog.dismissWithAnimation();
//                mRegProgress.dismiss();
                finish();
            }
        });
    }

    /**
     * Update the user's push token.
     */
    private void updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(getApplicationContext(), null);
    }

    /**
     * Updates the user's nickname.
     * @param userNickname  The new nickname of the user.
     */
    private void updateCurrentUserInfo(final String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(
                            getApplicationContext(), "" + e.getCode() + ":" + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show update failed snackbar
                    showSnackbar("Update user nickname failed");

                    return;
                }

                PreferenceUtils.setNickname(userNickname);
            }
        });
    }

    // Displays a Snackbar from the bottom of the screen
    private void showSnackbar(String text) {
        Snackbar snackbar = Snackbar.make(mLoginLayout, text, Snackbar.LENGTH_SHORT);

        snackbar.show();
    }

    // Shows or hides the ProgressBar
    private void showProgressBar(boolean show) {
        if (show) {
            mProgressBar.show();
        } else {
            mProgressBar.hide();
        }
    }


    private void register_user(final String display_name, final String email, final String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){


                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    String ud=uid+password;
                    String userd = ud.replaceAll("\\s", "");
                    PreferenceUtils.setUserId(userd);
                    PreferenceUtils.setNickname(email);

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("status", "Hi there I'm using WanLive on the go App.");
                    userMap.put("image", "default");
                    userMap.put("favs", "0");
                    userMap.put("thumb_image", "default");
                    userMap.put("device_token", device_token);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {



                            if(task.isSuccessful()){
                                pDialog.dismissWithAnimation();
//                                mRegProgress.dismiss();

//                                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(mainIntent);
                                connectToSendBird(userId, userNickname);


                            }

                        }
                    });


                } else {
//                    mRegProgress.hide();
                    pDialog.dismissWithAnimation();
                    Toast.makeText(getApplicationContext(), "Cannot Sign in. Please check the form and try again.", Toast.LENGTH_LONG).show();

                }

            }
        });

    }













    private void parseJsonFeed(JSONObject response) {
        try {

            mRegProgress.setTitle("WanLive");
            mRegProgress.setMessage("Loading Feed");

            mRegProgress.setCanceledOnTouchOutside(false);
            mRegProgress.show();
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                WanItem item = new WanItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));
//                Toast.makeText(this, ischecked, Toast.LENGTH_SHORT).show();
                item.setCost(feedObj.getString("cost"));

                item.setTimes(feedObj.getString("matime"));
                item.setStatus(feedObj.getString("status"));

                item.setRating(feedObj.getString("rating"));
                item.setLatitude(feedObj.getString("latitude"));
                item.setLongitude(feedObj.getString("longitude"));

                String longitudef=feedObj.getString("latitude");
//                 String longituded=feedObj.getString("longitude");
//                Toast.makeText(this, item.getLatitude(), Toast.LENGTH_SHORT).show();



                String name=feedObj.getString("name");
                String longitude=feedObj.getString("longitude");




                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);

                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("location");
                item.setUrl(feedUrl);
                String io=Integer.toString(i);


                markerItems.add(item);
            }

            // notify data changes to list adapater

            mRegProgress.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUser_id(String user_id)
    {
        this.user_id=user_id;


    }
}


