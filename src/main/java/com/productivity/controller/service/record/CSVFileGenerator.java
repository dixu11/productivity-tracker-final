package com.productivity.controller.service.record;

import com.productivity.model.FileRepository;
import com.productivity.model.record.Record;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;

public class CSVFileGenerator extends Service<Void> {

    private RecordsToCSVConverter converter = new RecordsToCSVConverter();
    private FileRepository<Record> csvSavingRepository;

    public CSVFileGenerator(File file) {
        csvSavingRepository = new FileRepository<>(file);
    }

    private void saveRecordsToFile() {
        FileRepository<Record>  recordLoadingRepository = new FileRepository<>(new File(FileRepository.RECORDS_FILE_NAME) );
        String csv = converter.convertToCSV(recordLoadingRepository.load());
        csvSavingRepository.save(csv);
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                saveRecordsToFile();
                return null;
            }
        };
    }
}
