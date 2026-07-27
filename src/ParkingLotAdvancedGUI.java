import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
        import java.util.ArrayList;

public class ParkingLotAdvancedGUI extends JFrame implements ActionListener {

    ArrayList<String> vehicles = new ArrayList<>();
    int totalSlots;

    JTextArea display;
    JTextField inputField;

    JButton parkBtn, removeBtn, viewBtn, checkBtn, exitBtn;

    public ParkingLotAdvancedGUI() {

        // Ask slots
        totalSlots = Integer.parseInt(JOptionPane.showInputDialog("Enter Total Parking Slots:"));

        // Frame settings
        setTitle("🚗 Smart Parking System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel (Title)
        JLabel title = new JLabel("Parking Lot System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(45, 62, 80));
        title.setPreferredSize(new Dimension(600, 50));
        add(title, BorderLayout.NORTH);

        // Center Panel (Display)
        display = new JTextArea();
        display.setFont(new Font("Consolas", Font.PLAIN, 14));
        display.setEditable(false);
        display.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(display);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel (Controls)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(3, 2, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));

        parkBtn = createButton("Park");
        removeBtn = createButton("Remove");
        viewBtn = createButton("View");
        checkBtn = createButton("Slots");
        exitBtn = createButton("Exit");

        bottomPanel.add(new JLabel("Vehicle Number:"));
        bottomPanel.add(inputField);
        bottomPanel.add(parkBtn);
        bottomPanel.add(removeBtn);
        bottomPanel.add(viewBtn);
        bottomPanel.add(checkBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // Exit Button separate
        add(exitBtn, BorderLayout.EAST);

        // Add actions
        parkBtn.addActionListener(this);
        removeBtn.addActionListener(this);
        viewBtn.addActionListener(this);
        checkBtn.addActionListener(this);
        exitBtn.addActionListener(this);

        setVisible(true);
    }

    // Custom Button Design
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        return btn;
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == parkBtn) {
            if (vehicles.size() >= totalSlots) {
                display.setText("❌ Parking Full!");
                return;
            }

            String v = inputField.getText();
            if (!v.isEmpty()) {
                vehicles.add(v);
                display.setText("✅ Vehicle Parked!");
                inputField.setText("");
            }
        }

        else if (e.getSource() == removeBtn) {
            String v = inputField.getText();
            if (vehicles.remove(v)) {
                display.setText("🚗 Vehicle Removed!");
            } else {
                display.setText("❌ Not Found!");
            }
        }

        else if (e.getSource() == viewBtn) {
            display.setText("📋 Parked Vehicles:\n\n");
            for (int i = 0; i < vehicles.size(); i++) {
                display.append((i + 1) + ". " + vehicles.get(i) + "\n");
            }
        }

        else if (e.getSource() == checkBtn) {
            display.setText("📊 Total: " + totalSlots +
                    "\nOccupied: " + vehicles.size() +
                    "\nAvailable: " + (totalSlots - vehicles.size()));
        }

        else if (e.getSource() == exitBtn) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new ParkingLotAdvancedGUI();
    }
}