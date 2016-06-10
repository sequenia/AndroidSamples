package com.sequenia.navigation;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

/**
 * Реализация составляющей меню, представляющая собой NavigationView.
 *
 * Класс абстрактный, так как NavigationView позволяет использовать как стандартную,
 * так и кастомную разметку. Для этого созданы два класса наследника.
 *
 * Created by chybakut2004 on 10.06.16.
 */

public abstract class NavigationDrawerMenuLayout implements NavigationMenuLayout {

    /**
     * ID элемента DrawerLayout
     */
    private int drawerLayoutId;

    /**
     * ID элемента NavigationView
     */
    private int navigationViewId;

    /**
     * ID ресурса для строки открытия дровера. Нужно по умолчанию.
     */
    private int openStrId;

    /**
     * ID ресурса для строки закрытия дровера. Нужно по умолчанию.
     */
    private int closeStrId;

    /**
     * Кнопка меню дровера
     */
    private ActionBarDrawerToggle toggle;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    /**
     * В конструктор передаются все неободимые данные для превоначальной настройки NavigationView
     * @param drawerLayoutId ID элемента DrawerLayout
     * @param navigationViewId ID элмента NavigationView
     * @param openStrId ID строки в ресурсах для отрытия
     * @param closeStrId ID строки в ресурсах для закрытия
     */
    public NavigationDrawerMenuLayout(int drawerLayoutId, int navigationViewId, int openStrId, int closeStrId) {
        this.drawerLayoutId = drawerLayoutId;
        this.openStrId = openStrId;
        this.closeStrId = closeStrId;
        this.navigationViewId = navigationViewId;
    }

    @Override
    public void setupLayout(NavigationActivity activity, NavigationActivity.NavigationActivitySettings settings) {
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        navigationView = (NavigationView) activity.findViewById(navigationViewId);
        drawerLayout = (DrawerLayout) activity.findViewById(drawerLayoutId);
        if(drawerLayout != null) {
            toggle = new ActionBarDrawerToggle(
                    activity,
                    drawerLayout,
                    openStrId,
                    closeStrId);

            toggle.setDrawerIndicatorEnabled(false);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }
    }

    @Override
    public boolean hasBackButtonLogic() {
        return true;
    }

    @Override
    public boolean isOpen() {
        return drawerLayout != null && navigationView != null && drawerLayout.isDrawerOpen(navigationView);
    }

    @Override
    public void close() {
        if(drawerLayout != null && navigationView != null) {
            drawerLayout.closeDrawer(navigationView);
        }
    }

    @Override
    public void updateBackButton(NavigationActivity navigationActivity, NavigationFragment fragment) {
        if(toggle != null) {
            toggle.setDrawerIndicatorEnabled(!navigationActivity.hasBackButton(fragment));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item);
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }
}
