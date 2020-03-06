package com.productivity.controller.service.statistics;

import com.productivity.model.category.CategoryManager;
import com.productivity.model.record.RecordManager;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Set;
import java.util.stream.Collectors;

public class TimeStatisticsGeneratorForDays extends TimeStatisticsGenerator {


    protected TimeStatisticsGeneratorForDays(CategoryManager categoryManager, RecordManager recordManager, LocalDate from, LocalDate to, int segmentDurationInDays) {
        super(categoryManager, recordManager, from, to, segmentDurationInDays);
    }

    @Override
    protected Set<LocalDate> computeSegmentStartDates() {
        return from.datesUntil(to.plusDays(segmentDurationInDays))
                .collect(Collectors.toSet());
    }

}
