package com.kingbogo.kblogview.demo;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kingbogo.logview.LogView;
import com.kingbogo.logview.listener.LogPanelListener;
import com.kingbogo.logview.util.LogUtil;

public class MainActivity extends AppCompatActivity implements Handler.Callback, LogPanelListener, View.OnClickListener {

    public static final String TAG = "MainActivity";

    private static final int WHAT_ADD_LOG = 100;

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogView.getInstance().init(this, BuildConfig.DEBUG);

        findViewById(R.id.main_click_tv).setOnClickListener(this);

        mHandler = new Handler(this);
        mHandler.sendEmptyMessageDelayed(WHAT_ADD_LOG, 1000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogView.getInstance().setArea("AAA", "BBB", "CCC123123", "ASDFASDFSADF", "546546546546", "021");
        LogView.getInstance().setTipsInfo("1、AAAAA; <br/>2、BBBB; ");
        LogView.getInstance().setPanelListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d("_onStart()......");
        LogView.getInstance().attach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d("_onStop()......");
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
            mHandler.sendEmptyMessageDelayed(WHAT_ADD_LOG, 2000);
        }
        return false;
    }

    @Override
    public void onClickLogPanel(int position) {
        LogUtil.d(TAG, "position: " + position);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.main_click_tv) {
            startActivity(new Intent(this, SecondActivity.class));

            //            LogView.getInstance().addTipsInfoItem("10、Hello，你好！");

        }
    }
}
