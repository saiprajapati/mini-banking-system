package com.banking.gui;

import com.banking.util.BankingService;
import com.banking.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends JDialog {

    private final BankingService service;
    private final JFrame parent;

    private JTextField     tfUsername, tfFullName, tfEmail;
    private JPasswordField pfPassword, pfConfirm;

    public RegisterFrame(BankingService service, JFrame parent) {
        super(parent, "Register New User", true);
        this.service = service;
        this.parent  = parent;
        setSize(420, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel();
        root.setBackground(UITheme.BG_DARK);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(new EmptyBorder(30, 36, 30, 36));
        setContentPane(root);

        JLabel title = UITheme.makeLabel("Create Account", UITheme.FONT_TITLE, UITheme.ACCENT_GOLD);
        title.setAlignmentX(CENTER_ALIGNMENT);
        root.add(title);
        root.add(Box.createVerticalStrut(24));

        tfFullName = UITheme.makeField(20);
        tfUsername = UITheme.makeField(20);
        tfEmail    = UITheme.makeField(20);
        pfPassword = UITheme.makePasswordField(20);
        pfConfirm  = UITheme.makePasswordField(20);

        root.add(fieldRow("Full Name",        tfFullName));
        root.add(Box.createVerticalStrut(12));
        root.add(fieldRow("Username",         tfUsername));
        root.add(Box.createVerticalStrut(12));
        root.add(fieldRow("Email (optional)", tfEmail));
        root.add(Box.createVerticalStrut(12));
        root.add(fieldRow("Password",         pfPassword));
        root.add(Box.createVerticalStrut(12));
        root.add(fieldRow("Confirm Password", pfConfirm));
        root.add(Box.createVerticalStrut(24));

        JButton btnReg = UITheme.makeGoldButton("Register");
        btnReg.setAlignmentX(CENTER_ALIGNMENT);
        btnReg.setMaximumSize(new Dimension(300, 44));
        root.add(btnReg);

        btnReg.addActionListener(e -> doRegister());
    }

    private JPanel fieldRow(String lbl, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(0, 5));
        row.setBackground(UITheme.BG_DARK);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));
        row.add(UITheme.makeLabel(lbl, UITheme.FONT_HEADER, UITheme.TEXT_PRIMARY), BorderLayout.NORTH);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private void doRegister() {
        String fullName  = tfFullName.getText().trim();
        String username  = tfUsername.getText().trim();
        String email     = tfEmail.getText().trim();
        String password  = new String(pfPassword.getPassword());
        String confirm   = new String(pfConfirm.getPassword());

        if (!password.equals(confirm)) {
            UITheme.showError(this, "Passwords do not match.");
            return;
        }

        String result = service.register(username, password, fullName, email);
        if ("SUCCESS".equals(result)) {
            UITheme.showSuccess(this, "Registration successful! You can now log in.");
            dispose();
        } else {
            UITheme.showError(this, result);
        }
    }
}
