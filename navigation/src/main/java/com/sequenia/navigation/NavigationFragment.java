package com.sequenia.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Навигационный фрагмент. Каждый экран в приложении представлен наследником этого класса.
 * Created by chybakut2004 on 07.06.16.
 */

public abstract class NavigationFragment extends Fragment {

    /**
     * Все фрагменты навигации должны быть пронумерованы неповторяющимися константами.
     * Под данным ключем константа передается во фрагмент.
     * Далее, по этому ключу в аргументах можно идентифицировать фрагмент.
     */
    private static final String ARG_SCREEN_ID = "ArgScreenId";

    private NavigationFragmentSettings fragmentSettings;

    /**
     * Задает номер экрана фрагменту
     * @param screenId id экрана
     */
    public void setScreenId(int screenId) {
        Bundle args = getArguments();

        if(args == null) {
            args = new Bundle();
        }

        args.putInt(ARG_SCREEN_ID,  screenId);

        setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        validateSettings();

        View view = inflater.inflate(getFragmentSettings().getLayoutId(), container, false);
        onLayoutCreated(inflater, view, savedInstanceState);

        if(getFragmentSettings().hasMenu()) {
            setHasOptionsMenu(true);
        }

        return view;
    }

    public NavigationActivity getNavigationActivity() {
        Activity activity = getActivity();

        if(activity instanceof NavigationActivity) {
            return (NavigationActivity) activity;
        } else {
            throw new IllegalStateException("Activity is not NavigationActivity");
        }
    }

    protected NavigationFragmentSettings getFragmentSettings() {
        if(fragmentSettings == null) {
            fragmentSettings = new NavigationFragmentSettings();
            setup(fragmentSettings);
        }

        return fragmentSettings;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(getFragmentSettings().hasMenu()) {
            inflater.inflate(getFragmentSettings().getMenuId(), menu);
        } else {
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    public String getTransactionTag(int index) {
        return getTransactionTag(getScreenId(), index);
    }

    public static String getTransactionTag(int screenId, int index) {
        return "fragment" + "_" + screenId + "index" + "_" + index;
    }

    public int getScreenId() {
        return getArguments().getInt(ARG_SCREEN_ID);
    }

    public boolean hasBackButton() {
        return getFragmentSettings().isHasBackButton();
    }

    public String getTitle() {
        return getFragmentSettings().getTitle();
    }

    private void validateSettings() {
        Bundle args = getArguments();

        if(args == null) {
            throw new IllegalStateException("Fragment arguments not set");
        }

        if(!args.containsKey(ARG_SCREEN_ID)) {
            throw new IllegalStateException("Fragment screen id not set");
        }

        // Создание разметки фрагмента. ID разметки обязательно должен быть задан
        if(!getFragmentSettings().hasLayoutId()) {
            throw new IllegalStateException("Fragment layoutId not set");
        }
    }

    /**
     * Здесь нужно настроить фрагмент, например,
     * задать ему id разметки, которая будет в нем отображаться.
     * @param fragmentSettings Настройки
     */
    public abstract void setup(NavigationFragmentSettings fragmentSettings);

    /**
     * Вызываетя после создания разметки фрагмента.
     * Здесь следует производить привязку слушателей, заполнение разметки изменяющимися данными и т.д.
     * @param inflater LayoutInflater, полученный в onCreateView методе фрагмента
     * @param view корневая View фрагмента
     * @param savedInstanceState состояние фрагмента
     */
    public abstract void onLayoutCreated(LayoutInflater inflater, View view, Bundle savedInstanceState);

    public static abstract class NavigationFragmentFabric {

        protected abstract NavigationFragment newInstance(int screenId);

        public NavigationFragment createScreen(int screenId) {
            return createScreen(screenId, new Bundle());
        }

        public NavigationFragment createScreen(int screenId, Bundle args) {
            NavigationFragment fragment = newInstance(screenId);
            fragment.setArguments(args);
            fragment.setScreenId(screenId);
            return fragment;
        }
    }

    protected interface BackButtonVisibilityRule {
        boolean hasBackButton();
    }

    protected interface TitleRule {
        String getTitle();
    }

    /**
     * Класс для хранения настроек фрагмента
     */
    protected class NavigationFragmentSettings {

        /**
         * ID разметки фрагмента
         */
        private Integer layoutId;

        /**
         * Хранит правило для отображения кнопки назад
         */
        private BackButtonVisibilityRule backButtonVisibilityRule;

        private TitleRule titleRule;

        private Integer menuId;

        public NavigationFragmentSettings setTitle(final String title) {
            this.titleRule = new TitleRule() {
                @Override
                public String getTitle() {
                    return title == null ? "" : title;
                }
            };
            return this;
        }

        public String getTitle() {
            return titleRule == null ? "" : titleRule.getTitle();
        }

        public NavigationFragmentSettings setTitleRule(TitleRule titleRule) {
            this.titleRule = titleRule;
            return this;
        }

        private Integer getLayoutId() {
            return layoutId;
        }

        public NavigationFragmentSettings setLayoutId(Integer layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public NavigationFragmentSettings setHasBackButton(final boolean hasBackButton) {
            this.backButtonVisibilityRule = new BackButtonVisibilityRule() {
                @Override
                public boolean hasBackButton() {
                    return hasBackButton;
                }
            };
            return this;
        }

        public NavigationFragmentSettings setBackButtonVisibilityRule(BackButtonVisibilityRule backButtonVisibilityRule) {
            this.backButtonVisibilityRule = backButtonVisibilityRule;
            return this;
        }

        public boolean isHasBackButton() {
            return backButtonVisibilityRule != null && backButtonVisibilityRule.hasBackButton();
        }

        public Integer getMenuId() {
            return menuId;
        }

        public void setMenuId(Integer menuId) {
            this.menuId = menuId;
        }

        public boolean hasMenu() {
            return menuId != null;
        }

        public boolean hasLayoutId() {
            return layoutId != null;
        }
    }
}
