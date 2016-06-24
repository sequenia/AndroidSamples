package com.sequenia.samples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.sequenia.navigation.NavigationFragment;

/**
 * Экран статистики. Главный экран приложения
 * Created by chybakut2004 on 07.06.16.
 */

public class DashboardFragment extends NavigationFragment {

    @Override
    public void setup(NavigationFragmentSettings fragmentSettings) {
        fragmentSettings
                .setLayoutId(R.layout.fragment_dashboard)
                .setHasBackButton(false)
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
        View openSectionButton = view.findViewById(R.id.open_settings_button);

        openSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().openScreenWithClear(MainActivity.SCREEN_SETTINGS);
            }
        });
    }
}
