package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShopList {
    private JLabel title;
    private JButton sepetiAktarButton;
    private JPanel panel;
    private JLabel list;
    private JFrame frame;
    private JPanel textFieldsPanel;
    private JPanel contentPanel;

    public ShopList() {
        frame = new JFrame("ShopList");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        sepetiAktarButton.setVisible(true);
        frame.setContentPane(this.panel);
        frame.pack();

        String [] code = {
                "150-03-03-270002210",
                "150-03-03-270002376",
                "150-03-03-270002375",
                "150-03-03-210006454",
                "150-03-03-270000123",
                "150-03-03-270012286",
                "150-03-03-270001881",
                "150-03-03-270001877",
                "150-03-03-210113908",
                "150-03-03-270002433",
                "150-03-03-270000173",
                "150-03-03-270000154",
                "150-03-03-270000154",
                "150-03-03-270000005",
                "150-03-03-270000005",
                "150-03-03-210032948",
                "150-03-03-270001593",
                "150-03-03-270001595",
                "150-03-03-210032946",
                "150-03-03-270001601",
                "150-03-03-270001609",
                "150-03-03-270001593",
                "150-03-03-270001605",
                "150-03-03-270001609",
                "150-03-03-270006568",
                "150-03-03-270001568",
                "150-03-03-270002636",
                "150-03-03-270002755",
                "150-03-03-270002434",
                "150-03-03-270001776",
                "150-03-03-270001583"

        };

        String [] name= {
                "ABESLANG DİL BASACAĞI, TAHTA, NONSTERİL",
                "ALKOL",
                "ANESTEZİ DEVRESİ, SU TUTUCUSUZ, YETİŞKİN (+ 0,8 M UZATMA)",
                "ANTİSEPTİK SOLÜSYON, YARA YIKAMA, 250ML",
                "ASPİRASYON KATETERİ, NO:12 ( DMO )",
                "ASPİRASYON KATETERİ, NO:14",
                "ASPİRASYON TORBASI 2000 ML",
                "AİR-WAY, ORAL, NO:3",
                "AİR-WAY, ORAL, NO:4 SARI ",
                "BAKTERİ FİLTRESİ HMEF FİLTRELİ -ELİTE",
                "BAKTERİ VENTİLATÖR FİLTRESİ, BAKTERİ/VİRUS, NEM TUTUCULU (HMEF), YETİŞKİN",
                "BASINÇ ÖLÇÜM SETİ, TRANSDUSERLİ, TEKLİ",
                "BASINÇ İZLEME SETİ, TEKLİ (TRANSDUSER)",
                "BATIKON 1000 ML",
                "BATIKON POLIVIDON IYOT1000 ML",
                "BATIKON POLIVIDON IYOT1000 ML",
                "BAĞLANTI TÜPÜ, Y-TUR (İRRİGASYON) SETİ, PUARLI",
                "BAĞLANTI TÜPÜ, Y-TUR (İRRİGASYON) SETİ, PUARSIZ",
                "BRONKOSKOPİK MUKUS EKSTRAKTÖR 20 ML TRAKEAL ASPİRASYON KÜLTÜR SETİ",
                "BRONŞİYAL LAVAJ KABI, 25ML",
                "BÖBREK KÜVET, PLASTİK",
                "BİSTURİ UCU NO:15",
                "BİSTÜRİ UCU NO:10",
                "BİSTÜRİ UCU NO:11",
                "BİSTÜRİ UCU NO:12 PLASMED",
                "BİSTÜRİ UCU NO:15 -DMO",
                "BİSTÜRİ UCU NO:22",
                "BİSTÜRİ UCU, KARBON ÇELİK, NO:10",
                "BİSTÜRİ UCU, KARBON ÇELİK, NO:20",
                "BİSTÜRİ UCU, KARBON ÇELİK, NO:22",
                "BİYOPSİ İĞNESİ, TABANCAYA UYUMLU 18G, 10CM",
                "BİYOPSİ İĞNESİ, TABANCAYA UYUMLU 18G, 15(±2)CM",
                "CERRAHİ ELDİVEN LATEKS PUDRASIZ STERİL NO:6,5",
                "CERRAHİ ELDİVEN PUDRALI, NO:7"
        };
        String [] numbers= {
                "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0", "0", "8", "15", "30", "35",
                "40", "42", "50", "53", "120", "155", "169", "200", "200",
                "300", "363", "400", "400", "400", "400", "561", "950", "1000", "4500"
        };

        StringBuilder combinedText = new StringBuilder("<html>");
        for (int i = 0; i < code.length; i++) {
            combinedText.append(i + 1).append(". ").append(code[i]).append(" - ").append(name[i]).append("<br>");
        }
       // combinedText.append("</html>");
        String[] combinedTexts = combinedText.toString().split("<br>");
        //list.setText(combinedText.toString());

//        textFieldsPanel.setLayout(new GridLayout(numbers.length, 1));
//        for (String number : numbers) {
//            JTextField textField = new JTextField(number);
//            textFieldsPanel.add(textField);
//        }
        // contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        //contentPanel.setLayout(new GridLayout(code.length, 1));
        JPanel combinedPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);  // Add padding between components
        gbc.anchor = GridBagConstraints.WEST;

        for (int i = 0; i < code.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel label = new JLabel((i + 1) + ". " + code[i] + " - " + name[i]);
            combinedPanel.add(label, gbc);

            gbc.gridx = 1;
            JTextField textField = new JTextField(numbers[i], 10);
            textField.setColumns(5);  // Set columns to indicate the preferred width
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;  // Allow the text field to expand horizontally
            combinedPanel.add(textField, gbc);
        }

        // Add the combined panel to the content panel
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(new JScrollPane(combinedPanel), BorderLayout.CENTER);
        sepetiAktarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        int height = Math.max(numbers.length * 30, 500);
        int width = Math.max(name.length*30, 500);
        frame.setSize(width, height);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ShopList();
            }
        });
    }
}
