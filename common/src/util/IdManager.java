package util;

import java.util.HashSet;

public class IdManager {
    private static long currentUsedId = 1;
    private static HashSet<Long> used = new HashSet<>();

    public static long getId() {
        long maybeId;
        do {
            maybeId = currentUsedId++;
        } while (used.contains(maybeId));

        return maybeId;
    }
}
