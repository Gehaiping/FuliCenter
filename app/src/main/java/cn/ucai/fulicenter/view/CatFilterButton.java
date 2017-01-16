package cn.ucai.fulicenter.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;

/**
 * Created by Administrator on 2017/1/16.
 */

public class CatFilterButton extends Button {
    boolean isExpand;
    PopupWindow mPopupWindow;
    Context mContext;

    public CatFilterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void initCatFileterButton(String groupName, ArrayList<CategoryChildBean> list) {
        this.setText(groupName);
        setCatFilterButtonListener();
    }

    private void setCatFilterButtonListener() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isExpand) {
                    initPopup();
                } else {
                    if (mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                }
                setArrow();
            }
        });
    }

    private void initPopup() {
        mPopupWindow = new PopupWindow();
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
        TextView textView = new TextView(mContext);
        textView.setText("HELLO...");
        mPopupWindow.setContentView(textView);
        mPopupWindow.showAsDropDown(this);
    }

    private void setArrow() {
        Drawable right;
        if (isExpand) {
            right = getResources().getDrawable(R.mipmap.arrow2_up);
        } else {
            right = getResources().getDrawable(R.mipmap.arrow2_down);
        }
        right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, right, null);
        isExpand = !isExpand;
    }

    class CatFilterAdapter extends BaseAdapter {
        Context context;
        ArrayList<CategoryChildBean> list;

        public CatFilterAdapter(Context context, ArrayList<CategoryChildBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CategoryChildBean getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            CatFilterViewHolder viewHolder = null;
            if (view == null) {
                view = View.inflate(context, R.layout.item_cat_filter, null);
                viewHolder = new CatFilterViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (CatFilterViewHolder) view.getTag();
            }
            return view;
        }

        class CatFilterViewHolder {
            @BindView(R.id.ivCategoryChildThumb)
            ImageView ivCategoryChildThumb;
            @BindView(R.id.tvCategoryChildName)
            TextView tvCategoryChildName;
            @BindView(R.id.layout_category_child)
            RelativeLayout layoutCategoryChild;

            CatFilterViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
