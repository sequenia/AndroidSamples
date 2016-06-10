package com.sequenia.navigation;

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseIntArray;
import android.view.MenuItem;

/**
 * Created by chybakut2004 on 09.06.16.
 */

public abstract class NavigationDrawerStandardLayoutMenu extends NavigationDrawerMenu {

    public NavigationDrawerStandardLayoutMenu(int drawerLayoutId, int drawerViewId,
                                              int openStrId, int closeStrId) {
        super(drawerLayoutId, drawerViewId, openStrId, closeStrId);
    }

    @Override
    public void bindMenuItems(final NavigationActivity activity, final SparseIntArray screensByMenuItems) {
        getDrawerView().setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
        getDrawerView().setCheckedItem(menuItemId);
    }

    @Override
    public void deselectMenuItem(int menuItemId) {

    }
}
