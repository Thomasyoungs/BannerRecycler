package com.thomas.greatgate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.thomas.bannerlib.BannerAdapterHelper;
import com.thomas.bannerlib.BannerRecyclerView;
import com.thomas.bannerlib.adapter.BannerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Author：yangzhikuan
 * Date：2020-10-27
 * Description:BannerSwipHelper
 */
public class CardAdapter extends BannerAdapter<String,CardAdapter.ViewHolder> {

    private BannerAdapterHelper mBannerAdapterHelper;

    public CardAdapter(List<String> mList, BannerRecyclerView bannerRecyclerView) {
        super(mList);
        mBannerAdapterHelper = new BannerAdapterHelper(mList.size());
        bannerRecyclerView.setAdapterHelper(mBannerAdapterHelper);
    }


    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


    @Override
    public CardAdapter.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_item, parent, false);
        mBannerAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindView(final ViewHolder holder, String data, final int position, int size) {
        mBannerAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCornerCenterCrop(4));
        options.placeholder(R.color.gray_f9f9f9);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(holder.itemView)
                .load(data)
                .apply(options)
                .into(holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RecyclerView) holder.itemView.getParent()).smoothScrollToPosition(position);
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

    }

}
