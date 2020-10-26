package com.android.common.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.android.common.R;

public class ToolBarBinding {
    ImageView mIvBack;
    TextView mTvTitle;
    Listener listener;

    public ToolBarBinding(Toolbar toolbar){
        mIvBack = toolbar.findViewById(R.id.iv_back);
        mTvTitle = toolbar.findViewById(R.id.title);
        initEvent();
    }

    private void initEvent(){
        mIvBack.setOnClickListener(view -> {
            if (null != listener){
                listener.onBackClickListener();
            }
        });
    }

    public void setTitleText(String text){
        if (!TextUtils.isEmpty(text)){
            mTvTitle.setText(text);
        }
    }

    public void showBack(boolean isShow){
        if (isShow){
            mIvBack.setVisibility(View.VISIBLE);
        }else {
            mIvBack.setVisibility(View.GONE);
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener{
        void onBackClickListener();
    }
}
