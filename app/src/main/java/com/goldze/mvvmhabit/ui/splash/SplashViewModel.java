package com.goldze.mvvmhabit.ui.splash;

import android.app.Application;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.android.FragmentEvent;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class SplashViewModel extends BaseViewModel {

    private Disposable mDisposable;

    public SplashViewModel(@NonNull Application application) {
        super(application);
    }

    //跳转到首页的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand jumpToHomeOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    public void startCountDown() {
        // 倒计时 10s
        mDisposable = Flowable.intervalRange(0, 11, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        tvCountDown.setText("跳过 " + String.valueOf(10 - aLong));
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe();
    }



}
