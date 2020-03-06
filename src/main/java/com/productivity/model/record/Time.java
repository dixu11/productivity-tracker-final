package com.productivity.model.record;

import java.io.Serializable;
import java.util.Objects;

public class Time implements Comparable<Time>, Serializable {

    private int minutes;

    public Time(int minutes) {
        this.minutes = minutes;
    }

    @Override
    public int compareTo(Time o) {
        return minutes - o.minutes;
    }

    @Override
    public String toString() {
        if (minutes < 60) {
            return minutes + "min";
        }
        int hours = minutes / 60;
        String add0 = "";
        if (minutes % 60 < 10) {
            add0 += "0";
        }
        return String.format("%d:%sh", hours, add0 + minutes % 60);
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return minutes == time.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutes);
    }
}
