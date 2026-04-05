package com.banking.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UITheme {

    // Color Palette - Deep navy banking theme
    public static final Color BG_DARK       = new Color(10, 15, 30);
    public static final Color BG_PANEL      = new Color(18, 25, 48);
    public static final Color BG_CARD       = new Color(24, 35, 65);
    public static final Color ACCENT_GOLD   = new Color(255, 195, 0);
    public static final Color ACCENT_BLUE   = new Color(64, 156, 255);
    public static final Color ACCENT_GREEN  = new Color(50, 205, 120);
    public static final Color ACCENT_RED    = new Color(255, 80, 80);
    public static final Color TEXT_PRIMARY  = new Color(230, 235, 255);
    public static final Color TEXT_MUTED    = new Color(130, 145, 180);
    public static final Color BORDER_COLOR  = new Color(40, 55, 100);
    public static final Color TABLE_HEADER  = new Color(30, 45, 90);
    public static final Color TABLE_ROW_ODD = new Color(20, 30, 58);
    public static final Color TABLE_ROW_EVN = new Color(16, 24, 48);

    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_MONO   = new Font("Consolas", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);

    // ── Factory Methods ───────────────────────────────────────────────────────

    public static JLabel makeLabel(String text, Font font, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        return lbl;
    }

    public static JTextField makeField(int cols) {
        JTextField f = new JTextField(cols);
        f.setBackground(BG_CARD);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(ACCENT_GOLD);
        f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    public static JPasswordField makePasswordField(int cols) {
        JPasswordField f = new JPasswordField(cols);
        f.setBackground(BG_CARD);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(ACCENT_GOLD);
        f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    public static JButton makePrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ACCENT_BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(10, 24, 10, 24));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(90, 175, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(ACCENT_BLUE);
            }
        });
        return btn;
    }

    public static JButton makeGoldButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ACCENT_GOLD);
        btn.setForeground(new Color(10, 15, 30));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(10, 24, 10, 24));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(255, 210, 40));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(ACCENT_GOLD);
            }
        });
        return btn;
    }

    public static JButton makeDangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ACCENT_RED);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(10, 24, 10, 24));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JComboBox<String> makeCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(BG_CARD);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(FONT_BODY);
        return cb;
    }

    public static JPanel makeCard(String title) {
        JPanel card = new JPanel();
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(16, 20, 16, 20)));
        return card;
    }

    public static JTable makeTable(Object[][] data, String[] cols) {
        JTable table = new JTable(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                c.setBackground(row % 2 == 0 ? TABLE_ROW_ODD : TABLE_ROW_EVN);
                c.setForeground(TEXT_PRIMARY);
                return c;
            }
        };
        table.setBackground(TABLE_ROW_ODD);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(FONT_BODY);
        table.setRowHeight(28);
        table.setGridColor(BORDER_COLOR);
        table.getTableHeader().setBackground(TABLE_HEADER);
        table.getTableHeader().setForeground(ACCENT_GOLD);
        table.getTableHeader().setFont(FONT_HEADER);
        table.setSelectionBackground(ACCENT_BLUE);
        table.setSelectionForeground(Color.WHITE);
        return table;
    }

    public static JScrollPane scrollPane(Component comp) {
        JScrollPane sp = new JScrollPane(comp);
        sp.setBackground(BG_PANEL);
        sp.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        sp.getViewport().setBackground(BG_PANEL);
        sp.getVerticalScrollBar().setBackground(BG_PANEL);
        return sp;
    }

    public static void applyDark(JPanel panel) {
        panel.setBackground(BG_PANEL);
    }

    public static void showSuccess(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static Border sectionBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                title,
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                FONT_HEADER, ACCENT_GOLD);
    }
}
