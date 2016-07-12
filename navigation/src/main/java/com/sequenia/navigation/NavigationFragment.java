package com.sequenia.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Навигационный фрагмент. Каждый экран в приложении представлен наследником этого класса.
 *
 * Для создания экрана унаследуйте фрагмент от этого класса
 * и настройте через переданный в метод setup объект настроек.
 *
 * Created by chybakut2004 on 07.06.16.
 */

public abstract class NavigationFragment extends Fragment {

    /**
     * Все фрагменты навигации должны быть пронумерованы неповторяющимися константами.
     * Под данным ключем константа передается во фрагмент.
     * Этот ключ участвует в именовании транзакции и тега при добавлении фрагмента.
     */
    private static final String ARG_SCREEN_ID = "ArgScreenId";

    private static final String ARG_MENU_ITEM_ID = "ArgMenuItemId";

    /**
     * Настройки фрагмента.
     */
    private NavigationFragmentSettings settings;

    /**
     * Задает номер экрана фрагменту
     * @param screenId id экрана
     */
    private void setScreenId(int screenId) {
        Bundle args = getArguments();

        if(args == null) {
            args = new Bundle();
        }

        args.putInt(ARG_SCREEN_ID,  screenId);

        setArguments(args);
    }

    private void setMenuItemId(int menuItemId) {
        Bundle args = getArguments();

        if(args == null) {
            args = new Bundle();
        }

        args.putInt(ARG_MENU_ITEM_ID, menuItemId);

        setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        validateSettings();

        View view = inflater.inflate(getSettings().getLayoutId(), container, false);
        updateToolbarLayout();
        onLayoutCreated(inflater, view, savedInstanceState);

        if(getSettings().hasMenu()) {
            setHasOptionsMenu(true);
        }

        return view;
    }

    /**
     * Обновление разметки тулбара.
     * Если в текущем фрагменте задана разметка тулбара - помещает ее в тулбар.
     * Если в текущем фрагменте нет разметки тулбара - удаляет все из тулбара.
     *      Если при этом в тулбаре ничего нет, то ничего и не делает.
     */
    private void updateToolbarLayout() {
        NavigationActivity activity = getNavigationActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null) {
            if(getSettings().hasCustomToolbarLayout()) {
                actionBar.setDisplayShowCustomEnabled(true);
                View view = activity.getLayoutInflater().inflate(getSettings().getCustomToolbarLayoutId(), null);
                actionBar.setCustomView(view);
            } else {
                actionBar.setDisplayShowCustomEnabled(false);
                if(actionBar.getCustomView() != null) {
                    actionBar.setCustomView(null);
                }
            }
        }

