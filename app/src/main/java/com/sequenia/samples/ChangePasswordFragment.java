package com.sequenia.samples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.sequenia.navigation.NavigationFragment;

/**
 * Created by chybakut2004 on 07.06.16.
 */

public class ChangePasswordFragment extends NavigationFragment {

    @Override
    public void setup(NavigationFragmentSettings fragmentSettings) {
        fragmentSettings
                .setLayoutId(R.layout.fragment_change_password)
                .setHasBackButton(true)
                .setTitle(getString(R.string.password_changing));
    }

    @Override
    public void onLayoutCreated(LayoutInflater inflater, View view, Bundle savedInstanceState) {
        View openMenuSectionButton = view.findViewById(R.id.open_contacts_button);
        openMenuSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().openScreenWithClear(MainActivity.SCREEN_CONTACTS);
            }
        });

        View openDeepScreenButton = view.findViewById(R.id.open_change_password_screen);
        openDeepScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().openScreen(MainActivity.SCREEN_CHANGE_PASSWORD);
            }
        });

        View applyButton = view.findViewById(R.id.apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().clear();
            }
        });

        View cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().closeLastScreen();
            }
        });
    }
}
