package com.sequenia.samples;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.sequenia.navigation.NavigationActivity;
import com.sequenia.navigation.NavigationDrawerCustomLayout;
import com.sequenia.navigation.NavigationFragment;
import com.sequenia.navigation.NavigationMenu;

public class MainActivity extends NavigationActivity {

    public static final int SCREEN_DASHBOARD = 1;
    public static final int SCREEN_MENU_1 = 2;
    public static final int SCREEN_MENU_2 = 3;
    public static final int SCREEN_DEEP = 4;

    private TabLayout tabLayout;

    @Override
    public void initViews(Bundle savedInstanceState) {
        tabLayout = (TabLayout) findViewById(R.id.toolbar_tabs);
    }

    @Override
    protected void setup(NavigationActivitySettings activitySettings) {
        activitySettings
                .setLayoutId(R.layout.activity_main)
                .setFragmentContainerId(R.id.frame_layout)
                .setToolbarId(R.id.toolbar)
                .setToolbarTitleId(R.id.toolbar_title)
                .setMenuId(R.menu.main_menu)
                .setDashboardScreenId(SCREEN_DASHBOARD)
                .setScreenChangeListener(new ScreenChangeListener() {
                    @Override
                    public void onScreenChanged(NavigationFragment currentFragment) {
                        if(tabLayout != null) {
                            switch (currentFragment.getScreenId()) {
                                case SCREEN_MENU_2:
                                    tabLayout.setVisibility(View.VISIBLE);
                                    break;

                                default:
                                    tabLayout.setVisibility(View.GONE);
                                    break;
                            }
                        }
                    }
                })
                .setNavigationMenu(new NavigationMenu() {
                    @Override
                    public void setupSettings(NavigationMenuSettings navigationMenuSettings) {
                        navigationMenuSettings
                                .bindMenuItem(R.id.navigation_menu_screen_1, SCREEN_MENU_1)
                                .bindMenuItem(R.id.navigation_menu_screen_2, SCREEN_MENU_2)
                                .bindScreen(SCREEN_DEEP, R.id.navigation_menu_screen_2)
                                .addLayout(new NavigationDrawerCustomLayout(R.id.drawer_layout, R.id.navigation, R.string.open, R.string.close) {
                                    @Override
                                    public void selectMenuItem(View view) {
                                        view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                                    }

                                    @Override
                                    public void deselectMenuItem(View view) {
                                        view.setBackgroundColor(Color.TRANSPARENT);
                                    }
                                });
                    }
                }).setFragmentFabric(new NavigationFragment.NavigationFragmentFabric() {
                    @Override
                    public NavigationFragment newInstance(int sectionId) {
                        NavigationFragment fragment = null;

                        switch (sectionId) {
                            case SCREEN_DASHBOARD:
                                fragment = new DashboardFragment();
                                break;

                            case SCREEN_MENU_1:
                                fragment = new MenuFragment1();
                                break;

                            case SCREEN_MENU_2:
                                fragment = new MenuFragment2();
                                break;

                            case SCREEN_DEEP:
                                fragment = new DeepFragment();
                                break;
                        }

                        return fragment;
                    }
                });
    }
}
