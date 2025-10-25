package data;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class AppModel {
    private final List<Medicine> medicines = new ArrayList<>();
    private ManagerData managerData;
    private boolean darkMode = false;

    // Table Models
    public final DefaultTableModel searchTableModel;
    public final DefaultTableModel reservationTableModel;

    // Mock Profile Data (for simplicity, stored in Model)
    public String currentUserName = "Guest User";
    public String currentUserEmail = "guest@pharmatrack.com";
    public String currentUserLocation = "Kolkata, India";

    public AppModel() {
        searchTableModel = new DefaultTableModel(new String[]{"Pharmacy", "Medicine", "Price (INR)", "Stock", "Expiry"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        reservationTableModel = new DefaultTableModel(new String[]{"Pharmacy", "Medicine", "Status", "Pickup By"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        seedMockData();
    }

    private void seedMockData() {
        medicines.add(new Medicine("City Pharmacy", "Paracetamol", 25.0, 12, "2026-05-01", "Fast Delivery Possible"));
        medicines.add(new Medicine("Apollo Meds", "Paracetamol", 26.5, 5, "2026-06-01", "Delivery Partner Not Available"));
        medicines.add(new Medicine("GreenLeaf Pharmacy", "Paracetamol", 24.0, 3, "2025-11-22", "Fast Delivery Possible"));
        medicines.add(new Medicine("HealthPlus", "Amoxicillin", 90.0, 20, "2026-02-18", "Fast Delivery Not Possible"));
        medicines.add(new Medicine("CareFirst", "Amoxicillin", 95.0, 1, "2025-12-30", "Delivery Partner Not Available"));
        medicines.add(new Medicine("Neighborhood Chemist", "Dolo-650", 40.0, 8, "2025-11-20", "Fast Delivery Possible"));
        medicines.add(new Medicine("Wellness Center", "Dolo-650", 41.5, 2, "2025-12-01", "Fast Delivery Possible"));

        // Initial reservation mock data
        reservationTableModel.addRow(new Object[]{"City Pharmacy", "Paracetamol", "Confirmed", "Within 2 hours"});
    }

    // Getters and Setters
    public List<Medicine> getMedicines() { return medicines; }
    public ManagerData getManagerData() { return managerData; }
    public void setManagerData(ManagerData managerData) { this.managerData = managerData; }
    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }
}