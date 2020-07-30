package com.android.common.base;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.android.common.R;
import com.android.common.utils.TabLayoutUtil;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;



public abstract class BaseMainTabActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {
    protected TabLayout tabLayout;
    protected final List<BaseFragment> fragmentList = new ArrayList<>();
    protected final List<String> tabNames = new ArrayList<>();
    protected int currentIndex;
    //protected Dialog mCommentResult;
    protected boolean hasComment = false;
    private View mContentView;
    private View mDividerLine;

    public BaseMainTabActivity() {
    }

    protected int onSetRootViewId() {
        return R.layout.activity_main_tab_layout;
    }

    public boolean setSupportFragment() {
        return false;
    }

    protected void onInitView() {
        super.onInitView();
        tabLayout = rootView.findViewById(R.id.base_tablayout);
        //mContentView = rootView.findViewById(R.id.main_tab_real_content);
        mDividerLine = rootView.findViewById(R.id.main_tab_divider_line);
        initFragmentList();
        initTabList();
        initTabs();
    }

    protected abstract void initFragmentList();

    protected abstract void initTabList();

    //protected void onClickFeedBack(){}

    protected void initTabs() {
        tabLayout.removeAllTabs();
        tabLayout.addOnTabSelectedListener(this);
        for (int i = 0; i < fragmentList.size(); i++) {
            TabLayout.Tab tab = tabLayout.newTab().setText(tabNames.get(i));
            tabLayout.addTab(tab, i);
        }
        //TabLayoutUtil.setMainTabText(this, tabLayout, tabNames, 16, 20);
        TabLayoutUtil.setTabIndicatorLengthFixed(tabLayout, 20);
        //selectTab(0);
    }

    protected void selectTab(int index) {
        if (tabLayout != null) {
            TabLayout.Tab tab = tabLayout.getTabAt(index);
            if (tab != null) tab.select();
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        onTabChanged(tab);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("current_index", this.currentIndex);
        outState.putBoolean("has_comment", this.hasComment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.currentIndex = savedInstanceState.getInt("current_index");
        this.hasComment = savedInstanceState.getBoolean("has_comment");
        selectTab(currentIndex);
    }

    public void onTabChanged(TabLayout.Tab tabSelected) {
        int tabCount = this.tabLayout.getTabCount();
        for (int i = 0; i < tabCount; ++i) {
            TabLayout.Tab tab = this.tabLayout.getTabAt(i);
            if (tab == tabSelected) {
                this.currentIndex = i;
            }
        }
        showIndex(this.currentIndex);
    }

    private void showIndex(int index) {
        if (index < 0 || index >= fragmentList.size()) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        Fragment prepareFragment = fragmentList.get(index);
        if (!prepareFragment.isAdded()) {
            //transaction.add(R.id.main_tab_real_content, prepareFragment, prepareFragment.getClass().getSimpleName());
        }
        for (int i = 0; i < fragments.size(); i++) {
            if (fragments.get(i) != prepareFragment) {
                transaction.hide(fragments.get(i));
            }
        }
        transaction.show(prepareFragment);
        if (index == 1) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.mContentView.getLayoutParams();
            params.bottomMargin = 0;
            tabLayout.setBackgroundResource(R.color.transparent);
            mDividerLine.setVisibility(View.GONE);
        } else {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) this.mContentView.getLayoutParams();
            params.bottomMargin = (int) (50 * getResources().getDisplayMetrics().density);
            tabLayout.setBackgroundResource(R.color.white);
            mDividerLine.setVisibility(View.VISIBLE);
        }
        transaction.commitNowAllowingStateLoss();
    }


    public int getCurrentItem(){
        return currentIndex;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected int getFragmentContainerId(int clickId) {
        return 0;
    }

    public Object getDataFromActivity(String tag) {
        return null;
    }

    public void updateDataFromFragment(String tag, Object data) {
    }

    public void onFragmentClickEvent(int clickId, Bundle bundle) {
    }
}
