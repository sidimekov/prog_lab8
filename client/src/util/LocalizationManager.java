package util;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationManager {
    //    private static LocalizationManager instance;
    private static ResourceBundle resourceBundle;
    private static Locale locale;

    private LocalizationManager() {
        setLocale(Locale.of("ru", "RU"));
    }
//    public static LocalizationManager getInstance() {
//        if (instance == null) {
//            instance = new LocalizationManager();
//        }
//        return instance;
//    }

    public static void setLocale() {
        setLocale(Locale.of("ru", "RU"));
    }

    public static void setLocale(Locale locale) {
        ResourceBundle.clearCache();
        LocalizationManager.locale = locale;
        resourceBundle = ResourceBundle.getBundle("resources.gui", locale);
        Locale.setDefault(locale);
    }

    public static ResourceBundle getBundle() {
        if (resourceBundle == null) {
            setLocale();
        }
        return resourceBundle;
    }

    public static String getString(String key) {
        if (resourceBundle == null) {
            setLocale();
        }
        return resourceBundle.getString(key);
    }

    public static Locale getLocale() {
        if (locale == null) {
            setLocale();
        }
        return locale;
    }
}
