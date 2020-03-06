package com.productivity.controller;

import com.productivity.controller.service.category.CategoryAddingResult;
import com.productivity.controller.service.category.CategoryAddingService;
import com.productivity.model.category.Category;
import com.productivity.model.category.CategoryManager;
import com.productivity.model.category.CategoryType;
import com.productivity.model.record.RecordManager;
import com.productivity.view.DialogsFactory;
import com.productivity.view.ViewFactory;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CategorySettingsController extends  BaseController {

    public CategorySettingsController(RecordManager recordManager, CategoryManager categoryManager, ViewFactory viewFactory, String fxmlName) {
        super(recordManager, categoryManager, viewFactory, fxmlName);
    }

    @FXML
    private ListView<Category> categoriesList;

    @FXML
    private TextField addCategoryText;

    @FXML
    private RadioButton productiveRadio;

    @FXML
    private ToggleGroup group1;

    @FXML
    private RadioButton breakRadio;

    @FXML
    private RadioButton freeTimeRadio;

    @FXML
    private TextField toRemoveText;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCategoriesList();
        initCategoriesSelectionAction();
        initRadioButtons();
    }

    private void initCategoriesSelectionAction() {
        categoriesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Category>() {
            @Override
            public void changed(ObservableValue<? extends Category> observable, Category oldValue, Category newValue) {
                if (Objects.nonNull(newValue)) {
                    toRemoveText.setText(newValue.getName());
                }
            }
        });
    }


    private void initRadioButtons() {
        breakRadio.setUserData(CategoryType.BREAK);
        freeTimeRadio.setUserData(CategoryType.FREE_TIME);
        productiveRadio.setUserData(CategoryType.PRODUCTIVE);
    }

    private void initCategoriesList() {
        categoriesList.getItems().addAll(categoryManager.getCategories());
    }

    @FXML
    void addAction() {
        Category category = new Category(addCategoryText.getText(), (CategoryType) group1.getSelectedToggle().getUserData());
        CategoryAddingService service = new CategoryAddingService(category, categoryManager);
        service.start();
        service.setOnSucceeded(event -> interpretAddingResult(service.getValue(), category));
    }

    private void interpretAddingResult(CategoryAddingResult result, Category category) {
        DialogsFactory dialogs = new DialogsFactory();
        String problemReason = "category name";
        switch (result) {
            case SUCCESS:
                categoriesList.getItems().add(category);
                addCategoryText.clear();
                break;
            case SHOULD_HAVE_NAME:
                String noNameMessage = "Category should have a name!";
                dialogs.showErrorDialog(problemReason, noNameMessage);
                break;
            case DUPLICATED:
                String duplicateMessage = "Category with this name already exists.";
                dialogs.showErrorDialog(problemReason, duplicateMessage);
                break;
        }
    }

    @FXML
    void removeAction() {
        //fixme also refreshes charts, asks if you shure and removes all records of that category
        Category selected = categoriesList.getSelectionModel().getSelectedItem();
        categoriesList.getItems().remove(selected);
        categoryManager.removeCategory(selected);
        toRemoveText.clear();
    }


}

//poprawna kategoria jeśli: nie ma jeszcze takiej, ma wpisane dane