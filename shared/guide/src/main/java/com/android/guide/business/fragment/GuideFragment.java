package com.android.guide.business.fragment;

import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import com.android.common.base.BaseFragment;
import com.android.common.constant.ModuleConstant;
import com.android.common.utils.Router;
import com.android.common.utils.SpUtils;
import com.android.common.view.CustomViewPager;
import com.android.common.view.pagerindicator.CircleLinePageIndicator;
import com.android.guide.BuildConfig;
import com.android.guide.R;
import com.android.guide.adapter.GuidePagerAdapter;

public class GuideFragment extends BaseFragment implements
    View.OnClickListener{


    /*滑动页面*/
    CustomViewPager viewPager;
    /*滑动页面标识*/
    CircleLinePageIndicator pageIndicator;
    /*enter main ui*/
    TextView start;

    private GuidePagerAdapter adapter;

    @Override
    protected int onSetRootViewId() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void onFindView() {
        super.onFindView();
        viewPager = mRootView.findViewById(R.id.viewPager);
        pageIndicator = mRootView.findViewById(R.id.pager_indicator);
        start = mRootView.findViewById(R.id.start);
    }

    @Override
    protected void onInitView() {
        super.onInitView();
        start.setScaleX(0.0f);
        start.setScaleY(0.0f);
        start.setAlpha(0.0f);
        start.setVisibility(View.GONE);

        adapter = new GuidePagerAdapter(this.getContext());
        viewPager.setAdapter(adapter);
        pageIndicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new GuidePageChange());
    }

    @Override
    protected void onInitEvent() {
        super.onInitEvent();
        start.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SpUtils.putBoolean(this.getContext(), ModuleConstant.IS_GUIDED,true);
        skip();
    }

    /**
     * 引导页变化事件
     */
    private class GuidePageChange implements CustomViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position == adapter.getCount() - 1 && start.getVisibility() == View.GONE) {
                start.animate().setDuration(400).alpha(1.0f).scaleX(1).scaleY(1).setInterpolator(new OvershootInterpolator()).start();
                start.setVisibility(View.VISIBLE);
            } else {
                if (start.getVisibility() == View.VISIBLE) {
                    start.animate().setDuration(400).alpha(0.0f).scaleX(0).scaleY(0).setInterpolator(new DecelerateInterpolator()).start();
                    start.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }


    private void skip() {
        if (!SpUtils.getBoolean(this.getContext(), ModuleConstant.IS_LOGIN, false)) {
            Router.getInstance().startActivity(this.getContext(), null, BuildConfig.LOGIN);
        } else {
            Router.getInstance().startActivity(this.getContext(), null, BuildConfig.MAIN);
        }
        getActivity().finish();
    }
}
