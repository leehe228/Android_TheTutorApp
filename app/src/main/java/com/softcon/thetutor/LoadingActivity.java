package com.softcon.thetutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadingActivity extends AppCompatActivity {

    /* Shared Preference (DATABASE) */
    public final String PREFERENCE = "com.studio572.samplesharepreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        /* 로고 애니메이션 */
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
        ImageView imgview = (ImageView)findViewById(R.id.imageview_logo);
        imgview.startAnimation(animation);

        /* 인터넷 연결 체크 Handler */
        Handler hand = new Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                /* 자동 로그인 정보 불러오기 */
                SharedPreferences pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                String result = pref.getString("autoLogin", "");

                /* 인터넷 연결 정상 */
                if(Get_Internet(LoadingActivity.this) == 1 || Get_Internet(LoadingActivity.this) == 2){
                    /* 자동 로그인 */
                    if(result.equals("true")){
                        Intent mainintent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainintent);
                        finish();
                    }
                    /* 자동 로그인 안될 시 */
                    else {
                        Intent intent = new Intent(getApplicationContext(), HelloActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                /* 인터넷 연결 안되는 경우 */
                else if(Get_Internet(LoadingActivity.this) == 0){
                    Intent intent = new Intent(getApplicationContext(), NoInternetActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }

    /* 인터넷 연결 여부 체크 메소드 */
    public static int Get_Internet(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            /* 와이파이로 연결된 경우 */
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return 1;
            }
            /* 셀룰러로 연결된 경우 */
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return 2;
            }
        }
        /* 연결 안된 경우 */
        return 0;
    }
}


