package components;

import data.Medicine;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;

public class ThemedComponentFactory {
    private boolean darkMode = false;

    // --- Theme Palette ---
    private Color bg;
    private Color panel;
    private Color text;
    private Color subtle;
    private Color fieldBg;

    public void setDarkMode(boolean dark) {
        this.darkMode = dark;
        bg = dark ? new Color(22, 23, 25) : new Color(247, 247, 249);
        panel = dark ? new Color(34, 35, 38) : new Color(255, 255, 255);
        text = dark ? new Color(230, 230, 230) : new Color(24, 24, 24);
        subtle = dark ? new Color(60, 60, 65) : new Color(210, 210, 215);
        fieldBg = dark ? new Color(45, 46, 49) : new Color(255, 255, 255);
    }

    public Color getBgColor() { return bg; }
    public Color getPanelColor() { return panel; }
    public Color getTextColor() { return text; }
    public Color getSubtleColor() { return subtle; }
    public Color getFieldBgColor() { return fieldBg; }

    // --- Component Creation Helpers ---

    public JTextField styledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 40));
        // Initial styling
        tf.setBackground(fieldBg);
        tf.setForeground(text);
        tf.setCaretColor(text);
        tf.setBorder(new LineBorder(subtle, 1, true));
        return tf;
    }

    public JButton styledSmallPill(String buttonText, ActionListener action) {
        JButton b = new JButton(buttonText);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);

        Color lightBg = new Color(230, 230, 235);
        Color lightFg = new Color(50, 50, 60);
        Color darkBg = new Color(60, 60, 65);
        Color darkFg = new Color(200, 200, 200);
        Color lightBorder = new Color(210, 210, 215);
        Color darkBorder = new Color(80, 80, 85);

        b.setBackground(darkMode ? darkBg : lightBg);
        b.setForeground(darkMode ? darkFg : lightFg);

        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(darkMode ? darkBorder : lightBorder, 1, true),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        b.setPreferredSize(new Dimension(b.getPreferredSize().width + 20, 32));
        b.addActionListener(action);
        return b;
    }

    // --- Theme Application ---

    public void updateTextFieldColors(JTextComponent comp) {
        if (comp == null) return;
        comp.setBackground(fieldBg);
        comp.setForeground(text);
        comp.setCaretColor(text);
        comp.setBorder(new LineBorder(subtle, 1, true));
    }

    public void applyThemeRecursively(Component comp) {
        if (comp == null) return;

        if (comp instanceof JPanel panelComp) {
            // Check specific named panels (Login, Manager, Registration) that use the main BG color
            String name = panelComp.getName();
            if ("login".equals(name) || "managerSetup".equals(name) || "pharmacyRegistration".equals(name)) {
                panelComp.setBackground(bg);
            } else if (comp.isOpaque()) {
                // Default opaque panels use the lighter panel color
                panelComp.setBackground(panel);
            }
        } else if (comp instanceof JScrollPane scrollComp) {
            scrollComp.setBackground(panel);
            JViewport vp = scrollComp.getViewport();
            if (vp != null) vp.setBackground(fieldBg);
            scrollComp.setBorder(new LineBorder(subtle, 1, true));

        } else if (comp instanceof JTable t) {
            t.setBackground(fieldBg);
            t.setForeground(text);
            t.setSelectionBackground(darkMode ? new Color(75, 75, 80) : new Color(225, 235, 245));
            t.setGridColor(subtle.darker());
            updateTableHeaderColors(t);
            // Re-format table to apply new cell renderer colors
            if (t.getName() != null) {
                if (t.getName().equals("searchTable")) formatTable(t, 5);
                else if (t.getName().equals("reservationsTable")) formatTable(t, 4);
            }

        } else if (comp instanceof JTextComponent) {
            updateTextFieldColors((JTextComponent) comp);

        } else if (comp instanceof JLabel || comp instanceof JCheckBox || comp instanceof JRadioButton) {
            comp.setForeground(text);
            if (comp instanceof JCheckBox || comp instanceof JRadioButton) comp.setBackground(panel);

        } else if (comp instanceof JButton button) {
            if (button instanceof GradientButton gb) {
                gb.setDarkMode(darkMode);
                gb.repaint(); // Force repaint for gradient
            } else if ("BackToLoginBtn".equals(button.getName())) {
                button.setForeground(text);
                button.setBackground(panel);
                button.setContentAreaFilled(true);
                button.setBorder(BorderFactory.createLineBorder(text.darker(), 1, true));
            } else {
                button.setForeground(text); // For non-gradient buttons
            }
        } else if (comp instanceof JComboBox) {
            comp.setForeground(text);
            comp.setBackground(fieldBg);
        }

        if (comp instanceof Container container) {
            for (Component c : container.getComponents()) {
                applyThemeRecursively(c);
            }
        }
    }

    private void updateTableHeaderColors(JTable table) {
        if (table.getTableHeader() == null) return;
        JTableHeader header = table.getTableHeader();
        header.setBackground(darkMode ? new Color(50, 50, 52) : new Color(245, 245, 247));
        header.setForeground(text);
        header.setBorder(new MatteBorder(0, 0, 1, 0, subtle));
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }


    public void formatTable(JTable t, int columnCount) {
        t.setFillsViewportHeight(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setRowHeight(28);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.getTableHeader().setReorderingAllowed(false);
        t.setShowGrid(false);

        // Set up the custom cell renderer for styling and column centering
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                Color selectionBg = darkMode ? new Color(75, 75, 80) : new Color(225, 235, 245);
                Color defaultBg = darkMode ? new Color(34, 35, 38) : Color.WHITE;
                Color alternateBg = darkMode ? new Color(30, 31, 33) : new Color(250, 250, 252);

                if (isSelected) {
                    c.setBackground(selectionBg);
                } else {
                    c.setBackground(row % 2 == 0 ? defaultBg : alternateBg);
                }
                c.setForeground(text);

                // Column centering logic
                if (columnCount == 5 && (column == 2 || column == 3)) { // Search Table Price/Stock
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                } else if (columnCount == 4 && (column == 2 || column == 3)) { // Reservations Table Status/Pickup
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }

                ((JComponent) c).setBorder(new EmptyBorder(0, 5, 0, 5));
                return c;
            }
        });

        // Set column widths
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumnModel cm = t.getColumnModel();

        if (columnCount == 5) { // Search Table
            if (cm.getColumnCount() > 0) cm.getColumn(0).setPreferredWidth(150);
            if (cm.getColumnCount() > 1) cm.getColumn(1).setPreferredWidth(150);
            if (cm.getColumnCount() > 2) cm.getColumn(2).setPreferredWidth(100);
            if (cm.getColumnCount() > 3) cm.getColumn(3).setPreferredWidth(120);
            if (cm.getColumnCount() > 4) cm.getColumn(4).setPreferredWidth(100);
        } else if (columnCount == 4) { // Reservations Table
            if (cm.getColumnCount() > 0) cm.getColumn(0).setPreferredWidth(150);
            if (cm.getColumnCount() > 1) cm.getColumn(1).setPreferredWidth(150);
            if (cm.getColumnCount() > 2) cm.getColumn(2).setPreferredWidth(120);
            if (cm.getColumnCount() > 3) cm.getColumn(3).setPreferredWidth(150);
        }
    }

    // Helper method to wrap a component (field/combo) with a label and spacing (moved from ManagerSetup)
    public JPanel createLabeledField(String labelText, JComponent component) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setForeground(text); // Apply theme color
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(label);
        wrapper.add(Box.createVerticalStrut(4));

        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(component);
        wrapper.add(Box.createVerticalStrut(10));

        wrapper.setMaximumSize(new Dimension(400, 80));
        return wrapper;
    }
}