package com.shangtao.vadk.ui.network;

import androidx.lifecycle.Observer;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.shangtao.vadk.BR;
import com.shangtao.vadk.R;
import com.shangtao.vadk.databinding.FragmentNetworkBinding;

import com.shangtao.base.BaseFragment;
import com.shangtao.utils.MaterialDialogUtils;
import com.shangtao.utils.ToastUtils;

/**
 * Created by goldze on 2017/7/17.
 * 网络请求列表界面
 */

public class NetWorkFragment extends BaseFragment<FragmentNetworkBinding, NetWorkViewModel> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fragment_network;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        //请求网络数据
        viewModel.requestNetWork();
    }

    @Override
    public void initViewObservable() {
        //监听下拉刷新完成
        viewModel.uc.finishRefreshing.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.twinklingRefreshLayout.finishRefreshing();
            }
        });
        //监听上拉加载完成
        viewModel.uc.finishLoadmore.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //结束刷新
                binding.twinklingRefreshLayout.finishLoadmore();
            }
        });
        //监听删除条目
        viewModel.deleteItemLiveData.observe(this, netWorkItemViewModel -> {
            int index = viewModel.getPosition(netWorkItemViewModel);
            //删除选择对话框
            MaterialDialogUtils.showBasicDialog(getContext(),
                    "提示",
                    "是否删除【" + netWorkItemViewModel.entity.get().getName() + "】？ position：" + index)
                    .onNegative((dialog, which) -> ToastUtils.showShort("取消"))
                    .onPositive((dialog, which) -> viewModel.deleteItem(netWorkItemViewModel))
                    .show();
        });
    }
}
