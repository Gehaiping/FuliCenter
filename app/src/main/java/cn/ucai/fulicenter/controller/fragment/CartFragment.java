package cn.ucai.fulicenter.controller.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.adapter.CartAdapter;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelCart;
import cn.ucai.fulicenter.model.net.ModeCart;
import cn.ucai.fulicenter.model.net.OnCompletListener;
import cn.ucai.fulicenter.model.util.CommonUtils;
import cn.ucai.fulicenter.model.util.ConvertUtils;
import cn.ucai.fulicenter.model.util.L;
import cn.ucai.fulicenter.view.SpaceItemDecoration;

public class CartFragment extends Fragment {
    private static final String TAG = CartFragment.class.getSimpleName();

    @BindView(R.id.tv_refresh)
    TextView mTvRefresh;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSwipeRefreshLayout;

    LinearLayoutManager mLayoutManager;
    CartAdapter mAdapter;

    IModelCart model;
    @BindView(R.id.tv_nothing)
    TextView mTvNoMore;
    User user;
    ArrayList<CartBean> cartList = new ArrayList<>();

    @BindView(R.id.tv_cart_sum_price)
    TextView mTvCartSumPrice;
    @BindView(R.id.tv_cart_save_price)
    TextView mTvCartSavePrice;

    UpdateCartReceiver mReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, layout);

        initView();
        model = new ModeCart();
        user = FuLiCenterApplication.getUser();
        initData(I.ACTION_DOWNLOAD);
        setPullDownListener();
        setReceiverListener();
        return layout;
    }

    private void setReceiverListener() {
        mReceiver = new UpdateCartReceiver();
        IntentFilter filter = new IntentFilter(I.BROADCAST_UPDATA_CART);
        getContext().registerReceiver(mReceiver, filter);
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
        if (user != null) {
            model.getCart(getContext(), user.getMuserName(), new OnCompletListener<CartBean[]>() {
                @Override
                public void onSuccess(CartBean[] result) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mTvRefresh.setVisibility(View.GONE);

                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mTvNoMore.setVisibility(View.GONE);

                    if (result != null && result.length > 0) {
                        ArrayList<CartBean> list = ConvertUtils.array2List(result);
                        L.e(TAG, "List.size=" + list.size());
                        cartList.addAll(list);
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
        mAdapter = new CartAdapter(getContext(), cartList);
        mRv.setAdapter(mAdapter);

        mSwipeRefreshLayout.setVisibility(View.GONE);
        mTvNoMore.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_nothing)
    public void onClick() {
        initData(I.ACTION_DOWNLOAD);
    }

    @Override
    public void onResume() {
        super.onResume();
        setPrice();
    }

    private void setPrice() {
        int sumPrice = 0;
        int savePrice = 0;
        if (cartList != null && cartList.size() > 0) {
            for (CartBean cart : cartList) {
                GoodsDetailsBean goods = cart.getGoods();
                if (cart.isChecked() && goods != null) {
                    sumPrice += cart.getCount() * getPrice(goods.getCurrencyPrice());
                    savePrice += cart.getCount() * (getPrice(goods.getCurrencyPrice()) - getPrice(goods.getRankPrice()));
                }
                mTvCartSumPrice.setText("合计：￥"+sumPrice);
                mTvCartSavePrice.setText("节省：￥"+savePrice);
            }
        } else {
            mTvCartSumPrice.setText("合计：￥0.00");
            mTvCartSavePrice.setText("节省：￥0.00");
        }
    }

    //currencyPrice:"￥88"
    int getPrice(String price) {
        int p = 0;
        p = Integer.valueOf(price.substring(price.indexOf("￥") + 1));
        return p;
    }

    class UpdateCartReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            L.e(TAG, "onReceive");
            setPrice();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            getContext().unregisterReceiver(mReceiver);
        }
    }
}
