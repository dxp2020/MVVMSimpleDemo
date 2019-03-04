package com.shangtao.vadk.ui.splash;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

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

    public ObservableField<String> countDownContent = new ObservableField<>("跳过 3");

    //跳转到首页的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand jumpToHomeOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startActivity(HomeActivity.class);
            finish();
        }
    });

    public void startCountDown() {
        // 倒计时 3s
        Flowable.intervalRange(1, 4, 1, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxUtils.bindToLifecycle(getLifecycleProvider()))//界面关闭自动取消
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if((3 - aLong)>=0){
                            countDownContent.set("跳过 " + String.valueOf(3 - aLong));
                        }else{
                            finish();
                        }
                    }
                });
    }



}
