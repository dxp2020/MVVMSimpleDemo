package com.shangtao.vadk.ui.splash;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import com.shangtao.base.BaseViewModel;
import com.shangtao.binding.command.BindingAction;
import com.shangtao.binding.command.BindingCommand;
import com.shangtao.utils.RxUtils;
import com.shangtao.vadk.ui.home.activity.HomeActivity;

public class SplashViewModel extends BaseViewModel {

    public SplashViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startCountDown();
    }

    public ObservableField<String> countDownContent = new ObservableField<>("跳过 3");

    //跳转到首页的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand jumpToHomeOnClickCommand = new BindingCommand(() -> {
        startActivity(HomeActivity.class);
        finish();
    });

    @SuppressLint("CheckResult")
    public void startCountDown() {
        // 倒计时 3s
        Flowable.intervalRange(1, 4, 1, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.bindToLifecycle(getLifecycleProvider()))//界面关闭自动取消
                .subscribe((Consumer<Long>) count -> {
                    if((3 - count)>=0){
                        countDownContent.set("跳过 " + String.valueOf(3 - count));
                    }else{
                        startActivity(HomeActivity.class);
                        finish();
                    }
                });
    }



}
