package com.productivity.controller.service.statistics;

public enum  TimeChartMode {
    DAYS("Days"), WEEKS("Weeks"), MONTHS("Months"), YEARS("Years"), CUSTOM("Custom");

    private String name;

    TimeChartMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
