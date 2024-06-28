package org.core.backend.ticketapp.passport.util;

import org.passay.*;

import static org.passay.IllegalCharacterRule.ERROR_CODE;

public class PasswordUtil {

    public static String clean(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "");
    }

    public static String generateCode(String moduleName, String actionName) {
        actionName = actionName.replaceAll("[^a-zA-Z0-9]", "_");
        moduleName = moduleName.toLowerCase();
        return moduleName+"."+actionName;
    }

    public static String normalizeString(String s) {
        return s.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
    }
    public static String generatePassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return IllegalCharacterRule.ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(10, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
        return password;
    }

}
