package commandManagers;

import builders.RouteBuilder;
import comparators.RouteComparator;
import database.DatabaseManager;
import entity.Coordinates;
import entity.LocationFrom;
import entity.LocationTo;
import entity.Route;
import exceptions.FailedValidationException;
import input.JSONManager;
import network.Server;
import util.InputManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

// Receiver
public class RouteManager {
    private PriorityQueue<Route> collection;
    private static RouteManager instance;
    private static Date initializationDate;
    private static final Logger logger = Server.getLogger();
    private final DatabaseManager dbManager = new DatabaseManager();

    private RouteManager() {
        collection = new PriorityQueue<>();
    }

    public static RouteManager getInstance() {
        if (!isInitialized()) {
            initialize();
        }
        return instance;
    }

    public static void initialize() {
        if (instance == null) {
            instance = new RouteManager();
            initializationDate = new Date();
            instance.loadCollection();
        }
    }

    public static boolean isInitialized() {
        return (instance != null);
    }

    public Date getInitializationDate() {
        return initializationDate;
    }

    public void loadCollection() {

        DatabaseManager dbManager = new DatabaseManager();
        PriorityQueue<Route> collection = dbManager.getCollection();

        if (collection != null) {
            if (checkIdUniqueness()) {
                if (collection.stream().allMatch(RouteManager::validateElement)) {
                    this.collection = collection;
                    logger.info("Коллекция была загружена из файла");
                } else {
                    logger.severe("Ошибка при загрузке из файла: Некоторые элементы из коллекции некорректны");
                    this.collection = new PriorityQueue<>();
                }
            } else {
                this.collection = new PriorityQueue<>();
                logger.severe("Ошибка при загрузке из файла: были обнаружены одинаковые id у элементов");
            }
        } else {
            this.collection = new PriorityQueue<>();
            logger.severe("Коллекция не была загружена из файла");
        }
    }

    public PriorityQueue<Route> getCollection() {
        return collection;
    }

    public Route getById(long id) {
        return collection.stream().filter(el -> el.getId() == id).findFirst().orElse(null);
    }

    public boolean hasElement(long id) {
        return (getById(id) != null);
    }

    public void addElement(Route el, long userId) throws FailedValidationException {
        addElement(el, userId, false);
    }

    public void addElement(Route el, long userId, boolean skipValidations) throws FailedValidationException {
        if (skipValidations) {
            long id = dbManager.addRoute(el, userId);
            if (id != -1) {
                el.setId(id);
                collection.add(el);
            } else {
                logger.severe("Ошибка с бд при добавлении элемента");
            }
        } else {
            if (RouteManager.validateElement(el)) {
                long id = dbManager.addRoute(el, userId);
                el.setId(id);
                if (id != -1) {
                    collection.add(el);
                } else {
                    logger.severe("Ошибка с бд при добавлении элемента");
                }
            } else {
                throw new FailedValidationException("Ошибка в валидации");
            }
        }
    }

    public static Route buildNew(BufferedReader reader, boolean withId) throws IOException {
        try {
            return RouteBuilder.build(reader, withId);
        } catch (NullPointerException e) {
            logger.severe(String.format("При построении нового объекта произошла ошибка: %s\n", e.getMessage()));
            return null;
        }
    }

    public static Route buildNew(BufferedReader reader) throws IOException {
        return buildNew(reader, false);
    }

    /**
     * Обновляет элемент в коллекции
     * @param element - элемент
     * @param userId - ид владельца элемента
     * @return id элемента обновлённого, если не получилось, то -1
     */
    public long update(Route element, long userId) throws FailedValidationException{
        if (RouteManager.validateElement(element, true)) {
            long id = element.getId();
            long updated = dbManager.updateObject(id, userId, element);
            if (updated != -1) {
                removeElement(element.getId());
                Route newElement = dbManager.getRoute(id);
                newElement.setId(updated);
                collection.add(newElement);
            }
            return updated;
        } else {
            return -1;
        }
    }

    public void removeElement(long id) {
        removeElement(getById(id));
    }

    public void removeElement(Route route) {
        List<Route> list = convertToList(collection);
        list.remove(route);
        collection = convertFromList(list);
    }

    public List<Long> getIds() {
        return collection.stream().map(Route::getId).collect(Collectors.toList());
    }

    /**
     * Проверить уникальность коллекции
     *
     * @return true, если уникальна, иначе false
     */
    public boolean checkIdUniqueness() {
        ArrayList<Long> ids = (ArrayList<Long>) getIds();
        Set<Long> idSet = new HashSet<>(ids);
        return (idSet.size() == ids.size());
    }

