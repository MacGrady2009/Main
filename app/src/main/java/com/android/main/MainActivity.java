package com.android.main;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.common.base.BaseActivity;

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
        mRlChat = findViewById(R.id.rl_chat);
        mIvChat = findViewById(R.id.iv_chat);
        mTvChat = findViewById(R.id.tv_chat);
        //contact
        mRlContact = findViewById(R.id.rl_contact);
        mIvContact = findViewById(R.id.iv_contact);
        //mine
        mRlMine = findViewById(R.id.rl_mine);
        mIvMine = findViewById(R.id.iv_mine);
        mTvMine = findViewById(R.id.tv_mine);
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
            case R.id.rl_chat:
                setSelected(0);
                break;
            case R.id.rl_contact:
                setSelected(1);
                break;
            case R.id.rl_mine:
                setSelected(2);
                break;
        }

    }
}
