package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.util.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/1/13.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<CategoryGroupBean> mGroupBeanList;
    ArrayList<ArrayList<CategoryChildBean>> mChildBeanList;

    public CategoryAdapter(Context context, ArrayList<CategoryGroupBean> groupBean,
                           ArrayList<ArrayList<CategoryChildBean>> childBean) {
        this.mContext = context;
        this.mGroupBeanList = new ArrayList<>();
        mGroupBeanList.addAll(groupBean);
        this.mChildBeanList = new ArrayList<>();
        mChildBeanList.addAll(childBean);
    }

    @Override
    public int getGroupCount() {
        return mGroupBeanList != null ? mGroupBeanList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildBeanList != null && mChildBeanList.get(groupPosition) != null ?
                mChildBeanList.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        if (mGroupBeanList != null && mGroupBeanList.get(groupPosition) != null) {
            return mGroupBeanList.get(groupPosition);
        }
        return null;
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        if (mChildBeanList != null && mChildBeanList.get(groupPosition) != null) {
            return mChildBeanList.get(groupPosition).get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        GroupViewHolder holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category_group, null);
            holder = new GroupViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (GroupViewHolder) view.getTag();
        }
        ImageLoader.downloadImg(mContext, holder.mIvGroupThumb, getGroup(groupPosition).getImageUrl());
        holder.mTvGroupName.setText(mGroupBeanList.get(groupPosition).getName());
        holder.mIvIndicator.setImageResource(isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        ChildViewHolder holder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category_child, null);
            holder = new ChildViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ChildViewHolder) view.getTag();
        }
        ImageLoader.downloadImg(mContext, holder.mIvCategoryChildThumb, getChild(groupPosition, childPosition).getImageUrl());
        holder.mTvCategoryChildName.setText(getChild(groupPosition, childPosition).getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MFGT.gotoCategoryChild(mContext, getChild(groupPosition, childPosition).getId());
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void initData(ArrayList<CategoryGroupBean> groupBeen, ArrayList<ArrayList<CategoryChildBean>> childBean) {
        mGroupBeanList.clear();
        mGroupBeanList.addAll(groupBeen);
        mChildBeanList.clear();
        mChildBeanList.addAll(childBean);
        notifyDataSetChanged();
    }

    static class GroupViewHolder {
        @BindView(R.id.iv_group_thumb)
        ImageView mIvGroupThumb;
        @BindView(R.id.tv_group_name)
        TextView mTvGroupName;
        @BindView(R.id.iv_indicator)
        ImageView mIvIndicator;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        @BindView(R.id.iv_category_child_thumb)
        ImageView mIvCategoryChildThumb;
        @BindView(R.id.tv_category_child_name)
        TextView mTvCategoryChildName;
        @BindView(R.id.layout_category_child)
        RelativeLayout mLayoutCategoryChild;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
