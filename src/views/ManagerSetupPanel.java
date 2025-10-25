package views;
import app.PharmaTrackApp;
import components.GradientButton;
import components.ThemedComponentFactory;
import data.AppModel;
import data.ManagerData;
import data.Medicine;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class ManagerSetupPanel extends JPanel {
    private final PharmaTrackApp mainApp;
    private final AppModel model;
    private final ThemedComponentFactory themeFactory;

    // UI Components
    private JTextField medNameField, medPriceField, medStockField, medExpiryField;
    private JComboBox<String> medDeliveryStatus;
    private JLabel topUserLabel;

    public ManagerSetupPanel(PharmaTrackApp mainApp, AppModel model, ThemedComponentFactory themeFactory) {
        this.mainApp = mainApp;
        this.model = model;
        this.themeFactory = themeFactory;
        this.setName(PharmaTrackApp.CARD_MANAGER_SETUP);
        this.setLayout(new GridBagLayout());
        buildUI();
    }

    public void updateTopLabel() {
        ManagerData manager = model.getManagerData();
        if (manager != null) {
            topUserLabel.setText("Manager: " + manager.name);
        } else {
            topUserLabel.setText("Manager: (Not logged in)");
        }
    }

    private void buildUI() {
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(themeFactory.getSubtleColor(), 1, true),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)));
        inner.setMaximumSize(new Dimension(500, 600));
        inner.setPreferredSize(new Dimension(500, 500));
        inner.setAlignmentX(Component.CENTER_ALIGNMENT);

        topUserLabel = new JLabel();
        topUserLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 16));
        topUserLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        updateTopLabel();
        inner.add(topUserLabel);
        inner.add(Box.createVerticalStrut(10));

        JLabel title = new JLabel("Add New Medicine Stock");
        title.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        inner.add(title);
        inner.add(Box.createVerticalStrut(20));

        // Initialize fields using the factory
        medNameField = themeFactory.styledTextField();
        medPriceField = themeFactory.styledTextField();
        medStockField = themeFactory.styledTextField();
        medExpiryField = themeFactory.styledTextField();

        medDeliveryStatus = new JComboBox<>(new String[]{
                "Fast Delivery Possible",
                "Fast Delivery Not Possible",
                "Delivery Partner Not Available"
        });
        medDeliveryStatus.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        medDeliveryStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        medDeliveryStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        medDeliveryStatus.setPreferredSize(new Dimension(400, 40));

        // Use the factory helper to wrap components
        inner.add(themeFactory.createLabeledField("Medicine Name:", medNameField));
        inner.add(themeFactory.createLabeledField("Price (INR):", medPriceField));
        inner.add(themeFactory.createLabeledField("Stock Quantity:", medStockField));
        inner.add(themeFactory.createLabeledField("Expiry Date (YYYY-MM-DD):", medExpiryField));
        inner.add(themeFactory.createLabeledField("Delivery Availability:", medDeliveryStatus));

        inner.add(Box.createVerticalStrut(20));

        JButton addStockBtn = new GradientButton("Add Stock and Continue", new Color(40, 180, 99), new Color(30, 150, 80));
        addStockBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addStockBtn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        addStockBtn.addActionListener(e -> finalizeMedicineEntry());
        inner.add(addStockBtn);
        inner.add(Box.createVerticalStrut(10));

        JButton backToLogin = new JButton("Back to Login");
        backToLogin.setName("BackToLoginBtn");
        backToLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        backToLogin.addActionListener(e -> {
            model.setManagerData(null); // Clear manager state on back/logout
            mainApp.showCard(PharmaTrackApp.CARD_LOGIN);
        });
        inner.add(backToLogin);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1; gbc.weighty = 1;
        this.add(inner, gbc);
    }

    private void finalizeMedicineEntry() {
        try {
            String name = medNameField.getText().trim();
            double price = Double.parseDouble(medPriceField.getText().trim());
            int qty = Integer.parseInt(medStockField.getText().trim());
            String expiry = medExpiryField.getText().trim();
            String delivery = (String) medDeliveryStatus.getSelectedItem();
            ManagerData manager = model.getManagerData();

            if (name.isEmpty() || expiry.isEmpty() || qty <= 0 || price <= 0 || manager == null) {
                JOptionPane.showMessageDialog(this, "Please fill in valid details and ensure manager data is set.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Medicine newMed = new Medicine(
                    manager.name,
                    name,
                    price,
                    qty,
                    expiry,
                    delivery
            );
            model.getMedicines().add(newMed);

            JOptionPane.showMessageDialog(this,
                    "Medicine '" + name + "' added successfully for " + manager.name + "! It is now searchable by users.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear fields for next entry
            medNameField.setText("");
            medPriceField.setText("");
            medStockField.setText("");
            medExpiryField.setText("");
            medDeliveryStatus.setSelectedIndex(0);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price and Quantity must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}