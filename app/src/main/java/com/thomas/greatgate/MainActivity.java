package com.thomas.greatgate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.thomas.bannerlib.BannerRecyclerView;
import com.thomas.bannerlib.BannerSwipHelper;
import com.thomas.bannerlib.LinePagerIndicatorDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private BannerRecyclerView mRecyclerView;
    private List<Integer> mList = new ArrayList<>();
    private BannerSwipHelper mBannerScaleHelper = null;
    private CardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        super.setContentView(R.layout.activity_main);
        initView();
        init();
    }

    private void init() {
        for (int i = 0; i < 3; i++) {
            mList.add(R.drawable.banner);
            mList.add(R.drawable.banner);
            mList.add(R.drawable.banner);
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new CardAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        // mRecyclerView绑定scale效果
        mBannerScaleHelper = new BannerSwipHelper();
        mBannerScaleHelper.setFirstItemPos(1000);
        mBannerScaleHelper.attachToRecyclerView(mRecyclerView);

        // pager indicator
        mRecyclerView.addItemDecoration(new LinePagerIndicatorDecoration(3));


    }

    private void initView() {
        mRecyclerView = (BannerRecyclerView) findViewById(R.id.recyclerView);

    }

}
