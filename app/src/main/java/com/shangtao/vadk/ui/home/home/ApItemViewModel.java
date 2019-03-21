package com.shangtao.vadk.ui.home.home;

import com.shangtao.base.ItemViewModel;
import com.shangtao.binding.command.BindingAction;
import com.shangtao.binding.command.BindingCommand;
import com.shangtao.utils.ToastUtils;
import com.shangtao.vadk.entity.DkAppEntity;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

public class ApItemViewModel extends ItemViewModel<HomeViewModel> {
    public ObservableField<DkAppEntity> entity = new ObservableField<>();

    public ApItemViewModel(@NonNull HomeViewModel viewModel,DkAppEntity pDkAppEntity) {
        super(viewModel);
        this.entity.set(pDkAppEntity);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort("单击   "+entity.get().getAppName());
        }
    });

    //条目的长按事件
    public BindingCommand itemLongClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort("长按   "+entity.get().getAppName());
        }
    });

}
