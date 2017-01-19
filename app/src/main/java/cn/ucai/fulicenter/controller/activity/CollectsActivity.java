package cn.ucai.fulicenter.controller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.CollectsAdapter;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompletListener;
import cn.ucai.fulicenter.model.util.CommonUtils;
import cn.ucai.fulicenter.model.util.ConvertUtils;
import cn.ucai.fulicenter.view.DisplayUtils;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class CollectsActivity extends AppCompatActivity {
    IModelUser model;
    CollectsActivity mContext;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;
    @BindView(R.id.tv_noMore)
    TextView mTvNoMore;

    CollectsAdapter mAdapter;
    ArrayList<CollectBean> mList;
    int pageId = 1;
    GridLayoutManager gm;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collects);
        ButterKnife.bind(this);
        mContext = this;
        mList = new ArrayList<>();
        mAdapter = new CollectsAdapter(mContext, mList);
        super.onCreate(savedInstanceState);
        DisplayUtils.initBackWithTitle(this, "收藏的宝贝");
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = gm.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == mAdapter.getItemCount() - 1
                        && mAdapter.isMore()) {
                    pageId++;
                    downloadCollects(I.ACTION_PULL_UP);
                }
            }
        });
    }

    private void setPullDownListener() {
        mSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSrl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                pageId = 1;
                downloadCollects(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void downloadCollects(final int action) {
        model = new ModelUser();
        model.getCollects(this, user.getMuserName(), pageId, new OnCompletListener<CollectBean[]>() {
            @Override
            public void onSuccess(CollectBean[] result) {
                mSrl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                if (result != null && result.length >= 0) {
                    ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mAdapter.initData(list);
                    } else {
                        mAdapter.addData(list);
                        mAdapter.setFooter("没有更多数据了");
                    }
                    if (list.size() < I.PAGE_SIZE_DEFAULT) {
                        mAdapter.setMore(false);
                    }
                } else {
                    mAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {
                mSrl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(false);
                CommonUtils.showShortToast(error);
            }
        });
    }

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            finish();
        }
        downloadCollects(I.ACTION_DOWNLOAD);
    }

    private void initView() {
        mSrl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow)
        );
        gm = new GridLayoutManager(mContext, I.COLUM_NUM);
        rv.setLayoutManager(gm);
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));
    }

    updateCollectReceiver mReceiver;

    class updateCollectReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int goodsId = intent.getIntExtra(I.Collect.GOODS_ID, 0);
            if (goodsId != 0) {
                CollectBean bean = new CollectBean();
                bean.setGoodsId(goodsId);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }
}
