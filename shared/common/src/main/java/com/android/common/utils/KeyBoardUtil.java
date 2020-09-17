package com.android.common.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class KeyBoardUtil {
    //是否已经显示
    private static boolean isShowing;

    public static void addGlobalLayoutListener(Activity activity, final KeyboardListener listener){
        View content = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        ViewTreeObserver observer = content.getViewTreeObserver();
        //当键盘弹出隐藏的时候会 调用此方法。
        observer.addOnGlobalLayoutListener(()->{
            final Rect rect = new Rect();
            content.getWindowVisibleDisplayFrame(rect);
            int keyBoardHeight = DisplayUtil.getScreenHeight() - (rect.bottom - rect.top);
            handleCallback(keyBoardHeight,listener);
        });
    }


    private static void handleCallback(int keyBoardHeight,KeyboardListener listener){
        //>0键盘显示
        if ( keyBoardHeight > 0 ){
            if (!isShowing && (null != listener)){
                listener.onKeyboardOpened(keyBoardHeight);
                isShowing = true;
            }
        }else {
            if (null != listener){
                listener.onKeyboardClosed();
                isShowing = false;
            }
        }
    }

    public interface KeyboardListener {

        void onKeyboardClosed();

        void onKeyboardOpened(int keyBoardHeight);
    }
}
