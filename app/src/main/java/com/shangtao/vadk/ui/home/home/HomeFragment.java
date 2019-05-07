package com.shangtao.vadk.ui.home.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import com.shangtao.base.BaseFragment;
import com.shangtao.vadk.BR;
import com.shangtao.vadk.R;
import com.shangtao.vadk.databinding.FragmentHomeBinding;


public class HomeFragment  extends BaseFragment<FragmentHomeBinding, HomeViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_home;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //监听下拉刷新完成
        viewModel.uc.finishRefreshing.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                binding.twinklingRefreshLayout.onRefreshComplete();
            }
        });
        //监听上拉加载完成
        viewModel.uc.finishLoadmore.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                binding.twinklingRefreshLayout.onRefreshComplete();
            }
        });
    }

    @Override
    public void initData() {
        //刷新、加载View 可定制、替换
        binding.twinklingRefreshLayout.setEmptyView(View.inflate(getActivity(),R.layout.layout_home_empty_view,null));
    }
}
