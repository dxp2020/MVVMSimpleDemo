package com.shangtao.vadk.ui.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shangtao.base.BaseFragment;
import com.shangtao.vadk.BR;
import com.shangtao.vadk.R;
import com.shangtao.vadk.databinding.FragmentNewsBinding;


public class NewsFragment  extends BaseFragment<FragmentNewsBinding, NewsViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return BR.viewModel;
    }

    @Override
    public int initVariableId() {
        return R.layout.fragment_news;
    }
}
