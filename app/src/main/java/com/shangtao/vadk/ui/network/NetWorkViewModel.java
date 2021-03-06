package com.shangtao.vadk.ui.network;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
import androidx.annotation.NonNull;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.shangtao.vadk.BR;
import com.shangtao.vadk.R;
import com.shangtao.vadk.entity.DemoEntity;
import com.shangtao.vadk.service.DemoApiService;
import com.shangtao.vadk.utils.RetrofitClient;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import com.shangtao.base.BaseViewModel;
import com.shangtao.binding.command.BindingAction;
import com.shangtao.binding.command.BindingCommand;
import com.shangtao.http.BaseResponse;
import com.shangtao.http.ResponseThrowable;
import com.shangtao.utils.RxUtils;
import com.shangtao.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by goldze on 2017/7/17.
 */

public class NetWorkViewModel extends BaseViewModel {
    private int itemIndex = 0;
    public MutableLiveData<NetWorkItemViewModel> deleteItemLiveData = new MutableLiveData();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //下拉刷新完成
        public ObservableBoolean finishRefreshing = new ObservableBoolean(false);
        //上拉加载完成
        public ObservableBoolean finishLoadmore = new ObservableBoolean(false);
    }

    public NetWorkViewModel(@NonNull Application application) {
        super(application);
    }

    //给RecyclerView添加ObservableList
    public ObservableList<NetWorkItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemBinding
    public ItemBinding<NetWorkItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_network);

    //给RecyclerView添加Adpter，请使用自定义的Adapter继承BindingRecyclerViewAdapter，重写onBindBinding方法，里面有你要的Item对应的binding对象
    public final BindingRecyclerViewAdapter<NetWorkItemViewModel> adapter = new BindingRecyclerViewAdapter<>();

    //下拉刷新、上拉加载
    public PullToRefreshBase.OnRefreshListener2 onRefreshLoadMoreCommand = new PullToRefreshBase.OnRefreshListener2(){

        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            ToastUtils.showShort("下拉刷新");
            requestNetWork();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            if (itemIndex > 50) {
                ToastUtils.showLong("兄dei，你太无聊啦~崩是不可能的~");
                uc.finishLoadmore.set(!uc.finishLoadmore.get());
                return;
            }
            //模拟网络上拉加载更多
            Observable.just("")
                    .delay(3, TimeUnit.SECONDS) //延迟3秒
                    .compose(RxUtils.bindToLifecycle(getLifecycleProvider()))//界面关闭自动取消
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            ToastUtils.showShort("上拉加载");
                        }
                    })
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            //刷新完成收回
                            uc.finishLoadmore.set(!uc.finishLoadmore.get());
                            //模拟一部分假数据
                            for (int i = 0; i < 10; i++) {
                                DemoEntity.ItemsEntity item = new DemoEntity.ItemsEntity();
                                item.setId(-1);
                                item.setName("模拟条目" + itemIndex++);
                                NetWorkItemViewModel itemViewModel = new NetWorkItemViewModel(NetWorkViewModel.this, item);
                                //双向绑定动态添加Item
                                observableList.add(itemViewModel);
                            }
                        }
                    });
        }
    };

    /**
     * 网络请求方法，在ViewModel中调用，Retrofit+RxJava充当Repository，即可视为Model层
     */
    public void requestNetWork() {
        RetrofitClient.getInstance().create(DemoApiService.class)
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
                                NetWorkItemViewModel itemViewModel = new NetWorkItemViewModel(NetWorkViewModel.this, entity);
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
                });
    }

    /**
     * 删除条目
     *
     * @param netWorkItemViewModel
     */
    public void deleteItem(NetWorkItemViewModel netWorkItemViewModel) {
        //点击确定，在 observableList 绑定中删除，界面立即刷新
        observableList.remove(netWorkItemViewModel);
    }

    /**
     * 获取条目下标
     *
     * @param netWorkItemViewModel
     * @return
     */
    public int getPosition(NetWorkItemViewModel netWorkItemViewModel) {
        return observableList.indexOf(netWorkItemViewModel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
