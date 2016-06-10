package com.sequenia.navigation;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;


/**
 * Created by chybakut2004 on 09.06.16.
 */

public abstract class NavigationDrawerCustomLayout extends NavigationDrawerMenuLayout {

    private SparseArray<View> menuItems;

    public NavigationDrawerCustomLayout(int drawerLayoutId, int drawerViewId, int openStrId, int closeStrId) {
        super(drawerLayoutId, drawerViewId, openStrId, closeStrId);
    }

    @Override
    public void bindMenuItems(final NavigationActivity activity, SparseIntArray screensByMenuItems) {
        menuItems = new SparseArray<>();

        for(int i = 0; i < screensByMenuItems.size(); i++) {
            int itemId = screensByMenuItems.keyAt(i);
            final int screenId = screensByMenuItems.get(itemId);

            View view = activity.findViewById(itemId);
            if(view != null) {
                menuItems.put(itemId, view);
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

    @Override
    public void selectMenuItem(int menuItemId) {
        View view = menuItems.get(menuItemId);
        if(view != null) {
            selectMenuItem(view);
        }
    }

    @Override
    public void deselectMenuItem(int menuItemId) {
        View view = menuItems.get(menuItemId);
        if(view != null) {
            deselectMenuItem(view);
        }
    }

    public abstract void selectMenuItem(View view);
    public abstract void deselectMenuItem(View view);
}
