package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Main {
    public static final String DATA_FOLDER_NAME = "data";
    public static void main(String[] args) {}

    public static void chooseFileButton() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Excel File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xlsx", "xls");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            // Get the current date and format it as "MonthYear"
            String formattedDate = new SimpleDateFormat("MMMM-yyyy").format(new Date());
            String fixedArffFilename = formattedDate + ".arff";

            // Define the fixed location for saving the ARFF file
            File dataFolder = new File(DATA_FOLDER_NAME);
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();  // Create the directory if it does not exist
            }
            File saveFile = new File(dataFolder, fixedArffFilename);
            String arffFilePath = saveFile.getAbsolutePath();

            // Convert the Excel file to ARFF and save it to the fixed location
            dataconverter.convertExcelToArff(filePath, arffFilePath);
        }
    }

    public static void fetchAndSaveYearlyWeatherData() {
        weatherhistoryAPI api = new weatherhistoryAPI();
        try {
            // Construct the file path for saving yearly weather data
            String filePath = Paths.get(DATA_FOLDER_NAME, "weather_data_" + LocalDate.now().getYear() + ".arff").toString();
            api.fetchAndSaveWeatherData("38.680969", "39.226398", LocalDate.now().getYear());
            System.out.println("Yearly weather data fetched and saved successfully!");
        } catch (IOException ex) {
            System.err.println("Failed to fetch yearly weather data: " + ex.getMessage());
        }
    }


    public static void fetchAndSaveMonthlyWeatherData() {
        weatherAPImonthly api = new weatherAPImonthly();
        try {
            // Construct the file path for saving monthly weather data
            String filePath = Paths.get(DATA_FOLDER_NAME, "weather_data_" + LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + ".arff").toString();
            api.fetchAndSaveWeatherData("38.680969", "39.226398");
            System.out.println("Monthly weather data fetched and saved successfully!");
        } catch (IOException ex) {
            System.err.println("Failed to fetch monthly weather data: " + ex.getMessage());
        }
    }
}


