package com.productivity.controller.service.statistics;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.productivity.controller.service.statistics.TimeChartMode.*;

public class TimeChartModePicker {

    public TimeChartMode determineMode(LocalDate from, LocalDate to) {
        int daysBetween = (int) from.until(to, ChronoUnit.DAYS);
        if (daysBetween < 14) {
            return DAYS;
        } else if (daysBetween < 60) {
            return WEEKS;
        } else if (daysBetween < 365 * 2) {
            return MONTHS;
        } else {
            return YEARS;
        }
    }
}
