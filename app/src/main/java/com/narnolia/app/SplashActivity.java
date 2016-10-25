package com.narnolia.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Admin on 24-10-2016.
 */

public class SplashActivity extends AbstractActivity implements Runnable {

    private Handler threadRunnable;
    private int time = 2000;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        try {
            mContext = this;

            threadRunnable = new Handler();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Start your app main activity
        pushActivity(SplashActivity.this,LoginActivity.class,null,true);
        // close this activity
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (threadRunnable != null) {
                threadRunnable.postDelayed(this, time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            if (threadRunnable != null) {
                threadRunnable.removeCallbacks(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
