package com.avi.leavemgmt.model;

public enum LeaveType {
    SICK("Sick Leave"),
    CASUAL("Casual Leave"),
    ANNUAL("Annual Leave");

    private final String displayName;

    LeaveType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}