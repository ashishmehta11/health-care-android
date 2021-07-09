package com.project.healthcare.data;

public enum FacilityType {
    Hospital,
    Clinic,
    HealthCare_Center,
    Nursing_Home,
    Radiology_Laboratory,
    Pathology_Laboratory;

    public static String toString(FacilityType type) {
        return type.toString().replace('_', ' ');
    }

    public static FacilityType fromString(String s) {
        return valueOf(s.replace(' ', '_'));
    }
}
