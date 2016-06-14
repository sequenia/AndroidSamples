package com.sequenia.samples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.sequenia.navigation.NavigationFragment;

/**
 * Фрагмент настроек
 * Created by chybakut2004 on 07.06.16.
 */

public class SettingsFragment extends NavigationFragment {

    @Override
    public void setup(NavigationFragmentSettings fragmentSettings) {
        fragmentSettings
                .setLayoutId(R.layout.fragment_settings)
                .setBackButtonVisibilityRule(new BackButtonVisibilityRule() {
                    @Override
                    public boolean hasBackButton() {
                        return true;
                    }
                })
                .setTitleRule(new TitleRule() {
                    @Override
                    public String getTitle() {
                        return getString(R.string.settings);
                    }
                })
                .setMenuId(R.menu.fragment_menu);
    }

    @Override
    public void onLayoutCreated(LayoutInflater inflater, View view, Bundle savedInstanceState) {
        View openDeepScreenButton = view.findViewById(R.id.change_password_button);

        openDeepScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().openScreen(MainActivity.SCREEN_CHANGE_PASSWORD);
            }
        });
    }
}
