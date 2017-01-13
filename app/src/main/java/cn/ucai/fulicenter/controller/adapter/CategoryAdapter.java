package cn.ucai.fulicenter.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;

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
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_category_group, null);
        return inflate;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_category_child, null);
        return inflate;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
