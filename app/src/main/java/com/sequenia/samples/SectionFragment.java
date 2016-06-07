package com.sequenia.samples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.sequenia.navigation.NavigationFragment;

/**
 * Created by chybakut2004 on 07.06.16.
 */

public class SectionFragment extends NavigationFragment {

    @Override
    public void setup(NavigationFragmentSettings settings) {
        settings.setLayoutId(R.layout.fragment_section);
    }

    @Override
    public void onLayoutCreated(LayoutInflater inflater, View view, Bundle savedInstanceState) {

    }
}
