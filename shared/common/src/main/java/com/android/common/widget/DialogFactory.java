package com.android.common.widget;

import android.content.Context;
import android.view.ViewGroup;
import com.android.common.R;


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

}
