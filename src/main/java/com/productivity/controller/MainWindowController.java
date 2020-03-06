package com.productivity.controller;

import com.productivity.controller.service.statistics.*;
import com.productivity.model.category.Category;
import com.productivity.model.category.CategoryManager;
import com.productivity.model.record.RecordManager;
import com.productivity.controller.service.record.RecordAddingResult;
import com.productivity.controller.service.record.RecordAddingService;
import com.productivity.model.record.Record;
import com.productivity.model.category.CategoryType;
import com.productivity.model.record.Time;
import com.productivity.view.ViewFactory;
import com.productivity.view.axisModes.TimeChartXAxisMode;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

public class MainWindowController extends BaseController {


    private static final CategoryType INITIAL_RECORD_TYPE = CategoryType.PRODUCTIVE;

    @FXML
    private ChoiceBox<Category> recordTypeSelect;

    @FXML
    private TextField recordTimeHourField;

    @FXML
    private TextField recordTimeMinField;

    @FXML
    private TableView<Record> recordTable;

    @FXML
    private TableColumn<Record, LocalDate> dateCol;

    @FXML
    private TableColumn<Record, Category> categoryCol;

    @FXML
    private TableColumn<Record, CategoryType> categoryTypeCol;

    @FXML
    private TableColumn<Record, Time> timeCol;

    @FXML
    private TableColumn<Record, String> noteCol;

    @FXML
    private TextField goalField;

    @FXML
    private TextArea recordNoteArea;

    @FXML
    private Label errorLabel;

    @FXML
    private LineChart<Number, Number> timeChart;

    @FXML
    private PieChart categoryChart;

    @FXML
    private Tab timeTab;

    @FXML
    private Tab categoryTab;

    @FXML
    private DatePicker fromDate;

    @FXML
    private DatePicker toDate;


    public MainWindowController(RecordManager recordManager, CategoryManager categoryManager, ViewFactory viewFactory, String fxmlName) {
        super(recordManager, categoryManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpTableView();
        setUpRecordTimeSelecting();
        setUpDatePickers();
        setUpTimeChartAxises();
        filterAndUpdateRecords();
    }

    private void setUpTableView() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
        categoryTypeCol.setCellValueFactory(new PropertyValueFactory<>("categoryType"));
        recordTable.setItems(recordManager.getRecords());
    }

    private void setUpRecordTimeSelecting() {
        ObservableList<Category> categories = categoryManager.getCategories();
        recordTypeSelect.setItems(categories);
        if (!categories.isEmpty()) {
            recordTypeSelect.setValue(categories.get(0));
        }
    }

    private void setUpDatePickers() {
        fromDate.setValue(LocalDate.now().minusWeeks(1));
        toDate.setValue(LocalDate.now());
    }

    private void setUpTimeChartAxises() {
        prepareTimeChartYAxis();
        prepareTimeChartXAxis();
    }
    private void prepareTimeChartXAxis() {
        NumberAxis axis = (NumberAxis) timeChart.getXAxis();
        axis.setAutoRanging(false);
        axis.setMinorTickVisible(false);
    }

    private void prepareTimeChartYAxis() {
        NumberAxis axis = (NumberAxis) timeChart.getYAxis();

        axis.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(Math.round(object.doubleValue() / 60));
            }

