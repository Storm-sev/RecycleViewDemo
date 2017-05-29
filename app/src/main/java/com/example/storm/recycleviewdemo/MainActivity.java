package com.example.storm.recycleviewdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.storm.recycleviewdemo.base.BaseFragment;
import com.example.storm.recycleviewdemo.fragment.DetailPager;
import com.example.storm.recycleviewdemo.fragment.DownLoadingPager;
import com.example.storm.recycleviewdemo.fragment.HomePager;
import com.example.storm.recycleviewdemo.fragment.UserPager;
import com.example.storm.recycleviewdemo.utils.LogUtils;
import com.jakewharton.rxbinding.widget.RxRadioGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.rb_local_video)
    RadioButton rbLocalVideo;
    @BindView(R.id.rb_local_audio)
    RadioButton rbLocalAudio;
    @BindView(R.id.rb_net_video)
    RadioButton rbNetVideo;
    @BindView(R.id.rb_net_audio)
    RadioButton rbNetAudio;
    @BindView(R.id.rg_navigation)
    RadioGroup rgNavigation;
    private ArrayList<BaseFragment> mFragments;
    private int position;
    private BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setFragment();
        initData();
        setFragmentChangeListener();
        rgNavigation.check(R.id.rb_local_video);

    }

    private void setFragmentChangeListener() {

        RxRadioGroup.checkedChanges(rgNavigation).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                LogUtils.d("获取的选择内容" + integer);
                switch (integer) {
                    case R.id.rb_local_video:
                        LogUtils.d("获取的数据" + R.id.rb_local_video);
                        position = 0;
                        break;
                    case R.id.rb_local_audio:
                        position = 1;
                        break;
                    case R.id.rb_net_video:
                        position = 2;
                        break;
                    case R.id.rb_net_audio:
                        position = 3;
                        break;
                }

                BaseFragment targetFragment = mFragments.get(position);
                addOrChangeFragment(targetFragment);

            }
        });
    }

    /**
     * show chageFragment
     *
     * @param targetFragment
     */
    private void addOrChangeFragment(BaseFragment targetFragment) {
        if (currentFragment != targetFragment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (!targetFragment.isAdded()) {
                if (currentFragment != null) {
                    ft.hide(currentFragment);
                }
                ft.add(R.id.fl_main, targetFragment);
            } else {
                if (currentFragment != null) {
                    ft.hide(currentFragment);
                }
                ft.replace(R.id.fl_main, targetFragment);
            }

            ft.commit();
            currentFragment = targetFragment;
        }
    }


    private void initData() {

    }


    /**
     * 获取fragment
     */
    private void setFragment() {
        mFragments = new ArrayList<>();
        mFragments.add(new HomePager());
        mFragments.add(new DetailPager());
        mFragments.add(new DownLoadingPager());
        mFragments.add(new UserPager());


    }
}
