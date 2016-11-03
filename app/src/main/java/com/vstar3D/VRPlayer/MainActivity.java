package com.vstar3D.VRPlayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vstar3D.VRPlayer.fragment.CatalogFragment;
import com.vstar3D.VRPlayer.fragment.VideoFragment;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    private TextView mTextView,mTextViewRight;
    private ViewPager mViewPager, mViewPagerRight;
    private PagerTabStrip mPagerTabStrip, mPagerTabStripRight;
    private PagerTitleStrip mPagerTitleStrip, mPagerTitleStripRight;
    private VideoFragment mVideoFragment, mVideoFragmentRight;
    private CatalogFragment mCatalogFragment,mCatalogFragmentRight;
    private ArrayList<Fragment> fragmentList, fragmentListRight;
    private ArrayList<String> titleList = new ArrayList<String>();
    private MyViewPagerAdapter myViewPagerAdapter;
    private MyViewPagerAdapterRight myViewPagerAdapterRight;
    private int position2;
    private RelativeLayout leftView, rightView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        initView();
        initDate();

    }

    private void initDate() {
        mVideoFragment = new VideoFragment();
        mVideoFragmentRight = new VideoFragment();
        mCatalogFragment = new CatalogFragment();
        mCatalogFragmentRight = new CatalogFragment();

        fragmentList = new ArrayList<Fragment>();
        fragmentListRight = new ArrayList<Fragment>();
        fragmentList.add(mVideoFragment);
        fragmentList.add(mCatalogFragment);

        fragmentListRight.add(mVideoFragmentRight);
        fragmentListRight.add(mCatalogFragmentRight);

        titleList.add("视频");
        titleList.add("目录");

        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myViewPagerAdapter);
        myViewPagerAdapterRight = new MyViewPagerAdapterRight(getSupportFragmentManager());
        mViewPagerRight.setAdapter(myViewPagerAdapterRight);

    }

    private void initView() {
        mTextView = (TextView) findViewById(R.id.tv_title);
        mTextViewRight = (TextView) findViewById(R.id.tv_title);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                position2 = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPagerRight = (ViewPager) findViewById(R.id.viewpager_right);
        mViewPagerRight.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position2 = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPagerTabStrip = (PagerTabStrip) findViewById(R.id.pagertab);
        mPagerTabStripRight = (PagerTabStrip) findViewById(R.id.pagertab_right);
        mPagerTabStrip.setTabIndicatorColor(getResources().getColor(android.R.color.holo_green_dark));
        mPagerTabStripRight.setTabIndicatorColor(getResources().getColor(android.R.color.holo_green_dark));
        mPagerTabStrip.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        mPagerTabStripRight.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));

    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        @Override
        public void setPrimaryItem(View container, int position, Object object) {
            super.setPrimaryItem(container, position, object);

        }

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {

            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

    }  public class MyViewPagerAdapterRight extends FragmentPagerAdapter {

        @Override
        public void setPrimaryItem(View container, int position, Object object) {
            super.setPrimaryItem(container, position, object);

        }

        public MyViewPagerAdapterRight(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {

            return fragmentListRight.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentListRight.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

    }

    long time2;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (position2 == 0) {
                mVideoFragment.OnkeyStaus();
                return true;
            }
            if (position2 == 1) {
                mCatalogFragment.OnkeyStaus();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
