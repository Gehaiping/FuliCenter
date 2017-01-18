package cn.ucai.fulicenter.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IModelUser;
import cn.ucai.fulicenter.model.net.ModelUser;
import cn.ucai.fulicenter.model.net.OnCompletListener;
import cn.ucai.fulicenter.model.util.ImageLoader;
import cn.ucai.fulicenter.model.util.L;
import cn.ucai.fulicenter.view.MFGT;

public class PersonalCenterFragment extends Fragment {
    private static final String TAG = PersonalCenterFragment.class.getSimpleName();

    @BindView(R.id.iv_user_avatar)
    ImageView mIvUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_collect_count)
    TextView mTvCollectCount;

    IModelUser model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_personal_center, container, false);
        ButterKnife.bind(this, layout);
        initData();
        return layout;
    }

    private void initData() {
        User user = FuLiCenterApplication.getUser();
        if (user != null) {
            loadUserInfo(user);
        } else {
            // MFGT.gotoLogin(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        getCollectCount();

    }

    private void loadUserInfo(User user) {
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), getContext(), mIvUserAvatar);
        mTvUserName.setText(user.getMuserNick());
        loadCollectCount("0");
    }

    private void getCollectCount() {
        model = new ModelUser();
        model.collectCount(getContext(), FuLiCenterApplication.getUser().getMuserName(),
                new OnCompletListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        L.e(TAG, "result=" + result);
                        if (result != null && result.isSuccess()) {
                            loadCollectCount(result.getMsg());
                        } else {
                            loadCollectCount("0");
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e(TAG, "error=" + error);
                        loadCollectCount("0");
                    }
                });
    }

    private void loadCollectCount(String count) {
        mTvCollectCount.setText(String.valueOf(count));
    }

    @OnClick({R.id.tv_center_settings, R.id.center_user_info})
    public void onClick() {
        MFGT.gotoSettings(getActivity());
    }
}
