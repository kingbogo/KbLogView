package com.kingbogo.logview.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingbogo.logview.R;
import com.kingbogo.logview.listener.LogPanelListener;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 面板控制区域Adapter
 * </p>
 *
 * @author Kingbo
 * @date 2019/6/27
 */
public class PanelAreaAdapter extends RecyclerView.Adapter {

    private List<String> mData = new ArrayList<>();
    private LogPanelListener mPanelListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kb_item_panel_area, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.nameTv.setText(mData.get(position));
        viewHolder.nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPanelListener != null) {
                    mPanelListener.onClickLogPanel(viewHolder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 添加一条数据
     */
    public void addData(String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    /**
     * 设置数据集
     */
    public void setData(List<String> list) {
        if (list != null && list.size() > 0) {
            mData.clear();
            mData.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 设备面板事件
     */
    public void setPanelListener(LogPanelListener panelListener) {
        mPanelListener = panelListener;
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;

        MyViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.item_panel_area_tv);
        }
    }

}
