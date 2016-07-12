# Fragment Navigation
Предоставляет удобный механизм для навигации через фрагменты
## Возможности
- Позволяет на описательном уровне задать список экранов, представленных фрагментами
- Легко открывать, закрывать и замещать фрагменты из любого места
- Задавать вид тулбара в зависимости от открытого фрагмента (Заголовок, кнопка назад, дополнительные элементы разметки)
- Реализовывать различные навигационные меню (NavigationDrawer уже реализован в двух видах - со стандартной разметкой и пользовательской разметкой)

## Использование
1) Унаследуйте Activity от **NavigationActivity** и реализуйте 2 метода:

> protected void setup(NavigationActivitySettings activitySettings);

> public void initViews(Bundle savedInstanceState);

В методе **initViews** можно настроить разметку Activity

2) Настройте поведение Активити через экземпляр **activitySettings** класса **NavigationActivitySettings** в методе **setup**.

```
// Каждому фрагменту нужно присвоить номер
public static final int SCREEN_MENU_1 = 1;
public static final int SCREEN_MENU_2 = 2;
public static final int SCREEN_DEEP = 3;
-------------
@Override
protected void setup(NavigationActivitySettings activitySettings) {
  activitySettings
          .setLayoutId(R.layout.activity_main)         // Разметка Activity (Обязательно)
          .setFragmentContainerId(R.id.frame_layout)   // Контейнер для фрагментов (Обязательно)
          .setToolbarId(R.id.toolbar)                  // ID тулбара в разметке
          .setMainScreenId(SCREEN_MENU_1)              // ID экрана стартового экрана
          // Фабрика фрагментов. Возвращает экземпляр фрагмента в зависимости от номера
          .setFragmentFabric(new NavigationFragment.NavigationFragmentFabric() {
              @Override
              public NavigationFragment newInstance(int sectionId) {
                  NavigationFragment fragment = null;
  
                  switch (sectionId) {
                      case SCREEN_MENU_1:
                          fragment = new MenuFragment1();
                          break;
                      case SCREEN_MENU_2:
                          fragment = new MenuFragment2();
                          break;
                      case SCREEN_DEEP:
                          fragment = new DeepFragment();
                          break;
                  }
  
                  return fragment;
              }
          });
}
```

3) Унаследуйте фрагменты от **NavigationFragment** и реализуйте 2 метода:

> public void setup(NavigationFragmentSettings fragmentSettings);

> public void onLayoutCreated(LayoutInflater inflater, View view, Bundle savedInstanceState);

В методе **onLayoutCreated** можно настроить разметку фрагмента

4) Настройте поведение Фрагментов через экземпляр **fragmentSettings** класса **NavigationFragmentSettings** в методе **setup**

```
@Override
public void setup(NavigationFragmentSettings fragmentSettings) {
    fragmentSettings
            .setLayoutId(R.layout.fragment_menu_1) // ID разметки фрагмента
            .setHasBackButton(false)               // Показывать ли кнопку назад в Toolbar
            .setTitle("Привет");                   // Заголовок, отображаемый в Toolbar
}
```

5) Используйте номера фрагментов для открытия экранов:

```
openScreen(SCREEN_DEEP);          // Открывает новый экран поверх текущего
openScreen(SCREEN_DEEP, bundle);  // Можно передавать аргументы
openScreenWithClear(SCREEN_DEEP); // Открывает новый экран, предварительно закрыв все предыдущие
```

## Дополнительно
### Навигационное меню
```
activitySettings
    .setNavigationMenu(new NavigationMenu() {
      @Override
      public void setupSettings(NavigationMenuSettings navigationMenuSettings) {
          navigationMenuSettings
                  // По элементу меню с id R.id.navigation_menu_screen_1 открыть экран SCREEN_MENU_1
                  .bindMenuItem(R.id.navigation_menu_screen_1, SCREEN_MENU_1)
                  .bindMenuItem(R.id.navigation_menu_screen_2, SCREEN_MENU_2)
                  // Можно добавить сколько угодно навигационных меню.
                  // Это может быть NavigationDrawer, BottomNavigation или любая другая реализация.
                  .addLayout(new NavigationMenuLayout() {
                    // Реализация меню
                  });
      }
  });
```

В библиотеке уже реализованы NavigationDrawer со стандартной разметкой и пользовательской. Описание [Тут](https://github.com/sequenia/FragmentNavigation/wiki/NavigationDrawer).

### Собственный TextView для заголовка тулбара
```
activitySettings.setToolbarTitleId(R.id.toolbar_title)
```

### Экран статистики (Вместо стартового экрана)
```
activitySettings.setDashboardScreenId(SCREEN_DASHBOARD);
```

### 
