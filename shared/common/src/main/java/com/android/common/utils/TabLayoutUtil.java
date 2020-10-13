package com.android.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.android.common.R;
import com.google.android.material.tabs.TabLayout;
import java.lang.reflect.Field;


public class TabLayoutUtil {
    private static final float DefaultTextSize = 14;
    private static final float DefaultSelectTextSize = 16;

    /**
     * 设置tab中的textView选中和和没选中时的文字内容，字体的大小和颜色
     * @param tab              tab
     * @param titles           titles
     * @param unSelectTextSize unSelectTextSize
     * @param selectedTextSize selectedTextSize
     */
    public static void setTabBack(Context context,TabLayout tab, String[] titles, final float unSelectTextSize, final float selectedTextSize) {
        final float unSelect = unSelectTextSize == 0 ? DefaultTextSize : unSelectTextSize;
        final float select = selectedTextSize == 0 ? DefaultSelectTextSize : selectedTextSize;
        setTabBack(context, tab, titles, unSelect, select, Color.parseColor("#303030"), Color.parseColor("#C73D00"));
    }

    /**
     * 设置tab中的textView选中和和没选中时的文字内容，字体的大小和颜色
     * @param tab              tab
     * @param titles           titles
     * @param unSelectTextSize unSelectTextSize
     * @param selectedTextSize selectedTextSize
     */
    public static void setTabText(Context context, TabLayout tab, String[] titles, final float unSelectTextSize, final float selectedTextSize) {
        final float unSelect = unSelectTextSize == 0 ? DefaultTextSize : unSelectTextSize;
        final float select = selectedTextSize == 0 ? DefaultSelectTextSize : selectedTextSize;
        setTabText(context, tab, titles, unSelect, select, Color.parseColor("#303030"), Color.parseColor("#303030"));
    }

