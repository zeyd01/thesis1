package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.*;

public class dataconverter {

    public static void convertExcelToArff(String excelFilePath, String arffFilePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(excelFilePath);

            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet

            FileWriter fileWriter = new FileWriter(arffFilePath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            String relationName = new File(excelFilePath).getName();

            int extensionIndex = relationName.lastIndexOf('.');
            if (extensionIndex != -1) {
                relationName = relationName.substring(0, extensionIndex);
            }

            bufferedWriter.write("@relation " + relationName + "\n\n");

            // Write fixed attribute headers
            bufferedWriter.write("@ATTRIBUTE code string\n");
            bufferedWriter.write("@ATTRIBUTE malzemeadi string\n");
            bufferedWriter.write("@ATTRIBUTE cikistop numeric\n");
            bufferedWriter.write("@ATTRIBUTE currentstock numeric\n");
            bufferedWriter.write("@ATTRIBUTE averageusage numeric\n\n");

            // Write data
            bufferedWriter.write("@data\n");
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                StringBuilder rowValues = new StringBuilder();

                // Write cell values
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            rowValues.append(cell.getNumericCellValue());
                        } else if (cell.getCellType() == CellType.STRING) {
                            String cellValue = cell.getStringCellValue();
                            // Remove spaces and special characters
                            cellValue = cellValue.replaceAll("[\\s()±*'%+-,]", "")
                                    .replaceAll("Ü", "U")
                                    .replaceAll("Ö", "O")
                                    .replaceAll("Ç", "C")
                                    .replaceAll("Ş", "S")
                                    .replaceAll("İ", "I")
                                    .replaceAll("Ğ", "G");
                            // Append comma if it's not the last column
                            if (j < row.getLastCellNum() - 1) {
                                rowValues.append(cellValue).append(",");
                            } else {
                                rowValues.append(cellValue);
                            }
                        }
                    }
                }

                bufferedWriter.write(rowValues + "\n");
            }

            bufferedWriter.close();
            fileWriter.close();
            workbook.close();

            JOptionPane.showMessageDialog(null, "Excel file converted to ARFF successfully. ARFF file saved at:\n" + arffFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
