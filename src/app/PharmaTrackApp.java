package app;

import components.ThemedComponentFactory;
import data.AppModel;
import views.*;

import javax.swing.*;
import java.awt.*;

public class PharmaTrackApp extends JFrame {
    // Card keys
    public static final String CARD_LOGIN = "login";
    public static final String CARD_APP = "app";
    public static final String CARD_MANAGER_SETUP = "managerSetup";
    public static final String CARD_PHARMACY_REGISTRATION = "pharmacyRegistration";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel rootPanel = new JPanel(cardLayout);

    // Core application components
    private final AppModel model = new AppModel();
    private final ThemedComponentFactory themeFactory = new ThemedComponentFactory();
    private AppShellPanel appShellPanel; // Needed to apply theme updates to sub-components

    public PharmaTrackApp() {
        setTitle("PharmaTrack — Manager/User Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 680);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        // 1. Build Views
        rootPanel.add(new LoginPanel(this, model, themeFactory), CARD_LOGIN);
        rootPanel.add(new PharmacyRegistrationPanel(this, model, themeFactory), CARD_PHARMACY_REGISTRATION);
        rootPanel.add(new ManagerSetupPanel(this, model, themeFactory), CARD_MANAGER_SETUP);

        // App Shell contains all user-side tabs
        appShellPanel = new AppShellPanel(this, model, themeFactory);
        rootPanel.add(appShellPanel, CARD_APP);

        add(rootPanel);

        // 2. Initial State and Theme
        cardLayout.show(rootPanel, CARD_LOGIN);
        applyTheme();
    }

    // --- Controller / Orchestration Methods ---

    public void applyTheme() {
        themeFactory.setDarkMode(model.isDarkMode());
        themeFactory.applyThemeRecursively(rootPanel);
        try {
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ignored) {}
    }

    public void showCard(String cardKey) {
        if (CARD_APP.equals(cardKey)) {
            appShellPanel.updateTopLabel();
        } else if (CARD_MANAGER_SETUP.equals(cardKey)) {
            ManagerSetupPanel managerPanel = (ManagerSetupPanel) getComponentByName(CARD_MANAGER_SETUP);
            if (managerPanel != null) managerPanel.updateTopLabel();
        }
        cardLayout.show(rootPanel, cardKey);
        applyTheme(); // Re-apply theme to ensure all components refresh
    }

    private Component getComponentByName(String name) {
        for (Component c : rootPanel.getComponents()) {
            if (name.equals(c.getName())) {
                return c;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            PharmaTrackApp app = new PharmaTrackApp();
            app.setVisible(true);
        });
    }
}