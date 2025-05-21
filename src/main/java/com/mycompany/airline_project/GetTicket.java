package com.mycompany.airline_project;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator; // Added for visual separation
import javax.swing.JTextField;
// import javax.swing.border.Border; // Not explicitly used after changes

/**
 * JInternalFrame to retrieve and display E-Ticket details based on Ticket ID.
 * @author ranji
 */
public class GetTicket extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(GetTicket.class.getName());

    // --- UI Styling Constants ---
    private static final Color FRAME_BACKGROUND = new Color(235, 235, 235);
    private static final Color TICKET_DISPLAY_PANEL_BACKGROUND = new Color(248, 248, 245); // Light cream/off-white for ticket
    private static final Color INPUT_LABEL_TEXT_COLOR = new Color(40, 40, 40);
    private static final Color FOOTER_MESSAGE_TEXT_COLOR = new Color(50, 100, 70);
    private static final Color DESCRIPTIVE_LABEL_TEXT_COLOR = new Color(60, 60, 60);
    private static final Color DATA_LABEL_TEXT_COLOR = new Color(30, 30, 30); // Darker for data
    private static final Color DATA_FIELD_BACKGROUND_COLOR = Color.WHITE; // For input field
    private static final Color BORDER_COLOR_INPUT_FIELD = new Color(180, 180, 180);
    private static final Color BORDER_COLOR_TICKET_OUTLINE = new Color(150, 150, 150); // Slightly darker outline

    // Button Colors
    private static final Color BUTTON_PRIMARY_BACKGROUND = new Color(0, 122, 204); // Blue
    private static final Color BUTTON_PRIMARY_FOREGROUND = Color.WHITE;

    // Fonts
    private static final Font INPUT_PROMPT_FONT = new Font("Segoe UI Semibold", Font.BOLD, 16);
    private static final Font TICKET_MAIN_TITLE_FONT = new Font("Tahoma", Font.BOLD, 20);
    private static final Font FOOTER_MESSAGE_FONT = new Font("Segoe UI", Font.ITALIC | Font.BOLD, 16);
    private static final Font DESCRIPTIVE_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font DATA_VALUE_FONT = new Font("Segoe UI", Font.PLAIN, 13); // Monospaced can also be an option: new Font("Monospaced", Font.PLAIN, 13)
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    // Dimensions
    private static final Dimension DATA_VALUE_LABEL_PREFERRED_SIZE = new Dimension(160, 28); // Slightly wider
    private static final Dimension TICKET_PANEL_PREFERRED_SIZE = new Dimension(720, 300); // Adjusted size for new elements


    /**
     * Establishes a connection to the MySQL database.
     * @return A Connection object.
     * @throws SQLException If a database access error occurs.
     * @throws ClassNotFoundException If the JDBC driver class is not found.
     */
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        // Consider externalizing connection details for production
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1/airline-database", "root", "Ranjith@1654R");
    }

    /**
     * Creates new form GetTicket.
     */
    public GetTicket() {
        initComponents();
        applyCustomStyles();
        clearTicketDetails(); // Initialize labels with "N/A"
    }

    /**
     * Applies custom visual styles to the UI components.
     */
    private void applyCustomStyles() {
        this.getContentPane().setBackground(FRAME_BACKGROUND);
        this.setTitle("Retrieve Your E-Ticket");

        ticketIdPromptLabel.setFont(INPUT_PROMPT_FONT);
        ticketIdPromptLabel.setForeground(INPUT_LABEL_TEXT_COLOR);

        ticketIdTextField.setFont(DATA_VALUE_FONT);
        ticketIdTextField.setBackground(DATA_FIELD_BACKGROUND_COLOR);
        ticketIdTextField.setForeground(DATA_LABEL_TEXT_COLOR);
        ticketIdTextField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR_INPUT_FIELD),
                BorderFactory.createEmptyBorder(4, 7, 4, 7)
        ));

        styleButton(retrieveTicketButton, BUTTON_PRIMARY_BACKGROUND, BUTTON_PRIMARY_FOREGROUND);

        ticketDisplayPanel.setBackground(TICKET_DISPLAY_PANEL_BACKGROUND);
        ticketDisplayPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR_TICKET_OUTLINE, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15) // Inner padding for the ticket
        ));
        ticketDisplayPanel.setPreferredSize(TICKET_PANEL_PREFERRED_SIZE);
        ticketDisplayPanel.setMinimumSize(TICKET_PANEL_PREFERRED_SIZE);

        eTicketMainTitleLabel.setFont(TICKET_MAIN_TITLE_FONT);
        eTicketMainTitleLabel.setForeground(new Color(0, 80, 150)); // Airline blue
        eTicketMainTitleLabel.setHorizontalAlignment(JLabel.CENTER);

        ticketFooterMessageLabel.setFont(FOOTER_MESSAGE_FONT);
        ticketFooterMessageLabel.setForeground(FOOTER_MESSAGE_TEXT_COLOR);
        ticketFooterMessageLabel.setHorizontalAlignment(JLabel.CENTER);

        // Apply styles to descriptive title labels (e.g., "First Name:")
        applyDescriptiveTitleLabelStyle(firstNameTitleLabel);
        applyDescriptiveTitleLabelStyle(lastNameTitleLabel);
        applyDescriptiveTitleLabelStyle(genderTitleLabel);
        applyDescriptiveTitleLabelStyle(arrivalTitleLabel);
        applyDescriptiveTitleLabelStyle(departureTitleLabel);
        applyDescriptiveTitleLabelStyle(contactNoTitleLabel);
        applyDescriptiveTitleLabelStyle(flightIdTitleLabel);

        // Apply styles to data value labels (e.g., the actual first name)
        applyDataValueLabelStyle(firstNameValueLabel);
        applyDataValueLabelStyle(lastNameValueLabel);
        applyDataValueLabelStyle(genderValueLabel);
        applyDataValueLabelStyle(arrivalValueLabel);
        applyDataValueLabelStyle(departureValueLabel);
        applyDataValueLabelStyle(contactNoValueLabel);
        applyDataValueLabelStyle(flightIdValueLabel);
    }

    private void applyDescriptiveTitleLabelStyle(JLabel label) {
        label.setFont(DESCRIPTIVE_TITLE_FONT);
        label.setForeground(DESCRIPTIVE_LABEL_TEXT_COLOR);
    }

    private void applyDataValueLabelStyle(JLabel label) {
        label.setFont(DATA_VALUE_FONT);
        label.setForeground(DATA_LABEL_TEXT_COLOR);
        // Removed individual borders and background for a cleaner look on the ticket
        label.setOpaque(false);
        label.setPreferredSize(DATA_VALUE_LABEL_PREFERRED_SIZE);
        label.setMinimumSize(DATA_VALUE_LABEL_PREFERRED_SIZE);
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
    }

    /**
     * Clears all ticket detail display labels and sets them to "N/A".
     */
    private void clearTicketDetails() {
        firstNameValueLabel.setText("N/A");
        lastNameValueLabel.setText("N/A");
        genderValueLabel.setText("N/A");
        arrivalValueLabel.setText("N/A");
        departureValueLabel.setText("N/A");
        contactNoValueLabel.setText("N/A");
        flightIdValueLabel.setText("N/A");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ticketIdPromptLabel = new javax.swing.JLabel();
        ticketIdTextField = new javax.swing.JTextField();
        retrieveTicketButton = new javax.swing.JButton();
        ticketDisplayPanel = new javax.swing.JPanel();
        ticketFooterMessageLabel = new javax.swing.JLabel();
        firstNameTitleLabel = new javax.swing.JLabel();
        lastNameTitleLabel = new javax.swing.JLabel();
        genderTitleLabel = new javax.swing.JLabel();
        arrivalTitleLabel = new javax.swing.JLabel();
        departureTitleLabel = new javax.swing.JLabel();
        contactNoTitleLabel = new javax.swing.JLabel();
        firstNameValueLabel = new javax.swing.JLabel();
        flightIdValueLabel = new javax.swing.JLabel();
        arrivalValueLabel = new javax.swing.JLabel();
        departureValueLabel = new javax.swing.JLabel();
        contactNoValueLabel = new javax.swing.JLabel();
        lastNameValueLabel = new javax.swing.JLabel();
        genderValueLabel = new javax.swing.JLabel();
        flightIdTitleLabel = new javax.swing.JLabel();
        eTicketMainTitleLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        ticketIdPromptLabel.setText("Ticket ID :");

        ticketIdTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ticketIdTextFieldActionPerformed(evt);
            }
        });

        retrieveTicketButton.setText("Get Ticket");
        retrieveTicketButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retrieveTicketButtonActionPerformed(evt);
            }
        });

        ticketFooterMessageLabel.setFont(new java.awt.Font("Times New Roman", 1, 18)); // Will be overridden by custom styles
        ticketFooterMessageLabel.setText("Thanks For Choosing Our AirLines");

        firstNameTitleLabel.setText("First Name:");

        lastNameTitleLabel.setText("Last Name:");

        genderTitleLabel.setText("Gender :");

        arrivalTitleLabel.setText("Arrival:");

        departureTitleLabel.setText("Departure:");

        contactNoTitleLabel.setText("Contact No. :");

        firstNameValueLabel.setText("N/A");

        flightIdValueLabel.setText("N/A");

        arrivalValueLabel.setText("N/A");

        departureValueLabel.setText("N/A");

        contactNoValueLabel.setText("N/A");

        lastNameValueLabel.setText("N/A");

        genderValueLabel.setText("N/A");

        flightIdTitleLabel.setText("Flight ID:");

        eTicketMainTitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 20)); // Will be overridden
        eTicketMainTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eTicketMainTitleLabel.setText("ELECTRONIC FLIGHT TICKET");

        javax.swing.GroupLayout ticketDisplayPanelLayout = new javax.swing.GroupLayout(ticketDisplayPanel);
        ticketDisplayPanel.setLayout(ticketDisplayPanelLayout);
        ticketDisplayPanelLayout.setHorizontalGroup(
                ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(ticketDisplayPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(eTicketMainTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jSeparator1)
                                        .addComponent(ticketFooterMessageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(ticketDisplayPanelLayout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addGroup(ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(firstNameTitleLabel)
                                                        .addComponent(lastNameTitleLabel)
                                                        .addComponent(genderTitleLabel))
                                                .addGap(35, 35, 35)
                                                .addGroup(ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(genderValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lastNameValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(firstNameValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                                                .addGroup(ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(arrivalTitleLabel)
                                                        .addComponent(departureTitleLabel)
                                                        .addComponent(contactNoTitleLabel)
                                                        .addComponent(flightIdTitleLabel))
                                                .addGap(35, 35, 35)
                                                .addGroup(ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(flightIdValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(contactNoValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(departureValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(arrivalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(24, 24, 24))
                                        .addComponent(jSeparator2))
                                .addContainerGap())
        );
        ticketDisplayPanelLayout.setVerticalGroup(
                ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ticketDisplayPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(eTicketMainTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(ticketDisplayPanelLayout.createSequentialGroup()
                                                .addGroup(ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(firstNameTitleLabel)
                                                        .addComponent(firstNameValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lastNameTitleLabel)
                                                        .addComponent(lastNameValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(genderTitleLabel)
                                                        .addComponent(genderValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(ticketDisplayPanelLayout.createSequentialGroup()
                                                .addGroup(ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(arrivalTitleLabel)
                                                        .addComponent(arrivalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(departureTitleLabel)
                                                        .addComponent(departureValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(contactNoTitleLabel)
                                                        .addComponent(contactNoValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(ticketDisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(flightIdValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(flightIdTitleLabel))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ticketFooterMessageLabel)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(249, 249, 249)
                                                .addComponent(ticketIdPromptLabel)
                                                .addGap(18, 18, 18)
                                                .addComponent(ticketIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(313, 313, 313)
                                                .addComponent(retrieveTicketButton))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(50, 50, 50)
                                                .addComponent(ticketDisplayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(ticketIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ticketIdPromptLabel))
                                .addGap(18, 18, 18)
                                .addComponent(retrieveTicketButton)
                                .addGap(18, 18, 18)
                                .addComponent(ticketDisplayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void retrieveTicketButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retrieveTicketButtonActionPerformed
        String ticketIDInput = ticketIdTextField.getText().trim();
        if (ticketIDInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Ticket ID.", "Input Required", JOptionPane.WARNING_MESSAGE);
            ticketIdTextField.requestFocus();
            return;
        }
        clearTicketDetails(); // Clear previous data before new search

        String sqlQuery = "SELECT * FROM ticket WHERE ticketid=?";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(sqlQuery)) {

            pre.setString(1, ticketIDInput);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    firstNameValueLabel.setText(rs.getString("FirstName"));
                    lastNameValueLabel.setText(rs.getString("LastName"));
                    arrivalValueLabel.setText(rs.getString("Arrival"));
                    departureValueLabel.setText(rs.getString("Departure"));
                    genderValueLabel.setText(rs.getString("Gender"));
                    // Ensure 'contact' is the correct column name in your database
                    contactNoValueLabel.setText(rs.getString("contact"));
                    // Ensure 'FligthID' is the correct column name (potential typo 'FlightID'?)
                    flightIdValueLabel.setText(rs.getString("FligthID"));
                } else {
                    JOptionPane.showMessageDialog(this, "Ticket with ID '" + ticketIDInput + "' not found.", "No Ticket Found", JOptionPane.INFORMATION_MESSAGE);
                    // Ticket details are already cleared by clearTicketDetails()
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error fetching ticket", ex);
            JOptionPane.showMessageDialog(this, "Database error occurred: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found.", ex);
            JOptionPane.showMessageDialog(this, "Database driver error: " + ex.getMessage(), "Driver Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_retrieveTicketButtonActionPerformed

    private void ticketIdTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ticketIdTextFieldActionPerformed
        // Trigger the "Get Ticket" button's action when Enter is pressed in the input field
        retrieveTicketButton.doClick();
    }//GEN-LAST:event_ticketIdTextFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel arrivalTitleLabel;
    private javax.swing.JLabel arrivalValueLabel;
    private javax.swing.JLabel contactNoTitleLabel;
    private javax.swing.JLabel contactNoValueLabel;
    private javax.swing.JLabel departureTitleLabel;
    private javax.swing.JLabel departureValueLabel;
    private javax.swing.JLabel eTicketMainTitleLabel;
    private javax.swing.JLabel firstNameTitleLabel;
    private javax.swing.JLabel firstNameValueLabel;
    private javax.swing.JLabel flightIdTitleLabel;
    private javax.swing.JLabel flightIdValueLabel;
    private javax.swing.JLabel genderTitleLabel;
    private javax.swing.JLabel genderValueLabel;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lastNameTitleLabel;
    private javax.swing.JLabel lastNameValueLabel;
    private javax.swing.JButton retrieveTicketButton;
    private javax.swing.JPanel ticketDisplayPanel;
    private javax.swing.JLabel ticketFooterMessageLabel;
    private javax.swing.JTextField ticketIdTextField;
    private javax.swing.JLabel ticketIdPromptLabel;
    // End of variables declaration//GEN-END:variables
}