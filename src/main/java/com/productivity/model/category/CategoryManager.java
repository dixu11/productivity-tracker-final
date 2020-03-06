package com.productivity.model.category;

import com.productivity.model.FileRepository;
import com.productivity.model.record.Record;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

public class CategoryManager {
    private ObservableList<Category> categories;
    private final FileRepository<Category> fileRepository;

    public CategoryManager() {
        categories = FXCollections.observableArrayList();
        fileRepository = new FileRepository<>(new File(FileRepository.CATEGORY_FILE_NAME));
        loadData();
    }
    public void addCategory(Category category) {
        categories.add(category);
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }

    public void saveData() {
        fileRepository.save(categories);
    }

    private void loadData() {
        categories.addAll(fileRepository.load());
    }

    public boolean hasCategoryWith(String name) {
      return   categories.stream()
                .map(category -> category.getName())
                .anyMatch(existingName -> existingName.equalsIgnoreCase(name));
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }

}
