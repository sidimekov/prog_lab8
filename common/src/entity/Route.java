package entity;

import network.User;
import util.IdManager;

import java.awt.*;
import java.io.Serial;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Route extends Entity implements Comparable {
    @Serial
    private static final long serialVersionUID = 2459895131011601780L;
    private long id; // Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private LocationFrom from; //Поле не может быть null
    private LocationTo to; //Поле может быть null
    private double distance; //Значение поля должно быть больше 1

    private Color color;


    public Route(String name, Coordinates coordinates, LocationFrom from, LocationTo to, double distance) {
        this.id = IdManager.getId();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Route(long id, String name, Coordinates coordinates, LocationFrom from, LocationTo to, double distance) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.from = from;
        this.to = to;
        this.distance = distance;
    }
    public Route(long id, String name, Date creationDate, Coordinates coordinates, LocationFrom from, LocationTo to, double distance) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            this.creationDate = sdf.parse(creationDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public LocationFrom getFrom() {
        return from;
    }

    public void setFrom(LocationFrom from) {
        this.from = from;
    }

    public LocationTo getTo() {
        return to;
    }

    public void setTo(LocationTo to) {
        this.to = to;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public static boolean checkName(String name) {
        return (name != null && !name.isEmpty());
    }
    public static boolean checkCoordinates(Coordinates coordinates) {
        return (coordinates != null);
    }
    public static boolean checkCreationDate(Date creationDate) {
        return (creationDate != null);
    }
    public static boolean checkFrom(LocationFrom from) {
        return (from != null);
    }
    public static boolean checkTo(LocationTo to) {
        return true;
    }
    public static boolean checkDistance(double distance) {
        return (distance > 1);
    }



    @Override
    public String toString() {
        return "id=" + id +
                "\nname='" + name + '\'' +
                "\ncoordinates=" + coordinates +
                "\ncreationDate=" + creationDate +
                "\nfrom=" + from +
                "\nto=" + to +
                "\ndistance=" + distance;
    }

    @Override
    public int compareTo(Object obj) {
        Route route2 = (Route) obj;
        Route route1 = this;
        double distDiff = route1.getDistance() - route2.getDistance();
        if (distDiff > 0) {
            return 1;
        } else if (distDiff < 0) {
            return -1;
        } else {
            return route1.getName().compareTo(route2.getName());
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public void setColorFromUser(User user) {
        Color userColor = new Color(user.hashCode());
        int red = userColor.getRed();
        int green = userColor.getGreen();
        int blue = userColor.getBlue();
        double brightness = 0.299 * red + 0.587 * green + 0.114 * blue;
        if (brightness < 128) {
            int newRed = (int) (red + (255 - red) * 0.5);
            int newGreen = (int) (green + (255 - green) * 0.5);
            int newBlue = (int) (blue + (255 - blue) * 0.5);
            userColor = new Color(newRed, newGreen, newBlue);
        }
        setColor(userColor);
    }

    public Color getColor() {
        return color;
    }
}
