package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.net.IModelBoutique;
import cn.ucai.fulicenter.model.net.ModelBoutique;
import cn.ucai.fulicenter.model.net.OnCompletListener;
import cn.ucai.fulicenter.model.util.CommonUtils;
import cn.ucai.fulicenter.model.util.ConvertUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class BoutiqueFragment extends Fragment {
    private static final String TAG = BoutiqueFragment.class.getSimpleName();

    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSwipeRefreshLayout;

    LinearLayoutManager mLayoutManager;
    ArrayList<BoutiqueBean> mList;
    BoutiqueAdapter mAdapter;

    IModelBoutique model;
    @BindView(R.id.tv_noMore)
    TextView mTvNoMore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, layout);

        initView();
        setListener();
        model = new ModelBoutique();
        initData(I.ACTION_DOWNLOAD);

        return layout;
    }

    private void setListener() {
        setPullDownListener();
    }

    private void setPullDownListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mTvRefresh.setVisibility(View.VISIBLE);
                initData(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void initData(final int action) {
        model.downData(getContext(), new OnCompletListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                mSwipeRefreshLayout.setRefreshing(false);
                mTvRefresh.setVisibility(View.GONE);

                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mTvNoMore.setVisibility(View.GONE);

                if (result != null && result.length > 0) {
                    ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mAdapter.initData(list);
                    } else {
                        mAdapter.addData(list);
                    }
                } else {
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    mTvNoMore.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String error) {
                mSwipeRefreshLayout.setRefreshing(false);
                mTvRefresh.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
                mTvNoMore.setVisibility(View.VISIBLE);
                CommonUtils.showShortToast(error);
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
        mLayoutManager = new LinearLayoutManager(getContext());
        mRv.addItemDecoration(new SpaceItemDecoration(12));
        mRv.setLayoutManager(mLayoutManager);
        mRv.setHasFixedSize(true);
        mList = new ArrayList<>();
        mAdapter = new BoutiqueAdapter(getContext(), null);
        mRv.setAdapter(mAdapter);

        mSwipeRefreshLayout.setVisibility(View.GONE);
        mTvNoMore.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_noMore)
    public void onClick() {
        initData(I.ACTION_DOWNLOAD);
    }
}
