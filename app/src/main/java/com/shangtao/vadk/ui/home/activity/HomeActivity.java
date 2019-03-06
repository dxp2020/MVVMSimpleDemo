package com.shangtao.vadk.ui.home.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shangtao.base.BaseActivity;
import com.shangtao.vadk.BR;
import com.shangtao.vadk.R;
import com.shangtao.vadk.databinding.ActivityHomeBinding;
import com.shangtao.vadk.ui.home.fragment.AddFragment;
import com.shangtao.vadk.ui.home.fragment.EventsFragment;
import com.shangtao.vadk.ui.home.fragment.HomeFragment;
import com.shangtao.vadk.ui.home.fragment.NewsFragment;
import com.shangtao.vadk.ui.home.fragment.PersonalCenterFragment;

import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;

public class HomeActivity extends BaseActivity<ActivityHomeBinding, HomeViewModel> {

    private PersonalCenterFragment personalCenterFragment;
    private AddFragment addFragment;
    private EventsFragment eventsFragment;
    private HomeFragment homeFragment;
    private NewsFragment newsFragment;

    @Override
    public void initData() {
        if (savedInstanceState != null) {
            addFragment = (AddFragment) getSupportFragmentManager()
                    .findFragmentByTag(AddFragment.class.getSimpleName());
            eventsFragment = (EventsFragment) getSupportFragmentManager()
                    .findFragmentByTag(EventsFragment.class.getSimpleName());
            homeFragment = (HomeFragment) getSupportFragmentManager()
                    .findFragmentByTag(HomeFragment.class.getSimpleName());
            newsFragment = (NewsFragment) getSupportFragmentManager()
                    .findFragmentByTag(NewsFragment.class.getSimpleName());
            personalCenterFragment = (PersonalCenterFragment) getSupportFragmentManager()
                    .findFragmentByTag(PersonalCenterFragment.class.getSimpleName());
        }
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
//        radioHome.setSelected(false);
//        radioNews.setSelected(false);
//        radioEvents.setSelected(false);
//        radioMy.setSelected(false);
//        view.setSelected(true);
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
