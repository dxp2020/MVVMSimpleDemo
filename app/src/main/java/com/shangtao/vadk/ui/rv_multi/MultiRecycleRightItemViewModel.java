package com.shangtao.vadk.ui.rv_multi;

import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

import com.shangtao.base.MultiItemViewModel;

import com.shangtao.binding.command.BindingAction;
import com.shangtao.binding.command.BindingCommand;
import com.shangtao.utils.ToastUtils;

/**
 * Create Author：goldze
 * Create Date：2019/01/25
 * Description：
 */

public class MultiRecycleRightItemViewModel extends MultiItemViewModel<MultiRecycleViewModel> {
    public ObservableField<String> text = new ObservableField<>("");

    public MultiRecycleRightItemViewModel(@NonNull MultiRecycleViewModel viewModel, String text) {
        super(viewModel);
        this.text.set(text);
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //拿到position
            int position = viewModel.observableList.indexOf(MultiRecycleRightItemViewModel.this);
            ToastUtils.showShort("position：" + position);
        }
    });
}
