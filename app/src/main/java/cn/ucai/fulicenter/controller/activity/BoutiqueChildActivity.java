package cn.ucai.fulicenter.controller.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.controller.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.view.MFGT;

public class BoutiqueChildActivity extends AppCompatActivity {
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.fragment_boutiqueChild)
    FrameLayout fragmentBoutiqueChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_child);
        ButterKnife.bind(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_boutiqueChild, new NewGoodsFragment())
                .commit();
        String title = getIntent().getStringExtra(I.Boutique.NAME);
        tvCommonTitle.setText(title);
    }

    @OnClick(R.id.back)
    public void onClick() {
        MFGT.finish(this);
    }
}
