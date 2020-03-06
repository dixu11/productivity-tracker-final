package com.productivity.controller.service.statistics;

import com.productivity.model.category.Category;
import com.productivity.model.category.CategoryManager;
import com.productivity.model.record.Record;
import com.productivity.model.record.RecordManager;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

public abstract class TimeStatisticsGenerator {

    private CategoryManager categoryManager;
    private RecordManager recordManager;
    LocalDate from;
    LocalDate to;
    int segmentDurationInDays;

    protected TimeStatisticsGenerator(CategoryManager categoryManager, RecordManager recordManager, LocalDate from, LocalDate to, int segmentDurationInDays) {
        this.categoryManager = categoryManager;
        this.recordManager = recordManager;
        this.from = from;
        this.to = to;
        this.segmentDurationInDays = segmentDurationInDays;
    }

    public final List<TimeChartData> computeTimeChartDataDistribution() {
        //zebrać wszystkie daty -> tyle będzie segmentów
        Set<LocalDate> segmentStartDates = computeSegmentStartDates();
        //dla każdego segmentu (od do) wygenereować mapę kategoria - ilość
        Map<LocalDate, Map<Category, Integer>> categoryDistribution = computeCategoryDistribution(segmentStartDates, segmentDurationInDays);
        //stworzyć listę segmentów
        return buildTimeChartDataSequence(categoryDistribution);
    }

    protected abstract Set<LocalDate> computeSegmentStartDates();

    protected Map<LocalDate, Map<Category, Integer>> computeCategoryDistribution(Set<LocalDate> segmentStartDates, int duration) {
        Map<LocalDate, Map<Category, Integer>> categoryDistribution = new HashMap<>();
        for (LocalDate segmentStartDate : segmentStartDates) {
            Map<Category, Integer> oneSegmentDistribution =
                    computeMinutesInCategoriesBetween(segmentStartDate, segmentStartDate.plusDays(duration - 1));
            categoryDistribution.put(segmentStartDate, oneSegmentDistribution);
        }
        return categoryDistribution;
    }

    private List<TimeChartData> buildTimeChartDataSequence(Map<LocalDate, Map<Category, Integer>> categoryDistribution) {
        return categoryDistribution.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new TimeChartData(interpret(e.getKey()), e.getValue()))
                .collect(Collectors.toList());
    }

    private int interpret(LocalDate segmentStartDate) {
        return (int) segmentStartDate.getLong(ChronoField.EPOCH_DAY);
    }

    Map<Category, Integer> computeMinutesInCategoriesBetween(LocalDate from, LocalDate to) {
        List<Record> recordsInRange = recordManager.getAllByDate(from, to);
        return categoryManager.getCategories().stream()
                .collect(Collectors.toMap(category -> category, category -> totalMinutesForCategory(category, recordsInRange)));
    }

    private int totalMinutesForCategory(Category category, List<Record> records) {
        return records.stream()
                .filter(record -> record.getCategory().equals(category))
                .mapToInt(record -> record.getTime().getMinutes())
                .sum();
    }

    public static TimeStatisticsGenerator getGeneratorFor(CategoryManager categoryManager, RecordManager recordManager,
                                                          LocalDate from, LocalDate to, TimeChartMode mode) {
        switch (mode) {
            default:
            case DAYS:
                return new TimeStatisticsGeneratorForDays(categoryManager, recordManager, from, to, 1);
            case WEEKS:
                return new TimeStatisticGeneratorForWeeks(categoryManager, recordManager, from, to, 7);
            case MONTHS:
                return new TimeStatisticsGeneratorForMonths(categoryManager, recordManager, from, to, 30);
            case YEARS:
                return new TimeStatisticsGeneratorForYears(categoryManager, recordManager, from, to, 365);
        }
    }

}
