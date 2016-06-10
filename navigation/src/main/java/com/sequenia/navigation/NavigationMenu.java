package com.sequenia.navigation;

import android.util.SparseIntArray;
import android.view.MenuItem;

/**
 * Created by chybakut2004 on 09.06.16.
 */

public abstract class NavigationMenu {

    private NavigationMenuSettings settings;
    private Integer currentSelectedItem;

    public NavigationMenuSettings getSettings() {
        initSettings();
        return settings;
    }

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

    public void selectByScreenId(int screenId) {
        int menuItemId = getSettings().getMenuItemsByScreens().get(screenId, -1);
        if(menuItemId != -1) {
            select(menuItemId);
        }
    }

    public void select(int menuItemId) {
        if(currentSelectedItem != null) {
            deselectMenuItem(currentSelectedItem);
        }

        currentSelectedItem = menuItemId;
        selectMenuItem(currentSelectedItem);
    }

    public abstract void setupLayout(NavigationActivity activity, NavigationActivity.NavigationActivitySettings settings);
    public abstract void bindMenuItems(NavigationActivity activity, SparseIntArray screensByMenuItems);
    public abstract void setupSettings(NavigationMenuSettings navigationMenuSettings);
    public abstract boolean hasBackButtonLogic();
    public abstract void selectMenuItem(int menuItemId);
    public abstract void deselectMenuItem(int menuItemId);
    public abstract void updateBackButton(NavigationActivity navigationActivity, NavigationFragment fragment);
    public abstract boolean isOpen();
    public abstract void close();
    public abstract boolean onOptionsItemSelected(MenuItem item);

    public class NavigationMenuSettings {

        private SparseIntArray screensByMenuItems;
        private SparseIntArray menuItemsByScreens;

        public NavigationMenuSettings bindMenuItem(int menuItemId, int screenId) {
            getScreensByMenuItems().put(menuItemId, screenId);
            return this;
        }

        public NavigationMenuSettings bindScreen(int screenId, int menuItemId) {
            getMenuItemsByScreens().put(screenId, menuItemId);
            return this;
        }

        public SparseIntArray getScreensByMenuItems() {
            if(screensByMenuItems == null) {
                screensByMenuItems = new SparseIntArray();
            }

            return screensByMenuItems;
        }

        public SparseIntArray getMenuItemsByScreens() {
            if(menuItemsByScreens == null) {
                menuItemsByScreens = new SparseIntArray();
            }

            return menuItemsByScreens;
        }
    }
}
