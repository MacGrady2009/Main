package com.android.common.base;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;


public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {

    public BaseHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData(T t, int position);

    public void toBindData(T t, int position) {
        if (t == null) {
            return;
        }
        bindData(t, position);
    }

}
