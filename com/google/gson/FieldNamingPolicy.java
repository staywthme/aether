/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.FieldNamingStrategy;
import java.lang.reflect.Field;
import java.util.Locale;

public enum FieldNamingPolicy implements FieldNamingStrategy
{
    IDENTITY{

        @Override
        public final String translateName(Field field) {
            return field.getName();
        }
    }
    ,
    UPPER_CAMEL_CASE{

        @Override
        public final String translateName(Field field) {
            return 2.upperCaseFirstLetter(field.getName());
        }
    }
    ,
    UPPER_CAMEL_CASE_WITH_SPACES{

        @Override
        public final String translateName(Field field) {
            return 3.upperCaseFirstLetter(3.separateCamelCase(field.getName(), ' '));
        }
    }
    ,
    UPPER_CASE_WITH_UNDERSCORES{

        @Override
        public final String translateName(Field field) {
            return 4.separateCamelCase(field.getName(), '_').toUpperCase(Locale.ENGLISH);
        }
    }
    ,
    LOWER_CASE_WITH_UNDERSCORES{

        @Override
        public final String translateName(Field field) {
            return 5.separateCamelCase(field.getName(), '_').toLowerCase(Locale.ENGLISH);
        }
    }
    ,
    LOWER_CASE_WITH_DASHES{

        @Override
        public final String translateName(Field field) {
            return 6.separateCamelCase(field.getName(), '-').toLowerCase(Locale.ENGLISH);
        }
    }
    ,
    LOWER_CASE_WITH_DOTS{

        @Override
        public final String translateName(Field field) {
            return 7.separateCamelCase(field.getName(), '.').toLowerCase(Locale.ENGLISH);
        }
    };


    static String separateCamelCase(String string, char c2) {
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = string.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            char c3 = string.charAt(i2);
            if (Character.isUpperCase(c3) && stringBuilder.length() != 0) {
                stringBuilder.append(c2);
            }
            stringBuilder.append(c3);
        }
        return stringBuilder.toString();
    }

    static String upperCaseFirstLetter(String string) {
        int n2 = string.length();
        for (int i2 = 0; i2 < n2; ++i2) {
            char c2 = string.charAt(i2);
            if (!Character.isLetter(c2)) continue;
            if (Character.isUpperCase(c2)) {
                return string;
            }
            n2 = Character.toUpperCase(c2);
            if (i2 == 0) {
                return (char)n2 + string.substring(1);
            }
            return string.substring(0, i2) + (char)n2 + string.substring(i2 + '\u0001');
        }
        return string;
    }
}

