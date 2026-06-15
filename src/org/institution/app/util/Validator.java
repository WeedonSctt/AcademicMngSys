package org.institution.app.util;

public class Validator {
    public static boolean emailHasExtension(String e) {
        char[] characters = e.toCharArray();
        
        int index = 0;
        boolean at = false;
        for (char c : characters) {
            if (c == '@') {
                at = true;
                break;
            }

            index++;
        }

        if (at) {
            for (int i = index; i < characters.length; i++) {
                if (characters[i] == '.') {
                    return true;
                }
            }

            return false;
        }

        return false;
    }

    public static boolean studentInputData(int age, String email) {
        if (age < 18 || !emailHasExtension(email)) {
            return false;
        }

        return true;
    }

    public static boolean teacherInputData(String email) {
        if (!emailHasExtension(email)) {
            return false;
        }

        return true;
    }

}