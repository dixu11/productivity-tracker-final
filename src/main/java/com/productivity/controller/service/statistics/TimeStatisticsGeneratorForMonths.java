package com.productivity.controller.service.statistics;

import com.productivity.model.category.Category;
import com.productivity.model.category.CategoryManager;
import com.productivity.model.record.RecordManager;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TimeStatisticsGeneratorForMonths extends TimeStatisticsGenerator {
    protected TimeStatisticsGeneratorForMonths(CategoryManager categoryManager, RecordManager recordManager, LocalDate from, LocalDate to, int segmentDurationInDays) {
        super(categoryManager, recordManager, from, to, segmentDurationInDays);
    }

    @Override
    protected Set<LocalDate> computeSegmentStartDates() {
        LocalDate firstDayOfTheMonth = findFirstMonth();
        return getAllFirstDaysOfMonth(firstDayOfTheMonth, to);
    }

    @Override
    protected Map<LocalDate, Map<Category, Integer>> computeCategoryDistribution(Set<LocalDate> segmentStartDates, int duration) {
        Map<LocalDate, Map<Category, Integer>> categoryDistribution = new HashMap<>();
        for (LocalDate segmentStartDate : segmentStartDates) {
            duration = segmentStartDate.getMonth().maxLength(); // added line
            Map<Category, Integer> oneSegmentDistribution =
                    computeMinutesInCategoriesBetween(segmentStartDate, segmentStartDate.plusDays(duration -1));
            categoryDistribution.put(segmentStartDate, oneSegmentDistribution);
        }
        return categoryDistribution;
    }

    private Set<LocalDate> getAllFirstDaysOfMonth(LocalDate firstDayOfTheMonth, LocalDate to) {
        Set<LocalDate> allDates = new HashSet<>();
        int addedMonths = 0;
        LocalDate firstDay = firstDayOfTheMonth;
        do {
            firstDayOfTheMonth = firstDay.plusMonths(addedMonths++);
            allDates.add(firstDayOfTheMonth);
        } while (!firstDayOfTheMonth.isAfter(to));
        return allDates;
    }


    private LocalDate findFirstMonth() {
        int addedDays = 0;
        LocalDate firstDay = from;
        LocalDate firstDayOfTheMonth;
        do {
            firstDayOfTheMonth = firstDay.plusDays(addedDays++);
        } while (firstDayOfTheMonth.getDayOfMonth()!=1);
        return firstDayOfTheMonth;
    }
}
