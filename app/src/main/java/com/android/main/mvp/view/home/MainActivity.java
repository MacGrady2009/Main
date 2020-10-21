package com.android.main.mvp.view.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.android.amap.AmapLocation;
import com.android.common.base.BaseActivity;
import com.android.main.R;
import com.android.main.business.home.fragment.HomeFirstFragment;
import com.android.main.business.home.fragment.HomeSecondFragment;
import com.android.main.business.home.fragment.HomeThirdFragment;

public class MainActivity extends BaseActivity
    implements View.OnClickListener {
    //chat
    RelativeLayout mRlChat;
    ImageView mIvChat;
    TextView mTvChat;
    //contact
    RelativeLayout mRlContact;
    ImageView mIvContact;
    //mine
    RelativeLayout mRlMine;
    ImageView mIvMine;
    TextView mTvMine;
    //目前正在展现的tab
    private int mSelectTabIndex = -1;

    private int mTabCount = 3;

    private Fragment[] fragments;

    ImageView mIvContainer[];


    @Override
    protected int onSetRootViewId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onInitView() {
        super.onInitView();
        //chat
        mRlChat = findViewById(R.id.rl_first);
        mIvChat = findViewById(R.id.iv_first);
        mTvChat = findViewById(R.id.tv_first);
        //contact
        mRlContact = findViewById(R.id.rl_second);
        mIvContact = findViewById(R.id.iv_second);
        //mine
        mRlMine = findViewById(R.id.rl_third);
        mIvMine = findViewById(R.id.iv_third);
        mTvMine = findViewById(R.id.tv_third);
        setUpIvContainer();
    }

    private void setUpIvContainer() {
        mIvContainer = new ImageView[mTabCount];
        mIvContainer[0] = mIvChat;
        mIvContainer[1] = mIvContact;
        mIvContainer[2] = mIvMine;
    }

    private void setUpFragment() {
        fragments = new Fragment[mTabCount];
        fragments[0] = HomeFirstFragment.getInstance();
        fragments[1] = HomeSecondFragment.getInstance();
        fragments[2] = HomeThirdFragment.getInstance();
    }

    private void setSelected(int selectIndex) {
        int index = 0;
        for (ImageView iv : mIvContainer) {
            if (index == selectIndex) {
                iv.setSelected(true);
            } else {
                iv.setSelected(false);
            }
            index++;
        }

    }

    @Override
    protected void onInitEvent() {
        super.onInitEvent();
        mRlChat.setOnClickListener(this);
        mRlContact.setOnClickListener(this);
        mRlMine.setOnClickListener(this);
        AmapLocation.getInstance(this).startOnceLocation();
    }

    @Override
    protected void onInitFragment() {
        super.onInitFragment();
        setUpFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, fragments[0])
            .add(R.id.content, fragments[1])
            .add(R.id.content, fragments[2]);
        ft.commit();
        selectTab(0);
    }

    public void showFragment(int showFragmentIndex){
        int index  = 0;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : fragments){
            if (showFragmentIndex == index){
                ft.show(fragment);
            }else {
                ft.hide(fragment);
            }
            index++;
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_first:
                selectTab(0);
                break;
            case R.id.rl_second:
                selectTab(1);
                break;
            case R.id.rl_third:
                selectTab(2);
                break;
        }

    }

    private void selectTab(int tabIndex){
        if (tabIndex != mSelectTabIndex){
            setSelected(tabIndex);
            showFragment(tabIndex);
            mSelectTabIndex = tabIndex;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