    /**
     * 设置tab中的textView选中和和没选中时的文字内容，字体的大小和颜色
     * @param tabLayout        tab
     * @param titles           titles
     * @param unSelectTextSize commonTextSize
     * @param selectedTextSize selectedTextSize
     * @param normalColor      normalColor
     * @param selectedColor    selectedColor
     */
    public static void setTabBack(Context context,TabLayout tabLayout, String[] titles, final float unSelectTextSize,
                                  final float selectedTextSize, final int normalColor,
                                  final int selectedColor) {
        for (int i = 0; i < titles.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(titles[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setMaxLines(1);//这里必须设置程1行，否则scrollable下可设置指示器固定长度可能会变两行
            if (i == 0) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedTextSize);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                textView.setTextColor(selectedColor);
                textView.setBackgroundResource(R.mipmap.ic_exam_question_tab_bg);
            } else {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, unSelectTextSize);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                textView.setTextColor(normalColor);
            }
            textView.setWidth(DisplayUtil.dp2px(40));
            textView.setHeight(DisplayUtil.dp2px(18));
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(textView);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView();
                if (textView != null) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedTextSize);
                    textView.setBackgroundResource(R.mipmap.ic_exam_question_tab_bg);
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    textView.setTextColor(selectedColor);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView();
                if (textView != null) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, unSelectTextSize);
                    textView.setBackgroundResource(0);
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    textView.setTextColor(normalColor);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 设置tab中的textView选中和和没选中时的文字内容，字体的大小和颜色
     * @param tabLayout        tab
     * @param titles           titles
     * @param unSelectTextSize commonTextSize
     * @param selectedTextSize selectedTextSize
     * @param normalColor      normalColor
     * @param selectedColor    selectedColor
     */
    public static void setTabText(Context context, TabLayout tabLayout, String[] titles, final float unSelectTextSize,
                                  final float selectedTextSize, final int normalColor,
                                  final int selectedColor) {
        for (int i = 0; i < titles.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(titles[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setMaxLines(1);//这里必须设置程1行，否则scrollable下可设置指示器固定长度可能会变两行
            if (i == 0) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedTextSize);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                textView.setTextColor(selectedColor);
            } else {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, unSelectTextSize);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                textView.setTextColor(normalColor);
            }
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(textView);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView();
                if (textView != null) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedTextSize);
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    textView.setTextColor(selectedColor);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView();
                if (textView != null) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, unSelectTextSize);
                    textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    textView.setTextColor(normalColor);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 设置tab中的textView选中和和没选中时的文字内容，字体的大小和颜色
     * @param tabLayout        tabLayout
     * @param titles           titles
     * @param commonTextSize   commonTextSize
     * @param selectedTextSize selectedTextSize
     */
    public static void setMainTabText(final Context context, final TabLayout tabLayout, String[] titles,
                                      final float commonTextSize, final float selectedTextSize) {
        final TextView[] tabText = new TextView[titles.length];
        for (int i = 0; i < titles.length; i++) {
            TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.tabitem_main,
                    tabLayout, false);
            textView.setText(titles[i]);
            if (i == 0) {
                textView.setTextColor(ContextCompat.getColor(context, R.color.main_bg_black));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedTextSize);
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else {
                textView.setTextColor(ContextCompat.getColor(context, R.color.help_bg_gray));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, commonTextSize);
            }
            tabText[i] = textView;
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(textView);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab != null) {
                    if (tab.getPosition() == 1) {
                        for (int i = 0; i < tabText.length; i++) {
                            if (i == 1) {
                                tabText[i].setTextColor(ContextCompat.getColor(context, R.color.white));
                            } else {
                                tabText[i].setTextColor(ContextCompat.getColor(context, R.color.home_tab_text));
                            }
                        }
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context, R.color.white));
                    } else {
                        for (int i = 0; i < tabText.length; i++) {
                            if (i == tab.getPosition()) {
                                tabText[i].setTextColor(ContextCompat.getColor(context, R.color.main_bg_black));
                            } else {
                                tabText[i].setTextColor(ContextCompat.getColor(context, R.color.help_bg_gray));
                            }
                        }
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context, R.color.black));
                    }
                    tabText[tab.getPosition()].setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedTextSize);
                    tabText[tab.getPosition()].setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab != null) {
                    tabText[tab.getPosition()].setTextSize(TypedValue.COMPLEX_UNIT_SP, commonTextSize);
                    tabText[tab.getPosition()].setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * setTabIndicatorLengthFixed
     * @param tabLayout      tabLayout
     * @param selectTextSize selectTextSize
     */
    public static void setTabIndicatorLengthFixed(final TabLayout tabLayout, final float selectTextSize) {
        tabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    tabLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    Class<?> tabClass = tabLayout.getClass();
                    Field tabStrip = tabClass.getDeclaredField("mTabStrip");
                    tabStrip.setAccessible(true);
                    LinearLayout ll_tab = (LinearLayout) tabStrip.get(tabLayout);

                    int maxLen = 0;
                    int maxTextSize = 0;
                    int tabCount = ll_tab.getChildCount();
                    for (int i = 0; i < tabCount; i++) {
                        View child = ll_tab.getChildAt(i);
                        child.setPadding(0, 0, 0, 0);
                        if (child instanceof ViewGroup) {
                            ViewGroup viewGroup = (ViewGroup) child;
                            for (int j = 0; j < viewGroup.getChildCount(); j++) {
                                if (viewGroup.getChildAt(j) instanceof TextView) {
                                    TextView tabTextView = (TextView) viewGroup.getChildAt(j);
                                    int length = tabTextView.getText().length();
                                    maxTextSize = Math.max((int) tabTextView.getTextSize(),
                                            Math.max(maxTextSize, DisplayUtil.sp2px(selectTextSize)));
                                    maxLen = Math.max(length, maxLen);
                                }
                            }
                        }
                        int margin = (tabLayout.getWidth() / tabCount - (maxTextSize + DisplayUtil.dp2px(2))
                                * maxLen) / 2 - DisplayUtil.dp2px(1);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                        params.leftMargin = margin;
                        params.rightMargin = margin;
                        child.setLayoutParams(params);
                        child.invalidate();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * FIXED模式设置tablayout指示器的长短,
     * @param tab tab
     */
    public static void setTabIndicatorLengthFixed(final TabLayout tab) {
        setTabIndicatorLengthFixed(tab, DefaultSelectTextSize);
    }

    /**
     * SCROLLABLE模式下，设置指示器长度
     * @param tabLayout tabLayout
     */
    public static void setTabIndicatorLengthScrollable(final TabLayout tabLayout) {
        setTabIndicatorLengthScrollable(tabLayout, DisplayUtil.dp2px(10),
                DisplayUtil.dp2px(10), DefaultSelectTextSize);
    }

    /**
     * SCROLLABLE模式下，设置指示器长度
     * @param tabLayout      tabLayout
     * @param margin         左右margin
     * @param selectTextSize 选中时文字大小
     */
    public static void setTabIndicatorLengthScrollable(final TabLayout tabLayout, final int margin,
                                                       final float selectTextSize) {
        setTabIndicatorLengthScrollable(tabLayout, margin, margin, selectTextSize);
    }

    /**
     * SCROLLABLE模式下，设置指示器长度
     * @param tabLayout      tabLayout
     * @param marginLeft     marginLeft
     * @param marginRight    marginRight
     * @param selectTextSize 选中时文字大小
     */
    public static void setTabIndicatorLengthScrollable(final TabLayout tabLayout, final int marginLeft,
                                                       final int marginRight, final float selectTextSize) {
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    if (tab != null) {
                        View tabView = mTabStrip.getChildAt(i);
                        tabView.setPadding(0, 0, 0, 0);
                        int maxLen = 0;
                        int maxTextSize = 0;
                        if (tabView instanceof ViewGroup) {
                            ViewGroup viewGroup = (ViewGroup) tabView;
                            for (int j = 0; j < viewGroup.getChildCount(); j++) {
                                if (viewGroup.getChildAt(j) instanceof TextView) {
                                    TextView tabTextView = (TextView) viewGroup.getChildAt(j);
                                    int length = tabTextView.getText().length();
                                    maxTextSize = Math.max((int) tabTextView.getTextSize(),
                                            Math.max(maxTextSize, DisplayUtil.sp2px(selectTextSize)));
                                    maxLen = Math.max(length, maxLen);
                                }
                            }
                        }
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = (maxTextSize + DisplayUtil.dp2px(2)) * maxLen;
                        params.leftMargin = marginLeft;
                        params.rightMargin = marginRight;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    }
                }
            }
        });
    }


    public static void addTabSelect(TabLayout tab, ViewPager pager, boolean anim) {
        tab.addOnTabSelectedListener(new TabViewPagerSelect(pager, anim));
    }

    /**
     * tab viewpager Select
     */
    private static class TabViewPagerSelect implements TabLayout.OnTabSelectedListener {
        private ViewPager pager;
        private boolean anim;

        public TabViewPagerSelect(ViewPager pager, boolean anim) {
            this.pager = pager;
            this.anim = anim;
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (pager != null && tab != null){
                pager.setCurrentItem(tab.getPosition(), anim);
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }
    }
}
