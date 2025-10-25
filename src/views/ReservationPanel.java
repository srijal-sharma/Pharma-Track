package views;
import app.PharmaTrackApp;
import components.GradientButton;
import components.ThemedComponentFactory;
import data.AppModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReservationPanel extends JPanel {
    private final PharmaTrackApp mainApp;
    private final AppModel model;
    private final ThemedComponentFactory themeFactory;

    private JTable reservationsTable;

    public ReservationPanel(PharmaTrackApp mainApp, AppModel model, ThemedComponentFactory themeFactory) {
        this.mainApp = mainApp;
        this.model = model;
        this.themeFactory = themeFactory;
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(14, 14, 14, 14));
        this.setOpaque(false);
        buildUI();
    }

    private void buildUI() {
        reservationsTable = new JTable(model.reservationTableModel);
        reservationsTable.setName("reservationsTable");
        themeFactory.formatTable(reservationsTable, 4);
        JScrollPane scroll = new JScrollPane(reservationsTable);
        this.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);

        JButton cancelBtn = new GradientButton("Cancel Selected", new Color(220, 60, 60), new Color(180, 50, 50));
        JButton exportBtn = new GradientButton("Export (mock)", new Color(90, 90, 120), new Color(70, 70, 100));
        cancelBtn.setPreferredSize(new Dimension(130, 36));
        exportBtn.setPreferredSize(new Dimension(130, 36));

        bottom.add(exportBtn);
        bottom.add(cancelBtn);
        this.add(bottom, BorderLayout.SOUTH);

        // Actions
        cancelBtn.addActionListener(e -> {
            int r = reservationsTable.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(this, "Select a reservation to cancel.", "Selection Required", JOptionPane.WARNING_MESSAGE); return; }
            int conf = JOptionPane.showConfirmDialog(this, "Cancel selected reservation?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                ((DefaultTableModel) reservationsTable.getModel()).removeRow(r);
                JOptionPane.showMessageDialog(this, "Reservation cancelled.");
            }
        });

        exportBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Exported reservations (mock)."));
    }
}