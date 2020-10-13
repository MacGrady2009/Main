package com.android.guide.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.viewpager.widget.PagerAdapter;
import com.android.guide.R;


public class GuidePagerAdapter extends PagerAdapter {
    private int[] imageResIds = new int[]{R.mipmap.guide_page0, R.mipmap.guide_page1,
            R.mipmap.guide_page2, R.mipmap.guide_page3, R.mipmap.guide_page4};
    private Context context;

    public GuidePagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageResIds.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guide_page, container, false);
        GuideHolder holder = new GuideHolder(view);
        holder.onBind(imageResIds[position]);
        container.addView(view);
        return view;
    }

    public class GuideHolder {

        ImageView image;

        public GuideHolder(View view) {
            image = view.findViewById(R.id.image);
        }

        public void onBind(@DrawableRes int resId) {
            image.setImageResource(resId);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            container.removeView((View) object);
        }
    }

}
