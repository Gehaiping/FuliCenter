package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.MessageBean;

/**
 * Created by Administrator on 2017/1/19.
 */

public interface IModelCart {
    void getCart(Context context, String userName, OnCompletListener<CartBean[]> listener);

    void updateCart(Context context, int action, String userName, int goodsId, int count, int cartId, OnCompletListener<MessageBean> listener);
}
