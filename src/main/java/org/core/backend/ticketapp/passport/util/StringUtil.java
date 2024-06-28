package org.core.backend.ticketapp.passport.util;

public class StringUtil {

    public static String clean(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "");
    }

    public static String generateCode(String moduleName, String actionName) {
        actionName = actionName.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
        moduleName = moduleName.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
        return moduleName+"."+actionName;
    }

    public static String replaceSpecialCharactersWithUnderscores(String name) {
        name = name.replaceAll("[^a-zA-Z0-9]", "_");
        name = name.toLowerCase();
        return name;
    }

    public static String normalizeString(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
    }

    public static String normalizeWithUnderscore(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
    }
}
