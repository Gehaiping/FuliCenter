package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.DBManager;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompletListener;
import cn.ucai.fulicenter.model.net.SharePrefrenceUtils;
import cn.ucai.fulicenter.model.util.CommonUtils;
import cn.ucai.fulicenter.model.util.L;
import cn.ucai.fulicenter.model.util.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = DBManager.class.getSimpleName();

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;

    IModelUser model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.backClickArea, R.id.btnLogin, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                MFGT.finish(this);
                break;
            case R.id.btnLogin:
                checkInput();
                break;
            case R.id.btnRegister:
                MFGT.gotoRegister(this);
                break;
        }
    }

    private void checkInput() {
        String userName = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            etUserName.setError(getString(R.string.user_name_connot_be_empty));
            etUserName.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.password_connot_be_empty));
            etPassword.requestFocus();
        } else {
            login(userName, password);
        }
    }

    private void login(String userName, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.logining));

        model = new ModelUser();
        model.login(this, userName, password, new OnCompletListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    L.e(TAG, "result=" + result);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            User user = (User) result.getRetData();
                            boolean saveUser = UserDao.getInstance().saveUser(user);//保存到数据库
                            L.e(TAG, "saveUser=" + saveUser);
                            if (saveUser) {
                                CommonUtils.showShortToast("登录成功");
                                SharePrefrenceUtils.getInstance(LoginActivity.this).saveUser(user.getMuserName());
                                FuLiCenterApplication.setUser(user);
                                setResult(RESULT_OK);
                                MFGT.finish(LoginActivity.this);
                            }
                        } else {
                            if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER) {
                                CommonUtils.showShortToast(getString(R.string.login_fail_unknow_user));
                            }
                            if (result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD) {
                                CommonUtils.showShortToast(getString(R.string.login_fail_error_password));
                            }
                        }
                    } else {
                        CommonUtils.showShortToast(getString(R.string.login_fail));
                    }
                } else {
                    CommonUtils.showShortToast(getString(R.string.login_fail));
                }
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                dialog.dismiss();
                CommonUtils.showShortToast(error);
            }
        });
    }
}
