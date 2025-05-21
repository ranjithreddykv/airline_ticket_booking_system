package com.mycompany.airline_project;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * A JInternalFrame for searching existing Customer records by Customer ID.
 *
 * @author ranji
 */
public class SearchCustomer extends javax.swing.JInternalFrame {

    private static final Logger LOGGER = Logger.getLogger(SearchCustomer.class.getName());

    // Consistent Grayish Theme Style Constants
    private static final Color FRAME_BACKGROUND = new Color(235, 235, 235);
    private static final Color PANEL_BACKGROUND = new Color(220, 220, 220);
    private static final Color TITLE_TEXT_COLOR = new Color(40, 40, 40);
    private static final Color LABEL_TEXT_COLOR = new Color(60, 60, 60);
    private static final Color TEXT_INPUT_BACKGROUND = Color.WHITE;
    private static final Color TEXT_INPUT_FOREGROUND = new Color(50, 50, 50);
    private static final Color DISPLAY_FIELD_BACKGROUND = new Color(230, 230, 230);
    private static final Color DISPLAY_FIELD_TEXT_COLOR = new Color(70, 70, 70);
    private static final Color BUTTON_BACKGROUND_COLOR = new Color(190, 190, 190);
    private static final Color BUTTON_FOREGROUND_COLOR = Color.BLACK;
    private static final Color BORDER_COLOR_LIGHT = new Color(200, 200, 200);
    private static final Color BORDER_COLOR_FIELD = new Color(180, 180, 180);

