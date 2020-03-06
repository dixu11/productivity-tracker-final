package com.productivity.model.record;

import com.productivity.model.category.Category;
import com.productivity.model.category.CategoryType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;

//https://stackoverflow.com/questions/41265266/how-to-solve-inaccessibleobjectexception-unable-to-make-member-accessible-m/41265267
// -> problem with gson serialization and LocalDate

public class Record implements Serializable {

    private Category category;
    private LocalDate date;
    private String note;
    private Time time;

    public Record(Category category, LocalDate date, String note, Time time) {
        this.category = category;
        this.note = note;
        this.date = date;
        this.time = time;
//        this.date = date.minusDays(new Random().nextInt(7)); //debug
//        this.time = new Time(new Random().nextInt(100));
    }

    public String getNote() {
        return note;
    }

    public Time getTime() {
        return time;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }


    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public CategoryType getCategoryType() {
        return category.getType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return Objects.equals(category, record.category) &&
                Objects.equals(date, record.date) &&
                Objects.equals(note, record.note) &&
                Objects.equals(time, record.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, date, note, time);
    }

    @Override
    public String toString() {
        return "Record{" +
                "category=" + category +
                ", categoryType=" + category.getType() +
                ", date=" + date +
                ", note='" + note + '\'' +
                ", minutes=" + time +
                '}';
    }
}
