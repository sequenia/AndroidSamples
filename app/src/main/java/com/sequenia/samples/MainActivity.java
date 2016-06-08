package com.sequenia.samples;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.sequenia.navigation.NavigationActivity;
import com.sequenia.navigation.NavigationDrawerMenu;
import com.sequenia.navigation.NavigationFragment;

public class MainActivity extends NavigationActivity {

    public static final int SCREEN_DASHBOARD = 1;
    public static final int SCREEN_MENU_SECTION = 2;
    public static final int SCREEN_DEEP_SECTION = 3;

    @Override
    protected void setup(NavigationActivitySettings activitySettings) {
        activitySettings
                .setLayoutId(R.layout.activity_main)
                .setFragmentContainerId(R.id.frame_layout)
                .setToolbarId(R.id.toolbar)
                .setToolbarTitleId(R.id.toolbar_title)
                .setMenuId(R.menu.main_menu)
                .setDashboardScreenId(SCREEN_DASHBOARD)
                //.setMainScreenId(SCREEN_MENU_SECTION)
                .addNavigationMenu(new NavigationDrawerMenu(R.id.drawer_layout, R.string.open, R.string.close))
                .setFragmentFabric(new NavigationFragment.NavigationFragmentFabric() {
                    @Override
                    public NavigationFragment newInstance(int sectionId) {
                        NavigationFragment fragment = null;

                        switch (sectionId) {
                            case SCREEN_DASHBOARD:
                                fragment = new DashboardFragment();
                                break;

                            case SCREEN_MENU_SECTION:
                                fragment = new MenuSectionFragment();
                                break;

                            case SCREEN_DEEP_SECTION:
                                fragment = new DeepScreenFragment();
                                break;
                        }

                        return fragment;
                    }
                });
    }
}
