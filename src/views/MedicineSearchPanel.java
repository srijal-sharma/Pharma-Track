package views;
import app.PharmaTrackApp;
import components.GradientButton;
import components.ThemedComponentFactory;
import data.AppModel;
import data.ManagerData;
import data.Medicine;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class MedicineSearchPanel extends JPanel {
    private final PharmaTrackApp mainApp;
    private final AppModel model;
    private final ThemedComponentFactory themeFactory;

    private JTable searchTable;
    private JTextField searchField;

    public MedicineSearchPanel(PharmaTrackApp mainApp, AppModel model, ThemedComponentFactory themeFactory) {
        this.mainApp = mainApp;
        this.model = model;
        this.themeFactory = themeFactory;
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(14, 14, 14, 14));
        this.setOpaque(false);
        buildUI();
    }

    private void buildUI() {
        // Top search row
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        searchField = themeFactory.styledTextField();
        searchField.setColumns(28);
        searchField.setPreferredSize(new Dimension(300, 36));

        JButton searchBtn = new GradientButton("Search", new Color(0, 110, 230), new Color(0, 90, 180));
        JButton resetBtn = new GradientButton("Reset", new Color(130, 130, 140), new Color(110, 110, 120));
        searchBtn.setPreferredSize(new Dimension(80, 36));
        resetBtn.setPreferredSize(new Dimension(80, 36));

        top.add(new JLabel("Search medicine:"));
        top.add(searchField);
        top.add(searchBtn);
        top.add(resetBtn);

        this.add(top, BorderLayout.NORTH);

        // Table with results
        searchTable = new JTable(model.searchTableModel);
        searchTable.setName("searchTable");
        themeFactory.formatTable(searchTable, 5); // Initial format call
        JScrollPane scroll = new JScrollPane(searchTable);
        this.add(scroll, BorderLayout.CENTER);

        // Bottom: reserve button and quick filters
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);

        JPanel leftInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftInfo.setOpaque(false);
        JButton btnInStock = themeFactory.styledSmallPill("In Stock", e -> refreshSearchTable(
                model.getMedicines().stream().filter(m -> m.qty > 3).collect(Collectors.toList())));
        JButton btnLow = themeFactory.styledSmallPill("Low Stock", e -> refreshSearchTable(
                model.getMedicines().stream().filter(m -> m.qty > 0 && m.qty <= 3).collect(Collectors.toList())));
        leftInfo.add(new JLabel("Quick Filters:"));
        leftInfo.add(btnInStock);
        leftInfo.add(btnLow);

        JPanel rightActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightActions.setOpaque(false);
        JButton reserveBtn = new GradientButton("Reserve Selected", new Color(220, 70, 70), new Color(180, 50, 50));
        reserveBtn.setPreferredSize(new Dimension(150, 36));
        rightActions.add(reserveBtn);

        bottom.add(leftInfo, BorderLayout.WEST);
        bottom.add(rightActions, BorderLayout.EAST);
        this.add(bottom, BorderLayout.SOUTH);

        // Actions
        searchField.addActionListener(e -> doSearch());
        searchBtn.addActionListener(e -> doSearch());
        resetBtn.addActionListener(e -> { searchField.setText(""); refreshSearchTable(model.getMedicines()); });
        searchTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int r = searchTable.getSelectedRow();
                    if (r >= 0) showMedicineDetails(r);
                }
            }
        });
        reserveBtn.addActionListener(e -> handleReservation());

        refreshSearchTable(model.getMedicines()); // Initial display of all medicines
    }

    private void doSearch() {
        String q = searchField.getText().trim().toLowerCase();
        List<Medicine> filtered = model.getMedicines().stream()
                .filter(m -> m.name.toLowerCase().contains(q))
                .collect(Collectors.toList());
        refreshSearchTable(filtered);
    }

    private void refreshSearchTable(List<Medicine> list) {
        DefaultTableModel tableModel = model.searchTableModel;
        tableModel.setRowCount(0);
        if (list.isEmpty() && !searchField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No pharmacies found stocking '" + searchField.getText().trim() + "'", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }

        for (Medicine m : list) {
            String status;
            if (m.qty <= 0) status = "Out of Stock";
            else if (m.qty <= 3) status = "Low Stock (" + m.qty + ")";
            else status = "In Stock (" + m.qty + ")";
            tableModel.addRow(new Object[]{m.pharmacy, m.name, m.price, status, m.expiry});
        }
    }

    private void handleReservation() {
        int r = searchTable.getSelectedRow();
        if (r == -1) { JOptionPane.showMessageDialog(this, "Select a medicine row to reserve.", "Selection Required", JOptionPane.WARNING_MESSAGE); return; }

        String pharmacy = (String) model.searchTableModel.getValueAt(r, 0);
        String medName = (String) model.searchTableModel.getValueAt(r, 1);
        String stock = (String) model.searchTableModel.getValueAt(r, 3);

        if (stock.toLowerCase().contains("out")) {
            JOptionPane.showMessageDialog(this, "Item out of stock at " + pharmacy, "Cannot Reserve", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this, "Reserve 1 unit of " + medName + " at " + pharmacy + "?", "Confirm Reservation", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            // Add reservation to the model
            model.reservationTableModel.addRow(new Object[]{pharmacy, medName, "Confirmed", "Within 2 hours"});

            // Optionally: reduce stock in the model and refresh search table
            Medicine reservedMed = model.getMedicines().stream()
                    .filter(m -> m.pharmacy.equals(pharmacy) && m.name.equals(medName) && m.qty > 0)
                    .findFirst().orElse(null);

            if (reservedMed != null) {
                reservedMed.qty--;
                refreshSearchTable(model.getMedicines().stream()
                        .filter(m -> m.name.toLowerCase().contains(searchField.getText().trim().toLowerCase()))
                        .collect(Collectors.toList()));
            }

            JOptionPane.showMessageDialog(this, "Reserved! Check My Reservations tab.");
        }
    }

    private void showMedicineDetails(int viewRow) {
        int modelRow = searchTable.convertRowIndexToModel(viewRow);

        String pharmacy = (String) model.searchTableModel.getValueAt(modelRow, 0);
        String med = (String) model.searchTableModel.getValueAt(modelRow, 1);
        Object price = model.searchTableModel.getValueAt(modelRow, 2);
        String stock = (String) model.searchTableModel.getValueAt(modelRow, 3);
        String expiry = (String) model.searchTableModel.getValueAt(modelRow, 4);

        Medicine m = model.getMedicines().stream()
                .filter(item -> item.pharmacy.equals(pharmacy) && item.name.equals(med))
                .findFirst().orElse(null);

        // ManagerData or a default mock value
        String helpline = (model.getManagerData() != null) ? model.getManagerData().contact : "N/A (Mocked)";
        String delivery = (m != null) ? m.deliveryTime : "N/A";

        Color textColor = themeFactory.getTextColor();
        Color bg = themeFactory.getFieldBgColor();

        StringBuilder sb = new StringBuilder();
        sb.append("Pharmacy: ").append(pharmacy).append("\n");
        sb.append("Medicine: ").append(med).append("\n");
        sb.append("Price: ₹").append(price).append("\n");
        sb.append("Stock: ").append(stock).append("\n");
        sb.append("Nearest expiry: ").append(expiry).append("\n");
        sb.append("Delivery Status: ").append(delivery).append("\n");
        sb.append("Helpline: ").append(helpline).append("\n\n");
        sb.append("This data is now dynamic based on Manager input!");

        JTextArea details = new JTextArea(sb.toString());
        details.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        details.setEditable(false);
        details.setBackground(bg);
        details.setForeground(textColor);
        details.setCaretColor(textColor);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(themeFactory.getPanelColor());
        messagePanel.add(new JScrollPane(details), BorderLayout.CENTER);

        JOptionPane pane = new JOptionPane(messagePanel, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog(mainApp, "Medicine Details");
        dialog.setVisible(true);
    }
}