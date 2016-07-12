package com.sequenia.samples;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.sequenia.navigation.NavigationActivity;
import com.sequenia.navigation.NavigationDrawerCustomLayout;
import com.sequenia.navigation.NavigationFragment;
import com.sequenia.navigation.NavigationMenu;

public class MainActivity extends NavigationActivity {

    public static final int SCREEN_DASHBOARD = 1;
    public static final int SCREEN_MENU_1 = 2;
    public static final int SCREEN_MENU_2 = 3;
    public static final int SCREEN_DEEP = 4;
    public static final int SCREEN_MENU_WITHOUT_TOOLBAR = 5;

    private View appBarLayout;
    private LinearLayout toolbarFooter;

    @Override
    public void initViews(Bundle savedInstanceState) {
        appBarLayout = findViewById(R.id.app_bar);
        toolbarFooter = (LinearLayout) findViewById(R.id.toolbar_footer);
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
                        switch (currentFragment.getScreenId()) {
                            case SCREEN_MENU_WITHOUT_TOOLBAR:
                                appBarLayout.setVisibility(View.GONE);
                                break;

                            default:
                                appBarLayout.setVisibility(View.VISIBLE);
                                break;
                        }

                        switch (currentFragment.getScreenId()) {
                            case SCREEN_MENU_2:
                                toolbarFooter.removeAllViews();
                                toolbarFooter.addView(getLayoutInflater().inflate(R.layout.tabs, toolbarFooter, false));
                                break;

                            default:
                                toolbarFooter.removeAllViews();
                                break;
                        }
                    }
                })
                .setNavigationMenu(new NavigationMenu() {
                    @Override
                    public void setupSettings(NavigationMenuSettings navigationMenuSettings) {
                        navigationMenuSettings
                                .bindMenuItem(R.id.navigation_menu_screen_1, SCREEN_MENU_1)
                                .bindMenuItem(R.id.navigation_menu_screen_2, SCREEN_MENU_2)
                                .bindMenuItem(R.id.navigation_menu_screen_3, SCREEN_MENU_WITHOUT_TOOLBAR)
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

                            case SCREEN_MENU_WITHOUT_TOOLBAR:
                                fragment = new MenuFragmentWithoutToolbar();
                                break;
                        }

                        return fragment;
                    }
                });
    }
}
