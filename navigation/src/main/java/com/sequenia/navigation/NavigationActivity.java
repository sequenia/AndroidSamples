package com.sequenia.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Активити для навигации через фрагменты.
 * Является контейнером для всех экранов, представленных фрагментами.
 * Created by chybakut2004 on 07.06.16.
 */

public abstract class NavigationActivity extends AppCompatActivity {

    private NavigationActivitySettings settings;
    private TextView customTitle;
    private FragmentManager.OnBackStackChangedListener onBackStackChangedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validateSettings();

        setContentView(getSettings().getLayoutId());

        if(getSettings().hasToolbar()) {
            initToolbar();
        }

        addBackStackListener();

        if(getSettings().hasDashboard()) {
            initFirstScreen(getSettings().getDashboardScreenId());
        } else {
            initFirstScreen(getSettings().getMainScreenId());
        }

        if(getSettings().hasNavigationMenu()) {
            initNavigationMenu();
        }

        setupScreen();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(getSettings().getToolbarId());

        if(toolbar == null) {
            throw new IllegalStateException("Toolbar с заданным id не найден в разметке");
        }

        setSupportActionBar(toolbar);

        if(getSettings().hasCustomTitle()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            customTitle = (TextView) findViewById(getSettings().getToolbarTitleId());

            if(customTitle == null) {
                throw new IllegalStateException("TextView для заголовка с указанным id не найден в разметке");
            }
        }
    }

    private void initFirstScreen(int firstScreenId) {
        String tag = NavigationFragment.getTransactionTag(firstScreenId, 0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        NavigationFragment firstScreen = (NavigationFragment) fragmentManager.findFragmentByTag(tag);

        if(firstScreen == null) {
            firstScreen = getSettings().getFabric().createScreen(firstScreenId, new Bundle(), getMenuItemForScreen(firstScreenId));
            fragmentManager
                    .beginTransaction()
                    .add(getSettings().getFragmentContainerId(), firstScreen, tag)
                    .addToBackStack(tag)
                    .commit();
        }
    }

    private Integer getMenuItemForScreen(int screenId) {
        if(getSettings().hasDashboard() && screenId == getSettings().getDashboardScreenId()) {
            return null;
        } else {
            return getSettings().getNavigationMenu().getMenuItemForScreen(screenId);
        }
    }

    private void addBackStackListener() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(onBackStackChangedListener != null) {
            fragmentManager.removeOnBackStackChangedListener(onBackStackChangedListener);
        }

        onBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                setupScreen();
            }
        };
        fragmentManager.addOnBackStackChangedListener(onBackStackChangedListener);
    }

    private void removeBackStackListener() {
        if(onBackStackChangedListener != null) {
            getSupportFragmentManager().removeOnBackStackChangedListener(onBackStackChangedListener);
        }
    }

    private void initNavigationMenu() {
        getSettings().getNavigationMenu().setup(this, getSettings());
    }

    public void setupScreen() {
        NavigationFragment currentFragment = getCurrentScreen();
        if(currentFragment != null) {
            updateBackButton(currentFragment);
            updateTitle(currentFragment);
            updateMenuSelection(currentFragment);
        }
    }

    public void updateBackButton(NavigationFragment fragment) {
        if(fragment != null) {
            if(getSettings().hasBackButtonLogicMenu()) {
                getSettings().getNavigationMenu().updateBackButton(this, fragment);
            } else {
                updateActivityBackButton(fragment);
            }
        }
    }

    private void updateActivityBackButton(NavigationFragment fragment) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(hasBackButton(fragment));
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void updateMenuSelection(NavigationFragment fragment) {
        if(getSettings().hasNavigationMenu()) {
            getSettings().getNavigationMenu().select(fragment.getMenuItemId());
        }
    }

    public boolean hasBackButton(NavigationFragment fragment) {
        return fragment.hasBackButton() && getDepth() > 1;
    }

    public void updateTitle(NavigationFragment fragment) {
        if(fragment != null) {
            setNavigationTitle(fragment.getTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(getSettings().hasMenu()) {
            getMenuInflater().inflate(getSettings().getMenuId(), menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    public void openScreenWithClear(int screenId) {
        openScreenWithClear(screenId, new Bundle());
    }

    public void openScreenWithClear(int screenId, Bundle args) {
        openScreen(screenId, args, 0);
    }

    public void openScreen(int screenId, int index) {
        openScreen(screenId, new Bundle(), index);
    }

    public void openScreen(int screenId) {
        openScreen(screenId, new Bundle());
    }

    public void openScreen(int screenId, Bundle args) {
        NavigationFragment fragment = getSettings().getFabric().createScreen(screenId, args, getMenuItemForScreen(screenId));
        String tag = fragment.getTransactionTag(getDepth());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(getSettings().getFragmentContainerId(), fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    public void openScreen(int screenId, Bundle args, int index) {
        clear(index);
        openScreen(screenId, args);
    }

    public void clear() {
        clear(0);
    }

    public void clear(int index) {
        removeBackStackListener();

        FragmentManager fragmentManager = getSupportFragmentManager();

        int depthAdjustment = getSettings().getDepthAdjustment();

        if(fragmentManager.getBackStackEntryCount() > depthAdjustment) {
            String name = fragmentManager.getBackStackEntryAt(depthAdjustment + index).getName();
            fragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        addBackStackListener();
    }

    @Override
    public void onBackPressed() {
        boolean menuOpened = false;

        if(getSettings().hasNavigationMenu()) {
            NavigationMenu navigationMenu = getSettings().getNavigationMenu();
            menuOpened = navigationMenu.isOpen();

            if(menuOpened) {
                navigationMenu.close();
            }
        }

        if(!menuOpened) {
            if (getDepth() > 1) {
                closeLastScreen();
            } else {
                finish();
            }
        }
    }

    public void closeLastScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > getSettings().getDepthAdjustment()) {
            fragmentManager.popBackStack();
        }
    }

    public NavigationFragment getCurrentScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        int fragmentsCount = fragmentManager.getBackStackEntryCount();

        if(fragmentsCount == 0) {
            return null;
        }

        FragmentManager.BackStackEntry lastEntry = fragmentManager.getBackStackEntryAt(fragmentsCount - 1);
        return (NavigationFragment) fragmentManager.findFragmentByTag(lastEntry.getName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(getSettings().getNavigationMenu().hasBackButtonLogic()) {
            if(getSettings().getNavigationMenu().onOptionsItemSelected(item)) {
                return true;
            }
        }

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void validateSettings() {
        if(!getSettings().hasFabric()) {
            throw new IllegalStateException("Не задана фабрика фрагментов");
        }

        if(!getSettings().hasFragmentContainerId()) {
            throw new IllegalStateException("Не задан id контейнера для фрагментов");
        }

        if(!getSettings().hasLayoutId()) {
            throw new IllegalStateException("Не задана разметка Активности");
        }

        if(!getSettings().hasDashboard() && !getSettings().hasMainScreenId()) {
            throw new IllegalStateException("DashboardId или MainScreenId должны быть заданы");
        }
    }

    protected int getDepth() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }

    protected void setNavigationTitle(String text) {
        if(getSettings().hasCustomTitle()) {
            customTitle.setText(text);
        } else {
            setTitle(text);
        }
    }

    protected NavigationActivitySettings getSettings() {
        if(settings == null) {
            settings = new NavigationActivitySettings();
            setup(settings);
        }

        return settings;
    }

    protected abstract void setup(NavigationActivitySettings activitySettings);

    protected class NavigationActivitySettings {

        private Integer layoutId;
        private Integer fragmentContainerId;
        private Integer toolbarId;
        private Integer toolbarTitleId;
        private Integer dashboardScreenId;
        private Integer mainScreenId;
        private Integer menuId;
        private NavigationFragment.NavigationFragmentFabric fabric;
        private NavigationMenu navigationMenu;

        public Integer getLayoutId() {
            return layoutId;
        }

        public NavigationActivitySettings setLayoutId(Integer layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Integer getToolbarId() {
            return toolbarId;
        }

        public NavigationActivitySettings setToolbarId(Integer toolbarId) {
            this.toolbarId = toolbarId;
            return this;
        }

        public Integer getToolbarTitleId() {
            return toolbarTitleId;
        }

        public NavigationActivitySettings setToolbarTitleId(Integer toolbarTitleId) {
            this.toolbarTitleId = toolbarTitleId;
            return this;
        }

        public Integer getDashboardScreenId() {
            return dashboardScreenId;
        }

        public NavigationActivitySettings setDashboardScreenId(Integer dashboardScreenId) {
            this.dashboardScreenId = dashboardScreenId;
            return this;
        }

        public NavigationFragment.NavigationFragmentFabric getFabric() {
            return fabric;
        }

        public NavigationActivitySettings setFragmentFabric(NavigationFragment.NavigationFragmentFabric fabric) {
            this.fabric = fabric;
            return this;
        }

        public Integer getFragmentContainerId() {
            return fragmentContainerId;
        }

        public NavigationActivitySettings setFragmentContainerId(Integer fragmentContainerId) {
            this.fragmentContainerId = fragmentContainerId;
            return this;
        }

        public Integer getMenuId() {
            return menuId;
        }

        public NavigationActivitySettings setMenuId(Integer menuId) {
            this.menuId = menuId;
            return this;
        }

        public Integer getMainScreenId() {
            return mainScreenId;
        }

        public NavigationActivitySettings setMainScreenId(Integer mainScreenId) {
            this.mainScreenId = mainScreenId;
            return this;
        }

        public NavigationActivitySettings setNavigationMenu(NavigationMenu navigationMenu) {
            this.navigationMenu = navigationMenu;
            return this;
        }

        public NavigationMenu getNavigationMenu() {
            return navigationMenu;
        }

        public int getDepthAdjustment() {
            if(hasDashboard()) {
                return 1;
            } else {
                return 0;
            }
        }

        public boolean hasCustomTitle() {
            return toolbarTitleId != null;
        }

        public boolean hasToolbar() {
            return toolbarId != null;
        }

        public boolean hasDashboard() {
            return dashboardScreenId != null;
        }

        public boolean hasFabric() {
            return fabric != null;
        }

        public boolean hasLayoutId() {
            return layoutId != null;
        }

        public boolean hasFragmentContainerId() {
            return fragmentContainerId != null;
        }

        public boolean hasMenu() {
            return menuId != null;
        }

        public boolean hasMainScreenId() {
            return mainScreenId != null;
        }

        public boolean hasNavigationMenu() {
            return navigationMenu != null;
        }

        public boolean hasBackButtonLogicMenu() {
            return navigationMenu.hasBackButtonLogic();
        }
    }
}
