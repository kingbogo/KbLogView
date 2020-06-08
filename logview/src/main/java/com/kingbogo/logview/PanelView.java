package com.kingbogo.logview;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kingbogo.logview.adapter.PanelAdapter;
import com.kingbogo.logview.adapter.PanelAreaAdapter;
import com.kingbogo.logview.listener.LogPanelListener;
import com.kingbogo.logview.util.CheckUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * <p>
 * 日志面板
 * </p>
 *
 * @author Kingbo
 * @date 2019/6/27
 */
public class PanelView extends ConstraintLayout implements View.OnClickListener {

    private static final String TIPS_DEFAULT = " #日志页面，供开发人员使用# ";
    private static final String LOG_TAG_FORMAT = "[ %s ][ %s : %s ]";
    private static final SimpleDateFormat mLogDf = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.CHINA);

    private View mRootView;

    private RecyclerView mRv;
    private RecyclerView mAreaRv;

    private PanelAdapter mPanelAdapter;
    private PanelAreaAdapter mAreaAdapter;

    private TextView mTipsTv;

    /** 是否横屏 */
    private boolean mIsLand = false;

    public PanelView(Context context) {
        this(context, null);
    }

    public PanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mAreaAdapter = new PanelAreaAdapter();
        mPanelAdapter = new PanelAdapter();

        initView();
    }

    public void scrollToLastLog() {
        if (mRv != null) {
            int itemCount = mPanelAdapter.getItemCount();
            if (itemCount > 10) {
                mRv.scrollToPosition(itemCount - 1);
            }
        }
    }

    public void addLog(String tag, String log) {
        mPanelAdapter.addLog(formatLog(tag, log));
    }

    public void clearData() {
        mPanelAdapter.clearLog();
    }

    public void notifyDataChanged() {
        mPanelAdapter.notifyDataSetChanged();

        // false表示已经滚动到底部
        if (!mRv.canScrollVertically(1)) {
            scrollToLastLog();
        }
    }

    public void notifyDataChangedAndScrollToBottom() {
        mPanelAdapter.notifyDataSetChanged();
        scrollToLastLog();
    }

    public void setPanelListener(LogPanelListener panelListener) {
        mAreaAdapter.setPanelListener(panelListener);
    }

    public void setArea(String... areas) {
        mAreaAdapter.setData(new ArrayList<>(Arrays.asList(areas)));
    }

    public void setArea(List<String> list) {
        mAreaAdapter.setData(list);
    }

    public void setTipsInfo(String tipsInfo) {
        String currentTipsInfo;
        if (!CheckUtil.isEmpty(tipsInfo)) {
            currentTipsInfo = TIPS_DEFAULT + "<br/>" + tipsInfo;
        } else {
            currentTipsInfo = TIPS_DEFAULT;
        }
        mTipsTv.setText(Html.fromHtml(currentTipsInfo));
    }

    public void refreshLayout(boolean isLand) {
        mIsLand = isLand;
        initView();
    }

    private void initView() {
        boolean isRefresh = false;
        if (mRootView != null && mRootView.getParent() != null) {
            isRefresh = true;
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        }

        if (mIsLand) {
            mRootView = View.inflate(getContext(), R.layout.kb_view_panel_land, null);
        } else {
            mRootView = View.inflate(getContext(), R.layout.kb_view_panel, null);
        }
        ConstraintLayout.LayoutParams layoutParams = new Constraints.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mRootView, layoutParams);

        // 初始化
        mRootView.findViewById(R.id.panel_clear_btn).setOnClickListener(this);

        mRv = mRootView.findViewById(R.id.panel_rv);
        mRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRv.setAdapter(mPanelAdapter);

        mAreaRv = mRootView.findViewById(R.id.panel_area_rv);
        int spanCount = 3;
        if (mIsLand) {
            spanCount = 2;
        }
        mAreaRv.setLayoutManager(new GridLayoutManager(getContext(), spanCount, GridLayoutManager.VERTICAL, false));
        mAreaRv.setAdapter(mAreaAdapter);

        mTipsTv = mRootView.findViewById(R.id.panel_tips_tv);

        if (isRefresh) {
            setTipsInfo(null);
            mAreaAdapter.clearAllData();
        }
    }

    private String formatLog(String tag, String log) {
        return String.format(LOG_TAG_FORMAT, mLogDf.format(new Date()), tag, log);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.panel_clear_btn) {
            mPanelAdapter.clearLog();
        }
    }
}
