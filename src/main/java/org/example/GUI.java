package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.example.Main.DATA_FOLDER_NAME;

public class GUI {
    private JButton dosyaSeçButton;
    private JLabel aylıkStokKartıYüklemesiTextField;
    private JLabel hastaneVeriTabanıMakineTextField;
    private JButton güncelleButton;
    private JLabel havaDurumuVerisiGüncelleTextField;
    private JLabel yıllıkHavaDurumuVerisiTextField;
    private JButton ekleButton;
    private javax.swing.JPanel JPanel;
    private JLabel havaDurumuT;
    private JLabel stokTarihi;
    private JLabel bolge;
    private JButton alışverişListesiniOluşturButton;
    private static final String DOSYA_SEC_KEY = "dosya_sec";
    private static final String GUNCELLE_KEY = "guncelle";
    private static final String EKLE_KEY = "ekle";
    private boolean dosyaSec = false;
    private boolean guncelle = false;
    private JFrame frame;

    public GUI() {

        frame = new JFrame("Main GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this.JPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setSize(700, 400);

        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM-yyyy"));
        String dataFileName = DATA_FOLDER_NAME + "/weather_data_" + formattedDate + ".arff";
        File dataFile = new File(dataFileName);
        if (dataFile.exists()) {
            ekleButton.setEnabled(false); // Disable the button if the data file already exists
        }

        ekleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.fetchAndSaveYearlyWeatherData();
                ekleButton.setEnabled(false);
                DateUtil.setLastUpdateDate(EKLE_KEY, LocalDate.now());
                checkAllActionsCompleted();
            }
        });

        güncelleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.fetchAndSaveMonthlyWeatherData();
                havaDurumuT.setText("Son Güncelleme Tarihi: " + LocalDate.now());
                DateUtil.setLastUpdateDate(GUNCELLE_KEY, LocalDate.now());
                guncelle = true; // Update guncelle status
                checkAllActionsCompleted();
            }
        });

        dosyaSeçButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Main.chooseFileButton();
                stokTarihi.setText("Son Güncelleme Tarihi: " + LocalDate.now());
                DateUtil.setLastUpdateDate(DOSYA_SEC_KEY, LocalDate.now());
                dosyaSec = true; // Update dosyaSec status
                checkAllActionsCompleted();
            }
        });

        alışverişListesiniOluşturButton.setEnabled(false); // Initially set to false
        alışverişListesiniOluşturButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current frame
                new ShopList();
            }
        });

        checkAndSetButtonStates(); // Ensure button states are set correctly on initialization
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI().frame.setVisible(true);
            }
        });
    }

    public void checkAndSetButtonStates() {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastDosyaSecDate = DateUtil.getLastUpdateDate(DOSYA_SEC_KEY);
        LocalDate lastGuncelleDate = DateUtil.getLastUpdateDate(GUNCELLE_KEY);
        LocalDate lastEkleDate = DateUtil.getLastUpdateDate(EKLE_KEY);

        dosyaSec = currentDate.equals(lastDosyaSecDate);
        guncelle = currentDate.equals(lastGuncelleDate);

        checkAllActionsCompleted();
    }

    public void checkAllActionsCompleted() {
        alışverişListesiniOluşturButton.setEnabled(dosyaSec && guncelle);
    }
}
