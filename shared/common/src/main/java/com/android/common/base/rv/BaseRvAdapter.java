package com.android.common.base.rv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseRvAdapter<T> extends RecyclerView.Adapter {

    public static final int TYPE_HEADER = 100;
    public static final int TYPE_FOOTER = TYPE_HEADER + 1;
    public static final int TYPE_BODY = TYPE_FOOTER + 1;

    protected List<T> mDatas = new ArrayList<>();
    protected Context mContext;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HEADER:
            case TYPE_BODY:
            case TYPE_FOOTER:
                return createDataViewHolder(parent, viewType);
        }
        return createDataViewHolder(parent, viewType);

    }

    protected View inflate(int layoutId, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(layoutId, parent, false);
    }

    public BaseRvAdapter(@NonNull Context context) {
        this.mContext = context;
    }

    public BaseRvAdapter(@NonNull Context context, List<T> datas) {
        this.mContext = context;
        if (datas != null) {
            this.mDatas.addAll(datas);
        }
    }

    public void setData(List<T> datas){
        if (datas == null) {
            datas = new ArrayList<>();
        }
        mDatas.clear();
        mDatas.addAll(datas);
    }

    public void setDataAndNotify(List<T> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void appendDataAndNotify(List<T> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void appendDataAndNotify(int index, List datas) {
        mDatas.addAll(index,datas);
        notifyItemInserted(index);
    }

    public void remove(int position){
        if (position >= 0 && position < mDatas.size()) {
            mDatas.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    protected abstract RecyclerView.ViewHolder createDataViewHolder(ViewGroup parent, int viewType);

    @Override
    public int getItemViewType(int position) {
        return getMultiplyItemViewType(position);
    }

    /**
     * 多个item类型复写此方法
     * @param position position
     * @return type
     */
    public int getMultiplyItemViewType(int position) {
        return TYPE_BODY;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseRvHolder) {
            BaseRvHolder dataRecyclerViewHolder = (BaseRvHolder) holder;
            dataRecyclerViewHolder.toBindData(getItem(holder.getAdapterPosition()), holder.getAdapterPosition());
            // 点击事件的处理
            initClickListener(holder, holder.getAdapterPosition());
        }
    }

    public T getItem(int position) {
        if (position >= 0 && position < mDatas.size()) {
            return mDatas.get(position);
        }
        return null;
    }

    /**
     * 条目的点击和长按事件
     * @param holder viewHolder
     */
    public void initClickListener(final RecyclerView.ViewHolder holder, final int position) {
        if (mOnItemClickListener != null && (holder.getItemViewType() != TYPE_HEADER)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
                }
            });
        }

        if (mOnItemLongClickListener != null && (holder.getItemViewType() != TYPE_HEADER)) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //int pos = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    /**
     * 单个条目的点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 单个条目的长按事件
     */
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }
}
