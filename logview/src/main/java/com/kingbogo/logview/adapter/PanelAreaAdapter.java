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
 * 面板控制区域Adapter
 * </p>
 *
 * @author Kingbo
 * @date 2019/6/27
 */
public class PanelAreaAdapter extends RecyclerView.Adapter {

    private List<String> mData = new ArrayList<>();

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

    public void setDat(String... values){

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

    private static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;

        MyViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.item_panel_area_tv);
        }
    }

}
