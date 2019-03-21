package com.shangtao.vadk.ui.home.home;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.shangtao.base.BaseViewModel;
import com.shangtao.vadk.BR;
import com.shangtao.vadk.R;
import com.shangtao.vadk.entity.DkAppEntity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class HomeViewModel extends BaseViewModel {

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        requestNetWork();
    }

    //给RecyclerView添加Adpter，请使用自定义的Adapter继承BindingRecyclerViewAdapter，重写onBindBinding方法，里面有你要的Item对应的binding对象
    public final BindingRecyclerViewAdapter<ApItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    //给RecyclerView添加ItemBinding
    public ItemBinding<ApItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_added_ap);

    public ObservableList<ApItemViewModel> observableList = new ObservableArrayList<>();

    /**
     * 网络请求方法，在ViewModel中调用，Retrofit+RxJava充当Repository，即可视为Model层
     */
    private void requestNetWork() {
        Flowable.create((FlowableOnSubscribe<DkAppEntity>) emitter -> {
            PackageManager pm = getApplication().getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            for (PackageInfo packageInfo : packages) {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) // 非系统应用
                {
                    DkAppEntity appInfo = new DkAppEntity();
                    appInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());//获取应用名称
                    appInfo.setPackageName(packageInfo.packageName); //获取应用包名，可用于卸载和启动应用
                    appInfo.setVersionName(packageInfo.versionName);//获取应用版本名
                    appInfo.setVersionCode(packageInfo.versionCode);//获取应用版本号
                    appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));//获取应用图标
                    emitter.onNext(appInfo);
                }
            }
            emitter.onComplete();
        },BackpressureStrategy.BUFFER)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<DkAppEntity>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }
            @Override
            public void onNext(DkAppEntity dkAppEntity) {
                observableList.add(new ApItemViewModel(HomeViewModel.this,dkAppEntity));
            }
            @Override
            public void onComplete() {}
            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
