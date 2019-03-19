package com.shangtao.vadk.ui.splash;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.shangtao.vadk.BR;
import com.shangtao.vadk.R;
import com.shangtao.vadk.databinding.ActivitySpalshBinding;

import com.shangtao.base.BaseActivity;

public class SplashActivity extends BaseActivity<ActivitySpalshBinding,SplashViewModel>{

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_spalsh;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public SplashViewModel initViewModel() {
        //View持有ViewModel的引用，如果没有特殊业务处理，这个方法可以不重写
        return ViewModelProviders.of(this).get(SplashViewModel.class);
    }

}
