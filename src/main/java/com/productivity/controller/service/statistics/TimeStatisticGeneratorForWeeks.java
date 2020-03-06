package com.productivity.controller.service.statistics;

import com.productivity.model.category.CategoryManager;
import com.productivity.model.record.RecordManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class TimeStatisticGeneratorForWeeks extends TimeStatisticsGenerator {
    protected TimeStatisticGeneratorForWeeks(CategoryManager categoryManager, RecordManager recordManager, LocalDate from, LocalDate to, int segmentDurationInDays) {
        super(categoryManager, recordManager, from, to, segmentDurationInDays);
    }

    @Override
    protected Set<LocalDate> computeSegmentStartDates() {
        LocalDate monday = findFirstMonday();
        return getAllMondaysBetween(monday, to);
    }

    private Set<LocalDate> getAllMondaysBetween(LocalDate monday, LocalDate to) {
        Set<LocalDate> allDates = new HashSet<>();
        int addedWeeks = 0;
        LocalDate firstDay = monday;
        do {
            monday = firstDay.plusWeeks(addedWeeks++);
            allDates.add(monday);
        } while (!monday.plusWeeks(1).isAfter(to));
        return allDates;
    }


    private LocalDate findFirstMonday() {
        int addedDays = 0;
        LocalDate firstDay = from;
        LocalDate monday;
        do {
            monday = firstDay.plusDays(addedDays++);
        } while (monday.getDayOfWeek() != DayOfWeek.MONDAY);
        return monday;
    }

}