            @Override
            public Number fromString(String string) {
                return Double.parseDouble(string) * 60;
            }
        });
        axis.setAutoRanging(false);
        axis.setMinorTickVisible(false);
        axis.setTickUnit(60);
        axis.setLabel("Hours");
    }

    @FXML
    void addAction() {
        if (!fieldsAreValid()) return;
        int minutes = getAsNumber(recordTimeMinField.getText());
        minutes += getAsNumber(recordTimeHourField.getText()) * 60;
        Record record = new Record(recordTypeSelect.getValue(), LocalDate.now(),
                recordNoteArea.getText(), new Time(minutes));
        RecordAddingService service = new RecordAddingService(record, recordManager);
        service.start();
        service.setOnSucceeded(event -> {
            RecordAddingResult result = service.getValue();
            switch (result) {
                case SUCCESS:
                    errorLabel.setTextFill(Color.GREEN);
                    if (errorLabel.getText().equals("Record added!")) {
                        errorLabel.setText("Next record added!");
                    } else {
                        errorLabel.setText("Record added!");
                    }
                    filterAndUpdateRecords();
                    return;
                case WRONG_TIME_RANGE:
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText("Time range: 1min to 24h!");
                    return;
                case NO_CATEGORY:
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText("No category selected!");
            }
        });
    }


    private int getAsNumber(String data) {
        if (data.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(data);
    }

    @FXML
    void clearAction() {
        recordNoteArea.setText("");
        recordTimeHourField.setText("");
        recordTimeMinField.setText("");
        errorLabel.setText("");
    }

    private boolean fieldsAreValid() {
        String hourString = recordTimeHourField.getText();
        String minString = recordTimeMinField.getText();

        if (hourString.isEmpty() && minString.isEmpty()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Please fill minutes / hours");
            return false;
        }
        Pattern onlyNumbers = Pattern.compile("\\d*");
        if (!onlyNumbers.matcher(hourString).matches() || !onlyNumbers.matcher(minString).matches()) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Only numbers!");
            return false;
        }
        // wybierz typ
        return true;
    }

    @FXML
    void recordKeyAction(KeyEvent event) {
        if (event.getCode() != KeyCode.DELETE) return;
        Record selected = recordTable.getSelectionModel().getSelectedItem();
        recordManager.removeRecord(selected);
        filterAndUpdateRecords();
    }

    @FXML
    void editTypeAction() {
        viewFactory.showCategorySettings();

    }

    @FXML
    void toAction() {
        filterAndUpdateRecords();
    }

    @FXML
    void fromAction() {
        filterAndUpdateRecords();
    }

    @FXML
    void closeAction() {

    }

    @FXML
    void lockAction() {

    }

    @FXML
    void optionsAction() {

    }

    @FXML
    void saveAction() {

    }



    void filterAndUpdateRecords() {
        filterRecords();
        updateCharts();
    }

    private void updateCharts() {
        updateCategoryChart();
        updateTimeChart();
    }

    private void updateCategoryChart() {
        CategoryStatisticsGenerator statistics = new CategoryStatisticsGenerator(recordManager, categoryManager);
        statistics.start();
        statistics.setOnSucceeded(event -> finalizeCategoryChartUpdate(statistics));
    }

    private void finalizeCategoryChartUpdate(CategoryStatisticsGenerator statistics) {
        Map<Category, Double> result = statistics.getValue();
        for (Category category : result.keySet()) {
            PieChart.Data newData = new PieChart.Data(category.getName(), result.get(category));
            updateCategoryChartData(newData);
        }
    }

    private void updateCategoryChartData(PieChart.Data newData) {
        Optional<PieChart.Data> oldData = findCategoryChartDataByName(newData.getName());

        if (oldData.isPresent()) {
            oldData.get().setPieValue(newData.getPieValue());
        } else {
            categoryChart.getData().add(newData);
        }

    }

    private Optional<PieChart.Data> findCategoryChartDataByName(String name) {
        return categoryChart.getData()
                .stream()
                .filter(data -> data.getName().equals(name))
                .findAny();
    }

    private void filterRecords() {
        LocalDate from = fromDate.getValue();
        LocalDate to = toDate.getValue();
        recordManager.filterByDate(from, to);
    }

    private void updateTimeChart() {
        updateTimeChartSeries();
        updateTimeChartXAxisMode();
        //removeOldSeries(); //todo
        TimeChartUpdater updater = new TimeChartUpdater(this, categoryManager, recordManager);
        updater.start();
        updater.setOnSucceeded(e-> addComputedSeriesData(updater));
    }

    private void updateTimeChartXAxisMode() {
        TimeChartMode mode = getTimeChartMode();
        NumberAxis xAxis = (NumberAxis) timeChart.getXAxis();
        xAxis.setLabel(mode.getName());
        TimeChartXAxisMode axisMode = TimeChartXAxisMode.getFormatter(mode);
        StringConverter<Number> converter = axisMode.getConverter();
        xAxis.setTickLabelFormatter(converter);
        xAxis.setTickUnit(axisMode.getPeriodInDays());
    }

    private void addComputedSeriesData(TimeChartUpdater updater) {
        Map<String, List<XYChart.Data<Number, Number>>> newData = updater.getValue();
        for (String categoryName : newData.keySet()) {
            var series = findSeriesWithName(categoryName);
           series.ifPresent(s -> {
               s.getData().clear();
               s.getData().addAll(newData.get(categoryName));
           });
        }

    }

    private Optional<XYChart.Series<Number, Number>> findSeriesWithName(String name) {
      return   timeChart.getData().stream()
                .filter(series -> series.getName().equalsIgnoreCase(name))
                .findAny();
    }

    private void updateTimeChartSeries() {
        var allSeries = timeChart.getData();
        categoryManager.getCategories().stream()
                .filter(category -> !containsSeriesForThisCategory(category))
                .forEach(category -> {
                    XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();
                    newSeries.setName(category.getName());
                    allSeries.add(newSeries);
                });
    }

    private boolean containsSeriesForThisCategory(Category category) {
        ObservableList<XYChart.Series<Number, Number>> allSeries = timeChart.getData();
        return allSeries.stream()
                .anyMatch(series -> series.getName().equalsIgnoreCase(category.getName()));
    }

  public    TimeChartMode getTimeChartMode() {
        LocalDate from = fromDate.getValue();
        LocalDate to = toDate.getValue();
        TimeChartModePicker modePicker = new TimeChartModePicker();
        return modePicker.determineMode(from, to);
    }

    public LocalDate getToDate() {
        return toDate.getValue();
    }

    public LocalDate getFromDate() {
        return fromDate.getValue();
    }

    public LineChart<Number, Number> getTimeChart() {
        return timeChart;
    }
}
