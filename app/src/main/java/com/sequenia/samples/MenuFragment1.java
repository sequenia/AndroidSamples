package com.sequenia.samples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.sequenia.navigation.NavigationFragment;

/**
 * Фрагмент настроек
 * Created by chybakut2004 on 07.06.16.
 */

public class MenuFragment1 extends NavigationFragment {

    @Override
    public void setup(NavigationFragmentSettings fragmentSettings) {
        fragmentSettings
                .setLayoutId(R.layout.fragment_menu_1)
                .setBackButtonVisibilityRule(new BackButtonVisibilityRule() {
                    @Override
                    public boolean hasBackButton() {
                        return true;
                    }
                })
                .setTitleRule(new TitleRule() {
                    @Override
                    public String getTitle() {
                        return getString(R.string.menu_fragment_1);
                    }
                })
                .setMenuId(R.menu.fragment_menu);
    }

    @Override
    public void onLayoutCreated(LayoutInflater inflater, View view, Bundle savedInstanceState) {
        View openDeepScreenButton = view.findViewById(R.id.open_deep_screen_button);

        openDeepScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().openScreen(MainActivity.SCREEN_DEEP);
            }
        });
    }
}
