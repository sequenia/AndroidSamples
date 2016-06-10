package com.sequenia.navigation;

import android.util.SparseIntArray;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Навигационное меню приложения.
 * Включает в себя список пунктов меню и экранов, которые должны быть показаны при нажатии на них.
 * Так же, каждому экрану может быть задан пункт меню, котоырй будет подкрашен при переходе на него.
 *
 * Меню имеет общий список пунктов и один текущий выделенный элемент,
 * но может быть представлено несколькими составляющими, например:
 * - NavigationDrawer
 * - Нижняя навигация
 * - и т.д.
 *
 * Created by chybakut2004 on 09.06.16.
 */

public abstract class NavigationMenu {

    /**
     * Настройки навигационного меню
     */
    private NavigationMenuSettings settings;

    /**
     * Текущий выделенный элемент меню. Null, если не выделено ничего
     */
    private Integer currentSelectedItem;

    /**
     * Настройка меню
     * @param activity навигационное активити
     * @param settings настройки навигационного активити
     */
    public void setup(NavigationActivity activity, NavigationActivity.NavigationActivitySettings settings) {
        setupLayout(activity, settings);
        bindMenuItems(activity, getSettings().getScreensByMenuItems());
    }

    /**
     * Здесь нужно произвести конфигурацию меню
     * - Привязать пункты меню к экранам.
     * - Привязать подстветку пунктов меню в зависимости от показанного экрана.
     * - Задать разметку для меню.
     * - и т.д.
     */
    public abstract void setupSettings(NavigationMenuSettings navigationMenuSettings);

    /**
     * Настройка разметки меню.
     * Меню может состоять из нескольких частей,
     * например Drawer и нижняя навигация.
     * Каждый из таких элементов нужно создать в отдельности.
     */
    public void setupLayout(NavigationActivity activity, NavigationActivity.NavigationActivitySettings settings) {
        for(NavigationMenuLayout layout : getSettings().getLayouts()) {
            layout.setupLayout(activity, settings);
        }
    }

    /**
     * Настройка нажатий по меню для открытия экранов.
     * Так как меню может состоять из нескольких частей, нужно настроить каждую в отдельности.
     * @param activity навигационное активити
     * @param screensByMenuItems список того, какие экраны нужно открывать
     *                           по клику на какой элемент меню
     */
    public void bindMenuItems(NavigationActivity activity, SparseIntArray screensByMenuItems) {
        for(NavigationMenuLayout layout : getSettings().getLayouts()) {
            layout.bindMenuItems(activity, getSettings().getScreensByMenuItems());
        }
    }

    /**
     * @return true, если в списке навигационных меню есть такое,
     * которое захватывает себе логику кнопки назад. Например, NavigationDrawer
     */
    public boolean hasBackButtonLogic() {
        return getSettings().hasBackItemLayout();
    }

    /**
     * @return true, если хотя бы одна составляюшая меню открыта
     */
    public boolean isOpen() {
        for(NavigationMenuLayout layout : getSettings().getLayouts()) {
            if(layout.isOpen()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Закрывает все составляюшие меню
     */
    public void close() {
        for(NavigationMenuLayout layout : getSettings().getLayouts()) {
            if(layout.isOpen()) {
                layout.close();
            }
        }
    }

    private void initSettings() {
        if(settings == null) {
            settings = new NavigationMenuSettings();
            setupSettings(settings);
        }
    }

    /**
     * Выделяет элемент меню, предварительно сняв выделение с предыдущего выделенного.
     * @param menuItemId id элемента меню, который нужно выделить.
     *                   Если null, то просто произойдет снятие текущего выделения.
     */
    public void select(Integer menuItemId) {
        for(NavigationMenuLayout layout : getSettings().getLayouts()) {
            if (currentSelectedItem != null) {
                layout.deselectMenuItem(currentSelectedItem);
            }

            currentSelectedItem = menuItemId;
            if(currentSelectedItem != null) {
                layout.selectMenuItem(currentSelectedItem);
            }
        }
    }

    /**
     * Обновление кнопки назад, если есть меню, ответственная за это
     */
    public void updateBackButton(NavigationActivity navigationActivity, NavigationFragment fragment) {
        if(getSettings().hasBackItemLayout()) {
            getSettings().getBackItemLayout().updateBackButton(navigationActivity, fragment);
        }
    }

    /**
     * Обработка нажатий по пунктам тулбара, если это требутся для навигационного меню
     * @return true, если было нажатие по элементу, необходимому для навигационного меню.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        return getSettings().hasBackItemLayout() && getSettings().getBackItemLayout().onOptionsItemSelected(item);
    }

    /**
     * @param screenId id экрана
     * @return id пункта меню, который нужно подкрасить для переданного экрана.
     * Если данных для экрана нет, нужно оставить текущий выделенный пункт меню.
     */
    public Integer getMenuItemForScreen(int screenId) {
        int menuItemId = getSettings().getMenuItemsByScreens().get(screenId, -1);
        return menuItemId == -1 ? currentSelectedItem : menuItemId;
    }

    public NavigationMenuSettings getSettings() {
        initSettings();
        return settings;
    }

    public class NavigationMenuSettings {

        /**
         * Экраны, которые нужно открывать по нажатию по элементам меню
         */
        private SparseIntArray screensByMenuItems;

        /**
         * Элементы меню, которые нужно подкрашивать при открытии экранов
         */
        private SparseIntArray menuItemsByScreens;

        /**
         * Список составляюших меню
         */
        private List<NavigationMenuLayout> layouts;

        /**
         * Составляющаяя меню, отвечающая за логику кнопки назад
         */
        private NavigationMenuLayout backItemLayout;

        public NavigationMenuSettings bindMenuItem(int menuItemId, int screenId) {
            getScreensByMenuItems().put(menuItemId, screenId);
            return this;
        }

        public NavigationMenuSettings bindScreen(int screenId, int menuItemId) {
            getMenuItemsByScreens().put(screenId, menuItemId);
            return this;
        }

        public NavigationMenuSettings addLayout(NavigationMenuLayout layout) {
            getLayouts().add(layout);
            if(layout.hasBackButtonLogic()) {
                this.backItemLayout = layout;
            }
            return this;
        }

        public SparseIntArray getScreensByMenuItems() {
            if(screensByMenuItems == null) {
                screensByMenuItems = new SparseIntArray();
            }

            return screensByMenuItems;
        }

        public SparseIntArray getMenuItemsByScreens() {
            if(menuItemsByScreens == null) {
                menuItemsByScreens = new SparseIntArray();
            }

            return menuItemsByScreens;
        }

        public List<NavigationMenuLayout> getLayouts() {
            if(layouts == null) {
                layouts = new ArrayList<>();
            }

            return layouts;
        }

        public NavigationMenuLayout getBackItemLayout() {
            return backItemLayout;
        }

        public boolean hasBackItemLayout() {
            return backItemLayout != null;
        }
    }
}
