package org.example;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class DateUtil {
    private static final String DATE_FILE_NAME = "data/update_dates.properties";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static {
        // Ensure the data directory exists
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        // Ensure the properties file exists
        File dateFile = new File(DATE_FILE_NAME);
        if (!dateFile.exists()) {
            try {
                dateFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static LocalDate getLastUpdateDate(String key) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(DATE_FILE_NAME)) {
            properties.load(fis);
            String date = properties.getProperty(key);
            if (date != null) {
                return LocalDate.parse(date, formatter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setLastUpdateDate(String key, LocalDate date) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(DATE_FILE_NAME)) {
            properties.load(fis);
        } catch (IOException e) {
            // Ignore if the file does not exist yet
        }

        properties.setProperty(key, date.format(formatter));
        try (FileOutputStream fos = new FileOutputStream(DATE_FILE_NAME)) {
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
