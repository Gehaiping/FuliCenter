package cn.ucai.fulicenter.model.net;


import android.content.Context;

import cn.ucai.fulicenter.model.bean.BoutiqueBean;


/**
 * Created by Administrator on 2017/1/11.
 */

public interface IModelUser {
    void login(Context context, String userName, String password, OnCompletListener<String> listener);

    void register(Context context, String userName, String userNick, String password, OnCompletListener<String> listener);
}