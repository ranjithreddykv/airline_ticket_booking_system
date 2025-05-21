/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.airline_project;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
// import javax.swing.UIManager; // Usually set in main application class
import javax.swing.border.Border;
import com.toedter.calendar.JTextFieldDateEditor; // For styling JDateChooser

/**
 * A JInternalFrame for adding new flight details to the system.
 * It includes fields for Flight ID (auto-generated), Flight Name, Arrival,
 * Departure, Duration, Seats, Fare, and Date of Flight.
 *
 * @author ranji
 */
public class AddFlight extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(AddFlight.class.getName());

    // Style Constants
    private static final Color FRAME_BACKGROUND = new Color(240, 243, 245); // Light grayish blue
    private static final Color PANEL_BACKGROUND = Color.WHITE; // Clean white for panels
    private static final Color TITLE_COLOR = new Color(0, 80, 150); // Professional Blue
    private static final Color LABEL_TEXT_COLOR = new Color(30, 30, 30); // Darker Gray for labels
    private static final Color BORDER_COLOR = new Color(200, 200, 205); // Softer border color
    private static final Color DISPLAY_FIELD_BACKGROUND = new Color(238, 241, 243); // For non-editable fields like FlightID
    private static final Color BUTTON_PRIMARY_BG = new Color(0, 122, 204); // Blue for primary action
    private static final Color BUTTON_CANCEL_BG = new Color(108, 117, 125); // Gray for cancel

    private static final Font TITLE_FONT = new Font("Segoe UI Semibold", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font TEXT_INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);


    /**
     * Establishes and returns a connection to the MySQL database.
     * @return A Connection object.
     * @throws SQLException If a database access error occurs.
     * @throws ClassNotFoundException If the MySQL JDBC driver class is not found.
     */
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        // Consider externalizing connection details for production
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1/airline-database", "root", "Ranjith@1654R");
    }

    /**
     * Creates new form AddFlight.
     * Initializes components, applies custom styles, and generates a Flight ID.
     */
    public AddFlight() {
        initComponents();
        applyCustomStyles();
        try {
            generateAutoFlightID();
            // Style flightIdTextField as a display field after AutoID
            flightIdTextField.setEditable(false);
            flightIdTextField.setBackground(DISPLAY_FIELD_BACKGROUND);
            flightIdTextField.setForeground(LABEL_TEXT_COLOR);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Failed to generate AutoID for Flight", ex);
            JOptionPane.showMessageDialog(this, "Error generating Flight ID: " + ex.getMessage(), "ID Generation Error", JOptionPane.ERROR_MESSAGE);
            // Optionally disable the form or parts of it if ID is critical
            addFlightButton.setEnabled(false); // Example: disable add button
        }
    }

    /**
     * Applies custom visual styles to the UI components.
     */
    private void applyCustomStyles() {
        this.getContentPane().setBackground(FRAME_BACKGROUND);
        this.setPreferredSize(new java.awt.Dimension(700, 500));

        // Title
        pageTitleLabel.setFont(TITLE_FONT);
        pageTitleLabel.setForeground(TITLE_COLOR);
        pageTitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0)); // Padding

        // Panels
        Border panelBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true), // Rounded corners option (not directly available in LineBorder)
                BorderFactory.createEmptyBorder(20, 20, 20, 20) // Inner padding
        );
        flightInfoPanel.setBackground(PANEL_BACKGROUND);
        flightInfoPanel.setBorder(panelBorder);
        flightAttributesPanel.setBackground(PANEL_BACKGROUND);
        flightAttributesPanel.setBorder(panelBorder);

        // Labels
        applyLabelStyle(flightIdLabel);
        applyLabelStyle(flightNameLabel);
        applyLabelStyle(arrivalLabel);
        applyLabelStyle(departureLabel);
        applyLabelStyle(durationLabel);
        applyLabelStyle(seatsLabel);
        applyLabelStyle(fareLabel);
        applyLabelStyle(dateOfFlightLabel);

        // Text Input Fields
        applyTextInputFieldStyle(flightIdTextField); // Will be made non-editable after AutoID
        applyTextInputFieldStyle(flightNameTextField);
        applyTextInputFieldStyle(arrivalTextField);
        applyTextInputFieldStyle(departureTextField);
        applyTextInputFieldStyle(durationTextField);
        applyTextInputFieldStyle(seatsTextField);
        applyTextInputFieldStyle(fareTextField);

        // JDateChooser Style
        dateOfFlightChooser.setFont(TEXT_INPUT_FONT);
        JTextFieldDateEditor editor = (JTextFieldDateEditor) dateOfFlightChooser.getDateEditor();
        editor.setFont(TEXT_INPUT_FONT);
        editor.setBackground(Color.WHITE);
        editor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        editor.setEditable(false); // Make date field non-editable by typing, only via chooser
        dateOfFlightChooser.getCalendarButton().setFont(TEXT_INPUT_FONT); // Style calendar button too
        dateOfFlightChooser.getCalendarButton().setCursor(new Cursor(Cursor.HAND_CURSOR));


        // Buttons
        styleButton(addFlightButton, BUTTON_PRIMARY_BG, Color.WHITE);
        styleButton(cancelButton, BUTTON_CANCEL_BG, Color.WHITE);
    }

    private void applyLabelStyle(JLabel label) {
        label.setFont(LABEL_FONT);
        label.setForeground(LABEL_TEXT_COLOR);
    }

    private void applyTextInputFieldStyle(JTextField textField) {
        textField.setFont(TEXT_INPUT_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(4, 7, 4, 7)
        ));
        textField.setPreferredSize(new java.awt.Dimension(textField.getPreferredSize().width, 30));
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setOpaque(true); // Necessary for custom background on some L&Fs
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pageTitleLabel = new javax.swing.JLabel();
        flightInfoPanel = new javax.swing.JPanel();
        flightIdLabel = new javax.swing.JLabel();
        flightIdTextField = new javax.swing.JTextField();
        arrivalLabel = new javax.swing.JLabel();
        flightNameLabel = new javax.swing.JLabel();
        departureLabel = new javax.swing.JLabel();
        flightNameTextField = new javax.swing.JTextField();
        arrivalTextField = new javax.swing.JTextField();
        departureTextField = new javax.swing.JTextField();
        durationLabel = new javax.swing.JLabel();
        durationTextField = new javax.swing.JTextField();
        flightAttributesPanel = new javax.swing.JPanel();
        seatsLabel = new javax.swing.JLabel();
        fareLabel = new javax.swing.JLabel();
        dateOfFlightLabel = new javax.swing.JLabel();
        addFlightButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        seatsTextField = new javax.swing.JTextField();
        fareTextField = new javax.swing.JTextField();
        dateOfFlightChooser = new com.toedter.calendar.JDateChooser();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Add New Flight");

        pageTitleLabel.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        pageTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pageTitleLabel.setText("Welcome to the Flight Panel");

        flightInfoPanel.setForeground(new java.awt.Color(204, 204, 204));

        flightIdLabel.setText("Flight ID : ");

        arrivalLabel.setText("Arrival : ");

        flightNameLabel.setText("Flight Name :");

        departureLabel.setText("Departure :");

        durationLabel.setText("Duration :");

        javax.swing.GroupLayout flightInfoPanelLayout = new javax.swing.GroupLayout(flightInfoPanel);
        flightInfoPanel.setLayout(flightInfoPanelLayout);
        flightInfoPanelLayout.setHorizontalGroup(
                flightInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(flightInfoPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(flightInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(arrivalLabel)
                                        .addComponent(flightNameLabel)
                                        .addComponent(flightIdLabel)
                                        .addComponent(durationLabel)
                                        .addComponent(departureLabel))
                                .addGap(37, 37, 37)
                                .addGroup(flightInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(durationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(flightIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(flightNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(arrivalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(departureTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(20, Short.MAX_VALUE))
        );
        flightInfoPanelLayout.setVerticalGroup(
                flightInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(flightInfoPanelLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(flightInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(flightIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(flightIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(flightInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(flightNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(flightNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(flightInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(arrivalLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(arrivalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(flightInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(departureLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(departureTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(flightInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(durationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(durationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(25, Short.MAX_VALUE))
        );

        seatsLabel.setText("Seats :");

        fareLabel.setText("Fare :");

        dateOfFlightLabel.setText("Date of Flight :");

        addFlightButton.setBackground(new java.awt.Color(51, 102, 255));
        addFlightButton.setText("Add");
        addFlightButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFlightButtonActionPerformed(evt);
            }
        });

        cancelButton.setBackground(new java.awt.Color(51, 102, 255));
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        dateOfFlightChooser.setDateFormatString("dd-MMM-yyyy");

        javax.swing.GroupLayout flightAttributesPanelLayout = new javax.swing.GroupLayout(flightAttributesPanel);
        flightAttributesPanel.setLayout(flightAttributesPanelLayout);
        flightAttributesPanelLayout.setHorizontalGroup(
                flightAttributesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(flightAttributesPanelLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(flightAttributesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(flightAttributesPanelLayout.createSequentialGroup()
                                                .addComponent(addFlightButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(cancelButton)
                                                .addGap(20, 20, 20))
                                        .addGroup(flightAttributesPanelLayout.createSequentialGroup()
                                                .addGroup(flightAttributesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(seatsLabel)
                                                        .addComponent(fareLabel)
                                                        .addComponent(dateOfFlightLabel))
                                                .addGap(28, 28, 28)
                                                .addGroup(flightAttributesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(seatsTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                                                        .addComponent(fareTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                                                        .addComponent(dateOfFlightChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addContainerGap(20, Short.MAX_VALUE))))
        );
        flightAttributesPanelLayout.setVerticalGroup(
                flightAttributesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(flightAttributesPanelLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(flightAttributesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(seatsLabel)
                                        .addComponent(seatsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(flightAttributesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(fareLabel)
                                        .addComponent(fareTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(flightAttributesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(dateOfFlightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(dateOfFlightChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(flightAttributesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cancelButton)
                                        .addComponent(addFlightButton))
                                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pageTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(flightInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(flightAttributesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pageTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(flightInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(flightAttributesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addFlightButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFlightButtonActionPerformed
        String flightIdValue = flightIdTextField.getText();
        String flightNameValue = flightNameTextField.getText().trim();
        String arrivalValue = arrivalTextField.getText().trim();
        String departureValue = departureTextField.getText().trim();
        String durationValue = durationTextField.getText().trim();
        String seatsValue = seatsTextField.getText().trim();
        String fareValue = fareTextField.getText().trim();

        if (flightNameValue.isEmpty() || arrivalValue.isEmpty() || departureValue.isEmpty() ||
                durationValue.isEmpty() || seatsValue.isEmpty() || fareValue.isEmpty() ||
                dateOfFlightChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // More specific validation (examples)
        try {
            Integer.parseInt(seatsValue); // Check if seats is a number
            Double.parseDouble(fareValue);  // Check if fare is a number
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Seats and Fare must be valid numbers.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Add regex for duration format (e.g., "2h 30m") if needed

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String flightDateString = dateFormat.format(dateOfFlightChooser.getDate());

        String sql = "INSERT INTO flight (flightID, flightName, arrival, departure, duration, seats, Fare, DateOfFlight) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(sql)) {

            pre.setString(1, flightIdValue);
            pre.setString(2, flightNameValue);
            pre.setString(3, arrivalValue);
            pre.setString(4, departureValue);
            pre.setString(5, durationValue);
            pre.setString(6, seatsValue);
            pre.setString(7, fareValue);
            pre.setString(8, flightDateString);

            pre.executeUpdate();
            JOptionPane.showMessageDialog(this, "Flight Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear fields and generate new ID for the next entry
            clearInputFields();
            generateAutoFlightID();
            flightIdTextField.setEditable(false); // Keep it non-editable
            flightIdTextField.setBackground(DISPLAY_FIELD_BACKGROUND);


        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found.", ex);
            JOptionPane.showMessageDialog(this, "Database driver error: " + ex.getMessage(), "Driver Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error adding flight", ex);
            if (ex.getErrorCode() == 1062) { // MySQL error code for duplicate entry
                JOptionPane.showMessageDialog(this, "Error adding flight: Flight ID already exists.\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_addFlightButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        int response = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel and close this window?",
                "Confirm Cancel",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            this.dispose(); // Closes the JInternalFrame
        }
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Generates a new unique Flight ID (e.g., FL001, FL002) and sets it in the flightIdTextField.
     * @throws SQLException if a database access error occurs or the driver is not found.
     */
    public void generateAutoFlightID() throws SQLException {
        String query = "SELECT MAX(flightID) AS max_id FROM flight";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(query);
             ResultSet rs = pre.executeQuery()) {

            if (rs.next()) {
                String maxId = rs.getString("max_id");
                if (maxId == null || maxId.isEmpty()) {
                    flightIdTextField.setText("FL001");
                } else {
                    try {
                        // Assuming ID format is "FLXXX"
                        long idNumber = Long.parseLong(maxId.substring(2)); // Extract numeric part, "FL" is 2 chars
                        idNumber++;
                        flightIdTextField.setText("FL" + String.format("%03d", idNumber)); // Format to FL001, FL002, etc.
                    } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                        LOGGER.log(Level.WARNING, "Could not parse existing max flightID: '" + maxId + "'. Defaulting to FL001.", e);
                        flightIdTextField.setText("FL001"); // Fallback if parsing fails
                    }
                }
            } else {
                // This case should ideally not be reached if MAX() is used on a non-empty table,
                // but it's a fallback if the table is empty or rs.next() is false for some reason.
                flightIdTextField.setText("FL001");
            }

        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found during AutoID generation.", ex);
            // Re-throw as SQLException to be handled by the caller, or show error directly
            throw new SQLException("Database driver not found, cannot generate Flight ID.", ex);
        }
        // SQLExceptions from getConnection() or prepareStatement() will propagate.
    }

    /**
     * Clears all input fields on the form.
     */
    private void clearInputFields() {
        flightNameTextField.setText("");
        arrivalTextField.setText("");
        departureTextField.setText("");
        durationTextField.setText("");
        seatsTextField.setText("");
        fareTextField.setText("");
        dateOfFlightChooser.setDate(null); // Clears the date chooser
        flightNameTextField.requestFocus(); // Set focus to the first editable field
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addFlightButton;
    private javax.swing.JLabel arrivalLabel;
    private javax.swing.JTextField arrivalTextField;
    private javax.swing.JButton cancelButton;
    private com.toedter.calendar.JDateChooser dateOfFlightChooser;
    private javax.swing.JLabel dateOfFlightLabel;
    private javax.swing.JLabel departureLabel;
    private javax.swing.JTextField departureTextField;
    private javax.swing.JLabel durationLabel;
    private javax.swing.JTextField durationTextField;
    private javax.swing.JLabel fareLabel;
    private javax.swing.JTextField fareTextField;
    private javax.swing.JPanel flightAttributesPanel;
    private javax.swing.JLabel flightIdLabel;
    private javax.swing.JTextField flightIdTextField;
    private javax.swing.JPanel flightInfoPanel;
    private javax.swing.JLabel flightNameLabel;
    private javax.swing.JTextField flightNameTextField;
    private javax.swing.JLabel pageTitleLabel;
    private javax.swing.JLabel seatsLabel;
    private javax.swing.JTextField seatsTextField;
    // End of variables declaration//GEN-END:variables
}