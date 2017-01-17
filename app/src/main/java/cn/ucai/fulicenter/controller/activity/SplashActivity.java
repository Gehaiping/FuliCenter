package cn.ucai.fulicenter.controller.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.net.SharePrefrenceUtils;
import cn.ucai.fulicenter.model.util.L;
import cn.ucai.fulicenter.view.MFGT;

/**
 * 控制闪屏的activity
 */
public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String userName = SharePrefrenceUtils.getInstance(SplashActivity.this).getUser();
                if (userName != null) {
                    User user = UserDao.getInstance().getUser(userName);
                    L.e(TAG, "user=" + user);
                    if (user != null) {
                        FuLiCenterApplication.setUser(user);
                    }
                }
                MFGT.startActivity(SplashActivity.this, MainActivity.class);
                MFGT.finish(SplashActivity.this);
            }
        }, 2000);
    }
}
