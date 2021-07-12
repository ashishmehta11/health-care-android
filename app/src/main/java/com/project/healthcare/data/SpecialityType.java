package com.project.healthcare.data;

public enum SpecialityType {
    General_Purpose,
    ENT,
    Dentist,
    Ophthalmology,
    Neurology,
    Pediatrician;

    public static String toString(SpecialityType type) {
        return type.toString().replace('_', ' ');
    }

    public static SpecialityType fromString(String s) {
        return valueOf(s.replace(' ', '_'));
    }
}
