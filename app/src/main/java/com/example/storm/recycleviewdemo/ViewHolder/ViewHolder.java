package com.example.storm.recycleviewdemo.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Storm on 2017/3/25.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    private SparseArray<View> mViews;

    public ViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mViews = new SparseArray<>();
    }


    /**
     * 通过id 获取控件
     * @param viewId
     * @return
     */
    public <T extends View> T  getView(int viewId) {
        View childView = mViews.get(viewId);
        if (childView == null) {
            childView = mItemView.findViewById(viewId);

            mViews.put(viewId, childView);
        }
        return (T) childView;
    }


    public View getmItemView(){
        return mItemView;
    }

    public void setText(int viewId, String data) {
        TextView textView = getView(viewId);
        textView.setText(data);
    }

    public void setBgColor(int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(colorId);
    }

    public static ViewHolder create(View itemView) {
        return new ViewHolder(itemView);
    }

    public static ViewHolder create(Context context, int layoutId, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(itemView);
    }
}
