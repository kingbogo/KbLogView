package com.kingbogo.logview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.imuxuan.floatingview.FloatingMagnetView;
import com.imuxuan.floatingview.FloatingView;
import com.imuxuan.floatingview.MagnetViewListener;
import com.kingbogo.logview.listener.LogPanelListener;
import com.kingbogo.logview.util.CheckUtil;

import java.util.List;

/**
 * <p>
 * 日志View
 * </p>
 *
 * @author Kingbo
 * @date 2019/6/27
 */
public class LogView {

    private PanelView mPanelView;
    private FloatingView mFloatView;
    private FrameLayout mContainer;

    private boolean mIsDeBug;

    private static volatile LogView mInstance;

    private LogView() {
        mIsDeBug = BuildConfig.DEBUG;
    }

    public static LogView getInstance() {
        if (mInstance == null) {
            synchronized (LogView.class) {
                if (mInstance == null) {
                    mInstance = new LogView();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化: 一般在 Application中调用、或在 Activity的onCreate中调用
     *
     * @param context 上下文
     * @param isDebug 是否Debug.   Debug模式下不启用LogView.
     */
    public void init(Context context, boolean isDebug) {
        mIsDeBug = isDebug;
        if (!mIsDeBug) {
            return;
        }

        if (mFloatView == null) {
            mFloatView = FloatingView.get();
        }
        if (mPanelView == null) {
            mPanelView = new PanelView(context);
        }

        mFloatView.add();
    }

    /**
     * 释放：App退出时调用
     */
    public void release() {
        if (!mIsDeBug) {
            return;
        }

        if (mFloatView != null) {
            mFloatView.remove();
            mFloatView = null;
        }
        if (mPanelView != null) {
            mPanelView.setPanelListener(null);
            removePanelView();
            mPanelView = null;
        }
    }

    /**
     * 一般在Activity(或基类) 的 onStart()中调用
     */
    public void attach(Activity activity) {
        if (!mIsDeBug) {
            return;
        }

        mFloatView.attach(activity);
        mFloatView.icon(R.drawable.kb_icon_log);
        mFloatView.listener(new MagnetViewListener() {
            @Override
            public void onRemove(FloatingMagnetView magnetView) {
            }

            @Override
            public void onClick(FloatingMagnetView magnetView) {
                changeLogState();
            }
        });

        FrameLayout container = getActivityRoot(activity);
        if (container == null || mPanelView == null) {
            mContainer = container;
            return;
        }
        if (mPanelView.getParent() == container) {
            return;
        }
        if (mContainer != null && mPanelView.getParent() == mContainer) {
            mContainer.removeView(mPanelView);
        }
        mContainer = container;

        mPanelView.setLayoutParams(getLayoutParams());
        mPanelView.setVisibility(View.GONE);
        container.addView(mPanelView, container.getChildCount() - 1);
    }

    /**
     * 设置 Log图标，在 attach 之后调用
     */
    public void setIconImg(@DrawableRes int resId) {
        if (!mIsDeBug) {
            return;
        }

        if (mFloatView != null) {
            mFloatView.icon(resId);
        }
    }

    /**
     * 设置面板点击事件
     */
    public void setPanelListener(LogPanelListener panelListener) {
        if (!mIsDeBug) {
            return;
        }

        if (mPanelView != null) {
            mPanelView.setPanelListener(panelListener);
        }
    }

    /**
     * 一般在Activity(或基类) 的 onStop()中调用
     */
    public void detach(Activity activity) {
        if (!mIsDeBug) {
            return;
        }

        if (mPanelView != null && mContainer != null && ViewCompat.isAttachedToWindow(mPanelView)) {
            mContainer.removeView(mPanelView);
        }
        mFloatView.detach(activity);
    }

    /**
     * 增加一条日志
     */
    public void addLog(final String tag, final String log) {
        if (!mIsDeBug) {
            return;
        }

        if (mPanelView != null) {
            mPanelView.post(new Runnable() {
                @Override
                public void run() {
                    mPanelView.addLog(tag, log);
                    if (isShow4PanelView()) {
                        mPanelView.notifyDataChanged();
                    }
                }
            });
        }
    }

    /**
     * 清空所有日志
     */
    public void clearLogs() {
        if (!mIsDeBug) {
            return;
        }

        if (mPanelView != null) {
            mPanelView.post(new Runnable() {
                @Override
                public void run() {
                    mPanelView.clearData();
                    if (isShow4PanelView()) {
                        mPanelView.notifyDataChanged();
                    }
                }
            });
        }
    }

    /**
     * 添加控制区域数据
     */
    public void addArea(final String area) {
        if (!mIsDeBug) {
            return;
        }

        if (CheckUtil.isEmpty(area)) {
            return;
        }
        if (mPanelView != null) {
            mPanelView.post(new Runnable() {
                @Override
                public void run() {
                    mPanelView.addArea(area);
                }
            });
        }
    }

    /**
     * 设置控制区域数据
     */
    public void setArea(final String... area) {
        if (!mIsDeBug) {
            return;
        }

        if (CheckUtil.isEmpty(area)) {
            return;
        }
        if (mPanelView != null) {
            mPanelView.post(new Runnable() {
                @Override
                public void run() {
                    mPanelView.setArea(area);
                }
            });
        }
    }

    /**
     * 设置控制区域数据
     */
    public void setArea(final List<String> list) {
        if (!mIsDeBug) {
            return;
        }

        if (CheckUtil.isEmpty(list)) {
            return;
        }
        if (mPanelView != null) {
            mPanelView.post(new Runnable() {
                @Override
                public void run() {
                    mPanelView.setArea(list);
                }
            });
        }
    }

    /**
     * 设置tips，支持Html格式
     */
    public void setTipsInfo(final String tipsInfo) {
        if (!mIsDeBug) {
            return;
        }

        if (mPanelView != null) {
            mPanelView.post(new Runnable() {
                @Override
                public void run() {
                    mPanelView.setTipsInfo(tipsInfo);
                }
            });
        }
    }


    // =================================================== @ private

    private FrameLayout getActivityRoot(Activity activity) {
        if (activity == null) {
            return null;
        }
        try {
            return (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private FrameLayout.LayoutParams getLayoutParams() {
        return new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    private void changeLogState() {
        if (mPanelView != null) {
            mPanelView.post(new Runnable() {
                @Override
                public void run() {
                    if (isShow4PanelView()) {
                        mPanelView.setVisibility(View.GONE);
                    } else {
                        mPanelView.setVisibility(View.VISIBLE);
                        mPanelView.notifyDataChanged();
                        mPanelView.scrollToLastLog();
                    }
                }
            });
        }
    }

    private boolean isShow4PanelView() {
        return mPanelView.getVisibility() == View.VISIBLE;
    }

    private void removePanelView() {
        if (mPanelView != null) {
            mPanelView.post(new Runnable() {
                @Override
                public void run() {
                    mPanelView.clearData();
                    if (ViewCompat.isAttachedToWindow(mPanelView) && mContainer != null) {
                        mContainer.removeView(mPanelView);
                    }
                }
            });
        }
    }

}
