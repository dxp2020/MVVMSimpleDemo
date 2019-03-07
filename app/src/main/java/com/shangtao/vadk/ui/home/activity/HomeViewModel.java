package com.shangtao.vadk.ui.home.activity;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.shangtao.base.BaseViewModel;
import com.shangtao.binding.command.BindingAction;
import com.shangtao.binding.command.BindingCommand;
import com.shangtao.binding.command.BindingConsumer;
import com.shangtao.utils.ToastUtils;
import com.shangtao.vadk.R;
import com.shangtao.vadk.ui.login.LoginViewModel;

public class HomeViewModel extends BaseViewModel {

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public ObservableField<View> mSelectedObservable = new ObservableField<>();
    }

    public BindingCommand menuClickCommand = new BindingCommand((BindingConsumer<View>) view -> {
        uc.mSelectedObservable.set(view);
        switch (view.getId()){
            case R.id.radio_home:
                ToastUtils.showShort("首页");
                break;
            case R.id.radio_news:
                ToastUtils.showShort("资讯");
                break;
            case R.id.iv_add_fs:
                ToastUtils.showShort("添加");
                break;
            case R.id.radio_events:
                ToastUtils.showShort("活动");
                break;
            case R.id.radio_my:
                ToastUtils.showShort("我的");
                break;
        }
    });
}