    private static final Font TITLE_FONT = new Font("Segoe UI Semibold", Font.BOLD, 26);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font TEXT_INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);


    /**
     * Establishes and returns a connection to the MySQL database.
     */
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1/airline-database", "root", "Ranjith@1654R");
    }

    /**
     * Creates new form SearchCustomer.
     */
    public SearchCustomer() {
        initComponents();
        applyCustomStyles();
        setDetailFieldsEditable(false); // Initially, detail fields are non-editable
    }

    /**
     * Applies custom visual styles to the components.
     */
    private void applyCustomStyles() {
        this.getContentPane().setBackground(FRAME_BACKGROUND);

        // Title Label
        titleHeaderLabel.setFont(TITLE_FONT);
        titleHeaderLabel.setForeground(TITLE_TEXT_COLOR);
        titleHeaderLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 25, 0));

        // Panel Styling
        Border panelInnerBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        Border panelOuterBorder = BorderFactory.createLineBorder(BORDER_COLOR_LIGHT, 1);
        Border panelCompoundBorder = BorderFactory.createCompoundBorder(panelOuterBorder, panelInnerBorder);

        customerInfoPanel.setBackground(PANEL_BACKGROUND);
        customerInfoPanel.setBorder(panelCompoundBorder);
        contactDetailsPanel.setBackground(PANEL_BACKGROUND);
        contactDetailsPanel.setBorder(panelCompoundBorder);

        // Labels
        applyLabelStyle(customerIdLabel);
        applyLabelStyle(firstNameLabel);
        applyLabelStyle(lastNameLabel);
        applyLabelStyle(passportNoLabel);
        applyLabelStyle(nationalIdLabel);
        applyLabelStyle(addressLabel);
        applyLabelStyle(contactLabel);
        applyLabelStyle(genderLabel);
        applyLabelStyle(dateOfBirthLabel);


        // Customer ID field (always editable for input)
        customerIdField.setFont(TEXT_INPUT_FONT);
        customerIdField.setBackground(TEXT_INPUT_BACKGROUND);
        customerIdField.setForeground(TEXT_INPUT_FOREGROUND);
        customerIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR_FIELD),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));

        // Buttons
        styleButton(searchButton, BUTTON_BACKGROUND_COLOR, BUTTON_FOREGROUND_COLOR);
        styleButton(clearButton, BUTTON_BACKGROUND_COLOR, BUTTON_FOREGROUND_COLOR);

        // Call setFieldsEditable to apply initial styling to display fields
        setDetailFieldsEditable(false);
    }

    private void applyLabelStyle(javax.swing.JLabel label) {
        label.setFont(LABEL_FONT);
        label.setForeground(LABEL_TEXT_COLOR);
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        int topBottomPadding = 8;
        int leftRightPadding = 18;
        button.setBorder(BorderFactory.createEmptyBorder(topBottomPadding, leftRightPadding, topBottomPadding, leftRightPadding));
    }

    /**
     * Sets the editable state and styles for customer detail fields (excluding customer ID).
     * @param editable true if fields should be editable, false otherwise.
     */
    private void setDetailFieldsEditable(boolean editable) {
        JTextField[] displayFields = {firstNameField, lastNameField, passportNoField, nationalIdField, contactField, genderField, dateOfBirthField};
        Border fieldBorder;

        if (!editable) {
            fieldBorder = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR_FIELD),
                    BorderFactory.createEmptyBorder(3, 6, 3, 6)
            );
        } else {
            // For editable state, use a slightly different or default look if desired
            fieldBorder = BorderFactory.createCompoundBorder(
                    UIManager.getBorder("TextField.border"), // Default system border
                    BorderFactory.createEmptyBorder(1, 4, 1, 4) // Minimal padding
            );
        }

        for (JTextField field : displayFields) {
            field.setEditable(editable);
            field.setFont(TEXT_INPUT_FONT);
            field.setBorder(fieldBorder);
            if (!editable) {
                field.setBackground(DISPLAY_FIELD_BACKGROUND);
                field.setForeground(DISPLAY_FIELD_TEXT_COLOR);
            } else {
                field.setBackground(TEXT_INPUT_BACKGROUND);
                field.setForeground(TEXT_INPUT_FOREGROUND);
            }
        }

        // Address JTextArea
        addressTextArea.setEditable(editable);
        addressTextArea.setFont(TEXT_INPUT_FONT);
        addressScrollPane.setBorder(fieldBorder); // Apply same border to scrollpane

        if (!editable) {
            addressTextArea.setBackground(DISPLAY_FIELD_BACKGROUND);
            addressTextArea.setForeground(DISPLAY_FIELD_TEXT_COLOR);
            addressScrollPane.getViewport().setBackground(DISPLAY_FIELD_BACKGROUND);
        } else {
            addressTextArea.setBackground(TEXT_INPUT_BACKGROUND);
            addressTextArea.setForeground(TEXT_INPUT_FOREGROUND);
            addressScrollPane.getViewport().setBackground(TEXT_INPUT_BACKGROUND);
        }
    }

    private void clearFieldsAndResetState() {
        firstNameField.setText("");
        lastNameField.setText("");
        passportNoField.setText("");
        nationalIdField.setText("");
        addressTextArea.setText("");
        genderField.setText("");
        dateOfBirthField.setText("");
        contactField.setText("");
        setDetailFieldsEditable(false); // Re-apply non-editable styling to detail fields
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleHeaderLabel = new javax.swing.JLabel();
        customerInfoPanel = new javax.swing.JPanel();
        customerIdLabel = new javax.swing.JLabel();
        customerIdField = new javax.swing.JTextField();
        lastNameLabel = new javax.swing.JLabel();
        firstNameLabel = new javax.swing.JLabel();
        addressLabel = new javax.swing.JLabel();
        nationalIdLabel = new javax.swing.JLabel();
        firstNameField = new javax.swing.JTextField();
        lastNameField = new javax.swing.JTextField();
        nationalIdField = new javax.swing.JTextField();
        addressScrollPane = new javax.swing.JScrollPane();
        addressTextArea = new javax.swing.JTextArea();
        searchButton = new javax.swing.JButton();
        passportNoLabel = new javax.swing.JLabel();
        passportNoField = new javax.swing.JTextField();
        contactDetailsPanel = new javax.swing.JPanel();
        genderLabel = new javax.swing.JLabel();
        dateOfBirthLabel = new javax.swing.JLabel();
        contactLabel = new javax.swing.JLabel();
        genderField = new javax.swing.JTextField();
        dateOfBirthField = new javax.swing.JTextField();
        contactField = new javax.swing.JTextField();
        clearButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Search Customer Details");
        setPreferredSize(new java.awt.Dimension(820, 550));

        titleHeaderLabel.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        titleHeaderLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleHeaderLabel.setText("Customer Search Panel");

        customerInfoPanel.setBackground(new java.awt.Color(224, 224, 224));
        customerInfoPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        customerInfoPanel.setForeground(new java.awt.Color(204, 204, 204));

        customerIdLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        customerIdLabel.setText("Customer ID : ");

        customerIdField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        customerIdField.setPreferredSize(new java.awt.Dimension(100, 26));
        customerIdField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerIdFieldActionPerformed(evt);
            }
        });

        lastNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lastNameLabel.setText("Last Name : ");

        firstNameLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        firstNameLabel.setText("First Name :");

        addressLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        addressLabel.setText("Address :");

        nationalIdLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nationalIdLabel.setText("National ID :");

        firstNameField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        firstNameField.setPreferredSize(new java.awt.Dimension(150, 26));

        lastNameField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lastNameField.setPreferredSize(new java.awt.Dimension(150, 26));

        nationalIdField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        nationalIdField.setPreferredSize(new java.awt.Dimension(150, 26));

        addressTextArea.setColumns(20);
        addressTextArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        addressTextArea.setLineWrap(true);
        addressTextArea.setRows(3);
        addressTextArea.setWrapStyleWord(true);
        addressScrollPane.setViewportView(addressTextArea);

        searchButton.setBackground(new java.awt.Color(0, 122, 204));
        searchButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        searchButton.setForeground(new java.awt.Color(255, 255, 255));
        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        passportNoLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        passportNoLabel.setText("Passport No.:");

        passportNoField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        passportNoField.setPreferredSize(new java.awt.Dimension(150, 26));

        javax.swing.GroupLayout customerInfoPanelLayout = new javax.swing.GroupLayout(customerInfoPanel);
        customerInfoPanel.setLayout(customerInfoPanelLayout);
        customerInfoPanelLayout.setHorizontalGroup(
                customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(customerInfoPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(firstNameLabel)
                                        .addComponent(lastNameLabel)
                                        .addComponent(passportNoLabel)
                                        .addComponent(nationalIdLabel)
                                        .addComponent(addressLabel)
                                        .addComponent(customerIdLabel))
                                .addGap(28, 28, 28)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(customerInfoPanelLayout.createSequentialGroup()
                                                .addComponent(customerIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(firstNameField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lastNameField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(passportNoField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(nationalIdField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(addressScrollPane))
                                .addContainerGap(20, Short.MAX_VALUE))
        );
        customerInfoPanelLayout.setVerticalGroup(
                customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(customerInfoPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(customerIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(customerIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(firstNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(firstNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lastNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lastNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(passportNoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(passportNoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(nationalIdLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(nationalIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(customerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(addressLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addressScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(20, Short.MAX_VALUE))
        );

        contactDetailsPanel.setBackground(new java.awt.Color(224, 224, 224));
        contactDetailsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        genderLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        genderLabel.setText("Gender :");

        dateOfBirthLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dateOfBirthLabel.setText("Date of Birth :");

        contactLabel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        contactLabel.setText("Contact :");

        genderField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        genderField.setPreferredSize(new java.awt.Dimension(150, 26));

        dateOfBirthField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dateOfBirthField.setPreferredSize(new java.awt.Dimension(150, 26));

        contactField.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        contactField.setPreferredSize(new java.awt.Dimension(150, 26));

        javax.swing.GroupLayout contactDetailsPanelLayout = new javax.swing.GroupLayout(contactDetailsPanel);
        contactDetailsPanel.setLayout(contactDetailsPanelLayout);
        contactDetailsPanelLayout.setHorizontalGroup(
                contactDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(contactDetailsPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(contactDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(dateOfBirthLabel)
                                        .addComponent(genderLabel)
                                        .addComponent(contactLabel))
                                .addGap(18, 18, 18)
                                .addGroup(contactDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(contactField, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                        .addComponent(genderField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(dateOfBirthField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(20, 20, 20))
        );
        contactDetailsPanelLayout.setVerticalGroup(
                contactDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(contactDetailsPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(contactDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(contactLabel)
                                        .addComponent(contactField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(contactDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(genderLabel)
                                        .addComponent(genderField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(contactDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(dateOfBirthLabel)
                                        .addComponent(dateOfBirthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        clearButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(28, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(titleHeaderLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(customerInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(contactDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(11, 11, 11)))))
                                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(titleHeaderLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(customerInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(contactDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)))
                                .addContainerGap(38, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Handles the action when the Search button is clicked or Enter is pressed in Customer ID field.
     * Fetches customer details from the database.
     */
    private void performSearch() {
        String custID = customerIdField.getText().trim();
        if (custID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Customer ID to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
            customerIdField.requestFocus();
            return;
        }

        // Clear previous results but keep the entered customer ID for context
        String currentCustIdText = customerIdField.getText();
        clearFieldsAndResetState(); // Clears detail fields and applies non-editable styling
        customerIdField.setText(currentCustIdText); // Restore the ID in the input field


        String sql = "SELECT * FROM customer WHERE customerID = ?";
        // Using try-with-resources for automatic resource management
        try (Connection con = getConnection();
             PreparedStatement pre = con.prepareStatement(sql)) {

            pre.setString(1, custID);
            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    firstNameField.setText(rs.getString("firstName"));
                    lastNameField.setText(rs.getString("lastName"));
                    passportNoField.setText(rs.getString("passport"));
                    nationalIdField.setText(rs.getString("nationalID"));
                    addressTextArea.setText(rs.getString("Address"));
                    genderField.setText(rs.getString("Gender"));

                    String dobFromDB = rs.getString("DOB");
                    if (dobFromDB != null && !dobFromDB.isEmpty()) {
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dobFromDB);
                            dateOfBirthField.setText(new SimpleDateFormat("dd-MMM-yyyy").format(date));
                        } catch (ParseException e) {
                            LOGGER.log(Level.WARNING, "Error parsing date from DB: " + dobFromDB, e);
                            dateOfBirthField.setText(dobFromDB); // Show raw date if parsing fails
                        }
                    } else {
                        dateOfBirthField.setText("");
                    }

                    contactField.setText(rs.getString("contact"));
                    // Fields are already non-editable due to clearFieldsAndResetState earlier
                    // or the initial call to setDetailFieldsEditable(false).
                    // If you specifically wanted to ensure they are non-editable *after* finding results:
                    setDetailFieldsEditable(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Customer with ID '" + custID + "' not found.", "No Results", JOptionPane.INFORMATION_MESSAGE);
                    // Detail fields are already cleared by clearFieldsAndResetState()
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL error during customer search.", ex);
            JOptionPane.showMessageDialog(this, "Database error during search: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found.", ex);
            JOptionPane.showMessageDialog(this, "Database driver error: " + ex.getMessage(), "Driver Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        performSearch();
    }//GEN-LAST:event_searchButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        customerIdField.setText(""); // Clear the customer ID input field as well
        clearFieldsAndResetState();
        customerIdField.requestFocus();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void customerIdFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerIdFieldActionPerformed
        performSearch(); // Allow search on Enter key press in customerid field
    }//GEN-LAST:event_customerIdFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane addressScrollPane;
    private javax.swing.JTextArea addressTextArea;
    private javax.swing.JLabel addressLabel;
    private javax.swing.JButton clearButton;
    private javax.swing.JTextField contactField;
    private javax.swing.JLabel contactLabel;
    private javax.swing.JPanel contactDetailsPanel;
    private javax.swing.JTextField customerIdField;
    private javax.swing.JLabel customerIdLabel;
    private javax.swing.JPanel customerInfoPanel;
    private javax.swing.JTextField dateOfBirthField;
    private javax.swing.JLabel dateOfBirthLabel;
    private javax.swing.JTextField firstNameField;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField genderField;
    private javax.swing.JLabel genderLabel;
    private javax.swing.JTextField lastNameField;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JTextField nationalIdField;
    private javax.swing.JLabel nationalIdLabel;
    private javax.swing.JTextField passportNoField;
    private javax.swing.JLabel passportNoLabel;
    private javax.swing.JButton searchButton;
    private javax.swing.JLabel titleHeaderLabel;
    // End of variables declaration//GEN-END:variables
}