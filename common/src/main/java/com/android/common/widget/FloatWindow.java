package com.android.common.widget;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.android.common.base.BaseListener;

public class FloatWindow {

  public Context mContext;
  public LayoutInflater layoutInflater;
  private WindowManager mWindowManager;
  private WindowManager.LayoutParams wmParams;
  public View mRootView ;
  public boolean isShow;


  public FloatWindow(Builder builder) {
    mContext = builder.context;
    layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    initFloatWindow(builder);
  }

  /**
   * 设置悬浮框基本参数（位置、宽高等）
   */
  public void initFloatWindow(Builder builder) {
    wmParams = getParams(builder);
    //得到容器，通过这个inflater来获得悬浮窗控件
    // 获取浮动窗口视图所在布局
    mRootView = builder.rootView == null ? layoutInflater.inflate(builder.layoutId, null)
      : builder.rootView;
    initView(builder,mRootView);
    // 添加悬浮窗的视图
    mWindowManager.addView(mRootView, wmParams);
    isShow = true;
  }

  public void initView(Builder builder, View view) {

    if (builder.needMove)
      mRootView.setOnTouchListener(new TouchListener());

    if (null != builder.listener)
      builder.listener.onInit(this, view);
  }

  public void closeWindow(){
    if (mWindowManager!=null){
      mWindowManager.removeView(mRootView);
      isShow = false;
    }
  }

  private WindowManager.LayoutParams getParams(Builder builder) {
    wmParams = new WindowManager.LayoutParams();
    if (builder.needOverlay){
      if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (Settings.canDrawOverlays(mContext)){
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
          } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
          }
        }
      }
    }

    if (!builder.needModel){
      wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
    }
    wmParams.gravity = Gravity.LEFT | Gravity.TOP;
    wmParams.x = builder.startX;
    wmParams.y = builder.startY;
    //设置悬浮窗口长宽数据
    wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
    wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    return wmParams;
  }

  public boolean isShow() {
    return isShow;
  }

  public static class Builder {
    private Context context;
    private int startX;
    private int startY;
    private int layoutId;
    private View rootView;
    private boolean needOverlay;
    private boolean needModel;
    private boolean needMove;
    private BaseListener<FloatWindow, View> listener;

    public Builder setContext(Context context) {
      this.context = context;
      return this;
    }

    public Builder setStartX(int startX) {
      this.startX = startX;
      return this;
    }

    public Builder setStartY(int startY) {
      this.startY = startY;
      return this;
    }

    public Builder setLayoutId(int layoutId) {
      this.layoutId = layoutId;
      return this;
    }

    public Builder setRootView(View rootView) {
      this.rootView = rootView;
      return this;
    }

    public Builder setNeedOverlay(boolean needOverlay) {
      this.needOverlay = needOverlay;
      return this;
    }

    public Builder setNeedModel(boolean needModel) {
      this.needModel = needModel;
      return this;
    }

    public Builder setListener(BaseListener<FloatWindow, View> listener) {
      this.listener = listener;
      return this;
    }

    public Builder setNeedMove(boolean needMove) {
      this.needMove = needMove;
      return this;
    }



    public FloatWindow build() {
      return new FloatWindow(this);
    }
  }

  private class TouchListener implements View.OnTouchListener {

    //开始触控的坐标，移动时的坐标（相对于屏幕左上角的坐标）
    private int mTouchStartX, mTouchStartY, mTouchCurrentX, mTouchCurrentY;
    //开始时的坐标和结束时的坐标（相对于自身控件的坐标）
    private int mStartX, mStartY, mStopX, mStopY;
    //判断悬浮窗口是否移动，这里做个标记，防止移动后松手触发了点击事件
    private boolean isMove;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
      int action = event.getAction();
      switch (action) {
        case MotionEvent.ACTION_DOWN:
          isMove = false;
          mTouchStartX = (int) event.getRawX();
          mTouchStartY = (int) event.getRawY();
          mStartX = (int) event.getX();
          mStartY = (int) event.getY();
          break;
        case MotionEvent.ACTION_MOVE:
          mTouchCurrentX = (int) event.getRawX();
          mTouchCurrentY = (int) event.getRawY();
          wmParams.x += mTouchCurrentX - mTouchStartX;
          wmParams.y += mTouchCurrentY - mTouchStartY;
          mWindowManager.updateViewLayout(mRootView, wmParams);

          mTouchStartX = mTouchCurrentX;
          mTouchStartY = mTouchCurrentY;
          break;
        case MotionEvent.ACTION_UP:
          mStopX = (int) event.getX();
          mStopY = (int) event.getY();
          if (Math.abs(mStartX - mStopX) >= 1 || Math.abs(mStartY - mStopY) >= 1) {
            isMove = true;
          }
          break;
      }

      //如果是移动事件不触发OnClick事件，防止移动的时候一放手形成点击事件
      return isMove;
    }
  }

}
