package com.sequenia.navigation;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by chybakut2004 on 08.06.16.
 */

public interface NavigationMenu {

    boolean hasBackButtonLogic();

    void setup(AppCompatActivity activity, NavigationActivity.NavigationActivitySettings settings);

    void updateBackButton(NavigationActivity navigationActivity, NavigationFragment fragment);
}
