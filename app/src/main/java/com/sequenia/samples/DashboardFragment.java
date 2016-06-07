package com.sequenia.samples;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sequenia.navigation.NavigationFragment;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chybakut2004 on 07.06.16.
 */

public class DashboardFragment extends NavigationFragment {

    private View openSectionButton;

    @Override
    public void setup(NavigationFragmentSettings settings) {
        settings.setLayoutId(R.layout.fragment_dashboard);
    }

    @Override
    public void onLayoutCreated(LayoutInflater inflater, View view, Bundle savedInstanceState) {
        openSectionButton = view.findViewById(R.id.open_section_button);

        openSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNavigationActivity().openSection(MainActivity.SECTION_1);
            }
        });
    }
}
