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
    public static final int SCREEN_SETTINGS = 2;
    public static final int SCREEN_CONTACTS = 3;
    public static final int SCREEN_CHANGE_PASSWORD = 4;

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
                                .bindMenuItem(R.id.drawer_settings, SCREEN_SETTINGS)
                                .bindMenuItem(R.id.drawer_contacts, SCREEN_CONTACTS)
                                .bindScreen(SCREEN_CHANGE_PASSWORD, R.id.drawer_contacts)
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

                            case SCREEN_SETTINGS:
                                fragment = new SettingsFragment();
                                break;

                            case SCREEN_CONTACTS:
                                fragment = new ContactsFragment();
                                break;

                            case SCREEN_CHANGE_PASSWORD:
                                fragment = new ChangePasswordFragment();
                                break;
                        }

                        return fragment;
                    }
                });
    }
}
