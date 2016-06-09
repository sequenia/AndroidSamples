package com.sequenia.navigation;

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
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
    public void setup(NavigationActivity activity, NavigationActivity.NavigationActivitySettings settings) {
        super.setup(activity, settings);

        getDrawerView().setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                close();
                return NavigationDrawerStandardLayoutMenu.this.onNavigationItemSelectedListener(item);
            }
        });
    }

    public abstract boolean onNavigationItemSelectedListener(MenuItem item);
}
