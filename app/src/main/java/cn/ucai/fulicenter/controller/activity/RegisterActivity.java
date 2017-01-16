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
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompletListener;
import cn.ucai.fulicenter.model.util.CommonUtils;
import cn.ucai.fulicenter.model.util.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.etUserName)
    EditText mUserName;
    @BindView(R.id.etNick)
    EditText mNick;
    @BindView(R.id.etPassword)
    EditText mPassword;
    @BindView(R.id.etConfirmPassword)
    EditText mConfirmPassword;

    IModelUser model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.ivReturn, R.id.btnRegister})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivReturn:
                MFGT.finish(this);
                break;
            case R.id.btnRegister:
                checkInput();
                break;
        }
    }

    private void checkInput() {
        String userName = mUserName.getText().toString().trim();
        String Nick = mNick.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String confirmPassword = mConfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            mUserName.setError(getResources().getString(R.string.user_name_connot_be_empty));
            mUserName.requestFocus();
        } else if (!userName.matches("[a-zA-Z]\\w{5,15}")) {
            mUserName.setError(getResources().getString(R.string.illegal_user_name));
            mUserName.requestFocus();
        } else if (TextUtils.isEmpty(Nick)) {
            mNick.setError(getResources().getString(R.string.nick_name_connot_be_empty));
            mNick.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            mPassword.setError(getResources().getString(R.string.password_connot_be_empty));
            mPassword.requestFocus();
        } else if (TextUtils.isEmpty(confirmPassword)) {
            mConfirmPassword.setError(getResources().getString(R.string.confirm_password_connot_be_empty));
            mConfirmPassword.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            mConfirmPassword.setError(getResources().getString(R.string.two_input_password));
            mConfirmPassword.requestFocus();
        } else {
            register(userName, Nick, password);
        }

    }

    private void register(String userName, String nick, String password) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.registering));

        model = new ModelUser();
        model.register(this, userName, nick, password, new OnCompletListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, Result.class);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            CommonUtils.showShortToast(R.string.register_success);
                            MFGT.finish(RegisterActivity.this);
                        } else {
                            CommonUtils.showShortToast(R.string.register_fail_exists);
                        }
                    } else {
                        CommonUtils.showShortToast(R.string.register_fail);
                    }
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
