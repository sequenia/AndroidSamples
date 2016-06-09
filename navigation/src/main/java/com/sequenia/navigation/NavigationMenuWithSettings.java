package com.sequenia.navigation;

import android.util.SparseIntArray;

/**
 * Created by chybakut2004 on 09.06.16.
 */

public abstract class NavigationMenuWithSettings implements NavigationMenu {

    private NavigationMenuSettings settings;

    public NavigationMenuSettings getSettings() {
        initSettings();
        return settings;
    }

    @Override
    public void setup(NavigationActivity activity, NavigationActivity.NavigationActivitySettings settings) {
        setupLayout(activity, settings);
        bindMenuItems(activity, getSettings().getScreensByMenuItems());
    }

    private void initSettings() {
        if(settings == null) {
            settings = new NavigationMenuSettings();
            setupSettings(settings);
        }
    }

    public abstract void setupLayout(NavigationActivity activity, NavigationActivity.NavigationActivitySettings settings);
    public abstract void bindMenuItems(NavigationActivity activity, SparseIntArray screensByMenuItems);
    public abstract void setupSettings(NavigationMenuSettings navigationMenuSettings);

    public class NavigationMenuSettings {

        private SparseIntArray screensByMenuItems;

        public NavigationMenuSettings bindMenuItem(int menuItemId, int screenId) {
            getScreensByMenuItems().put(menuItemId, screenId);
            return this;
        }

        public SparseIntArray getScreensByMenuItems() {
            if(screensByMenuItems == null) {
                screensByMenuItems = new SparseIntArray();
            }

            return screensByMenuItems;
        }
    }

}
