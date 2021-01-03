package com.eq.music;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import java.util.List;



public class LooperPagerAdapter extends PagerAdapter {
    private List<Integer> mPics = null;
    private OnpagerItemClickListener mItemClickListener = null;

    @Override
    public int getCount() {
        if (mPics != null){
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final int realPosition = position % mPics.size();
        ImageView imageView = new ImageView(container.getContext());
        //让图片和控件大小一样大
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(mPics.get(realPosition));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null){
                    mItemClickListener.onItemClick(realPosition);
                }
            }
        });
        container.addView(imageView);
        return imageView;
    }

    public void setpagerItemClickListener(OnpagerItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }

    public interface OnpagerItemClickListener{
        void onItemClick(int position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setData(List<Integer> colos) {
        this.mPics = colos;
    }

    public int getDataRealSize(){
        if (mPics != null){
            return mPics.size();
        }
        return 0;
    }
}
