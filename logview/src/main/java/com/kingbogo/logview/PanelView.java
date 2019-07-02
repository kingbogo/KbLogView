package com.kingbogo.logview;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.ColorInt;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

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

    private static final String LOG_TAG_FORMAT = "[ %s ][ %s : %s ]";
    private static final SimpleDateFormat mLogDf = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.CHINA);

    private RecyclerView mRv;
    private RecyclerView mAreaRv;

    private PanelAdapter mPanelAdapter;
    private PanelAreaAdapter mAreaAdapter;

    private View mBgView;
    private TextView mTipsTv;

    /** 是否横屏 */
    private boolean mIsLand;

    public PanelView(Context context) {
        this(context, null);
    }

    public PanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        checkOrientation();
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

    public void setBg(@ColorInt int colorResId) {
        mBgView.setBackgroundColor(colorResId);
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

    public void addArea(String area) {
        mAreaAdapter.addData(area);
    }

    public void setArea(String... areas) {
        mAreaAdapter.setData(new ArrayList<>(Arrays.asList(areas)));
    }

    public void setArea(List<String> list) {
        mAreaAdapter.setData(list);
    }

    public void setTipsInfo(String tipsInfo) {
        if (!CheckUtil.isEmpty(tipsInfo)) {
            mTipsTv.setText(Html.fromHtml(tipsInfo));
            mTipsTv.setVisibility(VISIBLE);
        } else {
            mTipsTv.setVisibility(GONE);
        }
    }


    /**
     * 检测屏幕方向
     */
    private void checkOrientation() {
        Configuration configuration = this.getResources().getConfiguration();
        int orientation = configuration.orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            mIsLand = true;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            mIsLand = false;
        }
    }

    private void initView() {
        View rootView;
        if (mIsLand) {
            rootView = View.inflate(getContext(), R.layout.kb_view_panel_land, this);
        } else {
            rootView = View.inflate(getContext(), R.layout.kb_view_panel, this);
        }

        rootView.findViewById(R.id.panel_clear_btn).setOnClickListener(this);

        mRv = rootView.findViewById(R.id.panel_rv);
        mRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mPanelAdapter = new PanelAdapter();
        mRv.setAdapter(mPanelAdapter);

        mAreaRv = rootView.findViewById(R.id.panel_area_rv);
        mAreaRv.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        mAreaAdapter = new PanelAreaAdapter();
        mAreaRv.setAdapter(mAreaAdapter);

        mBgView = rootView.findViewById(R.id.panel_bg_v);
        mTipsTv = rootView.findViewById(R.id.panel_tips_tv);
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
