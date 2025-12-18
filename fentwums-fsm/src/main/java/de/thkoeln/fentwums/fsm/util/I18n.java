package de.thkoeln.fentwums.fsm.util;

import java.util.ResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * Utility class for internationalization (i18n).
 * Manages resource bundles to provide multi-language support and fallback mechanisms.
 */
public class I18n {

    /* Base name of the properties files located in the resource folder */
    private static final String BUNDLE_NAME = "messages";

    /* The ResourceBundle automatically loads the appropriate language or the default file */
    private static ResourceBundle bundle = ResourceBundle.getBundle(String.format("bundle/%s", BUNDLE_NAME), Locale.getDefault());

    /**
     * Retrieves a localized string for the given key.
     * If the key is missing, the key itself is returned wrapped in exclamation marks
     * to highlight the missing translation in the UI.
     * * @param key The identification key for the desired string
     * @return The localized text or !key! if not found
     */
    public static String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            /* Return marked key to indicate missing translation during development */
            return '!' + key + '!';
        }
    }

    /**
     * Allows changing the application's locale at runtime.
     * Reloads the resource bundle based on the provided locale.
     * * @param locale The new locale to be used (e.g., Locale.ENGLISH)
     */
    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }
}
