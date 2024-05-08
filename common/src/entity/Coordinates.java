package entity;

import java.io.Serial;
import java.io.Serializable;

public class Coordinates extends Entity {
    @Serial
    private static final long serialVersionUID = -3485884422219031514L;
    private double x; //Максимальное значение поля: 790
    private Integer y; //Значение поля должно быть больше -858, Поле не может быть null

    public Coordinates(double x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public static boolean checkX(double x) {
        return (x <= 790);
    }

    public static boolean checkY(Integer y) {
        return (y != null && y > -858);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
