package com.sequenia.navigation;

import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.View;

import java.util.HashMap;

/**
 * Created by chybakut2004 on 09.06.16.
 */

public abstract class NavigationDrawerCustomLayoutMenu extends NavigationDrawerMenu {

    public NavigationMenuSettings settings;

    public NavigationDrawerCustomLayoutMenu(int drawerLayoutId, int drawerViewId, int openStrId, int closeStrId) {
        super(drawerLayoutId, drawerViewId, openStrId, closeStrId);
    }

    @Override
    public void setup(final NavigationActivity activity, NavigationActivity.NavigationActivitySettings settings) {
        super.setup(activity, settings);

        bindNavigationItems(getSettings());

        SparseIntArray items = getSettings().getNavigationItems();
        for(int i = 0; i < items.size(); i++) {
            int viewId = items.keyAt(i);
            final int screenId = items.get(viewId);

            View view = activity.findViewById(viewId);
            if(view != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        close();
                        activity.openScreenWithClear(screenId);
                    }
                });
            }
        }
    }

    public abstract void bindNavigationItems(NavigationMenuSettings menuSettings);

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

        public SparseIntArray getNavigationItems() {
            if(navigationItems == null) {
                navigationItems = new SparseIntArray();
            }

            return navigationItems;
        }
    }
}
