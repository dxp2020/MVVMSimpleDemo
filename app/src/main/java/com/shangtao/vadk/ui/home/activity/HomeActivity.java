package com.shangtao.vadk.ui.home.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.shangtao.base.BaseActivity;
import com.shangtao.vadk.BR;
import com.shangtao.vadk.R;
import com.shangtao.vadk.databinding.ActivityHomeBinding;
import com.shangtao.vadk.ui.home.center.PersonalCenterFragment;
import com.shangtao.vadk.ui.home.events.EventsFragment;
import com.shangtao.vadk.ui.home.home.HomeFragment;
import com.shangtao.vadk.ui.home.news.NewsFragment;

import androidx.databinding.Observable;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> {

    private PersonalCenterFragment personalCenterFragment;
    private EventsFragment eventsFragment;
    private HomeFragment homeFragment;
    private NewsFragment newsFragment;

    @Override
    public void initData() {
        if (savedInstanceState != null) {
            homeFragment = (HomeFragment) getSupportFragmentManager()
                    .findFragmentByTag(HomeFragment.class.getSimpleName());
            newsFragment = (NewsFragment) getSupportFragmentManager()
                    .findFragmentByTag(NewsFragment.class.getSimpleName());
            eventsFragment = (EventsFragment) getSupportFragmentManager()
                    .findFragmentByTag(EventsFragment.class.getSimpleName());
            personalCenterFragment = (PersonalCenterFragment) getSupportFragmentManager()
                    .findFragmentByTag(PersonalCenterFragment.class.getSimpleName());
        } else {
            switchTo(binding.radioHome);
        }
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.mSelectedObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                View view = viewModel.uc.mSelectedObservable.get();
                if(view==null){
                    return;
                }
                int viewId = view.getId();
                switch (viewId){
                    case R.id.radio_home:
                        switchTo(binding.radioHome);
                        break;
                    case R.id.radio_news:
                        switchTo(binding.radioNews);
                        break;
                    case R.id.radio_events:
                        switchTo(binding.radioEvents);
                        break;
                    case R.id.radio_my:
                        switchTo(binding.radioMy);
                        break;
                }
            }
        });

    }

    private void selectedFragment(LinearLayout view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (view.getId()) {
            case R.id.radio_home:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.home_container, homeFragment, HomeFragment.class.getSimpleName());
                } else
                    transaction.show(homeFragment);
                break;
            case R.id.radio_news:
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                    transaction.add(R.id.home_container, newsFragment, NewsFragment.class.getSimpleName());
                } else
                    transaction.show(newsFragment);
                break;
            case R.id.radio_events:
                if (eventsFragment == null) {
                    eventsFragment = new EventsFragment();
                    transaction.add(R.id.home_container, eventsFragment, EventsFragment.class.getSimpleName());
                } else
                    transaction.show(eventsFragment);
                break;
            case R.id.radio_my:
                if (personalCenterFragment == null) {
                    personalCenterFragment = new PersonalCenterFragment();
                    transaction.add(R.id.home_container, personalCenterFragment, PersonalCenterFragment.class.getSimpleName());
                } else
                    transaction.show(personalCenterFragment);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null)
            transaction.hide(homeFragment);
        if (newsFragment != null)
            transaction.hide(newsFragment);
        if (eventsFragment != null)
            transaction.hide(eventsFragment);
        if (personalCenterFragment != null)
            transaction.hide(personalCenterFragment);
    }

    private void tabSelected(LinearLayout view) {
        binding.radioHome.setSelected(false);
        binding.radioNews.setSelected(false);
        binding.radioEvents.setSelected(false);
        binding.radioMy.setSelected(false);
        view.setSelected(true);
    }

    /**
     * 跳转到指定fragment
     */
    private void switchTo(LinearLayout view) {
        if (view == null) {
            return;
        }
        selectedFragment(view);
        tabSelected(view);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_home;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

}
