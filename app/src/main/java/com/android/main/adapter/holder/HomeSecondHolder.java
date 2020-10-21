package com.android.main.adapter.holder;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.android.common.base.BaseHolder;
import com.android.main.R;
import com.android.main.mvp.model.home.HomeSecondBean;

public class HomeSecondHolder extends BaseHolder<HomeSecondBean.ResultBean> {
    private Context mContext;
    private final TextView mTvExamRoom;
    private final TextView mTvExamClassify;
    private final TextView mTvQuestionDes;
    private final TextView mTvNumber;
    private final TextView mTvTimer;
    private final TextView mTvIng;

    public HomeSecondHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mTvExamRoom = itemView.findViewById(R.id.tv_exam_room);
        mTvQuestionDes = itemView.findViewById(R.id.tv_question_description);
        mTvExamClassify = itemView.findViewById(R.id.tv_exam_classify);
        mTvNumber = itemView.findViewById(R.id.tv_number);
        mTvTimer = itemView.findViewById(R.id.tv_time);
        mTvIng = itemView.findViewById(R.id.tv_is_full);
    }

    @Override
    public void bindData(HomeSecondBean.ResultBean resultBean, int position) {
        mTvExamRoom.setText(resultBean.getExamRoomName());
        String questionDesc = String.format(mContext.getResources().getString(R.string.real_exam_question_des_sign_full)
            , resultBean.getUserAmount(), resultBean.getQuestionAmount(), resultBean.getQuestionAmount(), resultBean.getOnlineUserAmount());
        mTvQuestionDes.setText(Html.fromHtml(questionDesc));
        mTvNumber.setText(resultBean.getOnlineUserAmount()+"");
        mTvTimer.setText(resultBean.getBeginTime());
    }
}
