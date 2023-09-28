package com.jagsii.springxx.common.utils;

public class CaseUtils {
    /**
     * HelloWorld -&gt; helloWorld
     */
    public static String pascalToCamel(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean isPreviousUppercase = false;
        for (int i = 0, len = input.length(); i < len; i++) {
            char currentChar = input.charAt(i);

            if (i == 0) {
                result.append(Character.isUpperCase(currentChar) ? Character.toLowerCase(currentChar) : currentChar);
                isPreviousUppercase = true;
            } else if (Character.isUpperCase(currentChar)) {
                if (isPreviousUppercase) {
                    result.append(Character.toLowerCase(currentChar));
                } else {
                    result.append(currentChar);
                }
                isPreviousUppercase = true;
            } else {
                result.append(currentChar);
                isPreviousUppercase = false;
            }
        }
        return result.toString();
    }

    /**
     * HelloWorld -&gt; hello_world
     */
    public static String pascalToSnake(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean isPreviousUppercase = false;
        for (int i = 0, len = input.length(); i < len; i++) {
            char currentChar = input.charAt(i);

            // Append an underscore and the lowercase of the current character
            // if it's an uppercase letter and not the first character.
            if (Character.isUpperCase(currentChar)) {
                if (i > 0 && !isPreviousUppercase) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(currentChar));
                isPreviousUppercase = true;
            } else {
                result.append(currentChar);
                isPreviousUppercase = false;
            }
        }
        return result.toString();
    }

    /**
     * HelloWorld -> hello-world
     */
    public static String pascalToKebab(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean isPreviousUppercase = false;
        for (int i = 0, len = input.length(); i < len; i++) {
            char currentChar = input.charAt(i);

            // Append a hyphen and the lowercase of the current character
            // if it's an uppercase letter and not the first character.
            if (Character.isUpperCase(currentChar)) {
                if (i > 0 && !isPreviousUppercase) {
                    result.append('-');
                }
                result.append(Character.toLowerCase(currentChar));
                isPreviousUppercase = true;
            } else {
                result.append(currentChar);
                isPreviousUppercase = false;
            }
        }
        return result.toString();
    }

    /**
     * hello_world -> HelloWorld
     */
    public static String snakeToPascal(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        return convertSnake(input, result, capitalizeNext);
    }

    private static String convertSnake(String input, StringBuilder result, boolean capitalizeNext) {
        for (int i = 0, len = input.length(); i < len; i++) {
            char currentChar = input.charAt(i);
            if (currentChar == '_') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(currentChar));
                capitalizeNext = false;
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }

    /**
     * hello_world -> helloWorld
     */
    public static String snakeToCamel(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        return convertSnake(input, result, capitalizeNext);
    }

    /**
     * hello_world -> hello-world<br/>
     * HELLO_WORLD -> HELLO-WORLD
     */
    public static String snakeToKebab(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0, len = input.length(); i < len; i++) {
            char currentChar = input.charAt(i);
            if (currentChar == '_') {
                result.append('-');
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }

    /**
     * helloWorld -> HelloWorld
     */
    public static String camelToPascal(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

    /**
     * helloWorld -> hello_world
     */
    public static String camelToSnake(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean isFirstChar = true;
        boolean isPreviousUpperCase = false;

        for (int i = 0, len = input.length(); i < len; i++) {
            char currentChar = input.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                if (!isFirstChar && !isPreviousUpperCase) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(currentChar));
                isPreviousUpperCase = true;
            } else {
                result.append(currentChar);
                isPreviousUpperCase = false;
            }
            isFirstChar = false;
        }
        return result.toString();
    }

    /**
     * helloWorld -> hello-world
     */
    public static String camelToKebab(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean isFirstChar = true;
        boolean isPreviousUpperCase = false;

        for (int i = 0, len = input.length(); i < len; i++) {
            char currentChar = input.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                if (!isFirstChar && !isPreviousUpperCase) {
                    result.append('-');
                }
                result.append(Character.toLowerCase(currentChar));
                isPreviousUpperCase = true;
            } else {
                result.append(currentChar);
                isPreviousUpperCase = false;
            }
            isFirstChar = false;
        }
        return result.toString();
    }

    /**
     * hello-world -> HelloWorld
     */
    public static String kebabToPascal(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        return convertKebab(input, result, capitalizeNext);
    }

    private static String convertKebab(String input, StringBuilder result, boolean capitalizeNext) {
        for (int i = 0, len = input.length(); i < len; i++) {
            char currentChar = input.charAt(i);
            if (currentChar == '-') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(currentChar));
                capitalizeNext = false;
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }

    /**
     * hello-world -> hello_world<br/>
     * HELLO-WORLD -> HELLO_WORLD
     */
    public static String kebabToSnake(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0, len = input.length(); i < len; i++) {
            char currentChar = input.charAt(i);
            if (currentChar == '-') {
                result.append('_');
            } else {
                result.append(currentChar);
            }
        }
        return result.toString();
    }

    /**
     * hello-world -> helloWorld
     */
    public static String kebabToCamel(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;
        return convertKebab(input, result, capitalizeNext);
    }
}
