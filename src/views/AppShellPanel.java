package views;
import app.PharmaTrackApp;
import components.GradientButton;
import components.ThemedComponentFactory;
import data.AppModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AppShellPanel extends JPanel {
    private final PharmaTrackApp mainApp;
    private final AppModel model;
    private final ThemedComponentFactory themeFactory;

    private JCheckBox darkToggle;
    private JLabel topUserLabel;
    private JTabbedPane tabs;

    public AppShellPanel(PharmaTrackApp mainApp, AppModel model, ThemedComponentFactory themeFactory) {
        this.mainApp = mainApp;
        this.model = model;
        this.themeFactory = themeFactory;
        this.setName(PharmaTrackApp.CARD_APP);
        this.setLayout(new BorderLayout());
        buildUI();
    }

    public void updateTopLabel() {
        topUserLabel.setText("Signed in as: " + model.currentUserName);
    }

    private void buildUI() {
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(new EmptyBorder(12, 20, 12, 20));
        topBar.setOpaque(false);

        JLabel brand = new JLabel("PharmaTrack");
        brand.setFont(new Font("SF Pro Display", Font.BOLD, 20));

        // Right controls
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        right.setOpaque(false);
        topUserLabel = new JLabel("Signed in as: Guest");
        darkToggle = new JCheckBox("Dark");
        darkToggle.setSelected(model.isDarkMode());

        JButton logoutBtn = new GradientButton("Logout", new Color(130, 130, 140), new Color(110, 110, 120));
        logoutBtn.setPreferredSize(new Dimension(80, 28));

        right.add(topUserLabel);
        right.add(darkToggle);
        right.add(logoutBtn);

        topBar.add(brand, BorderLayout.WEST);
        topBar.add(right, BorderLayout.EAST);

        this.add(topBar, BorderLayout.NORTH);

        // Tabs
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabs.addTab("Medicine Search", new MedicineSearchPanel(mainApp, model, themeFactory));
        tabs.addTab("My Reservations", new ReservationPanel(mainApp, model, themeFactory));
        tabs.addTab("Profile", new ProfilePanel(mainApp, model, themeFactory));
        tabs.setBorder(new EmptyBorder(0, 10, 10, 10));

        this.add(tabs, BorderLayout.CENTER);

        // Events
        darkToggle.addActionListener(e -> {
            model.setDarkMode(darkToggle.isSelected());
            mainApp.applyTheme();
        });

        logoutBtn.addActionListener(e -> {
            model.setManagerData(null);
            model.setDarkMode(false); // Reset theme on logout
            mainApp.showCard(PharmaTrackApp.CARD_LOGIN);
        });
    }

    @Override
    public void repaint() {
        super.repaint();
        if (tabs != null) {
            tabs.setBackground(themeFactory.getPanelColor());
        }
    }
}