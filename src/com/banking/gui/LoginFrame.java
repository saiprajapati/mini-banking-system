package com.banking.gui;

import com.banking.model.User;
import com.banking.util.BankingService;
import com.banking.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private final BankingService service = new BankingService();

    private JTextField     tfUsername;
    private JPasswordField pfPassword;

    public LoginFrame() {
        setTitle("Mini Banking System — Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(440, 560);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_DARK);
        setContentPane(root);

        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(UITheme.BG_DARK);
        header.setBorder(new EmptyBorder(40, 30, 10, 30));

        JLabel logo = new JLabel("🏦");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));

        JLabel title = UITheme.makeLabel("MiniBankSystem", UITheme.FONT_TITLE, UITheme.ACCENT_GOLD);
        JLabel sub   = UITheme.makeLabel("Secure. Simple. Smart.", UITheme.FONT_SMALL, UITheme.TEXT_MUTED);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0; gc.insets = new Insets(0,0,8,0);
        header.add(logo, gc);
        gc.gridy = 1; gc.insets = new Insets(0,0,4,0);
        header.add(title, gc);
        gc.gridy = 2;
        header.add(sub, gc);

        root.add(header, BorderLayout.NORTH);

        // ── Form ──────────────────────────────────────────────────────────────
        JPanel form = new JPanel();
        form.setBackground(UITheme.BG_PANEL);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR, 1),
                new EmptyBorder(30, 36, 30, 36)));

        form.add(fieldRow("Username", tfUsername = UITheme.makeField(20)));
        form.add(Box.createVerticalStrut(16));
        form.add(fieldRow("Password", pfPassword = UITheme.makePasswordField(20)));
        form.add(Box.createVerticalStrut(28));

        JButton btnLogin    = UITheme.makeGoldButton("Login");
        JButton btnRegister = UITheme.makePrimaryButton("New Account? Register");

        btnLogin.setAlignmentX(CENTER_ALIGNMENT);
        btnRegister.setAlignmentX(CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(300, 44));
        btnRegister.setMaximumSize(new Dimension(300, 44));

        form.add(btnLogin);
        form.add(Box.createVerticalStrut(12));
        form.add(btnRegister);

        JPanel formWrap = new JPanel(new BorderLayout());
        formWrap.setBackground(UITheme.BG_DARK);
        formWrap.setBorder(new EmptyBorder(10, 30, 40, 30));
        formWrap.add(form, BorderLayout.CENTER);
        root.add(formWrap, BorderLayout.CENTER);

        // ── Footer ────────────────────────────────────────────────────────────
        JLabel footer = UITheme.makeLabel("© 2025 MiniBankSystem | JDBC + Swing",
                UITheme.FONT_SMALL, UITheme.TEXT_MUTED);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setBorder(new EmptyBorder(0, 0, 16, 0));
        root.add(footer, BorderLayout.SOUTH);

        // ── Listeners ─────────────────────────────────────────────────────────
        btnLogin.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> openRegister());
        pfPassword.addActionListener(e -> doLogin());

        getRootPane().setDefaultButton(btnLogin);
    }

    private JPanel fieldRow(String labelText, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(0, 6));
        row.setBackground(UITheme.BG_PANEL);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        JLabel lbl = UITheme.makeLabel(labelText, UITheme.FONT_HEADER, UITheme.TEXT_PRIMARY);
        row.add(lbl, BorderLayout.NORTH);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private void doLogin() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            UITheme.showError(this, "Please enter username and password.");
            return;
        }

        User user = service.login(username, password);
        if (user != null) {
            dispose();
            new DashboardFrame(user, service).setVisible(true);
        } else {
            UITheme.showError(this, "Invalid username or password.");
            pfPassword.setText("");
        }
    }

    private void openRegister() {
        new RegisterFrame(service, this).setVisible(true);
    }
}
