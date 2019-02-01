package com.ogalo.partympakache.wanlive.app;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.duowan.mobile.netroid.Network;
import com.duowan.mobile.netroid.RequestQueue;
import com.duowan.mobile.netroid.cache.DiskCache;
import com.duowan.mobile.netroid.toolbox.FileDownloader;
import com.duowan.mobile.netroid.toolbox.ImageLoader;
import com.ogalo.partympakache.wanlive.utils.PreferenceUtils;
import com.ogalo.partympakache.wanlive.volley.LruBitmapCache;
import com.sendbird.android.SendBird;

//import com.vincestyling.netroid.stack.HttpClientStack;
//import com.vincestyling.netroid.stack.HttpStack;
//import com.vincestyling.netroid.stack.HurlStack;
//import com.vincestyling.netroid.toolbox.BasicNetwork;


import java.util.concurrent.Executor;

import static java.net.Proxy.Type.HTTP;


public class AppController extends Application {
    public static final String USER_AGENT = "netroid_sample";
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    private Network mNetwork;
    private DiskCache mDiskCache;
    private FileDownloader mFileDownloader;
//    private ImageLoader mImageLoader;
    static LruBitmapCache mLruBitmapCache;
	private static final String APP_ID = "1DBD9158-6017-402D-81A0-E1946A1AD546"; // US-1 Demo
	public static final String VERSION = "3.0.40";

    private static AppController mInstance;
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

    @Override
    public void onCreate() {
        super.onCreate();



        PreferenceUtils.init(getApplicationContext());

        SendBird.init(APP_ID, getApplicationContext());



        mInstance = this;
    }


}