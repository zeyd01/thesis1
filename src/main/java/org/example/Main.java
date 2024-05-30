package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;

public class Main {
    private static final String DATA_FOLDER_NAME = "data";
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold the button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        frame.getContentPane().add(buttonPanel);

        JButton chooseFileButton = new JButton("Choose Excel File");
        chooseFileButton.setPreferredSize(new Dimension(150, 50)); // Set button size
        // Add an empty border around the button to control its size
        chooseFileButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chooseFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose Excel File");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xlsx", "xls");
            fileChooser.setFileFilter(filter);

            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();

                // Prompt user to choose save location for the ARFF file
                JFileChooser saveFileChooser = new JFileChooser();
                saveFileChooser.setDialogTitle("Choose Save Location");
                saveFileChooser.setSelectedFile(new File("output.arff"));
                int saveReturnValue = saveFileChooser.showSaveDialog(null);
                if (saveReturnValue == JFileChooser.APPROVE_OPTION) {
                    File saveFile = saveFileChooser.getSelectedFile();
                    String arffFilePath = saveFile.getAbsolutePath();
                    dataconverter.convertExcelToArff(filePath, arffFilePath);
                }
            }
        });

        // Add the button to the panel
        buttonPanel.add(chooseFileButton);




        frame.setSize(400, 200); // Set frame size
        frame.setLocationRelativeTo(null); // Open frame in the middle of the screen
        frame.setVisible(true);
    }
    // ELAZIĞ
    // Method to fetch and save yearly weather data using weatherhistoryAPI
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

    // Method to fetch and save monthly weather data using weatherAPImonthly
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
