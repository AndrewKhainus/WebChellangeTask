package com.task.webchallengetask.ui.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.task.webchallengetask.R;
import com.task.webchallengetask.ui.activities.presenters.MainActivityPresenter;
import com.task.webchallengetask.ui.base.BaseActivity;

public class MainActivity extends BaseActivity<MainActivityPresenter>
        implements MainActivityPresenter.MainView, NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private ImageView ivNavAvatar;
    private TextView tvNavTitle;
    private TextView tvNavSubTitle;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected MainActivityPresenter initPresenter() {
        return new MainActivityPresenter();
    }

    @Override
    protected int getFragmentContainer() {
        return R.id.fragment_container_CM;
    }

    @Override
    protected void findUI() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        ivNavAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.ivAvatar_ND);
        tvNavTitle = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tvTitle_ND);
        tvNavSubTitle = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tvSubTitle_ND);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.setToolbarNavigationClickListener(v1 -> onBackPressed());

        mNavigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            mDrawerToggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount() > 0);
            mDrawerToggle.syncState();
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_activities:
                getPresenter().onActivityListClicked();
                break;
            case R.id.nav_analytics:
                getPresenter().onAnalyticsClicked();
                break;
            case R.id.nav_settings:
                getPresenter().onSettingsClicked();
                break;
            case R.id.nav_logout:
                getPresenter().onLogoutClicked();
                break;
        }
        return true;
    }


    @Override
    public boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void closeDrawer(DrawerCallBack _callback) {
        if (_callback != null)
            mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(View drawerView) {

                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    _callback.onClosed();
                    mDrawerLayout.removeDrawerListener(this);
                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });
        new Handler().post(mDrawerLayout::closeDrawers);

    }

    @Override
    public void setHeaderAvatar(Bitmap _bitmap) {
        ivNavAvatar.setImageBitmap(_bitmap);
    }

    @Override
    public ImageView getImageView() {
        return ivNavAvatar;
    }

    @Override
    public void setHeaderSubTitle(String _subTitle) {
        tvNavSubTitle.setText(_subTitle);
    }

    @Override
    public void setHeaderTitle(String _title) {
        tvNavTitle.setText(_title);
    }

    public interface DrawerCallBack {
        void onClosed();
    }


}
