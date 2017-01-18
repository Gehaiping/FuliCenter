package cn.ucai.fulicenter.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.controller.activity.CategoryChildActivity;
import cn.ucai.fulicenter.controller.activity.GoodsDetailsActivity;
import cn.ucai.fulicenter.controller.activity.LoginActivity;
import cn.ucai.fulicenter.controller.activity.MainActivity;
import cn.ucai.fulicenter.controller.activity.RegisterActivity;
import cn.ucai.fulicenter.controller.activity.SettingsActivity;
import cn.ucai.fulicenter.controller.activity.UpdataNickActivity;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;

/**
 * Created by Administrator on 2017/1/10.
 */

public class MFGT {
    public static void startActivity(Activity context, Class<?> clz) {
        context.startActivity(new Intent(context, clz));
        context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void startActivity(Activity context, Intent intent) {
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void gotoBoutiqueChild(Context context, BoutiqueBean boutique) {
        Intent intent = new Intent(context, BoutiqueChildActivity.class);
        intent.putExtra(I.NewAndBoutiqueGoods.CAT_ID, boutique.getId());
        intent.putExtra(I.Boutique.NAME, boutique.getTitle());
        startActivity((Activity) context, intent);
    }

    public static void gotoGoodsDetail(Context context, int goodsId) {
        Intent intent = new Intent(context, GoodsDetailsActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID, goodsId);
        startActivity((Activity) context, intent);
    }

    public static void gotoCategoryChild(Context context, int catId, String groupName, ArrayList<CategoryChildBean> list) {
        Intent intent = new Intent(context, CategoryChildActivity.class);
        intent.putExtra(I.NewAndBoutiqueGoods.CAT_ID, catId);
        intent.putExtra(I.CategoryGroup.NAME, groupName);
        intent.putExtra(I.CategoryChild.DATA, list);
        startActivity((Activity) context, intent);
    }

    public static void gotoLogin(Activity context) {
        context.startActivityForResult(new Intent(context, LoginActivity.class)
                , I.REQUEST_CODE_LOGIN);
    }

    public static void gotoRegister(LoginActivity loginActivity) {
        startActivity(loginActivity, RegisterActivity.class);
    }

    public static void gotoSettings(Activity activity) {
        startActivity(activity, SettingsActivity.class);
    }

    public static void gotoUpDataNick(Activity activity) {
        activity.startActivityForResult(new Intent(activity, UpdataNickActivity.class), I.REQUEST_CODE_NICK);
    }
}
