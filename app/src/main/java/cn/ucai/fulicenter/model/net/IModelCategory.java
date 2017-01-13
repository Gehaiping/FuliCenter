package cn.ucai.fulicenter.model.net;


import android.content.Context;

import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;


/**
 * Created by Administrator on 2017/1/11.
 */

public interface IModelCategory {
    void downData(Context context, OnCompletListener<CategoryGroupBean[]> listener);

    void downData(Context context, int parentId, OnCompletListener<CategoryChildBean[]> listener);
}
