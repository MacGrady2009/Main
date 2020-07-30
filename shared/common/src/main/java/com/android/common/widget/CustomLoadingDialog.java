package com.android.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import com.android.common.R;


public class CustomLoadingDialog extends Dialog {
    private Context context;
    TextView msg;
    private String msgStr;

    public CustomLoadingDialog(Context context) {
        this(context, R.style.loading_dialog);
    }

    public CustomLoadingDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        if (window != null) {
            ViewGroup rootGroup = window.getDecorView().findViewById(android.R.id.content);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_loading_dialog, rootGroup, false);
            setContentView(view);
            msg = findViewById(R.id.loading_msg);
            updateInfo();
        }
    }

    /**
     * 更新信息
     */
    private void updateInfo() {
        if (msg != null) {
            if (!TextUtils.isEmpty(msgStr)) {
                msg.setText(msgStr);
            }
        }
    }

    /**
     * 设置msg
     * @param msg msg
     */
    public void setMsg(String msg){
        checkMsg(msg);
    }

    private void checkMsg(String msg){
        if (TextUtils.isEmpty(msg)){
            msgStr = context.getResources().getString(R.string.loading);
        } else {
            msgStr = msg;
        }
        updateInfo();
    }

    /**
     * 显示加载框
     * @param msg 需要显示的消息
     */
    public void show(String msg) {
        checkMsg(msg);
        super.show();
    }

    /**
     * 显示加载框，消息内容默认为：加载中…
     */
    public void show() {
        msgStr = context.getResources().getString(R.string.loading);
        updateInfo();
        super.show();
    }

}
