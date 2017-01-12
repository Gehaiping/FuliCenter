package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.content.Intent;
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
import cn.ucai.fulicenter.controller.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.util.ImageLoader;
import cn.ucai.fulicenter.view.FooterViewHolder;

/**
 * Created by Administrator on 2017/1/11.
 */

public class BoutiqueAdapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<BoutiqueBean> mList;


    public void initData(ArrayList<BoutiqueBean> list) {
        if (mList != null) {
            mList.clear();
        }
        addData(list);
    }

    public void addData(ArrayList<BoutiqueBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public BoutiqueAdapter(Context context, ArrayList<BoutiqueBean> list) {
        this.mContext = context;
        mList = new ArrayList<>();
        if (list != null) {
            mList.addAll(list);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = inflater.inflate(R.layout.item_boutique, null);
        return new BoutiqueViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder parentHolder, int position) {
        final BoutiqueBean boutique = mList.get(position);
        BoutiqueViewHolder holder = (BoutiqueViewHolder) parentHolder;
        holder.tvBoutiqueName.setText(boutique.getName());
        holder.tvBoutiqueTitle.setText(boutique.getTitle());
        holder.tvBoutiqueDescription.setText(boutique.getDescription());
        ImageLoader.downloadImg(mContext, holder.ivBoutiqueImg, boutique.getImageurl());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BoutiqueChildActivity.class);
                intent.putExtra(I.NewAndBoutiqueGoods.CAT_ID, boutique.getId());
                intent.putExtra(I.Boutique.NAME, boutique.getTitle());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivBoutiqueImg)
        ImageView ivBoutiqueImg;
        @BindView(R.id.tvBoutiqueTitle)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tvBoutiqueName)
        TextView tvBoutiqueName;
        @BindView(R.id.tvBoutiqueDescription)
        TextView tvBoutiqueDescription;
        @BindView(R.id.layout_boutique_item)
        RelativeLayout layoutBoutiqueItem;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
