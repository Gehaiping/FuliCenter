package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.view.DisplayUtils;

public class OrderActivity extends AppCompatActivity {
    int payPrice = 0;
    @BindView(R.id.ed_order_name)
    EditText mEdOrderName;
    @BindView(R.id.ed_order_phone)
    EditText mEdOrderPhone;
    @BindView(R.id.spin_order_province)
    Spinner mSpinOrderProvince;
    @BindView(R.id.ed_order_street)
    EditText mEdOrderStreet;
    @BindView(R.id.tv_order_price)
    TextView mTvOrderPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        payPrice = getIntent().getIntExtra(I.Cart.PAY_PRICE, 0);
        DisplayUtils.initBackWithTitle(this, "填写收货地址");
        setView();
    }

    private void setView() {
        mTvOrderPrice.setText("合计：￥" + payPrice);
    }

    @OnClick(R.id.tv_order_buy)
    public void onClick() {
        String receiveName = mEdOrderName.getText().toString();
        if (TextUtils.isEmpty(receiveName)) {
            mEdOrderName.setError("收货人姓名不能为空");
            mEdOrderName.requestFocus();
            return;
        }
        String phone = mEdOrderPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            mEdOrderPhone.setError("手机号码不能为空");
            mEdOrderPhone.requestFocus();
            return;
        }
        if (!phone.matches("[\\d]{11}")) {
            mEdOrderPhone.setError("手机号码格式有误");
            mEdOrderPhone.requestFocus();
            return;
        }
        String area = mSpinOrderProvince.getSelectedItem().toString();
        if (TextUtils.isEmpty(area)) {
            Toast.makeText(this, "收货地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = mEdOrderStreet.getText().toString();
        if (TextUtils.isEmpty(address)) {
            mEdOrderStreet.setError("街道地址不能为空");
            mEdOrderStreet.requestFocus();
            return;
        }
    }
}
