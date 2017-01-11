package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.GoodsAdapter;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.net.IModelNewGoods;
import cn.ucai.fulicenter.model.net.ModelNewGoods;
import cn.ucai.fulicenter.model.net.OnCompletListener;
import cn.ucai.fulicenter.model.util.ConvertUtils;
import cn.ucai.fulicenter.model.util.ImageLoader;

public class NewGoodsFragment extends Fragment {
    private static final String TAG = NewGoodsFragment.class.getSimpleName();

    static final int ACTION_DOWN = 0;
    static final int ACTION_PULL_DOWN = 1;
    static final int ACTION_PULL_UP = 2;

    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSwipeRefreshLayout;

    GridLayoutManager mLayoutManager;
    ArrayList<NewGoodsBean> mList;
    GoodsAdapter mAdapter;

    IModelNewGoods model;
    int mPageId = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, layout);

        initView();
        setListener();
        model = new ModelNewGoods();
        initData();

        return layout;
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        mRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = mLayoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.isMore() && lastPosition == mAdapter.getItemCount() - 1) {
                    mPageId++;
                    downloadData(ACTION_PULL_UP, mPageId);
                }
            }
        });
    }

    private void setPullDownListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mTvRefresh.setVisibility(View.VISIBLE);
                mPageId = 1;
                downloadData(ACTION_PULL_DOWN, mPageId);
            }
        });
    }

    private void initData() {
        mPageId = 1;
        downloadData(ACTION_DOWN, mPageId);
    }

    private void downloadData(final int action, int mPageId) {
        model.downData(getContext(), I.CAT_ID, mPageId, new OnCompletListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                    mAdapter.setMore(result != null && result.length > 0);
                    if (!mAdapter.isMore()) {
                        if (action == ACTION_PULL_UP) {
                            mAdapter.setFooter("没有更多数据。。。");
                        }
                        return;
                    }
                    mAdapter.setFooter("加载更多数据。。。");
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    switch (action) {
                        case ACTION_DOWN:
                            mAdapter.initData(list);
                            break;
                        case ACTION_PULL_DOWN:
                            ImageLoader.release();
                            mSwipeRefreshLayout.setRefreshing(false);
                            mTvRefresh.setVisibility(View.GONE);
                            mAdapter.initData(list);
                            break;
                        case ACTION_PULL_UP:
                            mAdapter.addData(list);
                            break;
                    }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        mLayoutManager = new GridLayoutManager(getContext(), I.COLUM_NUM);

        mRv.setLayoutManager(mLayoutManager);
        mRv.setHasFixedSize(true);
        mList = new ArrayList<>();
        mAdapter = new GoodsAdapter(getContext(), mList);
        mRv.setAdapter(mAdapter);

    }

}
