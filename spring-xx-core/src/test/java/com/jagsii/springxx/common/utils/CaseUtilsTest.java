package com.jagsii.springxx.common.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaseUtilsTest {
    @Test
    void pascalToCamel() {
        assertEquals("thisIsPascalCaseString", CaseUtils.pascalToCamel("ThisIsPascalCaseString"));
        assertEquals("singleWord", CaseUtils.pascalToCamel("SingleWord"));
        assertEquals("alluppercase", CaseUtils.pascalToCamel("ALLUPPERCASE"));
        assertEquals("", CaseUtils.pascalToCamel(""));
        assertEquals("a", CaseUtils.pascalToCamel("A"));
        assertEquals("alreadyCamelCase", CaseUtils.pascalToCamel("alreadyCamelCase"));
    }

    @Test
    void pascalToSnake() {
        assertEquals("this_is_pascal_case_string", CaseUtils.pascalToSnake("ThisIsPascalCaseString"));
        assertEquals("single_word", CaseUtils.pascalToSnake("SingleWord"));
        assertEquals("alluppercase", CaseUtils.pascalToSnake("ALLUPPERCASE"));
        assertEquals("", CaseUtils.pascalToSnake(""));
        assertEquals("a", CaseUtils.pascalToSnake("A"));
        assertEquals("already_snake_case", CaseUtils.pascalToSnake("already_snake_case"));
    }

    @Test
    void pascalToKebab() {
        assertEquals("this-is-pascal-case-string", CaseUtils.pascalToKebab("ThisIsPascalCaseString"));
        assertEquals("single-word", CaseUtils.pascalToKebab("SingleWord"));
        assertEquals("alluppercase", CaseUtils.pascalToKebab("ALLUPPERCASE"));
        assertEquals("", CaseUtils.pascalToKebab(""));
        assertEquals("a", CaseUtils.pascalToKebab("A"));
        assertEquals("already-kebab-case", CaseUtils.pascalToKebab("already-kebab-case"));
    }

    @Test
    void snakeToPascal() {
        assertEquals("ThisIsSnakeCaseString", CaseUtils.snakeToPascal("this_is_snake_case_string"));
        assertEquals("SingleWord", CaseUtils.snakeToPascal("single_word"));
        assertEquals("AllUppercase", CaseUtils.snakeToPascal("all_uppercase"));
        assertEquals("", CaseUtils.snakeToPascal(""));
        assertEquals("A", CaseUtils.snakeToPascal("a"));
        assertEquals("AlreadyPascalCase", CaseUtils.snakeToPascal("AlreadyPascalCase"));
    }

    @Test
    void snakeToCamel() {
        assertEquals("thisIsSnakeCaseString", CaseUtils.snakeToCamel("this_is_snake_case_string"));
        assertEquals("singleWord", CaseUtils.snakeToCamel("single_word"));
        assertEquals("allUppercase", CaseUtils.snakeToCamel("all_uppercase"));
        assertEquals("", CaseUtils.snakeToCamel(""));
        assertEquals("a", CaseUtils.snakeToCamel("a"));
        assertEquals("alreadyCamelCase", CaseUtils.snakeToCamel("alreadyCamelCase"));
    }

    @Test
    void snakeToKebab() {
        assertEquals("this-is-snake-case-string", CaseUtils.snakeToKebab("this_is_snake_case_string"));
        assertEquals("single-word", CaseUtils.snakeToKebab("single_word"));
        assertEquals("ALL-UPPERCASE", CaseUtils.snakeToKebab("ALL_UPPERCASE"));
        assertEquals("", CaseUtils.snakeToKebab(""));
        assertEquals("a", CaseUtils.snakeToKebab("a"));
        assertEquals("already-kebab-case", CaseUtils.snakeToKebab("already-kebab-case"));
    }

    @Test
    void camelToPascal() {
        assertEquals("ThisIsCamelCaseString", CaseUtils.camelToPascal("thisIsCamelCaseString"));
        assertEquals("SingleWord", CaseUtils.camelToPascal("singleWord"));
        assertEquals("AllUppercase", CaseUtils.camelToPascal("allUppercase"));
        assertEquals("", CaseUtils.camelToPascal(""));
        assertEquals("A", CaseUtils.camelToPascal("a"));
        assertEquals("AlreadyPascalCase", CaseUtils.camelToPascal("AlreadyPascalCase"));
    }

    @Test
    void camelToSnake() {
        assertEquals("this_is_camel_case_string", CaseUtils.camelToSnake("thisIsCamelCaseString"));
        assertEquals("single_word", CaseUtils.camelToSnake("singleWord"));
        assertEquals("alluppercase", CaseUtils.camelToSnake("ALLUPPERCASE"));
        assertEquals("", CaseUtils.camelToSnake(""));
        assertEquals("a", CaseUtils.camelToSnake("a"));
        assertEquals("already_snake_case", CaseUtils.camelToSnake("already_snake_case"));
    }

    @Test
    void camelToKebab() {
        assertEquals("this-is-camel-case-string", CaseUtils.camelToKebab("thisIsCamelCaseString"));
        assertEquals("single-word", CaseUtils.camelToKebab("singleWord"));
        assertEquals("alluppercase", CaseUtils.camelToKebab("ALLUPPERCASE"));
        assertEquals("", CaseUtils.camelToKebab(""));
        assertEquals("a", CaseUtils.camelToKebab("a"));
        assertEquals("already-camel-case", CaseUtils.camelToKebab("alreadyCamelCase"));
    }

    @Test
    void kebabToPascal() {
        assertEquals("ThisIsKebabCaseString", CaseUtils.kebabToPascal("this-is-kebab-case-string"));
        assertEquals("SingleWord", CaseUtils.kebabToPascal("single-word"));
        assertEquals("AllUppercase", CaseUtils.kebabToPascal("all-uppercase"));
        assertEquals("", CaseUtils.kebabToPascal(""));
        assertEquals("A", CaseUtils.kebabToPascal("a"));
        assertEquals("AlreadyPascalCase", CaseUtils.kebabToPascal("AlreadyPascalCase"));
    }

    @Test
    void kebabToSnake() {
        assertEquals("this_is_kebab_case_string", CaseUtils.kebabToSnake("this-is-kebab-case-string"));
        assertEquals("single_word", CaseUtils.kebabToSnake("single-word"));
        assertEquals("all_uppercase", CaseUtils.kebabToSnake("all-uppercase"));
        assertEquals("", CaseUtils.kebabToSnake(""));
        assertEquals("a", CaseUtils.kebabToSnake("a"));
        assertEquals("already_snake_case", CaseUtils.kebabToSnake("already_snake_case"));
    }

    @Test
    void kebabToCamel() {
        assertEquals("thisIsKebabCaseString", CaseUtils.kebabToCamel("this-is-kebab-case-string"));
        assertEquals("singleWord", CaseUtils.kebabToCamel("single-word"));
        assertEquals("allUppercase", CaseUtils.kebabToCamel("all-uppercase"));
        assertEquals("", CaseUtils.kebabToCamel(""));
        assertEquals("a", CaseUtils.kebabToCamel("a"));
        assertEquals("alreadyCamelCase", CaseUtils.kebabToCamel("alreadyCamelCase"));
    }
}