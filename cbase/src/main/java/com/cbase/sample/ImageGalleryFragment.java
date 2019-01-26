package com.cbase.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cbase.R;
import com.cbase.base.BaseFragment;
import com.cbase.widget.HackyViewPager;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

/**
 * @author : zhouyx
 * @date : 2017/9/15
 * @description : 图片墙Fragment
 */
public class ImageGalleryFragment extends BaseFragment {

    private HackyViewPager mViewPager;
    private AppCompatTextView mIndicator;

    private ArrayList<String> mImageUrls = new ArrayList<>();
    private int mImageIndex;
    private ImageLoadCallback mImageLoadCallback;
    private ImageAdapter mImageAdapter;

    public static ImageGalleryFragment getInstance(ArrayList<String> imageUrls, int imageIndex) {
        ImageGalleryFragment fragment = new ImageGalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("imageUrls", imageUrls);
        bundle.putInt("imageIndex", imageIndex);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.cbase_fragment_image_gallery;
    }

    @Override
    protected void initFragment(Bundle savedInstanceState, Bundle inputBundle) {
        mViewPager = findViewById(R.id.view_pager);
        mIndicator = findViewById(R.id.indicator);
        if (inputBundle == null) {
            return;
        }
        mImageUrls.clear();
        ArrayList<String> list = inputBundle.getStringArrayList("imageUrls");
        if (list != null) {
            mImageUrls.addAll(list);
        }
        mImageIndex = inputBundle.getInt("imageIndex");

        String text = mImageIndex + 1 + "/" + mImageUrls.size();
        mIndicator.setText(text);
        mIndicator.setVisibility(mImageUrls.size() <= 1 ? View.GONE : View.VISIBLE);
        mImageAdapter = new ImageAdapter();
        mViewPager.setAdapter(mImageAdapter);
        mViewPager.setCurrentItem(mImageIndex);

        // 更新下标监听
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                String text = position + 1 + "/" + mImageUrls.size();
                mIndicator.setText(text);
            }
        });
    }

    private class ImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageUrls.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            // root view
            RelativeLayout root = new RelativeLayout(mActivity);
            root.setBackgroundColor(Color.parseColor("#000000"));
            ViewGroup.LayoutParams rootParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(root, rootParams);

            // photo view
            PhotoView photoView = new PhotoView(mActivity);
            photoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            root.addView(photoView, params);

            // progress bar view
            ProgressBar progressBar = new ProgressBar(mActivity);
            progressBar.setVisibility(View.GONE);
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            root.addView(progressBar, params);

            // image load call back
            if (mImageLoadCallback != null) {
                mImageLoadCallback.onImageLoad(ImageGalleryFragment.this, mImageUrls.get(position), photoView, progressBar);
            }
            return root;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

    }

    /**
     * 设置图片加载回调
     *
     * @param imageLoadCallback
     */
    public void setImageLoadCallback(ImageLoadCallback imageLoadCallback) {
        this.mImageLoadCallback = imageLoadCallback;
        if (mImageAdapter != null) {
            mImageAdapter.notifyDataSetChanged();
        }
    }

    public interface ImageLoadCallback {
        /**
         * 图片加载回调
         *
         * @param fragment
         * @param url
         * @param photoView
         * @param progressBar
         */
        void onImageLoad(Fragment fragment, String url, PhotoView photoView, ProgressBar progressBar);
    }

}
