package com.android.common.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;
import com.android.common.R;
import com.android.common.utils.DisplayUtil;
import com.android.common.utils.ViewHolder;
import java.util.ArrayList;
import java.util.List;

public class WithHistoryEditText extends AppCompatEditText {
	/**
	 * 删除按钮的引用
	 */
	private Drawable mPullDownDrawable;
	private Drawable mPullUpDrawable;
	/**
	 * 历史数据
	 */
	private List<String> historyData;

	private PopupWindow pw;

	private ListView lv_history;
	private Context mContext;
	private int offset;

	public WithHistoryEditText(Context context) {
		this(context, null);
	}

	public WithHistoryEditText(Context context, AttributeSet attrs) {
		//这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public WithHistoryEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}


	private void init() {
		//获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
		mPullDownDrawable = getCompoundDrawables()[2];
		if (mPullDownDrawable == null) {
//          throw new NullPointerException("You can add drawableRight attribute in XML");
			mPullDownDrawable = getResources().getDrawable(R.drawable.arrow_down);
		}
		mPullUpDrawable = getResources().getDrawable(R.drawable.arrow_up);
		if(mPullDownDrawable != null && mPullUpDrawable != null) {
			mPullDownDrawable.setBounds(0, 0, mPullDownDrawable.getIntrinsicWidth(), mPullDownDrawable.getIntrinsicHeight());
			mPullUpDrawable.setBounds(0, 0, mPullUpDrawable.getIntrinsicWidth(), mPullUpDrawable.getIntrinsicHeight());
		}
		//默认设置隐藏图标
		setRightIconVisible(false);
	}

	public void setHistoryData(List<String> list){
		historyData = list;
		checkCanPulldown();
	}

	private void initPopu() {
		pw = new PopupWindow(mContext);
		pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_trans_rect));
		View contentView = View.inflate(mContext, R.layout.popwindow_list, null);
		View header = contentView.findViewById(R.id.header);
		header.getLayoutParams().height = getMeasuredHeight();
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(popupIsShowing()){
					dissmissPopup();
				}
			}
		});
		lv_history = (ListView) contentView.findViewById(R.id.lv_history);
		if(historyData == null){
			historyData = new ArrayList<>();
		}
		lv_history.setAdapter(new HistoryAdapter());
		pw.setContentView(contentView);
		int height = getPopuHeight(lv_history);
		pw.setHeight(height);
		pw.setWidth(getMeasuredWidth());
		pw.setOutsideTouchable(true);
		pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				setCompoundDrawables(null,null,mPullDownDrawable,null);
			}
		});
		offset = -(getMeasuredHeight()+getBottomPaddingOffset()+getPaddingBottom());
	}

	/**
	 * 获取PopuWindow的高度
	 * @param listView
	 * @return
	 */
	private int getPopuHeight(ListView listView) {
		ListAdapter mAdapter = listView.getAdapter();
		if (mAdapter == null) {
			return 0;
		}
		int totalHeight = 0;
		int count = mAdapter.getCount()<5?mAdapter.getCount():5;
		for (int i = 0; i <count; i++) {
			View mView = mAdapter.getView(i, null, listView);
			mView.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			totalHeight += mView.getMeasuredHeight();
		}

		int paddingBottom = DisplayUtil.dp2px(5);
		int height = totalHeight + (listView.getDividerHeight() * (count - 1))+getMeasuredHeight()+paddingBottom;
		return height;
	}

	public void checkCanPulldown() {
		setRightIconVisible(historyData.size()>0);
	}

	public interface OnHistoryDeleteListener{
		void onHistoryDelete();
	}


	public void dissmissPopup(){
		if(popupIsShowing()){
			pw.dismiss();
		}
	}

	public boolean popupIsShowing(){
		return pw!=null && pw.isShowing();
	}

	private OnHistoryDeleteListener mOnHistoryDeleteListener;
	public void setOnHistoryDeleteListener(OnHistoryDeleteListener onHistoryDeleteListener){
		mOnHistoryDeleteListener = onHistoryDeleteListener;
	}


	/**
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
	 * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
	 * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {

				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
						&& (event.getX() < ((getWidth() - getPaddingRight())));

				if (touchable) {
					if(historyData.size()>0) {
						if (pw == null) {
							initPopu();
						}
						if (pw.isShowing()) {
							pw.dismiss();
						} else {
							pw.showAsDropDown(this, 0,offset);
							setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1], mPullUpDrawable,getCompoundDrawables()[3]);
						}
					}
				}
			}
		}
		return super.onTouchEvent(event);
	}



	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * @param visible
	 */
	protected void setRightIconVisible(boolean visible) {
		Drawable right = visible ? mPullDownDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	/**
	 * 设置晃动动画
	 */
	public void setShakeAnimation(){
		this.setAnimation(shakeAnimation(5));
	}

	/**
	 * 晃动动画
	 * @param counts 1秒钟晃动多少下
	 * @return
	 */
	public static Animation shakeAnimation(int counts){
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(counts));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

	private class HistoryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return historyData.size();
		}

		@Override
		public String getItem(int position) {
			return historyData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflate(mContext, R.layout.item_edittext_history,null);
			}
			TextView tv = ViewHolder.getView(convertView,R.id.tv_history);
			tv.setText(getItem(position));
			ImageView iv_delete = ViewHolder.getView(convertView,R.id.iv_delete);
			iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					historyData.remove(position);
					notifyDataSetChanged();
					int height = getPopuHeight(lv_history);
					pw.setHeight(height);
					pw.dismiss();
					checkCanPulldown();
					if(mOnHistoryDeleteListener!=null){
						mOnHistoryDeleteListener.onHistoryDelete();
					}
				}
			});
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String text = historyData.get(position);
					setText(text);
					pw.dismiss();
				}
			});
			return convertView;
		}
	}
}
