package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.util.ImageLoader;
import cn.ucai.fulicenter.view.FooterViewHolder;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/19.
 */

public class CollectsAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<CollectBean> mList;
    String footer;
    boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
        notifyDataSetChanged();
    }

    public void initData(ArrayList<CollectBean> list) {
        if (mList != null) {
            mList.clear();
        }
        addData(list);
    }

    public void addData(ArrayList<CollectBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public CollectsAdapter(Context context, ArrayList<CollectBean> list) {
        this.mContext = context;
        this.mList = list;
//        mList = new ArrayList<>();
//        mList.addAll(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout;
        switch (viewType) {
            case I.TYPE_FOOTER:
                layout = inflater.inflate(R.layout.item_footer, null);
                return new FooterViewHolder(layout);
            case I.TYPE_ITEM:
                layout = inflater.inflate(R.layout.item_collects, null);
                return new CollectViewHolder(layout);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder holder = (FooterViewHolder) parentHolder;
            holder.tvFooter.setText(getFooter());
            return;
        }
        final CollectBean newGoods = mList.get(position);
        CollectViewHolder holder = (CollectViewHolder) parentHolder;
        holder.tvGoodsName.setText(newGoods.getGoodsName());
        ImageLoader.downloadImg(mContext, holder.ivGoodsThumb, newGoods.getGoodsThumb());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFGT.gotoGoodsDetail(mContext, newGoods.getGoodsId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    static class CollectViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.iv_collect_del)
        ImageView ivCollectDel;
        @BindView(R.id.layout_goods)
        RelativeLayout layoutGoods;

        CollectViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
