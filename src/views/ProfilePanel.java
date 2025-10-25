package views;
import app.PharmaTrackApp;
import components.GradientButton;
import components.ThemedComponentFactory;
import data.AppModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProfilePanel extends JPanel {
    private final PharmaTrackApp mainApp;
    private final AppModel model;
    private final ThemedComponentFactory themeFactory;

    // UI Components
    private JTextField profileNameField;
    private JTextField profileEmailField;
    private JTextField profileLocationField;

    public ProfilePanel(PharmaTrackApp mainApp, AppModel model, ThemedComponentFactory themeFactory) {
        this.mainApp = mainApp;
        this.model = model;
        this.themeFactory = themeFactory;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(18, 18, 18, 18));
        this.setOpaque(false);
        buildUI();
        loadProfileData(); // Load data once fields are created
    }

    private void buildUI() {
        JLabel profileTitle = new JLabel("Profile Information");
        profileTitle.setFont(new Font("SF Pro Display", Font.BOLD, 18));
        profileTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(profileTitle);
        this.add(Box.createVerticalStrut(18));

        // Initialize fields using the factory
        profileNameField = themeFactory.styledTextField();
        profileEmailField = themeFactory.styledTextField();
        profileLocationField = themeFactory.styledTextField();

        Dimension fieldMax = new Dimension(400, 36);
        profileNameField.setMaximumSize(fieldMax);
        profileEmailField.setMaximumSize(fieldMax);
        profileLocationField.setMaximumSize(fieldMax);
        profileNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        profileEmailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        profileLocationField.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.add(new JLabel("Name:")); this.add(Box.createVerticalStrut(6)); this.add(profileNameField); this.add(Box.createVerticalStrut(15));
        this.add(new JLabel("Email:")); this.add(Box.createVerticalStrut(6)); this.add(profileEmailField); this.add(Box.createVerticalStrut(15));
        this.add(new JLabel("Location:")); this.add(Box.createVerticalStrut(6)); this.add(profileLocationField); this.add(Box.createVerticalStrut(25));

        JButton save = new GradientButton("Save Profile", new Color(0, 110, 230), new Color(0, 90, 180));
        save.setPreferredSize(new Dimension(120, 36));
        save.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.add(save);

        save.addActionListener(e -> handleSaveProfile());
    }

    private void loadProfileData() {
        profileNameField.setText(model.currentUserName);
        profileEmailField.setText(model.currentUserEmail);
        profileLocationField.setText(model.currentUserLocation);
    }

    private void handleSaveProfile() {
        model.currentUserName = profileNameField.getText().trim();
        model.currentUserEmail = profileEmailField.getText().trim();
        model.currentUserLocation = profileLocationField.getText().trim();

        // Update the top bar label after saving
        Component shell = SwingUtilities.getAncestorOfClass(AppShellPanel.class, this);
        if (shell instanceof AppShellPanel appShell) {
            appShell.updateTopLabel();
        }

        JOptionPane.showMessageDialog(this, "Profile saved (mock).");
    }
}