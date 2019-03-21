package com.shangtao.vadk.ui.home.news;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shangtao.base.BaseFragment;
import com.shangtao.vadk.BR;
import com.shangtao.vadk.R;
import com.shangtao.vadk.databinding.FragmentNewsBinding;


public class NewsFragment  extends BaseFragment<FragmentNewsBinding, NewsViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_news;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
