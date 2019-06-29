package com.kingbogo.kblogview.demo;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kingbogo.logview.LogView;
import com.kingbogo.logview.listener.LogPanelListener;

public class MainActivity extends AppCompatActivity implements Handler.Callback, LogPanelListener {

    public static final String TAG = "MainActivity";

    private static final int WHAT_ADD_LOG = 100;

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogView.getInstance().init(this, BuildConfig.DEBUG);

        mHandler = new Handler(this);
        mHandler.sendEmptyMessageDelayed(WHAT_ADD_LOG, 500);

        LogView.getInstance().setArea("AAA", "BBB", "CCC123123", "ASDFASDFSADF", "546546546546", "021");

        LogView.getInstance().setTipsInfo("1、AAAAA; <br/>2、BBBB; <br/>3、CCCC;");

        LogView.getInstance().setPanelListener(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        LogView.getInstance().attach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogView.getInstance().detach(this);
    }

    @Override
    protected void onDestroy() {
        LogView.getInstance().release();
        super.onDestroy();
    }

    @Override
    public boolean handleMessage(Message msg) {
        int what = msg.what;
        if (what == WHAT_ADD_LOG) {
            mHandler.removeMessages(WHAT_ADD_LOG);
            LogView.getInstance().addLog(TAG, String.valueOf(System.currentTimeMillis()));
            mHandler.sendEmptyMessageDelayed(WHAT_ADD_LOG, 1000);
        }
        return false;
    }

    @Override
    public void onClickLogPanel(int position) {
        Log.d(TAG, "position: " + position);
    }
}
