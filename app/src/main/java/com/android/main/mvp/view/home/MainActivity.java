package com.android.main.mvp.view.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.common.base.BaseActivity;
import com.android.main.R;

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
        mIvContainer = new ImageView[3];
        mIvContainer[0] = mIvChat;
        mIvContainer[1] = mIvContact;
        mIvContainer[2] = mIvMine;
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_first:
                setSelected(0);
                break;
            case R.id.rl_second:
                setSelected(1);
                break;
            case R.id.rl_third:
                setSelected(2);
                break;
        }

    }
}
