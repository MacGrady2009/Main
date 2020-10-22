package com.android.common.widget;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.common.R;
import com.android.common.utils.AppUtils;


public class DialogFactory{

    public static AllDialog createLoading(Context context) {
        AllDialog.Builder builder = new AllDialog.Builder(context)
            .setLayoutId(R.layout.layout_custom_loading_dialog)
            .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
            .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
            .setCancelable(false)
            .setCanceledOnTouchOutside(false);
        AllDialog dialog = builder.build();
        return dialog;
    }


    public static AllDialog createPermissionDialog(Activity activity) {
        AllDialog.Builder builder = new AllDialog.Builder(activity)
            .setLayoutId(R.layout.layout_common_dialog)
            .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
            .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
            .setListener((allDialog, view) -> {
                TextView tvTitle = view.findViewById(R.id.title);
                TextView tvContent = view.findViewById(R.id.content);
                TextView tvCancel = view.findViewById(R.id.cancel);
                TextView tvOk = view.findViewById(R.id.ok);
                tvTitle.setText("提示");
                tvContent.setText("请到设置中开启您拒绝的权限，不开启会导致某些功能不能正常使用");
                tvCancel.setText("取消");
                tvOk.setText("确定");
                tvCancel.setOnClickListener(view1 -> {
                    allDialog.dismiss();
                });

                tvOk.setOnClickListener(view1 -> {
                    allDialog.dismiss();
                    AppUtils.openAppManagerSettings(activity);
                });
            })
            .setCancelable(false)
            .setCanceledOnTouchOutside(false);
        AllDialog dialog = builder.build();
        return dialog;
    }

}
