package com.shangtao.vadk.ui.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.shangtao.base.BaseFragment;
import com.shangtao.vadk.BR;
import com.shangtao.vadk.R;
import com.shangtao.vadk.databinding.FragmentPersonalCenterBinding;

import androidx.annotation.Nullable;

public class PersonalCenterFragment extends BaseFragment<FragmentPersonalCenterBinding, PersonalCenterViewModel> {

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_personal_center;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
