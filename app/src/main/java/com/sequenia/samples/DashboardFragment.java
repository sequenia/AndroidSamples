package com.sequenia.samples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.sequenia.navigation.NavigationFragment;

/**
 * Created by chybakut2004 on 07.06.16.
 */

public class DashboardFragment extends NavigationFragment {

    private View openSectionButton;

    @Override
    public void setup(NavigationFragmentSettings fragmentSettings) {
        fragmentSettings
                .setLayoutId(R.layout.fragment_dashboard)
                .setHasBackButton(false)
                .setTitle("Главный экран");
    }

    @Override
    public void onLayoutCreated(LayoutInflater inflater, View view, Bundle savedInstanceState) {
        openSectionButton = view.findViewById(R.id.open_section_button);

        openSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().openScreenWithClear(MainActivity.SCREEN_FIRST_MENU_SECTION);
            }
        });
    }
}
