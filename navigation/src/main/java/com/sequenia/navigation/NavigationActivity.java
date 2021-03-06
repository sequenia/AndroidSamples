package com.sequenia.navigation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Активити для навигации через фрагменты.
 * Является контейнером для всех экранов, представленных фрагментами.<br/>
 * <br/>
 * <b>Возможности</b><br/>
 * - Фрагменты можно открывать поверх других и
 * с очисткой до определенного фрагмента (в том числе с очисткой всех открытых экранов).<br/>
 * - Активити может иметь тулбар, который можно настроить в зависимости от текущего экрана.<br/>
 * - Активити может иметь общее меню различных видов (NavigationDrawer уже реализован).<br/>
 * - Активити может иметь Dashboard (Экран статистики), либо при старте приложения открывать
 * указанный экран.<br/>
 * - Каждый фрагмент имеет собственный заголовок.<br/>
 * <br/>
 * <b>Реализация</b><br/>
 * Открытие новых фрагментов производится вызовом метода replace с занесением в стек транзакций.
 * При нажатии на кнопку назад транзакция откатывается, и на экране появляется предыдущий фрагмент.<br/>
 * <br/>
 * Created by chybakut2004 on 07.06.16.
 */

public abstract class NavigationActivity extends AppCompatActivity {

    /**
     * Настройки навигационного активити
     */
    private NavigationActivitySettings settings;

    /**
     * View для заголовка
     */
    private TextView customTitle;

    /**
     * Слушатель изменения стека транзакций фрагментов.<br/>
     * При изменении состояния стека транзакций производится настройка экрана
     * в зависимости от текущего фрагмента.
     */
    private FragmentManager.OnBackStackChangedListener onBackStackChangedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validateSettings();

        setContentView(getSettings().getLayoutId());

        initViews(savedInstanceState);

        if(getSettings().hasToolbar()) {
            initToolbar();
        }

        addBackStackListener();

        if(getSettings().hasDashboard()) {
            initFirstScreen(getSettings().getDashboardScreenId());
        } else {
            initFirstScreen(getSettings().getMainScreenId());
        }

        if(getSettings().hasNavigationMenu()) {
            initNavigationMenu();
        }

