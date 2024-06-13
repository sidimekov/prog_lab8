# Лабораторная работа #8 про проге

## туду
- visualizacia objectov:
  - ~~draw objects~~
  - click and drag to move the panel
  - roll to zoom the panel
- filtres to table
- change the lang knopki

## Задание

Интерфейс должен быть реализован с помощью библиотеки Swing
Графический интерфейс клиентской части должен поддерживать русский, эстонский, украинский и испанский (Гондурас) языки / локали. Должно обеспечиваться корректное отображение чисел, даты и времени в соответстии с локалью. Переключение языков должно происходить без перезапуска приложения. Локализованные ресурсы должны храниться в классе.
Доработать программу из лабораторной работы №7 следующим образом:

Заменить консольный клиент на клиент с графическим интерфейсом пользователя(GUI).
В функционал клиента должно входить:

- Окно с авторизацией/регистрацией.
- Отображение текущего пользователя.
- Таблица, отображающая все объекты из коллекции
- Каждое поле объекта - отдельная колонка таблицы.
- Строки таблицы можно фильтровать/сортировать по значениям любой из колонок. Сортировку и фильтрацию значений столбцов реализовать с помощью Streams API.
- Поддержка всех команд из предыдущих лабораторных работ.
- Область, визуализирующую объекты коллекции
- Объекты должны быть нарисованы с помощью графических примитивов с использованием Graphics, Canvas или аналогичных средств графической библиотеки.
- При визуализации использовать данные о координатах и размерах объекта.
- Объекты от разных пользователей должны быть нарисованы разными цветами.
- При нажатии на объект должна выводиться информация об этом объекте.
- При добавлении/удалении/изменении объекта, он должен автоматически появиться/исчезнуть/измениться  на области как владельца, так и всех других клиентов.
- При отрисовке объекта должна воспроизводиться согласованная с преподавателем анимация.
- Возможность редактирования отдельных полей любого из объектов (принадлежащего пользователю). Переход к редактированию объекта возможен из таблицы с общим списком объектов и из области с визуализацией объекта.
- Возможность удаления выбранного объекта (даже если команды remove ранее не было).
- Перед непосредственной разработкой приложения необходимо согласовать прототип интерфейса с преподавателем. Прототип интерфейса должен быть создан с помощью средства для построения прототипов интерфейсов(mockplus, draw.io, etc.)

Вопросы к защите лабораторной работы:

- Компоненты пользовательского интерфейса. Иерархия компонентов.
- Базовые классы Component, Container, JComponent.
- Менеджеры компоновки.
- Модель обработки событий. Класс-слушатель и класс-событие.
- Технология JavaFX. Особенности архитектуры, отличия от AWT / Swing.
- Интернационализация. Локализация. Хранение локализованных ресурсов.
Форматирование локализованных числовых данных, текста, даты и времени. Классы NumberFormat, DateFormat, MessageFormat, ChoiceFormat.
