package com.productivity.controller.service.statistics;

import com.productivity.model.category.CategoryManager;
import com.productivity.model.record.RecordManager;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class TimeStatisticsGeneratorForYears extends TimeStatisticsGenerator {
    protected TimeStatisticsGeneratorForYears(CategoryManager categoryManager, RecordManager recordManager, LocalDate from, LocalDate to, int segmentDurationInDays) {
        super(categoryManager, recordManager, from, to, segmentDurationInDays);
    }

    @Override
    protected Set<LocalDate> computeSegmentStartDates() {
        Set<LocalDate> yearsFirstDays = new HashSet<>();
        LocalDate newYear = from.withDayOfYear(1);
        do {
            yearsFirstDays.add(newYear);
            newYear = newYear.plusYears(1);
        } while (!newYear.isAfter(to));
        return yearsFirstDays;
    }
}
