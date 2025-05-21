/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date; // For DOB validation
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup; // For radio buttons
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import com.toedter.calendar.JTextFieldDateEditor; // For JDateChooser styling


/**
 * A JInternalFrame for adding new Customer records to the system.
 *
 * @author ranji
 */
public class AddCustomer extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(AddCustomer.class.getName());

    // Style Constants
    private static final Color FRAME_BACKGROUND = new Color(240, 243, 245); // Light grayish blue
    private static final Color PANEL_BACKGROUND = Color.WHITE;
    private static final Color TITLE_COLOR = new Color(0, 80, 150); // Professional Blue
    private static final Color LABEL_TEXT_COLOR = new Color(30, 30, 30);
    private static final Color BORDER_COLOR = new Color(200, 200, 205);
    private static final Color DISPLAY_FIELD_BACKGROUND = new Color(238, 241, 243); // For non-editable CustomerID
    private static final Color BUTTON_PRIMARY_BG = new Color(0, 122, 204);
    private static final Color BUTTON_CANCEL_BG = new Color(108, 117, 125);

    private static final Font TITLE_FONT = new Font("Segoe UI Semibold", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font TEXT_INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font RADIO_BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);

    private static final Dimension INPUT_FIELD_PREFERRED_SIZE = new Dimension(160, 30); // Consistent size

    /**
     * Establishes and returns a connection to the MySQL database.
     *
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
     * Generates and sets a new unique Customer ID in the customerIdTextField.
     * The ID is in the format "CSXXX", where XXX is a zero-padded number.
     */
    public void generateAutoCustomerID() {
        String sql = "SELECT MAX(CustomerID) AS max_id FROM Customer";
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {

            if (rs.next()) {
                String maxId = rs.getString("max_id");
                if (maxId == null || maxId.isEmpty()) {
                    customerIdTextField.setText("CS001");
                } else {
                    try {
                        long id = Long.parseLong(maxId.substring(2)); // Extract numeric part, "CS" is 2 chars
                        id++;
                        customerIdTextField.setText("CS" + String.format("%03d", id));
                    } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                        LOGGER.log(Level.WARNING, "Could not parse existing max CustomerID: '" + maxId + "'. Defaulting to CS001.", e);
                        customerIdTextField.setText("CS001"); // Fallback
                    }
                }
            } else {
                customerIdTextField.setText("CS001"); // Fallback if table is empty or rs.next() is false
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error during AutoID generation for Customer.", ex);
            JOptionPane.showMessageDialog(this, "Error generating Customer ID: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            customerIdTextField.setText("Error");
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found during AutoID for Customer.", ex);
            JOptionPane.showMessageDialog(this, "Database driver error: " + ex.getMessage(), "Driver Error", JOptionPane.ERROR_MESSAGE);
            customerIdTextField.setText("Error");
        }
    }

    /**
     * Creates new form AddCustomer.
     * Initializes components, sets background color, generates Customer ID,
     * and groups gender radio buttons.
     */
    public AddCustomer() {
        initComponents();
        applyCustomStyles();
        generateAutoCustomerID();
        customerIdTextField.setEditable(false);
        customerIdTextField.setBackground(DISPLAY_FIELD_BACKGROUND);
        customerIdTextField.setForeground(LABEL_TEXT_COLOR);


        // Group radio buttons
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);
        maleRadioButton.setSelected(true); // Default selection
    }

    private void applyCustomStyles() {
        this.getContentPane().setBackground(FRAME_BACKGROUND);

        // Title
        pageTitleLabel.setFont(TITLE_FONT);
        pageTitleLabel.setForeground(TITLE_COLOR);
        pageTitleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Panels
        Border panelBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );
        customerInfoPanel.setBackground(PANEL_BACKGROUND);
        customerInfoPanel.setBorder(panelBorder);
        contactAndPersonalInfoPanel.setBackground(PANEL_BACKGROUND);
        contactAndPersonalInfoPanel.setBorder(panelBorder);

        // Labels
        applyLabelStyle(customerIdLabel);
        applyLabelStyle(firstNameLabel);
        applyLabelStyle(lastNameLabel);
        applyLabelStyle(nationalIdLabel);
        applyLabelStyle(passportNoLabel);
        applyLabelStyle(addressLabel);
        applyLabelStyle(contactNoLabel);
        applyLabelStyle(genderLabel);
        applyLabelStyle(dobLabel);

        // Text Input Fields
        applyTextInputFieldStyle(customerIdTextField);
        applyTextInputFieldStyle(firstNameTextField);
        applyTextInputFieldStyle(lastNameTextField);
        applyTextInputFieldStyle(nationalIdTextField);
        applyTextInputFieldStyle(passportNoTextField);
        applyTextInputFieldStyle(contactNoTextField);

        // JTextArea
        addressTextArea.setFont(TEXT_INPUT_FONT);
        addressTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(4, 7, 4, 7)
        ));
        addressScrollPane.getViewport().setBackground(Color.WHITE); // Ensure scroll pane background matches

        // JDateChooser Style
        dobDateChooser.setFont(TEXT_INPUT_FONT);
        dobDateChooser.setPreferredSize(INPUT_FIELD_PREFERRED_SIZE);
        JTextFieldDateEditor editor = (JTextFieldDateEditor) dobDateChooser.getDateEditor();
        editor.setFont(TEXT_INPUT_FONT);
        editor.setBackground(Color.WHITE);
        editor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        editor.setEditable(false);
        dobDateChooser.getCalendarButton().setFont(TEXT_INPUT_FONT);
        dobDateChooser.getCalendarButton().setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Radio Buttons
        maleRadioButton.setFont(RADIO_BUTTON_FONT);
        maleRadioButton.setBackground(PANEL_BACKGROUND); // Match panel background
        femaleRadioButton.setFont(RADIO_BUTTON_FONT);
        femaleRadioButton.setBackground(PANEL_BACKGROUND);

        // Buttons
        styleButton(addButton, BUTTON_PRIMARY_BG, Color.WHITE);
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
        textField.setPreferredSize(INPUT_FIELD_PREFERRED_SIZE);
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setOpaque(true);
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
        customerInfoPanel = new javax.swing.JPanel();
        customerIdLabel = new javax.swing.JLabel();
        customerIdTextField = new javax.swing.JTextField();
        lastNameLabel = new javax.swing.JLabel();
        firstNameLabel = new javax.swing.JLabel();
        addressLabel = new javax.swing.JLabel();
        nationalIdLabel = new javax.swing.JLabel();
        firstNameTextField = new javax.swing.JTextField();
        lastNameTextField = new javax.swing.JTextField();
        passportNoTextField = new javax.swing.JTextField();
        addressScrollPane = new javax.swing.JScrollPane();
        addressTextArea = new javax.swing.JTextArea();
        passportNoLabel = new javax.swing.JLabel();
        nationalIdTextField = new javax.swing.JTextField();
        contactAndPersonalInfoPanel = new javax.swing.JPanel();
        contactNoLabel = new javax.swing.JLabel();
        genderLabel = new javax.swing.JLabel();
        maleRadioButton = new javax.swing.JRadioButton();
        femaleRadioButton = new javax.swing.JRadioButton();
        dobLabel = new javax.swing.JLabel();
        contactNoTextField = new javax.swing.JTextField();
        dobDateChooser = new com.toedter.calendar.JDateChooser();
        addButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Add New Customer");

        pageTitleLabel.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        pageTitleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pageTitleLabel.setText("Welcome to the Customer Panel");

        customerInfoPanel.setBackground(new java.awt.Color(224, 224, 224));
        customerInfoPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        customerInfoPanel.setForeground(new java.awt.Color(204, 204, 204));

        customerIdLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        customerIdLabel.setText("Customer ID : ");

        customerIdTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lastNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lastNameLabel.setText("Last Name : ");

        firstNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        firstNameLabel.setText("First Name :");

        addressLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        addressLabel.setText("Address :");

        nationalIdLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nationalIdLabel.setText("National ID :");

        firstNameTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lastNameTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        passportNoTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        addressTextArea.setColumns(20);
        addressTextArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        addressTextArea.setLineWrap(true);
        addressTextArea.setRows(3);
        addressTextArea.setWrapStyleWord(true);
        addressScrollPane.setViewportView(addressTextArea);

        passportNoLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        passportNoLabel.setText("Passport No.:");

        nationalIdTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout customerInfoPanelLayout = new javax.swing.GroupLayout(customerInfoPanel);
        customerInfoPanel.setLayout(customerInfoPanelLayout);
        customerInfoPanelLayout.setHorizontalGroup(
                customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(customerInfoPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lastNameLabel)
                                        .addComponent(firstNameLabel)
                                        .addComponent(customerIdLabel)
                                        .addComponent(addressLabel)
                                        .addComponent(passportNoLabel)
                                        .addComponent(nationalIdLabel))
                                .addGap(28, 28, 28)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(nationalIdTextField)
                                        .addComponent(passportNoTextField)
                                        .addComponent(lastNameTextField)
                                        .addComponent(customerIdTextField)
                                        .addComponent(firstNameTextField)
                                        .addComponent(addressScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                                .addContainerGap(20, Short.MAX_VALUE))
        );
        customerInfoPanelLayout.setVerticalGroup(
                customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(customerInfoPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(customerIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(customerIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(firstNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(firstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lastNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(nationalIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nationalIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(passportNoLabel)
                                        .addComponent(passportNoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(addressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addressScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(20, Short.MAX_VALUE))
        );

        contactAndPersonalInfoPanel.setBackground(new java.awt.Color(224, 224, 224));
        contactAndPersonalInfoPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        contactNoLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        contactNoLabel.setText("Contact :");

        genderLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        genderLabel.setText("Gender :");

        maleRadioButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        maleRadioButton.setText("Male");

        femaleRadioButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        femaleRadioButton.setText("Female");

        dobLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dobLabel.setText("Date of Birth :");

        contactNoTextField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        dobDateChooser.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout contactAndPersonalInfoPanelLayout = new javax.swing.GroupLayout(contactAndPersonalInfoPanel);
        contactAndPersonalInfoPanel.setLayout(contactAndPersonalInfoPanelLayout);
        contactAndPersonalInfoPanelLayout.setHorizontalGroup(
                contactAndPersonalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(contactAndPersonalInfoPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(contactAndPersonalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(contactAndPersonalInfoPanelLayout.createSequentialGroup()
                                                .addComponent(genderLabel)
                                                .addGap(45, 45, 45)
                                                .addComponent(maleRadioButton)
                                                .addGap(18, 18, 18)
                                                .addComponent(femaleRadioButton)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(contactAndPersonalInfoPanelLayout.createSequentialGroup()
                                                .addGroup(contactAndPersonalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(contactNoLabel)
                                                        .addComponent(dobLabel))
                                                .addGap(18, 18, 18)
                                                .addGroup(contactAndPersonalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(contactNoTextField)
                                                        .addComponent(dobDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))))
                                .addGap(20, 20, 20))
        );
        contactAndPersonalInfoPanelLayout.setVerticalGroup(
                contactAndPersonalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(contactAndPersonalInfoPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(contactAndPersonalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(contactNoLabel)
                                        .addComponent(contactNoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(contactAndPersonalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(genderLabel)
                                        .addComponent(maleRadioButton)
                                        .addComponent(femaleRadioButton))
                                .addGap(18, 18, 18)
                                .addGroup(contactAndPersonalInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(dobLabel)
                                        .addComponent(dobDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        addButton.setBackground(new java.awt.Color(0, 153, 255));
        addButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addButton.setForeground(new java.awt.Color(255, 255, 255));
        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        cancelButton.setBackground(new java.awt.Color(204, 204, 204));
        cancelButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(20, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pageTitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(customerInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(contactAndPersonalInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(11, 11, 11)))))
                                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(pageTitleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(customerInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(contactAndPersonalInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(10, 10, 10)))
                                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Handles the action performed when the "Add" button is clicked.
     * Validates input and adds a new customer to the database.
     *
     * @param evt The action event.
     */
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        String customerIdValue = customerIdTextField.getText();
        String firstNameValue = firstNameTextField.getText().trim();
        String lastNameValue = lastNameTextField.getText().trim();
        String passportNoValue = passportNoTextField.getText().trim();
        String nationalIDValue = nationalIdTextField.getText().trim();
        String addressValue = addressTextArea.getText().trim();
        String contactNoValue = contactNoTextField.getText().trim();
        Date dateOfBirthValue = dobDateChooser.getDate();

        // Input Validation
        if (customerIdValue.isEmpty() || firstNameValue.isEmpty() || lastNameValue.isEmpty() ||
                passportNoValue.isEmpty() || nationalIDValue.isEmpty() || addressValue.isEmpty() ||
                contactNoValue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if ("Error".equals(customerIdValue)) {
            JOptionPane.showMessageDialog(this, "Customer ID generation failed. Cannot add customer.", "ID Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (dateOfBirthValue == null) {
            JOptionPane.showMessageDialog(this, "Please select a Date of Birth.", "Input Error", JOptionPane.WARNING_MESSAGE);
            dobDateChooser.requestFocus();
            return;
        }
        // Basic contact number validation (e.g., 10 digits - adjust regex as needed for international numbers)
        if (!contactNoValue.matches("\\d{10}")) { // Simple 10-digit check
            JOptionPane.showMessageDialog(this, "Contact number must be 10 digits.", "Input Error", JOptionPane.WARNING_MESSAGE);
            contactNoTextField.requestFocus();
            return;
        }
        // Basic National ID and Passport validation (e.g., not empty, specific length/format if known)
        // Example: Passport number should not be too short
        if (passportNoValue.length() < 5) {
            JOptionPane.showMessageDialog(this, "Passport Number seems too short.", "Input Error", JOptionPane.WARNING_MESSAGE);
            passportNoTextField.requestFocus();
            return;
        }


        String genderValue = "";
        if (maleRadioButton.isSelected()) {
            genderValue = "Male";
        } else if (femaleRadioButton.isSelected()) {
            genderValue = "Female";
        } else {
            // This case should ideally not be reached if one is selected by default and they are in a ButtonGroup
            JOptionPane.showMessageDialog(this, "Please select a gender.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dobString = dateFormat.format(dateOfBirthValue);

        String sql = "INSERT INTO customer (customerID, firstName, lastName, passport, nationalID, Address, contact, Gender, DOB) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(sql)) {

            pre.setString(1, customerIdValue);
            pre.setString(2, firstNameValue);
            pre.setString(3, lastNameValue);
            pre.setString(4, passportNoValue);
            pre.setString(5, nationalIDValue);
            pre.setString(6, addressValue);
            pre.setString(7, contactNoValue);
            pre.setString(8, genderValue);
            pre.setString(9, dobString);

            pre.executeUpdate();

            JOptionPane.showMessageDialog(this, "Customer Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearInputFields();
            generateAutoCustomerID(); // Generate new ID for the next entry
            customerIdTextField.setEditable(false); // Re-apply non-editable status
            customerIdTextField.setBackground(DISPLAY_FIELD_BACKGROUND);


        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found while adding customer.", ex);
            JOptionPane.showMessageDialog(this, "Database driver error: " + ex.getMessage(), "Driver Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error while adding customer.", ex);
            if (ex.getErrorCode() == 1062) { // MySQL error code for duplicate entry
                JOptionPane.showMessageDialog(this, "Error adding customer: Customer ID, Passport No., or National ID already exists.\n" + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error adding customer: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_addButtonActionPerformed

    /**
     * Clears all input fields in the form.
     */
    private void clearInputFields() {
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        passportNoTextField.setText("");
        nationalIdTextField.setText("");
        addressTextArea.setText("");
        contactNoTextField.setText("");
        dobDateChooser.setDate(null);
        maleRadioButton.setSelected(true); // Reset gender to default
        firstNameTextField.requestFocus(); // Set focus to the first editable field
    }

    /**
     * Handles the action performed when the "Cancel" button is clicked.
     * Closes the AddCustomer internal frame after confirmation.
     *
     * @param evt The action event.
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        int response = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel and close this window?",
                "Confirm Cancel",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }//GEN-LAST:event_cancelButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JScrollPane addressScrollPane;
    private javax.swing.JTextArea addressTextArea;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel contactAndPersonalInfoPanel;
    private javax.swing.JLabel contactNoLabel;
    private javax.swing.JTextField contactNoTextField;
    private javax.swing.JLabel customerIdLabel;
    private javax.swing.JTextField customerIdTextField;
    private javax.swing.JPanel customerInfoPanel;
    private com.toedter.calendar.JDateChooser dobDateChooser;
    private javax.swing.JLabel dobLabel;
    private javax.swing.JRadioButton femaleRadioButton;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField firstNameTextField;
    private javax.swing.JLabel genderLabel;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JTextField lastNameTextField;
    private javax.swing.JRadioButton maleRadioButton;
    private javax.swing.JLabel nationalIdLabel;
    private javax.swing.JTextField nationalIdTextField;
    private javax.swing.JLabel pageTitleLabel;
    private javax.swing.JLabel passportNoLabel;
    private javax.swing.JTextField passportNoTextField;
    // End of variables declaration//GEN-END:variables
}