package com.sequenia.navigation;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;


/**
 * Created by chybakut2004 on 09.06.16.
 */

public abstract class NavigationDrawerCustomLayoutMenu extends NavigationDrawerMenu {

    public NavigationDrawerCustomLayoutMenu(int drawerLayoutId, int drawerViewId, int openStrId, int closeStrId) {
        super(drawerLayoutId, drawerViewId, openStrId, closeStrId);
    }
}
