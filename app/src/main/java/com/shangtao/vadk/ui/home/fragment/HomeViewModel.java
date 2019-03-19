package com.shangtao.vadk.ui.home.fragment;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.shangtao.base.BaseViewModel;
import com.shangtao.binding.command.BindingCommand;
import com.shangtao.utils.ToastUtils;
import com.shangtao.vadk.BR;
import com.shangtao.vadk.R;
import com.shangtao.vadk.entity.DkAppEntity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class HomeViewModel extends BaseViewModel {
    private int itemIndex = 0;

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        requestNetWork();
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //下拉刷新完成
        ObservableBoolean finishRefreshing = new ObservableBoolean(false);
    }

    //给RecyclerView添加Adpter，请使用自定义的Adapter继承BindingRecyclerViewAdapter，重写onBindBinding方法，里面有你要的Item对应的binding对象
    public final BindingRecyclerViewAdapter<ApItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    //给RecyclerView添加ItemBinding
    public ItemBinding<ApItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_added_ap);

    public ObservableList<ApItemViewModel> observableList = new ObservableArrayList<>();

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(() -> {
        requestNetWork();
    });

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
            public void onComplete() {
                ToastUtils.showShort("应用加载完成！");
//                uc.finishRefreshing.set(!uc.finishRefreshing.get());
            }
            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }
        });
        /*RetrofitClient.getInstance().create(DemoApiService.class)
                .demoGet()
                .compose(RxUtils.bindToLifecycle(getLifecycleProvider())) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog("正在请求...");
                    }
                })
                .subscribe(new Consumer<BaseResponse<DemoEntity>>() {
                    @Override
                    public void accept(BaseResponse<DemoEntity> response) throws Exception {
                        itemIndex = 0;
                        //清除列表
                        observableList.clear();
                        //请求成功
                        if (response.getCode() == 1) {
                            //将实体赋给LiveData
                            for (DemoEntity.ItemsEntity entity : response.getResult().getItems()) {
                                NetWorkItemViewModel itemViewModel = new NetWorkItemViewModel(HomeViewModel.this, entity);
                                //双向绑定动态添加Item
                                observableList.add(itemViewModel);
                            }
                        } else {
                            //code错误时也可以定义Observable回调到View层去处理
                            ToastUtils.showShort("数据错误");
                        }
                    }
                }, new Consumer<ResponseThrowable>() {
                    @Override
                    public void accept(ResponseThrowable throwable) throws Exception {
                        //关闭对话框
                        dismissDialog();
                        //请求刷新完成收回
                        uc.finishRefreshing.set(!uc.finishRefreshing.get());
                        ToastUtils.showShort(throwable.message);
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //关闭对话框
                        dismissDialog();
                        //请求刷新完成收回
                        uc.finishRefreshing.set(!uc.finishRefreshing.get());
                    }
                });*/
    }

}
