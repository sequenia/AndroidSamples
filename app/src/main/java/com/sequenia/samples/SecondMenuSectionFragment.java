package com.sequenia.samples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.sequenia.navigation.NavigationFragment;

/**
 * Created by chybakut2004 on 09.06.16.
 */

public class SecondMenuSectionFragment extends NavigationFragment {
    @Override
    public void setup(NavigationFragmentSettings fragmentSettings) {
        fragmentSettings
                .setLayoutId(R.layout.fragment_second_menu_section)
                .setBackButtonVisibilityRule(new BackButtonVisibilityRule() {
                    @Override
                    public boolean hasBackButton() {
                        return true;
                    }
                })
                .setTitleRule(new TitleRule() {
                    @Override
                    public String getTitle() {
                        return "Секция меню 2";
                    }
                });
    }

    @Override
    public void onLayoutCreated(LayoutInflater inflater, View view, Bundle savedInstanceState) {

    }
}
