package com.kingbogo.kblogview.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kingbogo.logview.LogView;
import com.kingbogo.logview.util.LogUtil;

/**
 * <p>
 * </p>
 *
 * @author Kingbo
 * @date 2019/6/29
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // LogView.getInstance().setTipsInfo("xxxxxx; ");
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
}
