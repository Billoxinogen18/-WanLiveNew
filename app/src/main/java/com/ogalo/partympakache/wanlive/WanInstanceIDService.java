package com.ogalo.partympakache.wanlive;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class WanInstanceIDService extends FirebaseInstanceIdService {

//We're overriding onTokenRefresh from the FirebaseInstanceIDService class so that we can implement it as our own
//Implementation which involves calling it immediately our Token is generated
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        //This is the token
        String token = FirebaseInstanceId.getInstance().getToken();



   //This is the token and it will be printent in your android studio's LogCat, by specifying the debug filter, then enable regex search and search for 'FCM_STRING'
        //This is generated everytime you uninstall or reinstall the app, you can place it in a public class, and store it in a public variable to access it in your project
        Log.d("FCM_TOKEN", token);
    }
}