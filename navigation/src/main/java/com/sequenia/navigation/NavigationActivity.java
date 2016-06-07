package com.sequenia.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Активити для навигации через фрагменты.
 * Является контейнером для всех экранов, представленных фрагментами.
 * Created by chybakut2004 on 07.06.16.
 */

public abstract class NavigationActivity extends AppCompatActivity {

    private NavigationActivitySettings settings;

    private TextView customTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!getSettings().hasFabric()) {
            throw new IllegalStateException("Fragment fabric not set");
        }

        if(!getSettings().hasFragmentContainerId()) {
            throw new IllegalStateException("Fragment container id not set");
        }

        if(!getSettings().hasLayoutId()) {
            throw new IllegalStateException("Activity layoutId not set");
        }

        setContentView(getSettings().getLayoutId());

        if(getSettings().hasToolbar()) {
            initToolbar();
        }

        if(getSettings().hasDashboard()) {
            initDashboard();
        }
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
        String tag = NavigationFragment.getTransactionTag(getSettings().getDashboardSectionId());
        FragmentManager fragmentManager = getSupportFragmentManager();
        NavigationFragment dashboardFragment = (NavigationFragment) fragmentManager.findFragmentByTag(tag);

        if(dashboardFragment == null) {
            dashboardFragment = getSettings().getFabric().createSection(getSettings().getDashboardSectionId());
            fragmentManager
                    .beginTransaction()
                    .add(getSettings().getFragmentContainerId(), dashboardFragment, tag)
                    .addToBackStack(tag)
                    .commit();
        }
    }

    public void openSection(int sectionNumber) {
        NavigationFragment fragment = getSettings().getFabric().createSection(sectionNumber);
        String tag = fragment.getTransactionTag();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(getSettings().getFragmentContainerId(), fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if(getDepth() > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

    public NavigationFragment getCurrentSection() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        int fragmentsCount = fragmentManager.getBackStackEntryCount();

        if(fragmentsCount == 0) {
            return null;
        }

        FragmentManager.BackStackEntry lastEntry = fragmentManager.getBackStackEntryAt(fragmentsCount - 1);
        return (NavigationFragment) fragmentManager.findFragmentByTag(lastEntry.getName());
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
        private Integer dashboardSectionId;
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

        public Integer getDashboardSectionId() {
            return dashboardSectionId;
        }

        public NavigationActivitySettings setDashboardSectionId(Integer dashboardSectionId) {
            this.dashboardSectionId = dashboardSectionId;
            return this;
        }

        public NavigationFragment.NavigationFragmentFabric getFabric() {
            return fabric;
        }

        public NavigationActivitySettings setFabric(NavigationFragment.NavigationFragmentFabric fabric) {
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

        public boolean hasCustomTitle() {
            return toolbarTitleId != null;
        }

        public boolean hasToolbar() {
            return toolbarId != null;
        }

        public boolean hasDashboard() {
            return dashboardSectionId != null;
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
    }
}
