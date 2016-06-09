package com.sequenia.navigation;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by chybakut2004 on 08.06.16.
 */

public interface NavigationMenu {

    boolean hasBackButtonLogic();

    void setup(NavigationActivity activity, NavigationActivity.NavigationActivitySettings settings);

    void updateBackButton(NavigationActivity navigationActivity, NavigationFragment fragment);

    boolean isOpen();

    void close();

    boolean onOptionsItemSelected(final MenuItem item);
}
