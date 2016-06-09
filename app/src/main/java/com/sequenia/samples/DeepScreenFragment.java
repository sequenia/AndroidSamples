package com.sequenia.samples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.sequenia.navigation.NavigationFragment;

/**
 * Created by chybakut2004 on 07.06.16.
 */

public class DeepScreenFragment extends NavigationFragment {

    private View openMenuSectionButton;
    private View openDeepScreenButton;
    private View clearButton;

    @Override
    public void setup(NavigationFragmentSettings fragmentSettings) {
        fragmentSettings
                .setLayoutId(R.layout.fragment_deep_screen)
                .setHasBackButton(true);
    }

    @Override
    public void onLayoutCreated(LayoutInflater inflater, View view, Bundle savedInstanceState) {
        openMenuSectionButton = view.findViewById(R.id.open_section_button);
        openMenuSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().openScreenWithClear(MainActivity.SCREEN_FIRST_MENU_SECTION);
            }
        });

        openDeepScreenButton = view.findViewById(R.id.open_deep_screen_button);
        openDeepScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().openScreen(MainActivity.SCREEN_DEEP_SECTION);
            }
        });

        clearButton = view.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().clear();
            }
        });
    }
}
