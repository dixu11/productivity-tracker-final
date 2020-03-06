package com.productivity.view.axisModes;

import com.productivity.controller.service.statistics.TimeChartMode;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class TimeChartXAxisMode {
    public abstract StringConverter<Number> getConverter();

    private double periodInDays;

    public TimeChartXAxisMode(double periodInDays) {
        this.periodInDays = periodInDays;
    }

    public double getPeriodInDays() {
        return periodInDays;
    }

    public static TimeChartXAxisMode getFormatter(TimeChartMode mode) {
        switch (mode) {
            case DAYS:
                return new TimeChartXAxisModeForDays();
            case WEEKS:
                return new TimeChartXAxisModeForWeeks();
            case MONTHS:
                return new TimeChartXAxisModeForMonths();
            case YEARS:
                return new TimeChartXAxisModeForYears();
        }
        return new TimeChartXAxisModeForDays();
    }
}

class TimeChartXAxisModeForDays extends TimeChartXAxisMode {

    public TimeChartXAxisModeForDays() {
        super(1);
    }

    @Override
    public StringConverter<Number> getConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Number object) {
                LocalDate date = LocalDate.ofEpochDay(object.intValue());
                return date.format(DateTimeFormatter.ofPattern("dd.MM"));
            }

            @Override
            public Number fromString(String string) {
                return Integer.parseInt(string);
            }
        };
    }
}

class TimeChartXAxisModeForWeeks extends TimeChartXAxisMode {
    public TimeChartXAxisModeForWeeks() {
        super(7);
    }

    @Override
    public StringConverter<Number> getConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Number object) {
                LocalDate date = LocalDate.ofEpochDay(object.intValue());
                return date.format(DateTimeFormatter.ofPattern("dd.MM"));
            }

            @Override
            public Number fromString(String string) {
                return Integer.parseInt(string);
            }
        };
    }
}

class TimeChartXAxisModeForMonths extends TimeChartXAxisMode {
    public TimeChartXAxisModeForMonths() {
        super(30.42);
    }

    @Override
    public StringConverter<Number> getConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Number object) {
                LocalDate date = LocalDate.ofEpochDay(object.intValue());
                return date.getMonth().toString();
            }

            @Override
            public Number fromString(String string) {
                return Integer.parseInt(string);
            }
        };
    }
}

class TimeChartXAxisModeForYears extends TimeChartXAxisMode {
    public TimeChartXAxisModeForYears() {
        super(365.25);
    }

    @Override
    public StringConverter<Number> getConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Number object) {
                LocalDate date = LocalDate.ofEpochDay(object.intValue());
                return String.valueOf(date.getYear());
            }

            @Override
            public Number fromString(String string) {
                return Integer.parseInt(string);
            }
        };
    }
}