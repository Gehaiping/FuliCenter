package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelCart;
import cn.ucai.fulicenter.model.net.ModeCart;
import cn.ucai.fulicenter.model.net.OnCompletListener;
import cn.ucai.fulicenter.model.util.ImageLoader;

/**
 * Created by Administrator on 2017/1/20.
 */

public class CartAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CartBean> mList;
    IModelCart model;
    User user;

    public CartAdapter(Context context, ArrayList<CartBean> mlist) {
        this.mContext = context;
        this.mList = mlist;
        model = new ModeCart();
        user = FuLiCenterApplication.getUser();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = inflater.inflate(R.layout.item_cart, null);
        return new CartViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        CartViewHolder holder = (CartViewHolder) parentHolder;
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void initData(ArrayList<CartBean> list) {
        if (mList != null) {
            mList.clear();
        }
        addData(list);
    }

    public void addData(ArrayList<CartBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_cart_thumb)
        ImageView mIvCartThumb;
        @BindView(R.id.tv_cart_good_name)
        TextView mTvCartGoodName;
        @BindView(R.id.tv_cart_count)
        TextView mTvCartCount;
        @BindView(R.id.tv_cart_price)
        TextView mTvCartPrice;
        @BindView(R.id.cb_cart_selected)
        CheckBox mSelected;

        int listPosition;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            listPosition = position;
            GoodsDetailsBean detailsBean = mList.get(position).getGoods();
            if (detailsBean != null) {
                ImageLoader.downloadImg(mContext, mIvCartThumb, detailsBean.getGoodsThumb());
                mTvCartGoodName.setText(detailsBean.getGoodsName());
                mTvCartPrice.setText(detailsBean.getCurrencyPrice());
            }
            mTvCartCount.setText("(" + mList.get(position).getCount() + ")");
            mSelected.setChecked(mList.get(listPosition).isChecked());
        }

        @OnCheckedChanged(R.id.cb_cart_selected)
        public void checkListener(boolean checked) {
            mList.get(listPosition).setChecked(checked);
            mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
        }

        @OnClick(R.id.iv_cart_add)
        public void addCart() {
            model.updateCart(mContext, I.ACTION_CART_ADD, user.getMuserName(),
                    mList.get(listPosition).getGoodsId(), 1, mList.get(listPosition).getId(),
                    new OnCompletListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                mList.get(listPosition).setCount(mList.get(listPosition).getCount() + 1);
                                mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }

        @OnClick(R.id.iv_cart_delete)
        public void delCart() {
            final int count = mList.get(listPosition).getCount();
            int action;
            if (count > 1) {
                action = I.ACTION_CART_UPDATA;
            } else {
                action = I.ACTION_CART_DELETE;
            }
            model.updateCart(mContext,action, user.getMuserName(),
                    mList.get(listPosition).getGoodsId(), count - 1, mList.get(listPosition).getId(),
                    new OnCompletListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result != null && result.isSuccess()) {
                                if (count <= 1) {
                                    mList.remove(listPosition);
                                } else {
                                    mList.get(listPosition).setCount(count-1);
                                }
                                mContext.sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }
    }
}
