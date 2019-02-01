package com.ogalo.partympakache.wanlive;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ogalo.partympakache.wanlive.utils.PreferenceUtils;
import com.ogalo.partympakache.wanlive.utils.PushUtils;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

public class LoginActivity extends AppCompatActivity {



    private EditText mLoginEmail;
    private EditText mLoginPassword;

    private Button mConnectButton;
    private Button regist_button;
    private ProgressDialog mLoginProgress;
    private ContentLoadingProgressBar mProgressBar;

    private boolean isMainLobbyStart=false;
    private ProgressDialog mRegProgress;


    private String user_id;

    private static Context mContext;
    private FirebaseAuth mAuth;
    private RelativeLayout mLoginLayout;

    private DatabaseReference mUserDatabase;
    private EditText mEditText;
    private String display_name;
    private String userId;
     private SweetAlertDialog pDialog=null;
    String userNickname;

    private int i = -1;
    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

      pDialog=null;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

//    pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);

//        pDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        mRegProgress = new ProgressDialog(this);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mLoginEmail=(EditText) findViewById(R.id.city);
        mLoginEmail.setText("");
        mLoginPassword=(EditText) findViewById(R.id.pass);
        mLoginPassword.setText("");


//        mLoginPassword.setText(PreferenceUtils.getUserId());
        mLoginEmail.setText(PreferenceUtils.getNickname());


        mConnectButton=(Button)findViewById(R.id.login_button);



        mLoginLayout = (RelativeLayout) findViewById(R.id.layout_logina);
        regist_button=(Button)findViewById(R.id.logina);

        regist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginSignUp.class));
            }
        });


        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {









                String email = mLoginEmail.getText().toString();
                String password = mLoginPassword.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){

//                    final SweetAlertDialog pDialog;
                   pDialog=new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);

//                    mRegProgress.setTitle("Logging in");
//                    mRegProgress.setMessage("Checking in");
//                    mRegProgress.setCanceledOnTouchOutside(false);
//                    mRegProgress.show();
//                     pDialog = new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.PROGRESS_TYPE);
//                    pDialogf.getProgressHelper().setBarColor(Color.parseColor("#B21AAC"));
                    pDialog.setTitleText("Logging In");
                    pDialog.setContentText("Setting Profile");
                    pDialog.setCancelable(false);
                    pDialog.show();


                    new CountDownTimer(800 * 7, 800) {
                        public void onTick(long millisUntilFinished) {
                            // you can change the progress bar color by ProgressHelper every 800 millis
                            i++;
                            switch (i){
                                case 0:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(android.R.color.holo_red_light));
                                    break;
                                case 1:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorAccent));
                                    break;
                                case 2:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(android.R.color.holo_blue_light));
                                    break;
                                case 3:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(android.R.color.holo_orange_light));
                                    break;
                                case 4:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.facebook));
                                    break;
                                case 5:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.google_plus));
                                    break;
                                case 6:
                                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.ok_light));
                                    break;
                            }
                        }

                        public void onFinish() {
                            i = -1;
                            pDialog.setTitleText("Success!")
                                    .setConfirmText("OK")
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }
                    }.start();







                    loginUser(pDialog,email, password);


                    String useridnew=mLoginEmail.getText().toString()+mLoginPassword.getText().toString()+user_id;



//                userId = mLoginPassword.getText().toString();
                    userId=useridnew;
                    userNickname = mLoginEmail.getText().toString();


                    userId = userId.replaceAll("\\s", "");



                    PreferenceUtils.setUserId(userId);
                    PreferenceUtils.setNickname(userNickname);



                }

                else
                {

                    mLoginEmail.setHint("Please Enter Email");

                }

            }
        });




        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar_login);

    }



    private void connectToSendBird(final String userId, final SweetAlertDialog sweet) {
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
                    mRegProgress.dismiss();
                    sweet.dismissWithAnimation();



                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("Login to WanLive failed")
                            .setConfirmText("Okay")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();




//                    showSnackbar("Login to WanLive failed");
                    mConnectButton.setEnabled(true);
                    PreferenceUtils.setConnected(false);
                    return;
                }

else
                {
                PreferenceUtils.setNickname(user.getNickname());
                PreferenceUtils.setProfileUrl(user.getProfileUrl());
                PreferenceUtils.setConnected(true);

                // Update the user's nickname
//                updateCurrentUserInfo(userNickname);
                updateCurrentUserPushToken();
                    sweet.dismissWithAnimation();
                // Proceed to MainActivity
                Intent intent = new Intent(getApplicationContext(), WanMaps.class);

                startActivity(intent);

                finish();


            }

            }
        });
    }





    private void loginUser(final SweetAlertDialog dialog, String email, String password) {


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){


                    String current_user_id = mAuth.getCurrentUser().getUid();

                   setUser_id(current_user_id);

                    String deviceToken = FirebaseInstanceId.getInstance().getToken();

                    mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

//                            Intent mainIntent = new Intent(LoginActivity.this, WanMaps.class);
//                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(mainIntent);
//                            finish();
//                            mRegProgress.dismiss();

//                                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(mainIntent);
                            connectToSendBird(userId,dialog);

                        }
                    });




                } else {

                   dialog.dismissWithAnimation();

                    String task_result = task.getException().getMessage().toString();

//                    Toast.makeText(LoginActivity.this, "Error : " + task_result, Toast.LENGTH_LONG).show();
//                    showSnackbar("WanLive is not connected");

                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("WanLive cannot connect to your account")
                            .setConfirmText("Okay")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                }

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


    private void setUser_id(String user_id)
    {
        this.user_id=user_id;


    }

}
