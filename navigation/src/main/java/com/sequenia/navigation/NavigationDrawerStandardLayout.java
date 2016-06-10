package com.sequenia.navigation;

import android.support.design.widget.NavigationView;
import android.util.SparseIntArray;
import android.view.MenuItem;

/**
 * NavigationView со стандартной разметкой
 * Created by chybakut2004 on 09.06.16.
 */

public class NavigationDrawerStandardLayout extends NavigationDrawerMenuLayout {

    public NavigationDrawerStandardLayout(int drawerLayoutId, int drawerViewId,
                                          int openStrId, int closeStrId) {
        super(drawerLayoutId, drawerViewId, openStrId, closeStrId);
    }

    @Override
    public void bindMenuItems(final NavigationActivity activity, final SparseIntArray screensByMenuItems) {
        getNavigationView().setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int screenId = screensByMenuItems.get(item.getItemId(), -1);

                if(screenId != -1) {
                    activity.openScreenWithClear(screenId);
                    close();
                }

                return false;
            }
        });
    }

    @Override
    public void selectMenuItem(int menuItemId) {
        getNavigationView().setCheckedItem(menuItemId);
    }

    @Override
    public void deselectMenuItem(int menuItemId) {
        MenuItem item = getNavigationView().getMenu().findItem(menuItemId);
        if(item != null) {
            item.setChecked(false);
        }
    }
}
