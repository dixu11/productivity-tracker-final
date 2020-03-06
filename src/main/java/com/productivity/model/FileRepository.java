package com.productivity.model;


import com.productivity.model.record.Record;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FileRepository<E> {
    private final String fileName;

    public FileRepository(String fileName) {
        this.fileName = fileName;
    }

    public void save(ObservableList<E> data) {
        save(new ArrayList<>(data));
    }

    private void save(List<E> data) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<E> load() {
        List<E> data = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            data = (ArrayList<E>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            System.out.println("No previous data to load");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void saveNew(E element) {
        List<E> oldList = load();
        oldList.add(element);
        save(oldList);
    }

    public void remove(E element) {
        List<E> oldList = load();
        oldList.remove(element);
        save(oldList);
    }
}
