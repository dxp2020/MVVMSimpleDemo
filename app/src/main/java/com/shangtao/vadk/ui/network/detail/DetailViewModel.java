package com.shangtao.vadk.ui.network.detail;

import android.app.Application;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;

import com.shangtao.vadk.entity.DemoEntity;

import com.shangtao.base.BaseViewModel;

/**
 * Created by goldze on 2017/7/17.
 */

public class DetailViewModel extends BaseViewModel {
    public ObservableField<DemoEntity.ItemsEntity> entity = new ObservableField();

    public DetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void setDemoEntity(DemoEntity.ItemsEntity entity) {
        this.entity.set(entity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        entity = null;
    }
}
