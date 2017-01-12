package cn.ucai.fulicenter.model.net;


import android.content.Context;

import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.util.OkHttpUtils;

/**
 * Created by Administrator on 2017/1/12.
 */

public class ModelGoods implements IModelGoods {
    @Override
    public void downData(Context context,int goodId, OnCompletListener<GoodsDetailsBean> listener) {
        OkHttpUtils<GoodsDetailsBean> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.Goods.KEY_GOODS_ID, String.valueOf(goodId))
                .targetClass(GoodsDetailsBean.class)
                .execute(listener);
    }
}
