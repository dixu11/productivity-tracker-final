package com.productivity.controller.service.statistics;

import com.productivity.controller.MainWindowController;
import com.productivity.model.category.CategoryManager;
import com.productivity.model.record.RecordManager;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TimeChartUpdater extends Service<Map<String, List<XYChart.Data<Number, Number>>>> {

    private MainWindowController controller;
    private CategoryManager categoryManager;
    private RecordManager recordManager;

    public TimeChartUpdater(MainWindowController controller, CategoryManager categoryManager, RecordManager recordManager) {
        this.controller = controller;
        this.categoryManager = categoryManager;
        this.recordManager = recordManager;
    }

    private Map<String, List<XYChart.Data<Number, Number>>> update() {
        List<TimeChartData> timeChartData = generateNewData();
        Map<String, List<XYChart.Data<Number, Number>>> segments = createChartSegments(timeChartData);
        updateXYAxis(segments);
        return segments;
    }

    private void updateXYAxis(Map<String, List<XYChart.Data<Number, Number>>> segments) {
        NumberAxis yAxis = (NumberAxis) controller.getTimeChart().getYAxis();
        NumberAxis xAxis = (NumberAxis) controller.getTimeChart().getXAxis();
        double maxMinutes = computeMaxMinutesWithOffset(segments,30);
        yAxis.setUpperBound(maxMinutes);
        int minEpoch = computeMinEpoch(segments);
        int maxEpoch = computeMaxEpoch(segments);
        xAxis.setLowerBound(minEpoch);
        xAxis.setUpperBound(maxEpoch);
    }

    private double computeMaxMinutesWithOffset(Map<String, List<XYChart.Data<Number, Number>>> segments , double offsetInMinutes){ ;
        int maxMinutes = computeMaxMinutes(segments);
        return maxMinutes  + offsetInMinutes;
    }

    private int computeMinEpoch(Map<String, List<XYChart.Data<Number, Number>>> segments){
        return getDataStream(segments)
                .mapToInt(data -> data.getXValue().intValue())
                .min()
                .orElse(0);
    }

    private int computeMaxEpoch(Map<String, List<XYChart.Data<Number, Number>>> segments){
       return getDataStream(segments)
                .mapToInt(data -> data.getXValue().intValue())
                .max()
                .orElse(0);
    }

    private int computeMaxMinutes(Map<String, List<XYChart.Data<Number, Number>>> segments) {
      return   getDataStream(segments)
                .mapToInt(data -> data.getYValue().intValue())
                .max()
                .orElse(0);
    }

    private Stream<XYChart.Data<Number, Number>> getDataStream(Map<String, List<XYChart.Data<Number, Number>>> segments) {
        return segments.values().stream()
                  .flatMap(Collection::stream);
    }

    private List<TimeChartData> generateNewData() {
        TimeStatisticsGenerator statistics = TimeStatisticsGenerator.getGeneratorFor(
                categoryManager, recordManager, controller.getFromDate(), controller.getToDate(), controller.getTimeChartMode());
        return statistics.computeTimeChartDataDistribution();
    }

    private Map<String, List<XYChart.Data<Number, Number>>> createChartSegments(List<TimeChartData> timeChartData) {
        var allSeries = new HashSet<>(controller.getTimeChart().getData()) ;
        Map<String, List<XYChart.Data<Number, Number>>> categoryNameAndNewContent = new HashMap<>();
        for (XYChart.Series<Number, Number> series : allSeries) {
            categoryNameAndNewContent.put(series.getName(), getSeriesContent(series.getName(), timeChartData));
        }
        return categoryNameAndNewContent;
    }


    private List<XYChart.Data<Number, Number>> getSeriesContent(String categoryName, List<TimeChartData> timeChartData) {
        return timeChartData.stream()
                .map(data -> data.getAsChartDataFor(categoryName))
                .collect(Collectors.toList());
    }

    @Override
    protected Task<Map<String, List<XYChart.Data<Number, Number>>>> createTask() {
        return new Task<>() {
            @Override
            protected Map<String, List<XYChart.Data<Number, Number>>> call() throws Exception {
                return update();
            }
        };
    }
}
