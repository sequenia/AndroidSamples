package com.sequenia.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
    private static final String ARG_SECTION_ID = "ArgSectionId";

    private NavigationFragmentSettings fragmentSettings;

    /**
     * Задает номер секции фрагменту
     * @param sectionId id секции
     */
    public void setSectionNumber(int sectionId) {
        Bundle args = getArguments();

        if(args == null) {
            args = new Bundle();
        }

        args.putInt(ARG_SECTION_ID, sectionId);

        setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        if(args == null) {
            throw new IllegalStateException("Fragment arguments not set");
        }

        if(!args.containsKey(ARG_SECTION_ID)) {
            throw new IllegalStateException("Fragment section not set");
        }

        // Создание разметки фрагмента. ID разметки обязательно должен быть задан
        Integer layoutId = getFragmentSettings().getLayoutId();
        if(layoutId == null) {
            throw new IllegalStateException("Fragment layoutId not set");
        }

        View view = inflater.inflate(layoutId, container, false);
        onLayoutCreated(inflater, view, savedInstanceState);
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

    public String getTransactionTag() {
        return getTransactionTag(getSectionNumber());
    }

    public static String getTransactionTag(int sectionNumber) {
        return String.valueOf(sectionNumber);
    }

    public int getSectionNumber() {
        return getArguments().getInt(ARG_SECTION_ID);
    }

    public boolean hasBackButton() {
        return getFragmentSettings().isHasBackButton();
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

        protected abstract NavigationFragment newInstance(int sectionNumber);

        public NavigationFragment createSection(int sectionNumber) {
            NavigationFragment fragment = newInstance(sectionNumber);
            fragment.setSectionNumber(sectionNumber);
            return fragment;
        }
    }

    protected interface BackButtonVisibilityRule {
        boolean hasBackButton();
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

        private Integer getLayoutId() {
            return layoutId;
        }

        public NavigationFragmentSettings setLayoutId(Integer layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public boolean isHasBackButton() {
            return backButtonVisibilityRule.hasBackButton();
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
    }
}
