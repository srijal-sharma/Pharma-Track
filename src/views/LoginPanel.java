package views;

import app.PharmaTrackApp;
import components.ThemedComponentFactory;
import components.GradientButton;
import data.AppModel;
import data.ManagerData;

import javax.swing.*;
// Removing BorderFactory and CompoundBorder imports as they are no longer used
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class LoginPanel extends JPanel {
    private final PharmaTrackApp mainApp;
    private final AppModel model;
    private final ThemedComponentFactory themeFactory;

    private JPanel loginSheet;
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JRadioButton managerBtn;

    public LoginPanel(PharmaTrackApp mainApp, AppModel model, ThemedComponentFactory themeFactory) {
        this.mainApp = mainApp;
        this.model = model;
        this.themeFactory = themeFactory;
        setLayout(new GridBagLayout());
        setOpaque(false);
        buildUI();
    }

    private void buildUI() {
        loginSheet = new JPanel();
        loginSheet.setLayout(new BoxLayout(loginSheet, BoxLayout.Y_AXIS));

        // --- BORDER REMOVAL: Using only EmptyBorder for padding ---
        // Replaced CompoundBorder(BorderFactory.createLineBorder(...) with just EmptyBorder
        loginSheet.setBorder(new EmptyBorder(25, 30, 25, 30));

        loginSheet.setMaximumSize(new Dimension(460, 480));
        loginSheet.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("PharmaTrack Login");
        title.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        // --- FIELD SIZING FIX ---
        final int FIELD_MAX_WIDTH = 350;

        loginUsernameField = themeFactory.styledTextField();
        // Set fixed maximum width for both fields
        loginUsernameField.setMaximumSize(new Dimension(FIELD_MAX_WIDTH, 40));

        loginPasswordField = new JPasswordField();
        loginPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        // Set fixed maximum width for both fields
        loginPasswordField.setMaximumSize(new Dimension(FIELD_MAX_WIDTH, 40));
        themeFactory.updateTextFieldColors(loginPasswordField);
        // --- END FIELD SIZING FIX ---


        // --- ROLE SELECTION (CENTERED) FIX ---
        // FlowLayout.CENTER centers the radio buttons within the panel
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        rolePanel.setOpaque(false);
        JRadioButton userBtn = new JRadioButton("User");
        managerBtn = new JRadioButton("Manager");
        userBtn.setSelected(true);
        ButtonGroup grp = new ButtonGroup();
        grp.add(userBtn);
        grp.add(managerBtn);
        rolePanel.add(new JLabel("Login as:"));
        rolePanel.add(userBtn);
        rolePanel.add(managerBtn);
        // Component.CENTER_ALIGNMENT centers the panel in the BoxLayout
        rolePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // --- END ROLE SELECTION FIX ---


        // Gradient Buttons
        JButton signIn = new GradientButton("Sign In", new Color(0, 122, 255), new Color(0, 100, 200));
        JButton signUp = new GradientButton("Sign Up as Manager", new Color(40, 180, 99), new Color(30, 150, 80));

        // Horizontal Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        signIn.setPreferredSize(new Dimension(140, 40));
        signUp.setPreferredSize(new Dimension(180, 40));

        buttonPanel.add(signIn);
        buttonPanel.add(signUp);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components to the main sheet
        loginSheet.add(title);
        loginSheet.add(new JLabel("Username:"));
        loginSheet.add(Box.createVerticalStrut(6));
        loginSheet.add(loginUsernameField);
        loginSheet.add(Box.createVerticalStrut(6));
        loginSheet.add(new JLabel("Password:"));
        loginSheet.add(Box.createVerticalStrut(6));
        loginSheet.add(loginPasswordField);
        loginSheet.add(Box.createVerticalStrut(6));
        loginSheet.add(rolePanel);
        loginSheet.add(Box.createVerticalStrut(20));

        loginSheet.add(buttonPanel);
        loginSheet.add(Box.createVerticalStrut(20));

        // Center sheet in panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1; gbc.weighty = 1;
        this.add(loginSheet, gbc);

        // Actions
        signUp.addActionListener(e -> mainApp.showCard(PharmaTrackApp.CARD_PHARMACY_REGISTRATION));
        signIn.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String user = loginUsernameField.getText().trim();
        String pass = new String(loginPasswordField.getPassword()).trim();
        boolean mockSuccess = (user.equals("hrijit") && pass.equals("1234")) || user.length() > 0;

        if (!mockSuccess) {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (managerBtn.isSelected()) {
            // Manager Logic
            if (user.equalsIgnoreCase("manager") && pass.equals("1234")) {
                // Mock manager
                model.setManagerData(new ManagerData("Mock Manager Pharmacy", "9999-D-01", "8888-R-01", "9876543210"));
                mainApp.showCard(PharmaTrackApp.CARD_MANAGER_SETUP);
            } else if (model.getManagerData() != null && Objects.equals(user, model.getManagerData().name.toLowerCase())) {
                // Assume registered manager login success
                mainApp.showCard(PharmaTrackApp.CARD_MANAGER_SETUP);
            } else {
                JOptionPane.showMessageDialog(this, "No registered manager found for this username. Please Sign Up.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // User Logic
            model.currentUserName = user;
            model.currentUserEmail = user + "@pharmatrack.com";
            model.currentUserLocation = "Kolkata, India";
            mainApp.showCard(PharmaTrackApp.CARD_APP);
        }
    }

    // Theme logic is now in ThemedComponentFactory, but we can override if needed
    // to specifically refresh components after theme change.
    @Override
    public void repaint() {
        super.repaint();
        // Removed border logic to resolve 'cannot find symbol' error.
        if (loginSheet != null) {
            // If you wanted to re-add just the padding:
            // loginSheet.setBorder(new EmptyBorder(25, 30, 25, 30));
        }
    }
}