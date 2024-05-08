package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

public class InputManager {
    private static BufferedReader consoleReader;

    public static BufferedReader getConsoleReader() {
        if (consoleReader == null) {
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
        }
        return consoleReader;
    }

    public static BufferedReader getFileReader(String path) {
        try {
            path = path.replaceAll("\"","");
            return new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void write(String path, String data) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path)))) {
            writer.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getCollectionFilePath() {
        return System.getenv("JAVA_COLLECTION_PATH");
    }

    /**
     * Возвращает объект класса File, если указанный путь соответствует образцу
     * @param path - путь
     * @return файл
     */
    public static File validPath(String path) {
        if (path.startsWith("\"") && path.endsWith("\"")) {
            path = path.substring(1, path.length() - 1);
        }
        try {
            Paths.get(path);
        } catch (NullPointerException | InvalidPathException e) {
            return null;
        }
        File file = new File(path);
        if (file.exists()) {
//            System.out.printf("file %s exists\n", file.getName());
            return file;
        } else {
            return null;
        }
    }
}
