package com.sequenia.navigation;

import android.util.SparseIntArray;
import android.view.MenuItem;

/**
 * Операции, которые должна уметь совершать разметка навигационного меню.
 * Из этих частей состоит меню приложения.
 * Created by chybakut2004 on 10.06.16.
 */

public interface NavigationMenuLayout {

    /**
     * Настройка разметки меню.
     * Здесь следует создать разметку меню, сохранить все ссылки на его пункты и т.д.
     */
    void setupLayout(NavigationActivity activity, NavigationActivity.NavigationActivitySettings settings);

    /**
     * Здесь нужно привязать слушатели по нажатии на элементы меню для открытия экранов.
     * @param screensByMenuItems экраны, которые нужно открывать по элементам меню
     */
    void bindMenuItems(NavigationActivity activity, SparseIntArray screensByMenuItems);

    /**
     * @return true, если данная составляющая меню берет на себя работу по управлению кнопкой назад.
     */
    boolean hasBackButtonLogic();

    /**
     * Выделяет пункт меню.
     * Здесь нужно реализовать простое изменение разметки меню для подкрашивания текущего элемента.
     */
    void selectMenuItem(int menuItemId);

    /**
     * Снимает выделение с элемента меню.
     * Здесь нужно реализовать простое изменение разметки меню для снятия выделения с текущего элемента.
     */
    void deselectMenuItem(int menuItemId);

    /**
     * @return true, если меню открыто.
     * Если меню не подразумевает открытие, нужно всегда возвращать false
     */
    boolean isOpen();

    /**
     * Закрывает меню
     */
    void close();

    /**
     * Обновляет кнопку назад. Нужно реализовывать только в том случае, если меню управляет этой логикой.
     */
    void updateBackButton(NavigationActivity navigationActivity, NavigationFragment fragment);

    /**
     * Обработка нажатий по меню тулбара, если для навигационного меню это необходимо.
     * @return true, если нажатие на элемент меню было обработано навигационным меню.
     */
    boolean onOptionsItemSelected(MenuItem item);
}
