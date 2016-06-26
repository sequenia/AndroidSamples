package com.sequenia.samples;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.sequenia.navigation.NavigationFragment;

/**
 * Created by chybakut2004 on 09.06.16.
 */

public class MenuFragment2 extends NavigationFragment {
    @Override
    public void setup(NavigationFragmentSettings fragmentSettings) {
        fragmentSettings
                .setLayoutId(R.layout.fragment_menu_2)
                .setBackButtonVisibilityRule(new BackButtonVisibilityRule() {
                    @Override
                    public boolean hasBackButton() {
                        return true;
                    }
                })
                .setTitle("")
                .setCustomToolbarLayoutId(R.layout.fragment_dashboard_toolbar_layout)
                .setCustomToolbarLayoutListener(new CustomToolbarLayoutListener() {
                    @Override
                    public void onCustomLayoutInflated(View view) {
                        view.findViewById(R.id.toolbar_button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getContext(), R.string.toolbar_button, Toast.LENGTH_LONG).show();
                            }
                        });
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