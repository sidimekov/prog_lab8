package comparators;

import entity.Route;

import java.util.Comparator;

public class RouteComparator implements Comparator<Route> {
    @Override
    public int compare(Route r1, Route r2) {
        return r1.compareTo(r2);
    }
}
