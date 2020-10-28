package com.thomas.greatgate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.thomas.bannerlib.BannerRecyclerView;
import com.thomas.bannerlib.BannerSwipHelper;
import com.thomas.bannerlib.PagerIndicatorDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private BannerRecyclerView mRecyclerView;
    private List<String> mList = new ArrayList<>();
    private BannerSwipHelper mBannerScaleHelper = null;
    private CardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
    }

    private void init() {
        String data = "https://static.cacheserv.com/holla_admin/sandbox/pc_girl_banner/20201015105903_rectangle@2x.png";
        for (int i = 0; i < 3; i++) {
            mList.add(data);
            mList.add(data);
            mList.add(data);
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CardAdapter(mList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        // mRecyclerView绑定scale效果
        mBannerScaleHelper = new BannerSwipHelper();
        mBannerScaleHelper.setFirstItemPos(0);
        mBannerScaleHelper.attachToRecyclerView(mRecyclerView);

        // pager indicator
        mRecyclerView.addItemDecoration(new PagerIndicatorDecoration(3));
        mRecyclerView.start();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String data1 = "https://img.alicdn.com/simba/img/TB1AhFnPVXXXXa.XFXXSutbFXXX.jpg_q50.jpg";
                mList.add(data1);
                mAdapter.setDatas(mList);
                Toast.makeText(getApplicationContext(), "刷新", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(this, 4000);
            }
        };
        new Handler().postDelayed(runnable, 4000);


    }

    private void initView() {
        mRecyclerView = (BannerRecyclerView) findViewById(R.id.recyclerView);

    }

}
