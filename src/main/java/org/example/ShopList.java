package org.example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShopList {
    private JLabel title;
    private JScrollBar scrollBar1;
    private JButton sepetiAktarButton;
    private JPanel panel;
    private JFrame frame;

    public ShopList() {
        frame = new JFrame("ShopList");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        sepetiAktarButton.setVisible(true);
        frame.setContentPane(this.panel);
        frame.pack();
        frame.setSize(400, 500);



        sepetiAktarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ShopList();
            }
        });
    }
}
