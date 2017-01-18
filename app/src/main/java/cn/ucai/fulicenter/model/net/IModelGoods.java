package cn.ucai.fulicenter.model.net;


import android.content.Context;

import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;


/**
 * Created by Administrator on 2017/1/11.
 */

public interface IModelGoods {
    void downData(Context context, int goodId, OnCompletListener<GoodsDetailsBean> listener);

    void isCollect(Context context, int goodId, String userName, OnCompletListener<MessageBean> listener);
}