        setupScreen();
    }

    /**
     * В этом методе нужно обработать создание разметки (сохранить ссылки на View, настроить их и т.д.)
     */
    public abstract void initViews(Bundle savedInstanceState);

    /**
     * Данный метод должен быть перегружен в реализации Активити с Навигацией.<br/>
     * В него передается объект настроек, через который можно сконфигурировать Активити
     * под нужды приложения.<br/>
     * Дальнейшая жизнь Активити зависит от заданных здесь настроек.<br/>
     * @param activitySettings объект настроек для конфигурации.
     */
    protected abstract void setup(NavigationActivitySettings activitySettings);

    /**
     * Настройка кастомного тулбара и кастомного заголовка (если он передан).
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(getSettings().getToolbarId());

        if(toolbar == null) {
            throw new IllegalStateException("Toolbar с заданным id не найден в разметке");
        }

        setSupportActionBar(toolbar);

        // Если указан TextView для заголовка, нужно сохранить его и при отображении заголовка
        // обращаться в дальнейшем к нему. При этом стандартный заголовок скрыть
        if(getSettings().hasCustomTitle()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            customTitle = (TextView) findViewById(getSettings().getToolbarTitleId());

            if(customTitle == null) {
                throw new IllegalStateException("TextView для заголовка с указанным id не найден в разметке");
            }
        }
    }

    /**
     * Инициализация первого экрана.<br/>
     * Вызывается при создании Активити и создает переданный стартовый экран, если он еще не создан.<br/>
     * @param firstScreenId id экрана для создания
     */
    private void initFirstScreen(int firstScreenId) {
        // Создание тега для транзакции в зависимости от типа экрана и индекса экрана.
        // Для стартового экрана индекс 0
        String tag = NavigationFragment.getTransactionTag(firstScreenId, 0);
        FragmentManager fragmentManager = getSupportFragmentManager();
        NavigationFragment firstScreen = (NavigationFragment) fragmentManager.findFragmentByTag(tag);

        // Если стартовый экран уже создан, снова его создавать не нужно
        if(firstScreen == null) {
            firstScreen = getSettings().getFabric().createScreen(firstScreenId, new Bundle(), getMenuItemForScreen(firstScreenId));
            fragmentManager
                    .beginTransaction()
                    .add(getSettings().getFragmentContainerId(), firstScreen, tag)
                    .addToBackStack(tag)
                    .commit();
        }
    }

    /**
     * Открывает экран с переданными в него аргументами.<br/>
     * Фрагмент открывается путем замещения текущего фрагмента с занесением транзакции в стек.<br/>
     * @param screenId ID экрана
     * @param args аргументы
     */
    public void openScreen(int screenId, Bundle args) {
        // Создание экземпляра фрагмента по переданному ID
        NavigationFragment fragment = getSettings().getFabric().createScreen(screenId, args, getMenuItemForScreen(screenId));
        // Создание тега для транзации из ID фрагмента и текущего количества экранов
        String tag = fragment.getTransactionTag(getScreensCount());

        // Замена текущего экрана новым экраном.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(getSettings().getFragmentContainerId(), fragment, tag)
                .addToBackStack(tag) // Добавление в стек для удобного возвращения к предыдущему экрану
                .commit();
    }

    /**
     * Очистка экрана вплоть до переданного индекса (самый первый экран добавлен с индексом 0).<br/>
     * <b>Экран с переданным индексом так же удаляется.</b>
     * @param index индекс
     */
    public void clear(int index) {
        removeBackStackListener();

        FragmentManager fragmentManager = getSupportFragmentManager();

        int depthAdjustment = getSettings().getDepthAdjustment();

        if(fragmentManager.getBackStackEntryCount() > depthAdjustment) {
            String name = fragmentManager.getBackStackEntryAt(depthAdjustment + index).getName();
            fragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        addBackStackListener();
    }

    /**
     * Закрывает текущий экран
     */
    public void closeCurrentScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > getSettings().getDepthAdjustment()) {
            fragmentManager.popBackStack();
        }
    }

    /**
     * @return Текущий экран
     */
    public NavigationFragment getCurrentScreen() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        int fragmentsCount = fragmentManager.getBackStackEntryCount();

        if(fragmentsCount == 0) {
            return null;
        }

        FragmentManager.BackStackEntry lastEntry = fragmentManager.getBackStackEntryAt(fragmentsCount - 1);
        return (NavigationFragment) fragmentManager.findFragmentByTag(lastEntry.getName());
    }

    /**
     * Открывает экран, предварительно очистив все экраны снизу.<br/>
     * Не удаляется только dashboard.<br/>
     * @param screenId id экрана
     */
    public void openScreenWithClear(int screenId) {
        openScreenWithClear(screenId, new Bundle());
    }

    /**
     * Открывает экран, предварительно очистив стек экранов до индекса index.<br/>
     * <b>Экран с индексом index так же удаляется.</b>
     */
    public void openScreen(int screenId, Bundle args, int index) {
        clear(index);
        openScreen(screenId, args);
    }

    public void openScreenWithClear(int screenId, Bundle args) {
        openScreen(screenId, args, 0);
    }

    public void openScreen(int screenId, int index) {
        openScreen(screenId, new Bundle(), index);
    }

    public void openScreen(int screenId) {
        openScreen(screenId, new Bundle());
    }

    /**
     * Очищает экран до главного экрана.
     */
    public void clear() {
        if(getSettings().hasDashboard()) {
            clear(0);
        } else {
            clear(1);
        }
    }

    /**
     * Во фрагментах хранится номер элемента меню (например Drawer),
     * который должен быть выделен при его открытии.<br/>
     * <br/>
     * Возвращает ID элемента навигационного меню для указанного экрана.<br/>
     * Алгоритм:<br/>
     *   - Если в меню задан ID элемента для этого экрана - возвращется он.<br/>
     *   - Если не задан - возвращается текущий выделенный элемент меню.<br/>
     *   - Для DASHBOARD всегда возвращается null.<br/>
     * @param screenId - id экрана
     * @return id элемента меню
     */
    private Integer getMenuItemForScreen(int screenId) {
        if(getSettings().hasDashboard() && screenId == getSettings().getDashboardScreenId()) {
            return null;
        } else {
            return getSettings().getNavigationMenu().getMenuItemForScreen(screenId);
        }
    }

    /**
     * Настройка слушателя стека транзакций для обновления интерфейса при открытии и закрытии экрана
     */
    private void addBackStackListener() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(onBackStackChangedListener != null) {
            fragmentManager.removeOnBackStackChangedListener(onBackStackChangedListener);
        }

        onBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                setupScreen();
            }
        };
        fragmentManager.addOnBackStackChangedListener(onBackStackChangedListener);
    }

    /**
     * Удаляет слушатель стека фрагментов
     */
    private void removeBackStackListener() {
        if(onBackStackChangedListener != null) {
            getSupportFragmentManager().removeOnBackStackChangedListener(onBackStackChangedListener);
        }
    }

    /**
     * Настройка навигационного меню Активити
     */
    private void initNavigationMenu() {
        getSettings().getNavigationMenu().setup(this, getSettings());
    }

    /**
     * Настройка экрана для текущего фрагмента
     * (Кнопки на тулбаре, кнопка назад, заголовок и т.д.)
     */
    public void setupScreen() {
        NavigationFragment currentFragment = getCurrentScreen();
        if(currentFragment != null) {
            updateBackButton(currentFragment);
            updateTitle(currentFragment);
            updateMenuSelection(currentFragment);
        }
    }

    /**
     * Обновление кнопки НАЗАД для переданного фрагмента
     */
    public void updateBackButton(NavigationFragment fragment) {
        if(fragment != null) {
            // Drawer захватывает логику кнопки назад. Ее видимость при этом должна настраиваться
            // его реализованными методами, а не стандартными методами Activity.
            if(getSettings().hasBackButtonLogicMenu()) {
                getSettings().getNavigationMenu().updateBackButton(this, fragment);
            } else {
                updateActivityBackButton(fragment);
            }
        }
    }

    /**
     * Обновление стандартной кнопки назад в Активити
     */
    private void updateActivityBackButton(NavigationFragment fragment) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(hasBackButton(fragment));
            actionBar.setHomeButtonEnabled(true);
        }
    }

    /**
     * Обновление кнопки назад, реализованной через меню.
     * (Дровер захватывает логику кнопки назад).
     */
    private void updateMenuSelection(NavigationFragment fragment) {
        if(getSettings().hasNavigationMenu()) {
            getSettings().getNavigationMenu().select(fragment.getMenuItemId());
        }
    }

    /**
     * @return true, если должна быть показана кнопка назад.
     */
    public boolean hasBackButton(NavigationFragment fragment) {
        // Кнопка не должна быть показана на нижнем экране
        return fragment.hasBackButton() && getScreensCount() > 1;
    }

    /**
     * Обновляет заголовок, доставая заголовок фрагмента
     */
    public void updateTitle(NavigationFragment fragment) {
        if(fragment != null) {
            setNavigationTitle(fragment.getTitle());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(getSettings().hasMenu()) {
            // Если указана меню, его нужно отобразить в тулбаре.
            getMenuInflater().inflate(getSettings().getMenuId(), menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public void onBackPressed() {

        // Если открыто меню, его нужно закрыть и больше ничего не делать
        if(getSettings().hasNavigationMenu()) {
            NavigationMenu navigationMenu = getSettings().getNavigationMenu();

            if(navigationMenu.isOpen()) {
                navigationMenu.close();
                return;
            }
        }

        // Нужно закрыть последний открытый экран. Если он остался один, закрыть Активити.
        if (getScreensCount() > 1) {
            closeCurrentScreen();
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Дроверу нужно обработать нажатия по элементам меню в тулбаре.
        // Так же это может понадобиться и другим навигационным меню.
        if(getSettings().getNavigationMenu().hasBackButtonLogic()) {
            // Если вернулось true, то больше ничего делать не нужно.
            // Это означает клик по элементу меню, который отвечает за навигацию.
            if(getSettings().getNavigationMenu().onOptionsItemSelected(item)) {
                return true;
            }
        }

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Проверка настроек на правильность их задания
     */
    private void validateSettings() {
        if(!getSettings().hasFabric()) {
            throw new IllegalStateException("Не задана фабрика фрагментов");
        }

        if(!getSettings().hasFragmentContainerId()) {
            throw new IllegalStateException("Не задан id контейнера для фрагментов");
        }

        if(!getSettings().hasLayoutId()) {
            throw new IllegalStateException("Не задана разметка Активности");
        }

        if(!getSettings().hasDashboard() && !getSettings().hasMainScreenId()) {
            throw new IllegalStateException("DashboardId или MainScreenId должны быть заданы");
        }
    }

    /**
     * @return количество добавленных экранов
     */
    public int getScreensCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }

    /**
     * Задает переданный заголовок
     */
    protected void setNavigationTitle(String text) {
        if(getSettings().hasCustomTitle()) {
            customTitle.setText(text);
        } else {
            setTitle(text);
        }
    }

    /**
     * @return Возвращает настройки. Если их еще нет - создает их
     */
    protected NavigationActivitySettings getSettings() {
        if(settings == null) {
            settings = new NavigationActivitySettings();
            setup(settings);
        }

        return settings;
    }

    public interface ScreenChangeListener {
        void onScreenChanged(NavigationFragment currentFragment);
    }

    /**
     * Настройки Активити.
     * Создаются при первом выхозове метода getSettings, а далее возвращается уже созданный экземпляр.
     */
    protected class NavigationActivitySettings {

        /**
         * ID разметки активити
         */
        private Integer layoutId;

        /**
         * ID контейнера, куда будут помещаться фрагменты, представляющие собой экраны приложения
         */
        private Integer fragmentContainerId;

        /**
         * ID тулбара, если его нужно задавать
         */
        private Integer toolbarId;

        /**
         * ID для Custom заголовка Экрана
         */
        private Integer toolbarTitleId;

        /**
         * ID главного экрана приложения.
         * Ему не соответствует никакой пункт меню, он показывается под всеми экранами
         */
        private Integer dashboardScreenId;

        /**
         * ID главного экрана, если не задан Dashboard
         */
        private Integer mainScreenId;

        /**
         * ID меню тулбара
         */
        private Integer menuId;

        /**
         * Фабрика экранов
         */
        private NavigationFragment.NavigationFragmentFabric fabric;

        /**
         * Навигационно меню Активити
         */
        private NavigationMenu navigationMenu;

        /**
         * Слушатель на изменение экрана. Вызывается при смене фрагмента.
         */
        private ScreenChangeListener screenChangeListener;

        public Integer getLayoutId() {
            return layoutId;
        }

        public NavigationActivitySettings setLayoutId(Integer layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Integer getToolbarId() {
            return toolbarId;
        }

        public NavigationActivitySettings setToolbarId(Integer toolbarId) {
            this.toolbarId = toolbarId;
            return this;
        }

        public Integer getToolbarTitleId() {
            return toolbarTitleId;
        }

        public NavigationActivitySettings setToolbarTitleId(Integer toolbarTitleId) {
            this.toolbarTitleId = toolbarTitleId;
            return this;
        }

        public Integer getDashboardScreenId() {
            return dashboardScreenId;
        }

        public NavigationActivitySettings setDashboardScreenId(Integer dashboardScreenId) {
            this.dashboardScreenId = dashboardScreenId;
            return this;
        }

        public NavigationFragment.NavigationFragmentFabric getFabric() {
            return fabric;
        }

        /**
         * Задает фабрику фрагментов, в которой приводится соответствие ID экранов и фрагментов,
         * которые следует показать
         * @param fabric Фабрика фрагментов
         */
        public NavigationActivitySettings setFragmentFabric(NavigationFragment.NavigationFragmentFabric fabric) {
            this.fabric = fabric;
            return this;
        }

        public Integer getFragmentContainerId() {
            return fragmentContainerId;
        }

        public NavigationActivitySettings setFragmentContainerId(Integer fragmentContainerId) {
            this.fragmentContainerId = fragmentContainerId;
            return this;
        }

        public Integer getMenuId() {
            return menuId;
        }

        public NavigationActivitySettings setMenuId(Integer menuId) {
            this.menuId = menuId;
            return this;
        }

        public Integer getMainScreenId() {
            return mainScreenId;
        }

        public NavigationActivitySettings setMainScreenId(Integer mainScreenId) {
            this.mainScreenId = mainScreenId;
            return this;
        }

        public NavigationActivitySettings setNavigationMenu(NavigationMenu navigationMenu) {
            this.navigationMenu = navigationMenu;
            return this;
        }

        public NavigationMenu getNavigationMenu() {
            return navigationMenu;
        }

        /**
         * @return Возвращает корректировку глубины для транзакций в зависимости от того,
         * задан Dashboard или нет
         */
        public int getDepthAdjustment() {
            if(hasDashboard()) {
                return 1;
            } else {
                return 0;
            }
        }

        public ScreenChangeListener getScreenChangeListener() {
            return screenChangeListener;
        }

        public NavigationActivitySettings setScreenChangeListener(ScreenChangeListener screenChangeListener) {
            this.screenChangeListener = screenChangeListener;
            return this;
        }


        public boolean hasCustomTitle() {
            return toolbarTitleId != null;
        }

        public boolean hasToolbar() {
            return toolbarId != null;
        }

        public boolean hasDashboard() {
            return dashboardScreenId != null;
        }

        public boolean hasFabric() {
            return fabric != null;
        }

        public boolean hasLayoutId() {
            return layoutId != null;
        }

        public boolean hasFragmentContainerId() {
            return fragmentContainerId != null;
        }

        public boolean hasMenu() {
            return menuId != null;
        }

        public boolean hasMainScreenId() {
            return mainScreenId != null;
        }

        public boolean hasNavigationMenu() {
            return navigationMenu != null;
        }

        public boolean hasBackButtonLogicMenu() {
            return navigationMenu.hasBackButtonLogic();
        }

        public boolean hasScreenChangeListener() {
            return screenChangeListener != null;
        }
    }
}
