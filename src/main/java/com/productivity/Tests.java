package com.productivity;

import java.time.LocalDate;

import static java.time.temporal.ChronoField.EPOCH_DAY;

public class Tests {
    public static void main(String[] args) {
        LocalDate.now().datesUntil(LocalDate.now().plusDays(2)).forEach(d -> System.out.println(d));
        LocalDate test = LocalDate.now();
        System.out.println(test.getLong(EPOCH_DAY));
        System.out.println(LocalDate.ofEpochDay(test.getLong(EPOCH_DAY)));
    }
}