        if(activity.getSettings().hasToolbarFooter()) {
            ViewGroup toolbarFooter;
            toolbarFooter = (ViewGroup) activity.findViewById(activity.getSettings().getToolbarFooterId());
            if(toolbarFooter != null) {
                if (getSettings().hasToolbarFooter()) {
                    View view = activity.getLayoutInflater().inflate(getSettings().getToolbarFooterLayoutId(), null);

                    toolbarFooter.removeAllViews();
                    toolbarFooter.addView(view);
                    toolbarFooter.setVisibility(View.VISIBLE);
                } else {
                    toolbarFooter.setVisibility(View.GONE);
                    toolbarFooter.removeAllViews();
                }
            }
        }
    }

    /**
     * Пользуйтесь этим методом для обращения к навигационному Activity фрагмента.
     * Это необходимо, например, для открытия нового экрана или закрытия текущего.
     *
     * @return экземпляр Activity, к которому прикреплен фрагмент.
     */
    public NavigationActivity getNavigationActivity() {
        Activity activity = getActivity();

        if(activity instanceof NavigationActivity) {
            return (NavigationActivity) activity;
        } else {
            throw new IllegalStateException("Activity must extend from NavigationActivity");
        }
    }

    /**
     * @return Настройки фрагмента. Если настройки еще не созданы, создает их,
     * вызывая настроечный метод фрагмента.
     */
    public NavigationFragmentSettings getSettings() {
        if(settings == null) {
            settings = new NavigationFragmentSettings();
            setup(settings);
        }

        return settings;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(getSettings().hasMenu()) {
            inflater.inflate(getSettings().getMenuId(), menu);
        } else {
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    /**
     * Первичная проверка валидности настроек.
     */
    private void validateSettings() {
        Bundle args = getArguments();

        if(args == null) {
            throw new IllegalStateException("Fragment arguments not set");
        }

        if(!args.containsKey(ARG_SCREEN_ID)) {
            throw new IllegalStateException("Fragment screen id not set");
        }

        // Создание разметки фрагмента. ID разметки обязательно должен быть задан
        if(!getSettings().hasLayoutId()) {
            throw new IllegalStateException("Fragment layoutId not set");
        }
    }

    String getTransactionTag(int index) {
        return getTransactionTag(getScreenId(), index);
    }

    /**
     *
     * @param screenId id фрагмента, заданный константами.
     * @param index индекс фрагмента в стеке. Если это третий по счету открываемый экран, нужно подавать 2 (нумерация с нуля)
     * @return тег для транзакции и добавления фрагмента
     */
    static String getTransactionTag(int screenId, int index) {
        return "fragment" + "_" + screenId + "index" + "_" + index;
    }

    /**
     * @return id фрагмента, заданный константой в фабрике
     */
    public int getScreenId() {
        return getArguments().getInt(ARG_SCREEN_ID);
    }

    public Integer getMenuItemId() {
        int itemId = getArguments().getInt(ARG_MENU_ITEM_ID, -1);
        return itemId == -1 ? null : itemId;
    }

    /**
     * @return true, если в меню этого фрагмента должна быть кнопка назад в тулбаре.
     */
    boolean hasBackButton() {
        return getSettings().isHasBackButton();
    }

    /**
     * @return заголовок фрагмента в тулбаре
     */
    public String getTitle() {
        return getSettings().getTitle();
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

    /**
     * Фабрика фрагментов.
     * Используется для создания экземпляров экранов по их ID, заданными константами.
     */
    public static abstract class NavigationFragmentFabric {

        /**
         * Данный метод нужно реализовать для создания экранов по их ID.
         * При создании экземпляров НЕ НУЖНО задавать фрагментам никаких аргументов,
         * нужно просто создать экземпляр фрагмента вызовом конструктора через new.
         * @param screenId id экрана
         * @return экземпляр экрана (фрагмента).
         */
        protected abstract NavigationFragment newInstance(int screenId);

        /**
         * Создает экран с переданным ID и аргументами
         * @param screenId ID экранаvoid
         * @param args аргументы, которые будут переданы во фрагмент
         * @return созданный экземпляр экрана с заданным ID и аргументами.
         */
        NavigationFragment createScreen(int screenId, Bundle args, Integer menuItemId) {
            NavigationFragment fragment = newInstance(screenId);
            fragment.setArguments(args);
            fragment.setScreenId(screenId);
            if(menuItemId != null) {
                fragment.setMenuItemId(menuItemId);
            }
            return fragment;
        }
    }

    /**
     * Правило для видимости кнопки НАЗАД.
     * Видимость кнопки не обязательно будет строго заданным значением.
     * Экраны могут реализовывать эту логику в зависимости от своего текущего состояния.
     * Чтобы обновить вид кнопки, вызовите соответсвующий метод Активити Навигации
     */
    protected interface BackButtonVisibilityRule {

        /**
         * @return true, если у фрагмента есть кнопка назад в тулбаре
         */
        boolean hasBackButton();
    }

    /**
     * Правило для заголовка фрагмента.
     * Заголовок на экране не должен быть строго заданным значением.
     * Экраны могут реализовывать логику заголовка в зависимости от текущего состояния.
     * Чтобы обновить заголовок, вызовите соответствующий метод Активити Навигации
     */
    protected interface TitleRule {

        /**
         * @return заголовок экрана
         */
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

        /**
         * Хранит правило задания заголовка
         */
        private TitleRule titleRule;

        /**
         * ID Меню тулбара
         */
        private Integer menuId;

        /**
         * ID специфической для флагмента разметки в тулбаре
         */
        private Integer customToolbarLayoutId;

        /**
         * ID специфической разметки снизу тулбара
         */
        private Integer toolbarFooterLayoutId;

        /**
         * Задает правило, которое возвращает переданный заголовок
         * @param title заголовок
         */
        public NavigationFragmentSettings setTitle(final String title) {
            this.titleRule = new TitleRule() {
                @Override
                public String getTitle() {
                    return title == null ? "" : title;
                }
            };
            return this;
        }

        /**
         * @return заголовок фрагмента
         */
        public String getTitle() {
            return titleRule == null ? "" : titleRule.getTitle();
        }

        /**
         * Задает правило возвращения заголовка фрагмента
         * @param titleRule правило
         */
        public NavigationFragmentSettings setTitleRule(TitleRule titleRule) {
            this.titleRule = titleRule;
            return this;
        }

        /**
         * @return Возвращает id разметки фрагмента
         */
        private Integer getLayoutId() {
            return layoutId;
        }

        /**
         * Задает ID разметки фрагмента
         * @param layoutId id разметки
         */
        public NavigationFragmentSettings setLayoutId(Integer layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        /**
         * Задает видимость кнопки назад у фрагмента
         * @param hasBackButton видимость кнопки назад
         */
        public NavigationFragmentSettings setHasBackButton(final boolean hasBackButton) {
            this.backButtonVisibilityRule = new BackButtonVisibilityRule() {
                @Override
                public boolean hasBackButton() {
                    return hasBackButton;
                }
            };
            return this;
        }

        /**
         * Задает правило для отображения кнопки назад
         * @param backButtonVisibilityRule правило отображения пнопки назад
         */
        public NavigationFragmentSettings setBackButtonVisibilityRule(BackButtonVisibilityRule backButtonVisibilityRule) {
            this.backButtonVisibilityRule = backButtonVisibilityRule;
            return this;
        }

        /**
         * @return true, если должна быть показана кнопка назад
         */
        public boolean isHasBackButton() {
            return backButtonVisibilityRule != null && backButtonVisibilityRule.hasBackButton();
        }

        /**
         * @return ID меню в тулбаре
         */
        public Integer getMenuId() {
            return menuId;
        }

        /**
         * Задает id меню в тулбаре
         * @param menuId
         */
        public NavigationFragmentSettings setMenuId(Integer menuId) {
            this.menuId = menuId;
            return this;
        }

        /**
         * @return ID специфической для фрагмента разметки тулбара
         */
        public Integer getCustomToolbarLayoutId() {
            return customToolbarLayoutId;
        }

        /**
         * @param customToolbarLayoutId
         */
        public NavigationFragmentSettings setCustomToolbarLayoutId(Integer customToolbarLayoutId) {
            this.customToolbarLayoutId = customToolbarLayoutId;
            return this;
        }

        public Integer getToolbarFooterLayoutId() {
            return toolbarFooterLayoutId;
        }

        public NavigationFragmentSettings setToolbarFooterLayoutId(Integer toolbarFooterLayoutId) {
            this.toolbarFooterLayoutId = toolbarFooterLayoutId;
            return this;
        }

        /**
         * @return true, если задано меню в тулбаре
         */
        public boolean hasMenu() {
            return menuId != null;
        }

        /**
         * @return true, если задана разметка фрагмента
         */
        public boolean hasLayoutId() {
            return layoutId != null;
        }

        public boolean hasCustomToolbarLayout() {
            return customToolbarLayoutId != null;
        }

        public boolean hasToolbarFooter() {
            return toolbarFooterLayoutId != null;
        }

    }
}
