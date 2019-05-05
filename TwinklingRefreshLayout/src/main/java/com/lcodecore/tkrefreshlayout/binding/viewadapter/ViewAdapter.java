package com.lcodecore.tkrefreshlayout.binding.viewadapter;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableFloat;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;


public class ViewAdapter {

    @BindingAdapter(value = {"tr_head_height"}, requireAll = false)
    public static void tr_head_height(TwinklingRefreshLayout view, final ObservableFloat observableFloat) {
        view.setHeaderHeight(observableFloat.get());
    }

    @BindingAdapter(value = {"tr_wave_height"}, requireAll = false)
    public static void tr_wave_height(TwinklingRefreshLayout view, final ObservableFloat observableFloat) {
        view.setBottomHeight(observableFloat.get());
    }
}
