package com.project.healthcare.data;

public enum ManagedBy {
    Government,
    Private,
    Community,
    NGO,
    Social_Welfare;

    public static String toString(ManagedBy type) {
        return type.toString().replace('_', ' ');
    }

    public static ManagedBy fromString(String s) {
        return valueOf(s.replace(' ', '_'));
    }
}
