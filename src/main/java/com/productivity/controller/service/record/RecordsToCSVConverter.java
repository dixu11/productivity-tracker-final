package com.productivity.controller.service.record;

import com.productivity.model.record.Record;

import java.util.List;

public class RecordsToCSVConverter {

    public String convertToCSV(List<Record> load) {
        StringBuilder csv = new StringBuilder(createHeading() + "\n");
        for (Record record : load) {
            csv.append(convertToCSV(record)).append("\n");
        }
        return csv.toString();
    }

    private String convertToCSV(Record record) {
        return String.format("%s,%s,%s,%s,%s",
                record.getCategory().getName(),
                record.getCategory().getType(),
                record.getDate(),
                record.getNote(),
                record.getTime().getMinutes());
    }

    public String createHeading() {
        return "category,categoryType,date,note,minutes";
    }
}
