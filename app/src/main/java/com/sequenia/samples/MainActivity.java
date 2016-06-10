package com.sequenia.samples;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.sequenia.navigation.NavigationActivity;
import com.sequenia.navigation.NavigationDrawerCustomLayout;
import com.sequenia.navigation.NavigationFragment;
import com.sequenia.navigation.NavigationMenu;

public class MainActivity extends NavigationActivity {

    public static final int SCREEN_DASHBOARD = 1;
    public static final int SCREEN_FIRST_MENU_SECTION = 2;
    public static final int SCREEN_SECOND_MENU_SECTION = 3;
    public static final int SCREEN_DEEP_SECTION = 4;

    @Override
    protected void setup(NavigationActivitySettings activitySettings) {
        activitySettings
                .setLayoutId(R.layout.activity_main)
                .setFragmentContainerId(R.id.frame_layout)
                .setToolbarId(R.id.toolbar)
                .setToolbarTitleId(R.id.toolbar_title)
                .setMenuId(R.menu.main_menu)
                .setDashboardScreenId(SCREEN_DASHBOARD)
                .setNavigationMenu(new NavigationMenu() {
                    @Override
                    public void setupSettings(NavigationMenuSettings navigationMenuSettings) {
                        navigationMenuSettings
                                .bindMenuItem(R.id.drawer_section_1, SCREEN_FIRST_MENU_SECTION)
                                .bindMenuItem(R.id.drawer_section_2, SCREEN_SECOND_MENU_SECTION)
                                .bindScreen(SCREEN_FIRST_MENU_SECTION, R.id.drawer_section_1)
                                .bindScreen(SCREEN_SECOND_MENU_SECTION, R.id.drawer_section_2)
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
                    };
                }).setFragmentFabric(new NavigationFragment.NavigationFragmentFabric() {
                    @Override
                    public NavigationFragment newInstance(int sectionId) {
                        NavigationFragment fragment = null;

                        switch (sectionId) {
                            case SCREEN_DASHBOARD:
                                fragment = new DashboardFragment();
                                break;

                            case SCREEN_FIRST_MENU_SECTION:
                                fragment = new FirstMenuSectionFragment();
                                break;

                            case SCREEN_SECOND_MENU_SECTION:
                                fragment = new SecondMenuSectionFragment();
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
