package com.android.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import com.android.common.R;
import com.android.common.utils.DisplayUtil;
import com.android.common.utils.DrawableUtil;


/**
 * TopActionBar
 * <p>
 * 使用说明： TopBar提供以下功能：
 * <p/>
 * a.topbar分为四个区域，分别是左，中，右2，右，其中左区一般是返回键; 中区包含标题和标题旁边的一个提示图标；右2和右提供程序功能。
 * <p/>
 * b. 中区标题栏的功能包括，显示标题，显示标题旁边的小图片（一般是向下的箭头）
 * <p/>
 * c. 左，右，右2使用同一布局，默认提供显示图片，显示文字，显示提示更新数量的
 * 红色小园框功能。右布局出于兼容老版本原因，提供显示progressbar功能
 * <p/>
 * d. 如果要改变各布局内部的因素，请调用相应布局的customize方法，则原来的所有布局 中子view都将隐藏，来提供customize实现空间
 * <p/>
 * 使用方法: 1. 初始化topbar使用showConfig()，除了返回键默认有返回以外，右和右2无默认图片
 * <p/>
 * 2.设置图片调用showButtonImage();可以统一设置也可以单个设置，看参数选择
 * <p/>
 * 3.设置文字调用showButtonText();可以统一设置也可以单个设置，看参数选择
 * <p/>
 * 4. 如果想要自行定制图标的显示，调用customizeArea(int layoutId)
 */
public class TopActionBar extends RelativeLayout {

    /**
     * Resource ID...
     */
    protected int layoutResId = R.layout.top_actionbar_layout;

    /**
     * 标题栏左侧区域，显示返回按钮的
     */
    public static final int LEFT_AREA = 1;
    /**
     * 标题栏中部区域，显示标题文本
     */
    public static final int MIDDLE_AREA = 2;
    /**
     * 标题栏中间偏右的区域
     */
    public static final int RIGHT_AREA2 = 3;
    /**
     * 标题栏最右侧区域
     */
    public static final int RIGHT_AREA = 4;

    /**
     * 点击标题栏的Listener
     * @author peter
     */
    public interface OnTopBarTitleClickListener {
        void onTitleClick(View view);
    }

    /**
     * 三个按钮的listener
     * @author peter
     */
    public interface OnTopBarButtonClickListener {
        void onLeftButtonClick(View view);

        void onRightButton2Click(View view);

        void onRightButtonClick(View view);
    }

    private Context context;
    private OnTopBarButtonClickListener topbarButtonListener;
    private OnTopBarTitleClickListener topbarTitleListener;

    private ViewGroup leftLayout;
    private ViewGroup rightLayout;
    private ViewGroup rightLayout2;
    protected TextView titleTextView;

    private View dividerView;

    private String title;
    private boolean isLeftShow;
    private int leftImgRes;
    private String leftText;

    private boolean isRightShow;
    private int rightImgRes;
    private String rightText;

    private boolean isRight2Show;
    private int right2ImgRes;
    private String right2Text;

    private Drawable background;

