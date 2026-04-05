package com.banking.gui;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.util.BankingService;
import com.banking.util.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class DashboardFrame extends JFrame {

    private final User           currentUser;
    private final BankingService service;

    // Account Tab
    private JComboBox<String> cbAccountSelect;
    private JLabel            lblBalance, lblAccType, lblAccStatus, lblAccNumber;

    // Deposit/Withdraw Tab
    private JComboBox<String> cbDepWitAcc;
    private JTextField        tfDepWitAmount;
    private JComboBox<String> cbOperation;

    // Transfer Tab
    private JComboBox<String> cbTransferFrom;
    private JTextField        tfTransferTo, tfTransferAmount;

    // Transaction History
    private DefaultTableModel txTableModel;
    private JComboBox<String> cbTxAccount;

    // All Accounts (admin view)
    private DefaultTableModel allAccTableModel;

    public DashboardFrame(User user, BankingService service) {
        this.currentUser = user;
        this.service     = service;
        setTitle("Mini Banking System — " + user.getFullName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_DARK);
        setContentPane(root);

        // ── Top Bar ───────────────────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_PANEL);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_COLOR),
                new EmptyBorder(14, 24, 14, 24)));

        JLabel brand = UITheme.makeLabel("🏦 MiniBankSystem", UITheme.FONT_TITLE, UITheme.ACCENT_GOLD);
        JLabel welcome = UITheme.makeLabel("Welcome, " + currentUser.getFullName() + "  |  @" + currentUser.getUsername(),
                UITheme.FONT_BODY, UITheme.TEXT_MUTED);

        JButton btnLogout = UITheme.makeDangerButton("Logout");
        btnLogout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });

        topBar.add(brand,   BorderLayout.WEST);
        topBar.add(welcome, BorderLayout.CENTER);
        topBar.add(btnLogout, BorderLayout.EAST);
        root.add(topBar, BorderLayout.NORTH);

        // ── Tabs ──────────────────────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(UITheme.BG_DARK);
        tabs.setForeground(UITheme.TEXT_PRIMARY);
        tabs.setFont(UITheme.FONT_HEADER);
        tabs.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override protected void paintTabBackground(Graphics g, int tp, int ti, int x, int y, int w, int h, boolean sel) {
                g.setColor(sel ? UITheme.BG_CARD : UITheme.BG_PANEL);
                g.fillRect(x, y, w, h);
            }
            @Override protected void paintTabBorder(Graphics g, int tp, int ti, int x, int y, int w, int h, boolean sel) {
                g.setColor(sel ? UITheme.ACCENT_GOLD : UITheme.BORDER_COLOR);
                g.drawRect(x, y, w, h);
            }
            @Override protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) { return 38; }
        });

        tabs.addTab("  📂 My Accounts  ",  buildAccountsTab());
        tabs.addTab("  💰 Deposit/Withdraw  ", buildDepositTab());
        tabs.addTab("  🔁 Transfer  ",      buildTransferTab());
        tabs.addTab("  📋 Transactions  ",  buildTransactionsTab());
        tabs.addTab("  ➕ New Account  ",   buildNewAccountTab());
        tabs.addTab("  👥 All Accounts  ",  buildAllAccountsTab());

        root.add(tabs, BorderLayout.CENTER);

        // ── Status Bar ────────────────────────────────────────────────────────
        JLabel statusBar = UITheme.makeLabel(
                "  Connected to Mini Banking System  |  User ID: " + currentUser.getId(),
                UITheme.FONT_SMALL, UITheme.TEXT_MUTED);
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_COLOR),
                new EmptyBorder(6, 12, 6, 12)));
        statusBar.setBackground(UITheme.BG_PANEL);
        statusBar.setOpaque(true);
        root.add(statusBar, BorderLayout.SOUTH);
    }

    // ── Tab: My Accounts ──────────────────────────────────────────────────────

    private JPanel buildAccountsTab() {
        JPanel p = new JPanel(new BorderLayout(16, 16));
        p.setBackground(UITheme.BG_DARK);
        p.setBorder(new EmptyBorder(24, 28, 24, 28));

        // Selector row
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        top.setBackground(UITheme.BG_DARK);
        top.add(UITheme.makeLabel("Select Account:", UITheme.FONT_HEADER, UITheme.TEXT_PRIMARY));
        cbAccountSelect = buildAccountCombo();
        top.add(cbAccountSelect);
        JButton btnRefresh = UITheme.makePrimaryButton("🔄 Refresh");
        top.add(btnRefresh);
        p.add(top, BorderLayout.NORTH);

        // Details card
        JPanel card = new JPanel(new GridLayout(4, 2, 12, 12));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_COLOR),
                new EmptyBorder(20, 24, 20, 24)));

        lblAccNumber = UITheme.makeLabel("—", UITheme.FONT_MONO, UITheme.ACCENT_BLUE);
        lblAccType   = UITheme.makeLabel("—", UITheme.FONT_HEADER, UITheme.TEXT_PRIMARY);
        lblBalance   = UITheme.makeLabel("—", new Font("Segoe UI", Font.BOLD, 26), UITheme.ACCENT_GREEN);
        lblAccStatus = UITheme.makeLabel("—", UITheme.FONT_HEADER, UITheme.TEXT_PRIMARY);

        card.add(UITheme.makeLabel("Account Number:", UITheme.FONT_HEADER, UITheme.TEXT_MUTED));
        card.add(lblAccNumber);
        card.add(UITheme.makeLabel("Type:", UITheme.FONT_HEADER, UITheme.TEXT_MUTED));
        card.add(lblAccType);
        card.add(UITheme.makeLabel("Balance:", UITheme.FONT_HEADER, UITheme.TEXT_MUTED));
        card.add(lblBalance);
        card.add(UITheme.makeLabel("Status:", UITheme.FONT_HEADER, UITheme.TEXT_MUTED));
        card.add(lblAccStatus);
        p.add(card, BorderLayout.CENTER);

        // Freeze/Activate
        JPanel acts = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        acts.setBackground(UITheme.BG_DARK);
        JButton btnFreeze   = UITheme.makeDangerButton("❄ Freeze Account");
        JButton btnActivate = UITheme.makeGoldButton("✔ Activate Account");
        acts.add(btnFreeze);
        acts.add(btnActivate);
        p.add(acts, BorderLayout.SOUTH);

        cbAccountSelect.addActionListener(e -> loadAccountDetails());
        btnRefresh.addActionListener(e -> { refreshAccountCombos(); loadAccountDetails(); });
        btnFreeze.addActionListener(e -> {
            String acc = selectedAccount(cbAccountSelect);
            if (acc == null) return;
            String r = service.freezeAccount(acc);
            if (r.startsWith("SUCCESS")) { UITheme.showSuccess(p, "Account frozen."); loadAccountDetails(); }
            else UITheme.showError(p, r);
        });
        btnActivate.addActionListener(e -> {
            String acc = selectedAccount(cbAccountSelect);
            if (acc == null) return;
            String r = service.activateAccount(acc);
            if (r.startsWith("SUCCESS")) { UITheme.showSuccess(p, "Account activated."); loadAccountDetails(); }
            else UITheme.showError(p, r);
        });

        loadAccountDetails();
        return p;
    }

    // ── Tab: Deposit / Withdraw ───────────────────────────────────────────────

    private JPanel buildDepositTab() {
        JPanel p = new JPanel();
        p.setBackground(UITheme.BG_DARK);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(30, 60, 30, 60));

        JLabel title = UITheme.makeLabel("Deposit / Withdraw", UITheme.FONT_TITLE, UITheme.ACCENT_GOLD);
        title.setAlignmentX(CENTER_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(28));

        cbDepWitAcc = buildAccountCombo();
        tfDepWitAmount = UITheme.makeField(20);
        cbOperation  = UITheme.makeCombo(new String[]{"DEPOSIT", "WITHDRAW"});

        p.add(formRow("Account:", cbDepWitAcc));
        p.add(Box.createVerticalStrut(16));
        p.add(formRow("Operation:", cbOperation));
        p.add(Box.createVerticalStrut(16));
        p.add(formRow("Amount (₹):", tfDepWitAmount));
        p.add(Box.createVerticalStrut(28));

        JButton btnSubmit = UITheme.makeGoldButton("▶  Execute");
        btnSubmit.setAlignmentX(CENTER_ALIGNMENT);
        btnSubmit.setMaximumSize(new Dimension(240, 46));
        p.add(btnSubmit);

        btnSubmit.addActionListener(e -> {
            String acc = selectedAccount(cbDepWitAcc);
            if (acc == null) { UITheme.showError(p, "Please select an account."); return; }
            BigDecimal amount;
            try { amount = new BigDecimal(tfDepWitAmount.getText().trim()); }
            catch (NumberFormatException ex) { UITheme.showError(p, "Invalid amount."); return; }

            String op = (String) cbOperation.getSelectedItem();
            String result = "DEPOSIT".equals(op) ? service.deposit(acc, amount) : service.withdraw(acc, amount);

            if (result.startsWith("SUCCESS:")) {
                UITheme.showSuccess(p, result.substring(8));
                tfDepWitAmount.setText("");
                refreshAccountCombos();
            } else {
                UITheme.showError(p, result);
            }
        });
        return p;
    }

    // ── Tab: Transfer ─────────────────────────────────────────────────────────

    private JPanel buildTransferTab() {
        JPanel p = new JPanel();
        p.setBackground(UITheme.BG_DARK);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(30, 60, 30, 60));

        JLabel title = UITheme.makeLabel("Fund Transfer", UITheme.FONT_TITLE, UITheme.ACCENT_GOLD);
        title.setAlignmentX(CENTER_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(28));

        cbTransferFrom   = buildAccountCombo();
        tfTransferTo     = UITheme.makeField(20);
        tfTransferAmount = UITheme.makeField(20);

        p.add(formRow("From Account:", cbTransferFrom));
        p.add(Box.createVerticalStrut(16));
        p.add(formRow("To Account Number:", tfTransferTo));
        p.add(Box.createVerticalStrut(16));
        p.add(formRow("Amount (₹):", tfTransferAmount));
        p.add(Box.createVerticalStrut(28));

        JButton btnTransfer = UITheme.makeGoldButton("🔁  Transfer Funds");
        btnTransfer.setAlignmentX(CENTER_ALIGNMENT);
        btnTransfer.setMaximumSize(new Dimension(260, 46));
        p.add(btnTransfer);

        btnTransfer.addActionListener(e -> {
            String from = selectedAccount(cbTransferFrom);
            String to   = tfTransferTo.getText().trim();
            if (from == null || to.isEmpty()) { UITheme.showError(p, "Fill all fields."); return; }
            BigDecimal amount;
            try { amount = new BigDecimal(tfTransferAmount.getText().trim()); }
            catch (NumberFormatException ex) { UITheme.showError(p, "Invalid amount."); return; }

            String result = service.transfer(from, to, amount);
            if (result.startsWith("SUCCESS:")) {
                UITheme.showSuccess(p, result.substring(8));
                tfTransferTo.setText(""); tfTransferAmount.setText("");
                refreshAccountCombos();
            } else {
                UITheme.showError(p, result);
            }
        });
        return p;
    }

    // ── Tab: Transactions ─────────────────────────────────────────────────────

    private JPanel buildTransactionsTab() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(UITheme.BG_DARK);
        p.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        top.setBackground(UITheme.BG_DARK);
        top.add(UITheme.makeLabel("Account:", UITheme.FONT_HEADER, UITheme.TEXT_PRIMARY));
        cbTxAccount = buildAccountCombo();
        top.add(cbTxAccount);
        JButton btnLoad = UITheme.makePrimaryButton("Load Transactions");
        top.add(btnLoad);
        p.add(top, BorderLayout.NORTH);

        String[] cols = {"Txn ID", "Type", "Amount (₹)", "Balance After (₹)", "Description", "Date"};
        txTableModel  = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r,int c){return false;} };
        JTable table  = UITheme.makeTable(new Object[0][], cols);
        table.setModel(txTableModel);
        p.add(UITheme.scrollPane(table), BorderLayout.CENTER);

        btnLoad.addActionListener(e -> {
            String acc = selectedAccount(cbTxAccount);
            if (acc == null) return;
            txTableModel.setRowCount(0);
            List<Transaction> txns = service.getTransactions(acc);
            for (Transaction t : txns) {
                txTableModel.addRow(new Object[]{
                        t.getTransactionId(), t.getTransactionType(),
                        t.getAmount(), t.getBalanceAfter(),
                        t.getDescription(), t.getTransactionDate()
                });
            }
        });
        return p;
    }

    // ── Tab: New Account ──────────────────────────────────────────────────────

    private JPanel buildNewAccountTab() {
        JPanel p = new JPanel();
        p.setBackground(UITheme.BG_DARK);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(30, 60, 30, 60));

        JLabel title = UITheme.makeLabel("Open New Account", UITheme.FONT_TITLE, UITheme.ACCENT_GOLD);
        title.setAlignmentX(CENTER_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(30));

        JComboBox<String> cbType = UITheme.makeCombo(new String[]{"SAVINGS", "CURRENT", "FIXED_DEPOSIT"});
        JTextField tfInitial     = UITheme.makeField(20);

        p.add(formRow("Account Type:", cbType));
        p.add(Box.createVerticalStrut(16));
        p.add(formRow("Initial Deposit (₹):", tfInitial));
        p.add(Box.createVerticalStrut(28));

        JButton btnCreate = UITheme.makeGoldButton("✚  Create Account");
        btnCreate.setAlignmentX(CENTER_ALIGNMENT);
        btnCreate.setMaximumSize(new Dimension(260, 46));
        p.add(btnCreate);

        btnCreate.addActionListener(e -> {
            String type = (String) cbType.getSelectedItem();
            BigDecimal initial;
            try { initial = new BigDecimal(tfInitial.getText().trim()); }
            catch (NumberFormatException ex) { UITheme.showError(p, "Invalid initial deposit amount."); return; }

            String result = service.createAccount(currentUser.getId(), type, initial);
            if (result.startsWith("SUCCESS:")) {
                String accNo = result.substring(8);
                UITheme.showSuccess(p, "Account created!\nAccount Number: " + accNo);
                tfInitial.setText("");
                refreshAccountCombos();
            } else {
                UITheme.showError(p, result);
            }
        });
        return p;
    }

    // ── Tab: All Accounts (admin view) ────────────────────────────────────────

    private JPanel buildAllAccountsTab() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(UITheme.BG_DARK);
        p.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        top.setBackground(UITheme.BG_DARK);
        top.add(UITheme.makeLabel("All Accounts in System", UITheme.FONT_HEADER, UITheme.TEXT_PRIMARY));
        JButton btnRefresh = UITheme.makePrimaryButton("🔄 Refresh");
        top.add(btnRefresh);
        p.add(top, BorderLayout.NORTH);

        String[] cols = {"Account No", "Owner", "Type", "Balance (₹)", "Status", "Created"};
        allAccTableModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r,int c){return false;} };
        JTable table = UITheme.makeTable(new Object[0][], cols);
        table.setModel(allAccTableModel);
        p.add(UITheme.scrollPane(table), BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> loadAllAccounts());
        loadAllAccounts();
        return p;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private JComboBox<String> buildAccountCombo() {
        JComboBox<String> cb = UITheme.makeCombo(new String[]{});
        List<Account> accounts = service.getUserAccounts(currentUser.getId());
        for (Account a : accounts) cb.addItem(a.getAccountNumber() + " (" + a.getAccountType() + ")");
        return cb;
    }

    private void refreshAccountCombos() {
        List<Account> accounts = service.getUserAccounts(currentUser.getId());
        JComboBox<?>[] combos = {cbAccountSelect, cbDepWitAcc, cbTransferFrom, cbTxAccount};
        for (JComboBox<?> cb : combos) {
            if (cb == null) continue;
            String selected = (String) cb.getSelectedItem();
            cb.removeAllItems();
            for (Account a : accounts)
                ((JComboBox<String>) cb).addItem(a.getAccountNumber() + " (" + a.getAccountType() + ")");
            if (selected != null) cb.setSelectedItem(selected);
        }
    }

    private String selectedAccount(JComboBox<String> cb) {
        String item = (String) cb.getSelectedItem();
        if (item == null || item.isEmpty()) return null;
        return item.split(" ")[0];
    }

    private void loadAccountDetails() {
        String acc = selectedAccount(cbAccountSelect);
        if (acc == null) { lblBalance.setText("—"); return; }
        Account account = service.getAccount(acc);
        if (account == null) return;
        lblAccNumber.setText(account.getAccountNumber());
        lblAccType.setText(account.getAccountType());
        lblBalance.setText("₹ " + account.getBalance().toPlainString());
        lblAccStatus.setText(account.getStatus());
        lblAccStatus.setForeground("ACTIVE".equals(account.getStatus()) ? UITheme.ACCENT_GREEN : UITheme.ACCENT_RED);
    }

    private void loadAllAccounts() {
        allAccTableModel.setRowCount(0);
        List<Account> all = service.getAllAccounts();
        for (Account a : all) {
            allAccTableModel.addRow(new Object[]{
                    a.getAccountNumber(), a.getOwnerName(), a.getAccountType(),
                    a.getBalance(), a.getStatus(), a.getCreatedAt()
            });
        }
    }

    private JPanel formRow(String lbl, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(UITheme.BG_DARK);
        row.setMaximumSize(new Dimension(500, 48));
        JLabel label = UITheme.makeLabel(lbl, UITheme.FONT_HEADER, UITheme.TEXT_MUTED);
        label.setPreferredSize(new Dimension(180, 36));
        row.add(label, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }
}
