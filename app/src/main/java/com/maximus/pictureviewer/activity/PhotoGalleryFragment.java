package com.maximus.pictureviewer.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.maximus.pictureviewer.R;
import com.maximus.pictureviewer.listener.RecyclerViewItemClickListener;
import com.maximus.pictureviewer.model.PictureItem;
import com.maximus.pictureviewer.utils.NetworkLoading;
import com.maximus.pictureviewer.utils.SlideShowDialogFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = PhotoGalleryFragment.class.getSimpleName();

    private List<PictureItem> pictureItems = new ArrayList<>();
    private RecyclerView pictureRecyclerView;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchPictures().execute();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.picture_fragment, container, false);

        pictureRecyclerView = view.findViewById(R.id.photo_recycler_view);
        pictureRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        pictureRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity()
                , pictureRecyclerView, new RecyclerViewItemClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", (Serializable) pictureItems);
                bundle.putInt("position", position);

                SlideShowDialogFragment fragment = SlideShowDialogFragment.newInstance();
                fragment.setArguments(bundle);
                fragment.show(getFragmentManager(), "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "Long click", Toast.LENGTH_SHORT).show();
            }
        }));


        return view;
    }

    private class PictureHolder extends RecyclerView.ViewHolder {

        private ImageView picturesImageView;

        public PictureHolder(View itemView) {
            super(itemView);

            picturesImageView = itemView.findViewById(R.id.item_image);
        }

        public void bindWithGlide(PictureItem pictureItem) {
            Glide.with(getActivity())
                    .asBitmap()
                    .load(pictureItem.getUrl())
                    .into(picturesImageView);
        }
    }


    private class PictureAdapter extends RecyclerView.Adapter<PictureHolder> {

        private List<PictureItem> pictureItems;

        public PictureAdapter(List<PictureItem> pictureItems) {
            this.pictureItems = pictureItems;
        }

        @Override
        public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gallery_image, parent, false);

            return new PictureHolder(view);
        }

        @Override
        public void onBindViewHolder(PictureHolder holder, int position) {
            final PictureItem pictureItem = pictureItems.get(position);

            holder.bindWithGlide(pictureItem);
        }

        @Override
        public int getItemCount() {
            return pictureItems.size();
        }
    }

    private class FetchPictures extends AsyncTask<Void, Void, List<PictureItem>> {

        @Override
        protected List<PictureItem> doInBackground(Void... voids) {
            return new NetworkLoading().fetchItems();
        }

        @Override
        protected void onPostExecute(List<PictureItem> items) {
            pictureItems = items;
            setupAdapter();
        }
    }

    private void setupAdapter() {
        if (isAdded()) {
            pictureRecyclerView.setAdapter(new PictureAdapter(pictureItems));
        }
    }
}
