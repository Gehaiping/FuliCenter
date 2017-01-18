package cn.ucai.fulicenter.controller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompletListener;
import cn.ucai.fulicenter.model.util.CommonUtils;
import cn.ucai.fulicenter.model.util.L;
import cn.ucai.fulicenter.model.util.ResultUtils;
import cn.ucai.fulicenter.view.DisplayUtils;

public class UpdataNickActivity extends AppCompatActivity {
    private static final String TAG = UpdataNickActivity.class.getSimpleName();

    @BindView(R.id.et_updata_user_name)
    EditText mEtUpdataUserName;
    User user;
    IModelUser model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_nick);
        ButterKnife.bind(this);
        DisplayUtils.initBackWithTitle(this, "修改昵称");
        initData();
    }

    private void initData() {
        user = FuLiCenterApplication.getUser();
        if (user == null) {
            finish();
        } else {
            mEtUpdataUserName.setText(user.getMuserNick());
        }
    }

    @OnClick(R.id.btn_save)
    public void checkInput() {
        String nick = mEtUpdataUserName.getText().toString().trim();
        if (TextUtils.isEmpty(nick)) {
            CommonUtils.showShortToast(R.string.confirm_password_connot_be_empty);
        } else if (nick.equals(user.getMuserNick())) {
            CommonUtils.showShortToast(R.string.update_nick_fail_unmodify);
        } else {
            updateNick(nick);
        }
    }

    private void updateNick(String nick) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.update_user_nick));
        dialog.show();
        model = new ModelUser();
        model.updatNick(this, user.getMuserName(), nick, new OnCompletListener<String>() {
            @Override
            public void onSuccess(String s) {
                int msg = R.string.update_fail;
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            msg = R.string.update_user_nick_success;
                            User user = (User) result.getRetData();
                            L.e("update success,user=" + user);
                            saveNewUser(user);
                            setResult(RESULT_OK);
                            finish();
                        } else if (result.getRetCode() == I.MSG_USER_SAME_NICK ||
                                result.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL) {
                            msg = R.string.update_nick_fail_unmodify;
                        }
                    }
                }
                CommonUtils.showShortToast(msg);
                dialog.dismiss();
            }

            @Override
            public void onError(String error) {
                CommonUtils.showShortToast(R.string.update_fail);
                dialog.dismiss();
                L.e(TAG, "error=" + error);
            }
        });
    }

    private void saveNewUser(User user) {
        FuLiCenterApplication.setUser(user);
        UserDao.getInstance().saveUser(user);
    }
}
