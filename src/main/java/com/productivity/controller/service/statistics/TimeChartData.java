package com.productivity.controller.service.statistics;

import com.productivity.model.category.Category;
import javafx.scene.chart.XYChart;

import java.util.Map;
import java.util.stream.Collectors;

public class TimeChartData {

    private Map<String, Integer> stats;
    private int epochDay;

    public TimeChartData(int epochDay, Map<Category, Integer> stats) {
        this.stats = convertToCategoryNames(stats);
        this.epochDay = epochDay;
    }

    private Map<String,Integer> convertToCategoryNames(Map<Category, Integer> stats){
        return stats.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
    }

    public XYChart.Data<Number, Number> getAsChartDataFor(String categoryName) {
        return new XYChart.Data<>(epochDay, stats.get(categoryName));
    }

}
