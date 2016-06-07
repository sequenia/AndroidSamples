package com.sequenia.samples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.sequenia.navigation.NavigationFragment;

/**
 * Created by chybakut2004 on 07.06.16.
 */

public class MenuSectionFragment extends NavigationFragment {

    private View openDeepScreenButton;

    @Override
    public void setup(NavigationFragmentSettings fragmentSettings) {
        fragmentSettings
                .setLayoutId(R.layout.fragment_menu_section)
                .setHasBackButton(true)
                .setMenuId(R.menu.fragment_menu);
    }

    @Override
    public void onLayoutCreated(LayoutInflater inflater, View view, Bundle savedInstanceState) {
        openDeepScreenButton = view.findViewById(R.id.open_deep_screen_button);

        openDeepScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().openScreen(MainActivity.SCREEN_DEEP_SECTION);
            }
        });
    }
}
