package com.sequenia.navigation;

import android.util.SparseIntArray;
import android.view.MenuItem;

/**
 * Created by chybakut2004 on 10.06.16.
 */

public abstract class NavigationMenuLayout {

    public abstract void setupLayout(NavigationActivity activity, NavigationActivity.NavigationActivitySettings settings);
    public abstract void bindMenuItems(NavigationActivity activity, SparseIntArray screensByMenuItems);
    public abstract boolean hasBackButtonLogic();
    public abstract void selectMenuItem(int menuItemId);
    public abstract void deselectMenuItem(int menuItemId);
    public abstract boolean isOpen();
    public abstract void close();
    public abstract void updateBackButton(NavigationActivity navigationActivity, NavigationFragment fragment);
    public abstract boolean onOptionsItemSelected(MenuItem item);
}
