package com.productivity;

import com.productivity.model.category.CategoryManager;
import com.productivity.model.record.RecordManager;
import com.productivity.view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

   private final RecordManager recordManager;
   private final CategoryManager categoryManager;

    public Launcher() {
        recordManager = new RecordManager();
        categoryManager = new CategoryManager();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        ViewFactory factory = new ViewFactory(recordManager, categoryManager);
        factory.showMainWindow();
    }

    @Override
    public void stop() throws Exception {
        categoryManager.saveData();
    }
}
