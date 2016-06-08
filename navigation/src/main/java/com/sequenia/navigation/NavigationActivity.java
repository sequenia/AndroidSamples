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
            initDashboard();
        }

        setupScreen();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(getSettings().getToolbarId());
        setSupportActionBar(toolbar);

        if(getSettings().hasCustomTitle()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            customTitle = (TextView) findViewById(getSettings().getToolbarTitleId());
        }
    }

    private void initDashboard() {
        String tag = NavigationFragment.getTransactionTag(getSettings().getDashboardScreenId());
        FragmentManager fragmentManager = getSupportFragmentManager();
        NavigationFragment dashboardFragment = (NavigationFragment) fragmentManager.findFragmentByTag(tag);

        if(dashboardFragment == null) {
            dashboardFragment = getSettings().getFabric().createScreen(getSettings().getDashboardScreenId());
            fragmentManager
                    .beginTransaction()
                    .add(getSettings().getFragmentContainerId(), dashboardFragment, tag)
                    .addToBackStack(tag)
                    .commit();
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

    public void setupScreen() {
        NavigationFragment currentFragment = getCurrentScreen();
        if(currentFragment != null) {
            updateBackButton(currentFragment);
        }
    }

    public void updateBackButton(NavigationFragment fragment) {
        if(fragment != null) {
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(fragment.hasBackButton());
                actionBar.setHomeButtonEnabled(true);
            }
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

    public void openScreen(int screenId, int depth) {
        openScreen(screenId, new Bundle(), depth);
    }

    public void openScreen(int screenId) {
        openScreen(screenId, new Bundle());
    }

    public void openScreen(int screenId, Bundle args) {
        NavigationFragment fragment = getSettings().getFabric().createScreen(screenId, args);
        String tag = fragment.getTransactionTag();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(getSettings().getFragmentContainerId(), fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    public void openScreen(int screenId, Bundle args, int depth) {
        removeBackStackListener();

        FragmentManager fragmentManager = getSupportFragmentManager();

        int depthAdjustment = getSettings().getDepthAdjustment();

        if(fragmentManager.getBackStackEntryCount() > 1) {
            String name = fragmentManager.getBackStackEntryAt(depthAdjustment).getName();
            fragmentManager.popBackStack(name, depthAdjustment);
        }

        addBackStackListener();

        openScreen(screenId, args);
    }

    @Override
    public void onBackPressed() {
        if(getDepth() > 1) {
            super.onBackPressed();
        } else {
            finish();
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
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void validateSettings() {
        if(!getSettings().hasFabric()) {
            throw new IllegalStateException("Fragment fabric not set");
        }

        if(!getSettings().hasFragmentContainerId()) {
            throw new IllegalStateException("Fragment container id not set");
        }

        if(!getSettings().hasLayoutId()) {
            throw new IllegalStateException("Activity layoutId not set");
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
        private Integer menuId;
        private NavigationFragment.NavigationFragmentFabric fabric;

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
    }
}
