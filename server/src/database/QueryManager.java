package database;

public class QueryManager {
    public static final String FIND_USER = "SELECT password, salt, id FROM users where login = ?; ";

    public static final String ADD_USER = "INSERT INTO users(login, password, salt) VALUES (?, ?, ?) RETURNING id";

    public static final String ADD_ROUTE = "INSERT INTO routes(route_name, creation_date, coordinates_x, coordinates_y, " +
            "from_x, from_y, from_z, to_name, to_x, to_y, to_z, distance, user_id) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;";

    public static final String CLEAR_USER_OBJECTS = "delete from routes where (user_id = ?) returning id;";

    public static final String REMOVE_USER_OBJECT = "delete from routes where (id = ?) and (user_id = ?) returning id;";
    public static final String REMOVE_OBJECT = "delete from routes where (id = ?) returning id;";

    public static final String UPDATE_OBJECT = "update routes set (route_name, creation_date, coordinates_x, coordinates_y, " +
            "from_x, from_y, from_z, to_name, to_x, to_y, to_z, distance) = " +
            "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) where (id = ?) and (user_id) = ? returning id;";
    public static final String GET_OBJECT = "select * from routes where id = ?;";
    public static final String GET_OBJECTS = "select * from routes;";
    public static final String GET_USER_OBJECTS = "select * from routes where (user_id = ?);";
    public static final String GET_USER = "select * from users where (id = ?);";
}
