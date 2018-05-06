package com.maximus.pictureviewer.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maximus.pictureviewer.activity.PhotoGalleryFragment;
import com.maximus.pictureviewer.activity.SingleFragmentActivity;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}

