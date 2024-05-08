package entity;

import java.io.Serial;
import java.io.Serializable;

public class LocationFrom extends Entity {
    @Serial
    private static final long serialVersionUID = -1915912694650092159L;
    private int x;
    private Integer y; //Поле не может быть null
    private float z;

    public LocationFrom(int x, Integer y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public static boolean checkX(int x) {
        return true;
    }

    public static boolean checkY(Integer y) {
        return (y != null);
    }
    public static boolean checkZ(float z) {
        return true;
    }

    @Override
    public String toString() {
        return "LocationFrom{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
