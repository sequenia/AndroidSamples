package com.sequenia.navigation;

import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import java.util.HashMap;

/**
 * Created by chybakut2004 on 09.06.16.
 */

public abstract class NavigationDrawerCustomLayoutMenu extends NavigationDrawerMenu {

    private NavigationMenuSettings settings;
    private SparseArray<View> menuItemViews;
    private Integer currentScreenId;

    public NavigationDrawerCustomLayoutMenu(int drawerLayoutId, int drawerViewId, int openStrId, int closeStrId) {
        super(drawerLayoutId, drawerViewId, openStrId, closeStrId);
    }

    @Override
    public void setup(final NavigationActivity activity, NavigationActivity.NavigationActivitySettings settings) {
        super.setup(activity, settings);

        bindNavigationItems(getSettings());

        menuItemViews = new SparseArray<>();
        SparseIntArray items = getSettings().getNavigationItems();

        for(int i = 0; i < items.size(); i++) {
            int viewId = items.keyAt(i);
            int screenId = items.get(viewId);

            final View itemView = activity.findViewById(viewId);
            if(itemView != null) {
                bindMenuItem(screenId, itemView, activity);
            }
        }
    }

    private void bindMenuItem(final int screenId, final View itemView, final NavigationActivity activity) {
        menuItemViews.put(screenId, itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentScreenId != null) {
                    deselectItem(menuItemViews.get(currentScreenId));
                }
                selectItem(itemView);
                currentScreenId = screenId;
                close();
                activity.openScreenWithClear(screenId);
            }
        });
    }

    public abstract void bindNavigationItems(NavigationMenuSettings menuSettings);
    public abstract void selectItem(View view);
    public abstract void deselectItem(View view);

    public NavigationMenuSettings getSettings() {
        if(settings == null) {
            settings = new NavigationMenuSettings();
        }

        return settings;
    }

    public class NavigationMenuSettings {

        private SparseIntArray navigationItems;

        public NavigationMenuSettings addNavigationItem(int viewId, int screenId) {
            if(navigationItems == null) {
                navigationItems = new SparseIntArray();
            }

            navigationItems.put(viewId, screenId);

            return this;
        }

        private SparseIntArray getNavigationItems() {
            if(navigationItems == null) {
                navigationItems = new SparseIntArray();
            }

            return navigationItems;
        }
    }
}
