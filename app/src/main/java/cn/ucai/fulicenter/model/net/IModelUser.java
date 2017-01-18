package cn.ucai.fulicenter.model.net;


import android.content.Context;

import java.io.File;

import cn.ucai.fulicenter.model.bean.MessageBean;


/**
 * Created by Administrator on 2017/1/11.
 */

public interface IModelUser {
    void login(Context context, String userName, String password, OnCompletListener<String> listener);

    void register(Context context, String userName, String userNick, String password, OnCompletListener<String> listener);

    void updatNick(Context context, String userName, String userNick, OnCompletListener<String> listener);

    void uploadAvatar(Context context, String userName, File file, OnCompletListener<String> listener);

    void collectCount(Context context, String userName, OnCompletListener<MessageBean> listener);
}
