package com.maximus.pictureviewer.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.maximus.pictureviewer.R;
import com.maximus.pictureviewer.model.PictureItem;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.List;


public class SlideShowDialogFragment extends DialogFragment {

    private String TAG = SlideShowDialogFragment.class.getSimpleName();
    private List<PictureItem> pictures;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView countOfPictures;
    private TextView pictureTitle;
    private int selectedPosition = 0;


    public static SlideShowDialogFragment newInstance() {
        SlideShowDialogFragment fragment = new SlideShowDialogFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.
                inflate(R.layout.image_slider, container, false);
        viewPager = view.findViewById(R.id.viewpager);
        countOfPictures = view.findViewById(R.id.count);
        pictureTitle = view.findViewById(R.id.title_text);

        pictures = (List<PictureItem>) getArguments().getSerializable("images");

        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + pictures.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);
        //TODO
        return view;
    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void displayMetaInfo(int position) {
        countOfPictures.setText((position + 1) + " of " + pictures.size());
        PictureItem image = pictures.get(position);
        //TODO
        pictureTitle.setText(image.getTitle());
    }


    private class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.full_screen_preview, container, false);

            ImageView imageViewPreview = view.findViewById(R.id.image_preview);

            PictureItem image = pictures.get(position);

            Glide.with(getActivity())
                    .load(image.getUrl())
//                    .thumbnail(0.5f)
                    .apply(new RequestOptions().override(150, 200)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(imageViewPreview);



            container.addView(view);

            return view;
        }
        @Override
        public int getCount() {
            return pictures.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == (object);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }

}
