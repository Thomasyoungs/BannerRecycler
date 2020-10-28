package com.thomas.greatgate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.recyclerview.widget.RecyclerView;

import com.thomas.bannerlib.BannerAdapterHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Author：yangzhikuan
 * Date：2020-10-27
 * Description:BannerSwipHelper
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<Integer> mList = new ArrayList<>();
    private BannerAdapterHelper mBannerAdapterHelper;

    public CardAdapter(List<Integer> mList) {
        this.mList = mList;
        mBannerAdapterHelper = new BannerAdapterHelper(mList.size());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_item, parent, false);
        mBannerAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mBannerAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        holder.mImageView.setImageResource(mList.get(position % mList.size()));
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((RecyclerView) holder.itemView.getParent()).smoothScrollToPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }


    public int getRealcount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

    }

}
