package com.shangtao.vadk.binding.refreshlayout;


import androidx.databinding.BindingAdapter;

import com.shangtao.binding.command.BindingCommand;
import com.shangtao.refreshlayout.RefreshLayout;


/**
 * Created by goldze on 2017/6/16.
 * TwinklingRefreshLayout列表刷新的绑定适配器
 */
public class ViewAdapter {

    @BindingAdapter(value = {"onRefreshCommand", "onLoadMoreCommand"}, requireAll = false)
    public static void onRefreshAndLoadMoreCommand(RefreshLayout layout, final BindingCommand onRefreshCommand, final BindingCommand onLoadMoreCommand) {
        layout.setDelegate(new RefreshLayout.RefreshLayoutDelegate() {
            @Override
            public void onRefreshLayoutBeginRefreshing(RefreshLayout refreshLayout) {
                if (onRefreshCommand != null) {
                    onRefreshCommand.execute();
                }
            }

            @Override
            public boolean onRefreshLayoutBeginLoadingMore(RefreshLayout refreshLayout) {
                if (onLoadMoreCommand != null) {
                    onLoadMoreCommand.execute();
                }
                return true;
            }
        });
    }

}
