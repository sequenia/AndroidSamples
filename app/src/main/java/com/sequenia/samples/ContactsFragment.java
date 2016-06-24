package com.sequenia.samples;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.sequenia.navigation.NavigationFragment;

/**
 * Created by chybakut2004 on 09.06.16.
 */

public class ContactsFragment extends NavigationFragment {
    @Override
    public void setup(NavigationFragmentSettings fragmentSettings) {
        fragmentSettings
                .setLayoutId(R.layout.fragment_contacts)
                .setBackButtonVisibilityRule(new BackButtonVisibilityRule() {
                    @Override
                    public boolean hasBackButton() {
                        return true;
                    }
                })
                .setTitleRule(new TitleRule() {
                    @Override
                    public String getTitle() {
                        return getString(R.string.contacts);
                    }
                });
    }

    @Override
    public void onLayoutCreated(LayoutInflater inflater, View view, Bundle savedInstanceState) {
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.toolbar_tabs);
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
    }
}
