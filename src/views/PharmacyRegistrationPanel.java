package views;
import app.PharmaTrackApp;
import components.GradientButton;
import components.ThemedComponentFactory;
import data.AppModel;
import data.ManagerData;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PharmacyRegistrationPanel extends JPanel {
    private final PharmaTrackApp mainApp;
    private final AppModel model;
    private final ThemedComponentFactory themeFactory;

    // UI Components
    private JTextField regUsernameField;
    private JPasswordField regPasswordField;
    private JTextField regPharmacyNameField, regDrugLicenseField, regRegNumberField, regContactField;
    private JCheckBox regAuthorizationCheck;

    public PharmacyRegistrationPanel(PharmaTrackApp mainApp, AppModel model, ThemedComponentFactory themeFactory) {
        this.mainApp = mainApp;
        this.model = model;
        this.themeFactory = themeFactory;
        this.setName(PharmaTrackApp.CARD_PHARMACY_REGISTRATION);
        this.setLayout(new GridBagLayout());
        buildUI();
    }

    private void buildUI() {
        JPanel sheet = new JPanel();
        sheet.setLayout(new BoxLayout(sheet, BoxLayout.Y_AXIS));
        sheet.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(themeFactory.getSubtleColor(), 1, true),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)));
        sheet.setMaximumSize(new Dimension(500, 600));
        sheet.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("New Pharmacy Registration");
        title.setFont(new Font("SF Pro Display", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        sheet.add(title);
        sheet.add(Box.createVerticalStrut(20));

        // Initialize fields using the factory
        regUsernameField = themeFactory.styledTextField();
        regPasswordField = new JPasswordField();
        themeFactory.updateTextFieldColors(regPasswordField);
        regPharmacyNameField = themeFactory.styledTextField();
        regDrugLicenseField = themeFactory.styledTextField();
        regRegNumberField = themeFactory.styledTextField();
        regContactField = themeFactory.styledTextField();

        regAuthorizationCheck = new JCheckBox("I confirm I am authorized to represent this pharmacy.");
        regAuthorizationCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        regAuthorizationCheck.setOpaque(false);

        // Add fields to sheet
        sheet.add(new JLabel("Manager Username:")); sheet.add(Box.createVerticalStrut(4)); sheet.add(regUsernameField); sheet.add(Box.createVerticalStrut(10));
        sheet.add(new JLabel("Password:")); sheet.add(Box.createVerticalStrut(4)); sheet.add(regPasswordField); sheet.add(Box.createVerticalStrut(10));
        sheet.add(new JLabel("Pharmacy Name:")); sheet.add(Box.createVerticalStrut(4)); sheet.add(regPharmacyNameField); sheet.add(Box.createVerticalStrut(10));
        sheet.add(new JLabel("Drug Licence Number:")); sheet.add(Box.createVerticalStrut(4)); sheet.add(regDrugLicenseField); sheet.add(Box.createVerticalStrut(10));
        sheet.add(new JLabel("Pharmacy Registration Number:")); sheet.add(Box.createVerticalStrut(4)); sheet.add(regRegNumberField); sheet.add(Box.createVerticalStrut(10));
        sheet.add(new JLabel("Helpline Contact Number:")); sheet.add(Box.createVerticalStrut(4)); sheet.add(regContactField); sheet.add(Box.createVerticalStrut(15));

        sheet.add(regAuthorizationCheck);
        sheet.add(Box.createVerticalStrut(20));

        JButton registerBtn = new GradientButton("Register Pharmacy", new Color(40, 180, 99), new Color(30, 150, 80));
        registerBtn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 40));
        sheet.add(registerBtn);
        sheet.add(Box.createVerticalStrut(10));

        JButton backBtn = new JButton("Back to Login");
        backBtn.setName("BackToLoginBtn");
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> mainApp.showCard(PharmaTrackApp.CARD_LOGIN));
        sheet.add(backBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1; gbc.weighty = 1;
        this.add(sheet, gbc);

        // Registration Action
        registerBtn.addActionListener(e -> handleRegistration());
    }

    private void handleRegistration() {
        String user = regUsernameField.getText().trim();
        String pass = new String(regPasswordField.getPassword()).trim();
        String pharName = regPharmacyNameField.getText().trim();
        String drugLicense = regDrugLicenseField.getText().trim();
        String regNumber = regRegNumberField.getText().trim();
        String contact = regContactField.getText().trim();

        if (user.isEmpty() || pass.isEmpty() || pharName.isEmpty() || drugLicense.isEmpty() || regNumber.isEmpty() || contact.isEmpty() || !regAuthorizationCheck.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields and confirm authorization.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mock Registration Success
        ManagerData newManager = new ManagerData(pharName, drugLicense, regNumber, contact);
        model.setManagerData(newManager);

        JOptionPane.showMessageDialog(this,
                "Registration Successful for " + pharName + "! You are now signed in.",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear fields and move to manager setup
        mainApp.showCard(PharmaTrackApp.CARD_MANAGER_SETUP);
    }

    @Override
    public void repaint() {
        super.repaint();
        // Manually update the border color for the sheet on theme change
        if (this.getComponentCount() > 0 && this.getComponent(0) instanceof JPanel sheet) {
            sheet.setBorder(new CompoundBorder(
                    BorderFactory.createLineBorder(themeFactory.getSubtleColor(), 1, true),
                    BorderFactory.createEmptyBorder(25, 30, 25, 30)));
        }
    }
}