package com.nphc.hr.services;

import com.opencsv.CSVReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.io.FileReader;
import java.io.IOException;

@Service
public class CsvReaderService {

    private static final Logger logger = LogManager.getLogger(CsvReaderService.class);

    public void readDataLineByLine(String file)
    {
        try(FileReader filereader = new FileReader(file);
            CSVReader csvReader = new CSVReader(filereader)){

            String[] nextRecord;

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    logger.info(cell + "\t");
                }
                logger.info("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