    public Route getMinElement() {
        return collection.stream().min(new RouteComparator()).orElse(null);
    }

    /**
     * Конвертировать лист в коллекцию
     *
     * @param list - лист
     * @return - коллекция
     */
    public static PriorityQueue<Route> convertFromList(List<Route> list) {
        PriorityQueue<Route> collection = new PriorityQueue<>();
        collection.addAll(list);
        return collection;
    }


    public static List<Route> convertToList(PriorityQueue<Route> collection) {
        return new ArrayList<>(collection);
    }

    public static boolean validateElement(Route el) {
        return validateElement(el, false);
    }

    public static boolean validateElement(Route el, boolean idSkip) {
        if (!idSkip) {
            if (!checkId(el.getId())) {
                logger.warning("Неверный id (возможно, он уже занят)");
                return false;
            }
        }

        if (!Route.checkName(el.getName())) {
            logger.warning("Неверное имя элемента (Поле не может быть null, Строка не может быть пустой)");
            return false;
        }

        if (!Route.checkCreationDate(el.getCreationDate())) {
            logger.warning("Неверная дата создания (Поле не может быть null)");
            return false;
        }

        Coordinates coordinates = el.getCoordinates();
        if (!Route.checkCoordinates(coordinates)) {
            logger.warning("Некорректные координаты (Поле не может быть null)");
            return false;
        }
        if (!Coordinates.checkX(coordinates.getX()) || !Coordinates.checkY(coordinates.getY())) {
            logger.warning("Некорректные координаты (x: Максимальное значение поля: 790, y: Значение поля должно быть больше -858, Поле не может быть null");
            return false;
        }

        LocationFrom from = el.getFrom();
        if (!Route.checkFrom(from)) {
            logger.warning("Некорректная изначальная локация (Поле не может быть null)");
            return false;
        }
        if (!LocationFrom.checkY(from.getY())) {
            logger.warning("Некорректная изначальная локация (y: Поле не может быть null");
            return false;
        }

        LocationTo to = el.getTo();
        if (to != null) {
            if (!LocationTo.checkY(to.getY()) || !LocationTo.checkName(to.getName())) {
                logger.warning("Некорректная окончательная локация (y: Поле не может быть null, name: Длина строки не должна быть больше 443, Поле не может быть null)");
                return false;
            }
        }

        if (!Route.checkDistance(el.getDistance())) {
            logger.warning("Некорректная дистанция (Значение поля должно быть больше 1)");
            return false;
        }

        return true;
    }

    public void removeAllByDistance(double distance) {
//        collection
//                .stream()
//                .filter(el -> (el.getDistance() == distance))
//                .forEach(el -> commandManagers.RouteManager.getInstance().getCollection().remove(el));
        collection.removeIf(el -> (el.getDistance() == distance));
    }

    public long countGreaterThanDistance(double distance) {
        return collection
                .stream()
                .filter(el -> (el.getDistance() > distance))
                .count();
    }

    public static String returnCollection(PriorityQueue<Route> collection) {
        List<Route> list = convertToList(collection);
        if (collection.isEmpty()) {
            return ("Коллекция пуста!");
        } else {
            StringBuilder response = new StringBuilder();
            for (int i = 0; i < collection.size(); i++) {
                response.append(String.format("Элемент %s / %s:\n%s\n", i + 1, collection.size(), list.get(i)));
            }
            return response.toString();
        }
    }

    public String returnDescending() {
        List<Route> list = RouteManager.convertToList(collection);
        list.sort(new RouteComparator());
        Collections.reverse(list);
        if (collection.isEmpty()) {
            return ("Коллекция пуста!");
        } else {
            StringBuilder response = new StringBuilder();
            for (int i = 0; i < collection.size(); i++) {
                response.append(String.format("Элемент %s / %s:\n%s\n", i + 1, collection.size(), list.get(i)));
            }
            return response.toString();
        }
    }

//    /** Удаляет все объекты, созданные заданным id пользователя
//     *
//     * @param userId - id пользователя
//     * @return true, если успешно, false - если не удалось найти объекты
//     */
//    public boolean clearUserObjects(long userId) {
//        PriorityQueue<Route> coll = getCollection();
//        return false;
//    }

    @Deprecated
    public void saveCollection(String path) {
        String jsonContent = JSONManager.collectionToJson();
        InputManager.write(path, jsonContent);
    }

    public static boolean checkId(long id) {
        if (RouteManager.isInitialized()) {
            return (id > 0 && !RouteManager.getInstance().getIds().contains(id));
        } else {
            return (id > 0);
        }
    }
}
