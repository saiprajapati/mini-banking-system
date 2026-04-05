package com.banking;

import com.banking.gui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Use system look and feel as base, then we override with our theme
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Apply global UI tweaks
        UIManager.put("OptionPane.background",        new java.awt.Color(18, 25, 48));
        UIManager.put("Panel.background",             new java.awt.Color(18, 25, 48));
        UIManager.put("OptionPane.messageForeground", new java.awt.Color(230, 235, 255));
        UIManager.put("Button.background",            new java.awt.Color(64, 156, 255));
        UIManager.put("Button.foreground",            java.awt.Color.WHITE);

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
