package com.shangtao.vadk.ui.home.home;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.swipe.SwipeMenuCreator;
import com.handmark.pulltorefresh.library.swipe.SwipeMenuItem;
import com.handmark.pulltorefresh.library.swipe.SwipeMenuListView;
import com.shangtao.base.BaseViewModel;
import com.shangtao.utils.ConvertUtils;
import com.shangtao.utils.RxUtils;
import com.shangtao.utils.ToastUtils;
import com.shangtao.vadk.BR;
import com.shangtao.vadk.R;
import com.shangtao.vadk.entity.DkAppEntity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.BindingListViewAdapter;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class HomeViewModel extends BaseViewModel {

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

  /*  //给RecyclerView添加Adpter，请使用自定义的Adapter继承BindingRecyclerViewAdapter，重写onBindBinding方法，里面有你要的Item对应的binding对象
    public final BindingRecyclerViewAdapter<ApItemViewModel> adapter = new BindingRecyclerViewAdapter<>();*/

    //给ListView添加Adpter，请使用自定义的Adapter继承BindingListViewAdapter，重写onBindBinding方法，里面有你要的Item对应的binding对象
    public final BindingListViewAdapter<ApItemViewModel> adapter = new BindingListViewAdapter<>(1);

    //给RecyclerView添加ItemBinding
    public ItemBinding<ApItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_added_ap);

    public ObservableList<ApItemViewModel> observableList = new ObservableArrayList<>();

    //下拉刷新、上拉加载
    public PullToRefreshBase.OnRefreshListener2 onRefreshLoadMoreCommand = new PullToRefreshBase.OnRefreshListener2(){
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            loadData();
        }
        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            loadMore();
        }
    };

    public SwipeMenuCreator menuCreator = menu -> {
        SwipeMenuItem deleteItem = new SwipeMenuItem(getApplication());
        deleteItem.setBackground(new ColorDrawable(Color.parseColor("#e83f22")));
        deleteItem.setWidth(ConvertUtils.dp2px(90));
        deleteItem.setTitle("删除");
        deleteItem.setTitleSize(16);
        deleteItem.setTitleColor(Color.WHITE);
        menu.addMenuItem(deleteItem);
    };

    public SwipeMenuListView.OnMenuItemClickListener menuItemClickCommand = (position, menu, index) -> {
        if (index == 0) {
            ToastUtils.showShort(menu.getMenuItem(index).getTitle() + observableList.get(position).entity.get().getAppName());
            observableList.remove(position);
        }
        return false;
    };

    /**
     * 网络请求方法，在ViewModel中调用，Retrofit+RxJava充当Repository，即可视为Model层
     */
    private void loadData() {
        Flowable.create((FlowableOnSubscribe<List<DkAppEntity>>) emitter -> {
            PackageManager pm = getApplication().getPackageManager();
            List<PackageInfo> packages = pm.getInstalledPackages(0);
            List<DkAppEntity> dkAppEntities = new ArrayList<>();
            for (PackageInfo packageInfo : packages) {
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) // 非系统应用
                {
                    DkAppEntity appInfo = new DkAppEntity();
                    appInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());//获取应用名称
                    appInfo.setPackageName(packageInfo.packageName); //获取应用包名，可用于卸载和启动应用
                    appInfo.setVersionName(packageInfo.versionName);//获取应用版本名
                    appInfo.setVersionCode(packageInfo.versionCode);//获取应用版本号
                    appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));//获取应用图标
                    dkAppEntities.add(appInfo);
                }
            }
            emitter.onNext(dkAppEntities);
            emitter.onComplete();
        },BackpressureStrategy.BUFFER)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<DkAppEntity>>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }
            @Override
            public void onNext(List<DkAppEntity> list) {
                List<ApItemViewModel> apItemViewModelList = new ArrayList<>();
                for(DkAppEntity entity:list){
                    apItemViewModelList.add(new ApItemViewModel(HomeViewModel.this,entity));
                }
                observableList.clear();
                observableList.addAll(apItemViewModelList);
                uc.finishRefreshing.set(!uc.finishRefreshing.get());
            }
            @Override
            public void onComplete() {}
            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadMore(){
        if(observableList.size()==0){
            uc.finishLoadmore.set(!uc.finishLoadmore.get());
            return;
        }
        Observable.just("")
                .delay(3, TimeUnit.SECONDS) //延迟3秒
                .compose(RxUtils.bindToLifecycle(getLifecycleProvider()))//界面关闭自动取消
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .subscribe((Consumer<Object>) o -> {
                    //刷新完成收回
                    for(int i=0;i<3;i++){
                        observableList.add(observableList.get(observableList.size()-1));
                    }
                    uc.finishLoadmore.set(!uc.finishLoadmore.get());
                });
    }

    public class UIChangeObservable {
        //下拉刷新完成
        ObservableBoolean finishRefreshing = new ObservableBoolean(false);
        //上拉加载完成
        ObservableBoolean finishLoadmore = new ObservableBoolean(false);
    }

}
