package com.kingbogo.logview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingbogo.logview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 日志面板Adapter
 * </p>
 *
 * @author Kingbo
 * @date 2019/6/27
 */
public class PanelAdapter extends RecyclerView.Adapter {

    private List<String> mData = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kb_item_panel, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.nameTv.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 添加一条日志
     */
    public void addLog(String log) {
        mData.add(log);
        // 当日志超过1000条就清空500条
        if (mData.size() > 1000) {
            List<String> tempLogList = mData.subList(500, 1000);
            mData.clear();
            mData = tempLogList;
        }
        // notifyDataSetChanged();
    }

    /**
     * 清空所有日志
     */
    public void clearLog() {
        mData.clear();
        notifyDataSetChanged();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;

        MyViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.item_panel_tv);
        }
    }

}
