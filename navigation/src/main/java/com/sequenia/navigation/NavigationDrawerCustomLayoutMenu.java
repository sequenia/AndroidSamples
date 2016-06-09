package com.sequenia.navigation;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;


/**
 * Created by chybakut2004 on 09.06.16.
 */

public abstract class NavigationDrawerCustomLayoutMenu extends NavigationDrawerMenu {

    private SparseArray<View> menuItems;

    public NavigationDrawerCustomLayoutMenu(int drawerLayoutId, int drawerViewId, int openStrId, int closeStrId) {
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
}
