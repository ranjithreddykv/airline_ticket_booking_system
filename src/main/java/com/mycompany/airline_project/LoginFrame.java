/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package com.mycompany.airline_project;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
// No need for java.awt.event.ActionEvent and ActionListener here if using lambda or anonymous inner class directly
import java.sql.*;
// import java.sql.DriverManager; // Example for a real getConnection
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginFrame extends javax.swing.JFrame {

    // Declare UI components as instance variables
    private JPanel headerPanel;
    private JLabel titleLabel;
    private JPanel loginFormPanel;
    private JLabel usernameLabel;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;

    public LoginFrame() {
        initComponents();
        setTitle("Login - Airline Management System"); // Set a title for the frame
        setLocationRelativeTo(null); // Center the frame on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensure the application exits when the frame is closed
    }

    private void initComponents() { // Renamed from setupUI for convention, and it's the main init method
        setResizable(false);

        headerPanel = new JPanel();
        titleLabel = new JLabel();
        loginFormPanel = new JPanel();
        usernameLabel = new JLabel();
        usernameField = new JTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        loginButton = new JButton();
        cancelButton = new JButton();

        headerPanel.setBackground(new java.awt.Color(51, 102, 255));
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        titleLabel.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Airline Management System");
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
                headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        headerPanelLayout.setVerticalGroup(
                headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(headerPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(titleLabel)
                                .addContainerGap(20, Short.MAX_VALUE))
        );

        loginFormPanel.setBackground(new java.awt.Color(255, 255, 255));
        loginFormPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        usernameLabel.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setText("Username:");

        usernameField.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)),
                javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        passwordLabel.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setText("Password:");

        passwordField.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)),
                javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        loginButton.setBackground(new java.awt.Color(51, 102, 255));
        loginButton.setFont(new java.awt.Font("Segoe UI", Font.BOLD, 14));
        loginButton.setForeground(new java.awt.Color(255, 255, 255));
        loginButton.setText("Login");
        loginButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginButtonActionPerformed(evt);
            }
        });

        cancelButton.setBackground(new java.awt.Color(255, 255, 255));
        cancelButton.setFont(new java.awt.Font("Segoe UI", Font.PLAIN, 14));
        cancelButton.setText("Cancel");
        cancelButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                System.exit(0); // Exits the application
            }
        });

        javax.swing.GroupLayout loginFormPanelLayout = new javax.swing.GroupLayout(loginFormPanel);
        loginFormPanel.setLayout(loginFormPanelLayout);
        loginFormPanelLayout.setHorizontalGroup(
                loginFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(loginFormPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(loginFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(usernameLabel)
                                        .addComponent(passwordLabel))
                                .addGap(18, 18, 18)
                                .addGroup(loginFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(usernameField)
                                        .addComponent(passwordField)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginFormPanelLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
        );
        loginFormPanelLayout.setVerticalGroup(
                loginFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(loginFormPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(loginFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(usernameLabel)
                                        .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(loginFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(passwordLabel)
                                        .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(loginFormPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(loginButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        // Use 'this.getContentPane()' as LoginFrame is now a JFrame
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(loginFormPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(loginFormPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack(); // Use 'this.pack()'
    }

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String inputUsername = usernameField.getText();
        String inputPassword = new String(passwordField.getPassword());

        if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
            // 'this' now correctly refers to the LoginFrame (a Component)
            JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Use try-with-resources for better resource management
        String sqlQuery = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = (connection != null) ? connection.prepareStatement(sqlQuery) : null) {

            if (preparedStatement == null) {
                if (connection == null) { // Check if connection itself was null
                    JOptionPane.showMessageDialog(this, "Failed to establish database connection.",
                            "Database Error", JOptionPane.ERROR_MESSAGE);
                }
                // If connection was not null, but preparedStatement is, it means an error occurred in getConnection or prepareStatement
                // The getConnection method should ideally throw an exception if it fails, which would be caught below.
                // If getConnection returns null without throwing, this message is a fallback.
                return;
            }

            preparedStatement.setString(1, inputUsername);
            preparedStatement.setString(2, inputPassword); // Note: Storing passwords in plain text is insecure.

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);


                     Main mainFrame = new Main(); // Assuming you have this
                     mainFrame.setVisible(true);
                    this.dispose(); // 'this' correctly refers to the LoginFrame to close it
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.",
                            "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, "Database query failed.", ex);
            JOptionPane.showMessageDialog(this, "Database error occurred: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (ClassNotFoundException ex) { // If getConnection() potentially throws this for JDBC driver
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, "Database driver not found.", ex);
            JOptionPane.showMessageDialog(this, "Database driver not found: " + ex.getMessage(),
                    "Configuration Error", JOptionPane.ERROR_MESSAGE);
        }
        // Resources (Connection, PreparedStatement, ResultSet) are automatically closed by try-with-resources
    }

    // Dummy getConnection() method for compilation - REPLACE WITH ACTUAL IMPLEMENTATION
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        // Example of a real connection (requires MySQL Connector/J in classpath):

        String jdbcURL = "jdbc:mysql://localhost:3306/airline-database"; // Replace with your DB URL
        String dbUsername = "root"; // Replace with your DB username
        String dbPassword = "Ranjith@1654R"; // Replace with your DB password
        Class.forName("com.mysql.cj.jdbc.Driver"); // Or "com.mysql.jdbc.Driver" for older versions
        return DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);

    }


    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.WARNING, "Nimbus L&F not found or could not be set.", ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);

                // Directly instantiate and show the LoginFrame
            }
        });
    }
}