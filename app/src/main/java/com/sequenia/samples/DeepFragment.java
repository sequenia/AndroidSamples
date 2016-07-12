package com.sequenia.samples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.sequenia.navigation.NavigationFragment;

import java.util.Locale;

/**
 * Created by chybakut2004 on 07.06.16.
 */

public class DeepFragment extends NavigationFragment {

    @Override
    public void setup(NavigationFragmentSettings fragmentSettings) {
        fragmentSettings
                .setLayoutId(R.layout.fragment_deep)
                .setHasBackButton(true)
                .setTitleRule(new TitleRule() {
                    @Override
                    public String getTitle() {
                        return String.format(
                                Locale.US,
                                getString(R.string.fragment_deep),
                                getNavigationActivity().getScreensCount()
                        );
                    }
                });
    }

    @Override
    public void onLayoutCreated(LayoutInflater inflater, View view, Bundle savedInstanceState) {
        View openMenuSectionButton = view.findViewById(R.id.open_menu_2_button);
        openMenuSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().openScreenWithClear(MainActivity.SCREEN_MENU_2);
            }
        });

        View openDeepScreenButton = view.findViewById(R.id.open_deep_screen);
        openDeepScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().openScreen(MainActivity.SCREEN_DEEP);
            }
        });

        View applyButton = view.findViewById(R.id.close_all_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().clear();
            }
        });

        View cancelButton = view.findViewById(R.id.close_screen_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().closeCurrentScreen();
            }
        });
    }
}
