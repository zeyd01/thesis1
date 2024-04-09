package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class Main {

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
}