    /* TopActionBar */
    public TopActionBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TopActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getAttrs(attrs);
        init();
    }

    public TopActionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        getAttrs(attrs);
        init();
    }


    private void getAttrs(AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopActionBar);
        try {
            background = ta.getDrawable(R.styleable.TopActionBar_android_background);
            title = ta.getString(R.styleable.TopActionBar_topbarTitle);
            isLeftShow = ta.getBoolean(R.styleable.TopActionBar_topbarIsLeftShow, false);
            leftImgRes = ta.getResourceId(R.styleable.TopActionBar_topbarLeftImgRes, R.mipmap.ic_back);
            leftText = ta.getString(R.styleable.TopActionBar_topbarLeftText);
            if (!isLeftShow) leftImgRes = 0;

            isRightShow = ta.getBoolean(R.styleable.TopActionBar_topbarIsRightShow, false);
            rightImgRes = ta.getResourceId(R.styleable.TopActionBar_topbarRightImgRes, 0);
            rightText = ta.getString(R.styleable.TopActionBar_topbarRightText);
            if (!isRightShow) rightImgRes = 0;

            isRight2Show = ta.getBoolean(R.styleable.TopActionBar_topbarIsRight2Show, false);
            right2ImgRes = ta.getResourceId(R.styleable.TopActionBar_topbarRight2ImgRes, 0);
            right2Text = ta.getString(R.styleable.TopActionBar_topbarRight2Text);
            if (!isRight2Show) right2ImgRes = 0;
        } finally {
            ta.recycle();
        }
    }

    protected void init() {
        LayoutInflater.from(context).inflate(layoutResId, this);
        if (background == null) setBackgroundResource(R.color.white);
        dividerView = findViewById(R.id.action_bar_divider);
        titleTextView = findViewById(R.id.action_bar_title);
        leftLayout = findViewById(R.id.top_action_bar_left_layout);
        rightLayout = findViewById(R.id.top_action_bar_right_layout);
        rightLayout2 = findViewById(R.id.top_action_bar_right_layout2);

        showConfig(title, isLeftShow, isRight2Show, isRightShow);

        initImageAndText(leftText, leftImgRes, LEFT_AREA);
        initImageAndText(rightText, rightImgRes, RIGHT_AREA);
        initImageAndText(right2Text, right2ImgRes, RIGHT_AREA2);
        setDividerShow(false);
    }

    private void initImageAndText(String text, int imgRes, int areaId) {
        if (!TextUtils.isEmpty(text)) {
            showButtonText(text, areaId);
        } else if (imgRes != 0) {
            showButtonImage(imgRes, areaId);
        }
    }

    public void setDividerShow(boolean isShow) {
        if (dividerView != null) {
            if (isShow) {
                dividerView.setVisibility(View.VISIBLE);
            } else {
                dividerView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置标题，同时设置topbar包含的区域。如果要显示，则传true,否则传false
     * 初始化topbar的方法，必须调用，来设置topbar上面显示的元素 直接调用三个参数的那个
     * @param title        标题
     * @param isLeftShow   返回键是否显示（默认显示为一个返回图片）
     * @param isRight2Show 标题右侧按钮是否显示
     * @param isRightShow  最右侧按钮是否显示
     */
    private void showConfig(String title, boolean isLeftShow,
                            boolean isRight2Show, boolean isRightShow) {
        this.title = title;
        this.setTitle(title);
        showConfig(isLeftShow, isRight2Show, isRightShow);
    }

    /**
     * 设置topbar包含的区域。如果要显示，则传true,否则传false
     * @param isLeftShow   返回键是否显示（默认显示为一个返回图片）
     * @param isRight2Show 标题右侧按钮是否显示
     * @param isRightShow  最右侧按钮是否显示
     */
    private void showConfig(boolean isLeftShow, boolean isRight2Show, boolean isRightShow) {
        initArea(LEFT_AREA);
        initArea(RIGHT_AREA);
        initArea(RIGHT_AREA2);

        this.isLeftShow = isLeftShow;
        this.isRight2Show = isRight2Show;
        this.isRightShow = isRightShow;
    }

    public void initArea(final int type) {
        ViewGroup layout = getArea(type);
        final TopBarHolder holder = new TopBarHolder();
        holder.imageButton = layout.findViewById(R.id.action_bar_content_btn);
        holder.imageButton.setVisibility(View.GONE);
        holder.imageButton.setClickable(false);
        holder.textView = layout.findViewById(R.id.action_bar_content_text);
        holder.textView.setVisibility(View.GONE);
        holder.textView.setClickable(false);
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onLayoutClicked(type);
            }
        });
        layout.setTag(holder);
    }

    /**
     * 根据所提供的type,获取相应的layout
     * @param type 就是layoutId
     * @return 获取相应的layout(包括左 ， 右2 ， 右)
     */
    public ViewGroup getArea(final int type) {
        switch (type) {
            case LEFT_AREA:
                return leftLayout;
            case RIGHT_AREA:
                return rightLayout;
            case RIGHT_AREA2:
                return rightLayout2;
            default:
                break;
        }
        return null;
    }

    /**
     * 如果你要自定义topBar的某一个区域，请调用这个方法，并且传入下列参数的一个： TopBar.LEFT_AREA
     * TopBar.MIDDLE_AREA TopBar.RIGHT_AREA TopBar.RIGHT_AREA2
     * @param area 相应的areaId
     * @return 相应的RelativeLayout
     */
    public ViewGroup customizeArea(int area) {
        ViewGroup tempLayout = getArea(area);
        tempLayout.removeAllViews();
        return tempLayout;
    }

    /**
     * 设置相应的layout是否显示
     * @param areaId  areaId
     * @param visible visible
     */
    public void showArea(int areaId, boolean visible) {
        ViewGroup tempLayout = getArea(areaId);
        if (visible) {
            tempLayout.setVisibility(View.VISIBLE);
        } else {
            tempLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 设置单个button中的图片
     * @param res  要显示的图片
     * @param area area
     */
    public void showButtonImage(@DrawableRes int res, int area) {
        ViewGroup tempLayout = getArea(area);
        TopBarHolder holder = (TopBarHolder) tempLayout.getTag();
        if (res != 0) {
            tempLayout.setVisibility(View.VISIBLE);
            holder.imageButton.setVisibility(View.VISIBLE);
            holder.imageButton.setImageResource(res);
            //holder.textView.setVisibility(View.GONE);
        } else {
            holder.imageButton.setVisibility(View.GONE);
        }
    }

    /**
     * 设置单个button中的图片
     * @param res       要显示的图片
     * @param tintColor 要显示的图片颜色
     * @param layoutId  选取的buttonId
     */
    public void showButtonImage(@DrawableRes int res, @ColorRes int tintColor, int layoutId) {
        ViewGroup tempLayout = getArea(layoutId);
        TopBarHolder holder = (TopBarHolder) tempLayout.getTag();
        if (res != 0) {
            tempLayout.setVisibility(View.VISIBLE);
            holder.imageButton.setVisibility(View.VISIBLE);
            holder.imageButton.setImageDrawable(DrawableUtil.tintDrawable(
                    ContextCompat.getDrawable(context, res),
                    ContextCompat.getColor(context, tintColor)));
        } else {
            holder.imageButton.setVisibility(View.GONE);
        }
    }

    /**
     * 设置单个button中的图片
     * @param text     要显示的文字
     * @param layoutId 选取的buttonId
     */
    public void showButtonText(String text, int layoutId) {
        showButtonText(text, layoutId, R.color.text_color_dark);
    }

    public void showButtonText(String text, int layoutId, int colorId) {
        showButtonText(text, layoutId, colorId, 0);
    }

    public void setButtonEnable(int layoutId, boolean isEnable) {
        ViewGroup tempLayout = getArea(layoutId);
        if (tempLayout != null) {
            TopBarHolder holder = (TopBarHolder) tempLayout.getTag();
            holder.textView.setEnabled(isEnable);
            holder.imageButton.setEnabled(isEnable);
            tempLayout.setEnabled(isEnable);
        }
    }

    public void showButtonText(String text, int layoutId, int colorId, int drawableResId) {
        ViewGroup tempLayout = getArea(layoutId);
        TopBarHolder holder = (TopBarHolder) tempLayout.getTag();
        if (!TextUtils.isEmpty(text)) {
            tempLayout.setVisibility(View.VISIBLE);
            holder.textView.setText(text);
            holder.textView.setTextColor(ContextCompat.getColorStateList(context, colorId));
            holder.textView.setVisibility(View.VISIBLE);
            //holder.imageButton.setVisibility(View.GONE);
        } else {
            tempLayout.setVisibility(View.GONE);
        }
        if (drawableResId != 0) {
            Drawable drawable = context.getResources().getDrawable(drawableResId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.textView.setCompoundDrawables(drawable, null, null, null);
            holder.textView.setCompoundDrawablePadding(DisplayUtil.dp2px(5));
        }
    }

    public void updateButtonText(String text, int layoutId) {
        ViewGroup tempLayout = getArea(layoutId);
        TopBarHolder holder = (TopBarHolder) tempLayout.getTag();
        holder.textView.setText(text);
    }

    /**
     * show left button
     */
    public void showLeftButton(boolean isShow) {
        if (isShow) {
            leftLayout.setVisibility(View.VISIBLE);
        } else {
            leftLayout.setVisibility(View.GONE);
        }
    }

    /**
     * show right button
     */
    public void showRightButton(boolean isShow) {
        if (isShow) {
            rightLayout.setVisibility(View.VISIBLE);
        } else {
            rightLayout.setVisibility(View.GONE);
        }
    }

    /**
     * show right2 button
     */
    public void showRight2Button(boolean isShow) {
        if (isShow) {
            rightLayout2.setVisibility(View.VISIBLE);
        } else {
            rightLayout2.setVisibility(View.GONE);
        }
    }

    /**
     * 该方法调用后，button中显示的是文字
     * @param text1 text1
     * @param text2 text2
     * @param text3 text3
     */
    public void showButtonText(String text1, String text2, String text3) {
        showButtonText(text1, LEFT_AREA);
        showButtonText(text2, RIGHT_AREA2);
        showButtonText(text3, RIGHT_AREA);
    }

    public TextView getTitleView() {
        return titleTextView;
    }

    public String getTitle() {
        return titleTextView.getText().toString();
    }

    public void setTitle(CharSequence title) {
        titleTextView.setText(title);
    }

    public void setTitle(CharSequence title, @ColorInt int color) {
        titleTextView.setText(title);
        titleTextView.setTextColor(color);
    }

    public void setTitleCanClick() {
        titleTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                performTitleClick(arg0);
            }
        });
    }

    public ImageView getImage(int layoutId) {
        ViewGroup tempLayout = getArea(layoutId);
        TopBarHolder holder = (TopBarHolder) tempLayout.getTag();
        return holder.imageButton;
    }

    /**
     * 获取topBar的标题点击事件
     * @return listener
     */
    public OnTopBarTitleClickListener getTitleClickListener() {
        return topbarTitleListener;
    }

    /**
     * 设置topBar标题的点击事件
     * @param mTListener listener
     */
    public void setTitleClickListener(OnTopBarTitleClickListener mTListener) {
        this.topbarTitleListener = mTListener;
    }

    /**
     * 获取topBar的三个按钮点击事件
     */
    public void setButtonClickListener(OnTopBarButtonClickListener mListener) {
        this.topbarButtonListener = mListener;
    }

    /**
     * 获取topBar的三个按钮点击事件
     */
    public OnTopBarButtonClickListener getTopBarButtonClickListener() {
        return topbarButtonListener;
    }

    private void onLayoutClicked(int layoutId) {
        switch (layoutId) {
            case LEFT_AREA:
                performLeftButtonClick(leftLayout);
                break;
            case RIGHT_AREA:
                performRightButtonClick(rightLayout);
                break;
            case RIGHT_AREA2:
                performRightButton2Click(rightLayout2);
                break;
            default:
                break;
        }

    }

    private void performTitleClick(View view) {
        if (topbarTitleListener != null) {
            topbarTitleListener.onTitleClick(view);
        }
    }

    private void performLeftButtonClick(View view) {
        if (topbarButtonListener != null) {
            topbarButtonListener.onLeftButtonClick(view);
        }
    }

    private void performRightButton2Click(View view) {
        if (topbarButtonListener != null) {
            topbarButtonListener.onRightButton2Click(view);
        }
    }

    private void performRightButtonClick(View view) {
        if (topbarButtonListener != null) {
            topbarButtonListener.onRightButtonClick(view);
        }
    }

    /**
     * 一个viewholder，包含了leftlayout里面的button和textview
     * @author Peter
     */
    public class TopBarHolder {
        public ImageView imageButton; // 主要显示的ImageButton
        public TextView textView;// 如果有消息提示的话，就是这个了
    }

}

