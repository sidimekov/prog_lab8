package entity;

import java.io.Serial;
import java.io.Serializable;

public class LocationTo extends Entity {
    @Serial
    private static final long serialVersionUID = -5172728822641131375L;
    private float x;
    private Integer y; //Поле не может быть null
    private long z;
    private String name; //Длина строки не должна быть больше 443, Поле не может быть null

    public LocationTo(String name, float x, Integer y, long z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public long getZ() {
        return z;
    }

    public void setZ(long z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean checkX(float x) {
        return true;
    }
    public static boolean checkY(Integer y) {
        return (y != null);
    }
    public static boolean checkZ(long z) {
        return true;
    }
    public static boolean checkName(String name) {
        return (name != null && name.length() <= 443);
    }


    @Override
    public String toString() {
        return "LocationTo{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name='" + name + '\'' +
                '}';
    }
}
