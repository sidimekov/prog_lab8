package database;

import entity.Coordinates;
import entity.LocationFrom;
import entity.LocationTo;
import entity.Route;
import network.Server;
import network.User;
import util.HashManager;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Logger;

public class DatabaseManager {
    private final Logger logger = Server.getLogger();

    private Connection connect() {
        try {
//            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "vgmkscir");
        } catch (SQLException e) {
            logger.severe("Ошибка подключения к базе данных");
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Проверяет, можно ли зарегистрировать пользователя (то есть нет ли указанного логина в таблице users)
     *
     * @param login - логин
     * @return false, если нельзя зарегистрировать, true - если можно
     */
    public boolean checkRegister(String login) {
        Connection connection = connect();
        try {
            if (connection != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(QueryManager.FIND_USER);
                preparedStatement.setString(1, login);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    connection.close();
                    return false;
                }
                connection.close();
                return true;
            } else {
                logger.severe("Подключение - null");
                return false;
            }
        } catch (SQLException e) {
            logger.severe("Ошибка выполнения запроса");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Проверяет, есть ли пользователь с указанным логином и паролем, возвращает id
     *
     * @param user - пользователь
     * @return ид пользователя, если пользователя можно авторизовать
     */
    public long checkUser(User user) {
        Connection connection = connect();
        try {
            if (connection != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(QueryManager.FIND_USER);
                preparedStatement.setString(1, user.getLogin());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String password = user.getPassword() + resultSet.getString("salt");
                    long id = resultSet.getLong("id");
                    if (resultSet.getString("password").equals(HashManager.hashPassword(password))) {
                        return id;
                    }
                }
            } else {
                logger.severe("Подключение - null");
                return -1;
            }
            connection.close();
        } catch (SQLException e) {
            logger.severe("Ошибка выполнения запроса");
            e.printStackTrace();
            return -1;
        }
        return -1;
    }


    public long addUser(User user) {
        Connection connection = connect();
        try {
            if (connection != null) {
                String salt = saltGenerator();
                String password = HashManager.hashPassword(user.getPassword() + salt);

                PreparedStatement pr = connection.prepareStatement(QueryManager.ADD_USER);
                pr.setString(1, user.getLogin());
                pr.setString(2, password);
                pr.setString(3, salt);
                ResultSet result = pr.executeQuery();

                connection.close();

                return result.getLong(1);
            } else {
                logger.severe("Подключение - null");
                return -1;
            }
        } catch (SQLException e) {
            logger.severe("Ошибка при выполнении запроса");
//            e.printStackTrace();
            return -1;
        }
    }


    public long addRoute(Route route, long userId) {
        Connection connection = connect();
        try {
            if (connection != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(QueryManager.ADD_ROUTE);
                preparedStatement.setString(1, route.getName());
                preparedStatement.setString(2, route.getCreationDate().toString());
                preparedStatement.setDouble(3, route.getCoordinates().getX());
                preparedStatement.setInt(4, route.getCoordinates().getY());
                preparedStatement.setInt(5, route.getFrom().getX());
                preparedStatement.setInt(6, route.getFrom().getY());
                preparedStatement.setFloat(7, route.getFrom().getZ());
                preparedStatement.setString(8, route.getTo().getName());
                preparedStatement.setFloat(9, route.getTo().getX());
                preparedStatement.setInt(10, route.getTo().getY());
                preparedStatement.setLong(11, route.getTo().getZ());
                preparedStatement.setDouble(12, route.getDistance());
                preparedStatement.setLong(13, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    logger.severe("Не удалось добавить объект");
                    return -1;
                }
                logger.info("Объект был успешно добавлен");

                connection.close();
                return resultSet.getInt(1);
            } else {
                logger.severe("Подключение - null");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("Ошибка при выполнении запроса");
            return -1;
        }
    }

    public boolean deleteUserObjects(User user, List<Long> ids) {
        Connection connection = connect();
        try {
            if (connection != null) {
                for (Long id : ids) {
                    PreparedStatement preparedStatement = connection.prepareStatement(QueryManager.CLEAR_OBJECTS);
                    preparedStatement.setString(1, user.getLogin());
                    preparedStatement.setLong(2, id);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    connection.close();
                    return resultSet.next();
                }
                connection.close();
                return false;
            } else {
                logger.severe("Подключение - null");
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteObject(Long id) {
        Connection connection = connect();
        try {
            if (connection != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(QueryManager.DELETE_OBJECT);
                preparedStatement.setLong(1, id);
//                preparedStatement.setString(1, user.getLogin());
                ResultSet resultSet = preparedStatement.executeQuery();
                connection.close();
                return resultSet.next();
            } else {
                logger.severe("Подключение - null");
                return false;
            }
        } catch (SQLException e) {
            return false;
        }

    }

    public long updateObject(Long id, long userId, Route route) {
        Connection connection = connect();
        try {
            if (connection != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(QueryManager.UPDATE_OBJECT);
                preparedStatement.setString(1, route.getName());
                preparedStatement.setString(2, route.getCreationDate().toString());
                preparedStatement.setDouble(3, route.getCoordinates().getX());
                preparedStatement.setInt(4, route.getCoordinates().getY());
                preparedStatement.setInt(5, route.getFrom().getX());
                preparedStatement.setInt(6, route.getFrom().getY());
                preparedStatement.setFloat(7, route.getFrom().getZ());
                preparedStatement.setString(8, route.getTo().getName());
                preparedStatement.setFloat(9, route.getTo().getX());
                preparedStatement.setInt(10, route.getTo().getY());
                preparedStatement.setLong(11, route.getTo().getZ());
                preparedStatement.setDouble(12, route.getDistance());
                preparedStatement.setLong(13, id);
                preparedStatement.setLong(14, userId);
                ResultSet resultSet = preparedStatement.executeQuery();
                connection.close();

                long updated = -1;
                while (resultSet.next()) {
                    updated = resultSet.getInt(1);
                }
                return updated;
            } else {
                logger.severe("Подключение - null");

                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Route getRoute(long id) {
        Connection connection = connect();
        Route route = null;
        try {
            if (connection != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(QueryManager.GET_OBJECT);
                preparedStatement.setLong(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    route = parseRoute(resultSet);
                }
            } else {
                logger.severe("Подключение - null");
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return route;
    }

    public PriorityQueue<Route> getCollection() {
        Connection connection = connect();
        PriorityQueue<Route> routeCollection = new PriorityQueue<>();
        try {
            if (connection != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(QueryManager.GET_OBJECTS);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Route route = parseRoute(resultSet);
                    route.setId(resultSet.getLong(1));
                    route.setCreationDate(resultSet.getString(3));
                    routeCollection.add(route);
                }
                connection.close();
                return routeCollection;
            } else {
                logger.severe("Подключение - null");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe("Ошибка выполнения запроса");
            return null;
        }
    }

    private Route parseRoute(ResultSet resultSet) throws SQLException {
        return new Route(resultSet.getString(2),
                new Coordinates(
                        resultSet.getDouble(4),
                        resultSet.getInt(5)
                ),
                new LocationFrom(
                        resultSet.getInt(6),
                        resultSet.getInt(7),
                        resultSet.getFloat(8)
                ),
                new LocationTo(
                        resultSet.getString(9),
                        resultSet.getFloat(10),
                        resultSet.getInt(11),
                        resultSet.getLong(12)
                ),
                resultSet.getDouble(13));
    }


    private String saltGenerator() {
        String alphabet = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(15);

        for (int i = 0; i < 15; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
