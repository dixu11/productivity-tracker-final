package com.productivity.controller.service.statistics;

import com.productivity.model.category.Category;
import com.productivity.model.category.CategoryManager;
import com.productivity.model.record.Record;
import com.productivity.model.record.RecordManager;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryStatisticsGenerator extends Service<Map<Category, Double>> {

    private RecordManager recordManager;
    private CategoryManager categoryManager;

    public CategoryStatisticsGenerator(RecordManager recordManager, CategoryManager categoryManager) {
        this.recordManager = recordManager;
        this.categoryManager = categoryManager;
    }

    private Map<Category, Double> computeCategoryDistribution() {
        ObservableList<Record> records = recordManager.getRecords();
        ObservableList<Category> categories = categoryManager.getCategories();
        Map<Category, Integer> minutesInCategories = categories.stream()
                .collect(Collectors.toMap(category -> category, category -> totalMinutesForCategory(category, records)));
        double allMinutes = getSum(minutesInCategories);
        double allMinutesCorrected = eliminate0Value(allMinutes);
        Map<Category, Double> collect = minutesInCategories.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue() * 100 / allMinutesCorrected));
        return collect;
    }

    private double eliminate0Value(double allMinutes) {
        if (allMinutes<1) {
            return .00000000001;
        }
        return allMinutes;
    }

    private int getSum(Map<Category, Integer> minutesInCategories) {
        return minutesInCategories.values()
                .stream()
                .mapToInt(num -> num)
                .sum();
    }


    private int totalMinutesForCategory(Category category, List<Record> records) {
        return records.stream()
                .filter(record -> record.getCategory().equals(category))
                .mapToInt(record -> record.getTime().getMinutes())
                .sum();
    }


    @Override
    protected Task<Map<Category, Double>> createTask() {
        return new Task<>() {
            @Override
            protected Map<Category, Double> call() throws Exception {
                return computeCategoryDistribution();
            }
        };
    }
}
