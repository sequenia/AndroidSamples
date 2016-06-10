package com.sequenia.navigation;

import android.util.SparseIntArray;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

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

    public void bindMenuItems(NavigationActivity activity, SparseIntArray screensByMenuItems) {
        for(NavigationMenuLayout layout : getSettings().getLayouts()) {
            layout.bindMenuItems(activity, getSettings().getScreensByMenuItems());
        }
    }

    public void setupLayout(NavigationActivity activity, NavigationActivity.NavigationActivitySettings settings) {
        for(NavigationMenuLayout layout : getSettings().getLayouts()) {
            layout.setupLayout(activity, settings);
        }
    }

    public boolean hasBackButtonLogic() {
        for(NavigationMenuLayout layout : getSettings().getLayouts()) {
            if(layout.hasBackButtonLogic()) {
                return true;
            }
        }

        return false;
    }

    public boolean isOpen() {
        for(NavigationMenuLayout layout : getSettings().getLayouts()) {
            if(layout.isOpen()) {
                return true;
            }
        }

        return false;
    }

    public void close() {
        for(NavigationMenuLayout layout : getSettings().getLayouts()) {
            if(layout.isOpen()) {
                layout.close();
            }
        }
    }

    private void initSettings() {
        if(settings == null) {
            settings = new NavigationMenuSettings();
            setupSettings(settings);
        }
    }

    public void select(Integer menuItemId) {
        for(NavigationMenuLayout layout : getSettings().getLayouts()) {
            if (currentSelectedItem != null) {
                layout.deselectMenuItem(currentSelectedItem);
            }

            currentSelectedItem = menuItemId;
            if(currentSelectedItem != null) {
                layout.selectMenuItem(currentSelectedItem);
            }
        }
    }

    public void updateBackButton(NavigationActivity navigationActivity, NavigationFragment fragment) {
        for(NavigationMenuLayout layout : getSettings().getLayouts()) {
            if(layout.hasBackButtonLogic()) {
                layout.updateBackButton(navigationActivity, fragment);
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        for(NavigationMenuLayout layout : getSettings().getLayouts()) {
            if(layout.hasBackButtonLogic() && layout.onOptionsItemSelected(item)) {
                return true;
            }
        }

        return false;
    }

    public Integer getMenuItemForScreen(int screenId) {
        int menuItemId = getSettings().getMenuItemsByScreens().get(screenId, -1);
        return menuItemId == -1 ? currentSelectedItem : menuItemId;
    }

    public abstract void setupSettings(NavigationMenuSettings navigationMenuSettings);

    public class NavigationMenuSettings {

        private SparseIntArray screensByMenuItems;
        private SparseIntArray menuItemsByScreens;
        private List<NavigationMenuLayout> layouts;

        public NavigationMenuSettings bindMenuItem(int menuItemId, int screenId) {
            getScreensByMenuItems().put(menuItemId, screenId);
            return this;
        }

        public NavigationMenuSettings bindScreen(int screenId, int menuItemId) {
            getMenuItemsByScreens().put(screenId, menuItemId);
            return this;
        }

        public NavigationMenuSettings addLayout(NavigationMenuLayout layout) {
            getLayouts().add(layout);
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

        public List<NavigationMenuLayout> getLayouts() {
            if(layouts == null) {
                layouts = new ArrayList<>();
            }

            return layouts;
        }
    }
}
