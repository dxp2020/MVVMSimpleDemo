package com.handmark.pulltorefresh.library.binding.viewadapter.view;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.handmark.pulltorefresh.library.binding.viewadapter.recycleview.LineManagers;

import java.util.List;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.LayoutManagers;

public class ViewAdapter {

    @BindingAdapter(value = {"onRefreshCommand"}, requireAll = false)
    public static void onRefreshCommand(PullToRefreshBase view, PullToRefreshBase.OnRefreshListener pOnRefreshListener) {
        view.setOnRefreshListener(pOnRefreshListener::onRefresh);
    }

    @BindingAdapter(value = {"onRefreshLoadMoreCommand"}, requireAll = false)
    public static void onRefreshAndLoadMoreCommand(PullToRefreshBase view, PullToRefreshBase.OnRefreshListener2 pOnRefreshListener) {
        view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                pOnRefreshListener.onPullDownToRefresh(refreshView);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                pOnRefreshListener.onPullUpToRefresh(refreshView);
            }
        });
    }

}
