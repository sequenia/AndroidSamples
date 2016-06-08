package com.sequenia.navigation;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by chybakut2004 on 08.06.16.
 */

public class NavigationDrawerMenu implements NavigationMenu {

    private int drawerLayoutId;
    private int openStrId;
    private int closeStrId;

    private ActionBarDrawerToggle toggle;

    public NavigationDrawerMenu(int drawerLayoutId, int openStrId, int closeStrId) {
        this.drawerLayoutId = drawerLayoutId;
        this.openStrId = openStrId;
        this.closeStrId = closeStrId;
    }

    @Override
    public boolean hasBackButtonLogic() {
        return true;
    }

    @Override
    public void setup(AppCompatActivity activity, NavigationActivity.NavigationActivitySettings settings) {
        Toolbar toolbar = (Toolbar) activity.findViewById(settings.getToolbarId());

        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        DrawerLayout drawerLayout = (DrawerLayout) activity.findViewById(drawerLayoutId);
        if(drawerLayout != null) {
            toggle = new ActionBarDrawerToggle(
                    activity,
                    drawerLayout,
                    toolbar,
                    openStrId,
                    closeStrId);

            toggle.setDrawerIndicatorEnabled(false);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
    }

    @Override
    public void updateBackButton(NavigationActivity navigationActivity, NavigationFragment fragment) {
        if(toggle != null) {
            toggle.setDrawerIndicatorEnabled(!navigationActivity.hasBackButton(fragment));
        }
    }
}
