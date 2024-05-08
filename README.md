# Лабораторная работа 7

## Вариант: 3301

## Задание
Доработать программу из лабораторной работы №6 следующим образом:

- Организовать хранение коллекции в реляционной СУБД (PostgresQL). Убрать хранение коллекции в файле.
- Для генерации поля id использовать средства базы данных (sequence).
- Обновлять состояние коллекции в памяти только при успешном добавлении объекта в БД
- Все команды получения данных должны работать с коллекцией в памяти, а не в БД
- Организовать возможность регистрации и авторизации пользователей. У пользователя есть возможность указать пароль.
- Пароли при хранении хэшировать алгоритмом ***MD2***
- Запретить выполнение команд не авторизованным пользователям.
- При хранении объектов сохранять информацию о пользователе, который создал этот объект.
- Пользователи должны иметь возможность просмотра всех объектов коллекции, но модифицировать могут только принадлежащие им.
- Для идентификации пользователя отправлять логин и пароль с каждым запросом.

**Необходимо реализовать многопоточную обработку запросов.**

- Для многопоточного чтения запросов использовать `Cached thread pool`
- Для многопотчной обработки полученного запроса использовать `ForkJoinPool`
- Для многопоточной отправки ответа использовать `Fixed thread pool`
- Для синхронизации доступа к коллекции использовать синхронизацию чтения и записи с помощью `java.util.concurrent.locks.ReentrantLock`

**Порядок выполнения работы:**

- В качестве базы данных использовать PostgreSQL.
- Для подключения к БД на кафедральном сервере использовать хост pg, имя базы данных - studs, имя пользователя/пароль совпадают с таковыми для подключения к серверу.
