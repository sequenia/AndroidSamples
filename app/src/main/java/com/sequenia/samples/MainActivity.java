package com.sequenia.samples;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sequenia.navigation.NavigationActivity;
import com.sequenia.navigation.NavigationFragment;

public class MainActivity extends NavigationActivity {

    public static final int SECTION_DASHBOARD = 1;
    public static final int SECTION_1 = 2;

    @Override
    protected void setup(NavigationActivitySettings activitySettings) {
        activitySettings
                .setLayoutId(R.layout.activity_main)
                .setFragmentContainerId(R.id.frame_layout)
                .setToolbarId(R.id.toolbar)
                .setToolbarTitleId(R.id.toolbar_title)
                .setDashboardSectionId(SECTION_DASHBOARD)
                .setFabric(new NavigationFragment.NavigationFragmentFabric() {
                    @Override
                    public NavigationFragment newInstance(int sectionId) {
                        NavigationFragment fragment = null;

                        switch (sectionId) {
                            case SECTION_DASHBOARD:
                                fragment = new DashboardFragment();
                                break;

                            case SECTION_1:
                                fragment = new SectionFragment();
                                break;
                        }

                        return fragment;
                    }
                });
    }
}
