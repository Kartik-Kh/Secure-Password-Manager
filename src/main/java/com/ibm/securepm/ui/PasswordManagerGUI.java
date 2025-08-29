package com.ibm.securepm.ui;

import com.ibm.securepm.dao.PasswordDAO;
import com.ibm.securepm.encryption.AESEncryption;
import com.ibm.securepm.generator.PasswordGenerator;
import com.ibm.securepm.model.PasswordEntry;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;

public class PasswordManagerGUI extends JFrame {
    private final PasswordDAO passwordDAO;
    private String masterPassword;
    private JTable passwordTable;
    private DefaultTableModel tableModel;
    private final String[] columnNames = {"ID", "Title", "Username", "URL", "Notes", "Last Modified"};
    private final Color PRIMARY_COLOR = new Color(100, 181, 246);    // Light blue
    private final Color SECONDARY_COLOR = new Color(144, 202, 249);  // Lighter blue
    private final Color BACKGROUND_COLOR = new Color(255, 255, 255); // Pure white
    private final Color ACCENT_COLOR = new Color(129, 212, 250);     // Sky blue
    private final Color TEXT_COLOR = new Color(33, 33, 33);         // Dark gray for text
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public PasswordManagerGUI() {
        this.passwordDAO = new PasswordDAO();
        initializeMasterPassword();
        initializeUI();
    }

    private void initializeMasterPassword() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBackground(BACKGROUND_COLOR);
        
        JLabel label = new JLabel("Enter Master Password");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(PRIMARY_COLOR);
        
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        panel.add(label);
        panel.add(passwordField);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create custom dialog
        JDialog dialog = new JDialog(this, "Secure Password Manager", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(panel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton okButton = createStyledButton("OK", null);
        JButton cancelButton = createStyledButton("Cancel", null);
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setContentPane(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        
        okButton.addActionListener(e -> {
            masterPassword = new String(passwordField.getPassword());
            dialog.dispose();
        });
        
        cancelButton.addActionListener(e -> {
            dialog.dispose();
            System.exit(0);
        });
        
        dialog.setVisible(true);
        
        if (masterPassword == null || masterPassword.trim().isEmpty()) {
            System.exit(0);
        }
    }

    private void initializeUI() {
        setTitle("Secure Password Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Secure Password Manager");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titlePanel.add(titleLabel);
        
        // Add a subtle line under the title
        JSeparator separator = new JSeparator();
        separator.setForeground(ACCENT_COLOR);
        titlePanel.add(separator);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(PRIMARY_COLOR);
        JButton addButton = createStyledButton("Add Password", null);
        JButton generateButton = createStyledButton("Generate Password", null);
        JButton deleteButton = createStyledButton("Delete Password", null);
        JButton viewButton = createStyledButton("View Password", null);

        buttonPanel.add(addButton);
        buttonPanel.add(generateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        // Create table
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        passwordTable = new JTable(tableModel);
        styleTable(passwordTable);

        // Add search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(BACKGROUND_COLOR);
        JTextField searchField = new JTextField(30);
        searchField.setFont(BUTTON_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setFont(BUTTON_FONT);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        JScrollPane scrollPane = new JScrollPane(passwordTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        // Add components to main panel
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(e -> showAddPasswordDialog());
        generateButton.addActionListener(e -> showGeneratePasswordDialog());
        deleteButton.addActionListener(e -> deleteSelectedPassword());
        viewButton.addActionListener(e -> viewSelectedPassword());

        // Set main panel
        setContentPane(mainPanel);

        // Load passwords
        refreshPasswordTable();
    }

    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(ACCENT_COLOR);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setIntercellSpacing(new Dimension(10, 10));
        table.setFillsViewportHeight(true);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        
        // Style header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(ACCENT_COLOR);
        header.setForeground(TEXT_COLOR);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        // Add zebra striping
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 247, 255));
                    c.setForeground(TEXT_COLOR);
                } else {
                    c.setBackground(ACCENT_COLOR);
                    c.setForeground(TEXT_COLOR);
                }
                
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return c;
            }
        });
    }

    private void refreshPasswordTable() {
        tableModel.setRowCount(0);
        List<PasswordEntry> entries = passwordDAO.findAll();
        for (PasswordEntry entry : entries) {
            tableModel.addRow(new Object[]{
                entry.getId(),
                entry.getTitle(),
                entry.getUsername(),
                entry.getUrl(),
                entry.getNotes(),
                dateFormat.format(new java.util.Date(entry.getLastModified()))
            });
        }
    }

    private void showAddPasswordDialog() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBackground(BACKGROUND_COLOR);
        
        JTextField titleField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField urlField = new JTextField();
        JTextField notesField = new JTextField();
        
        // Add password generation button
        JButton generateBtn = new JButton("Generate Strong Password");
        generateBtn.setFont(BUTTON_FONT);
        generateBtn.setBackground(ACCENT_COLOR);
        generateBtn.setForeground(TEXT_COLOR);
        generateBtn.addActionListener(e -> {
            String generatedPassword = PasswordGenerator.generatePassword(16, true, true, true, true);
            passwordField.setText(generatedPassword);
        });
        
        // Add show/hide password toggle
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setBackground(BACKGROUND_COLOR);
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        JPanel passwordPanel = new JPanel(new BorderLayout(5, 0));
        passwordPanel.setBackground(BACKGROUND_COLOR);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(showPassword, BorderLayout.EAST);
        panel.add(passwordPanel);
        
        panel.add(new JLabel(""));
        panel.add(generateBtn);
        
        panel.add(new JLabel("URL:"));
        panel.add(urlField);
        panel.add(new JLabel("Notes:"));
        panel.add(notesField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String password = new String(passwordField.getPassword());
                String encryptedPassword = AESEncryption.encrypt(password, masterPassword);
                
                PasswordEntry entry = new PasswordEntry(
                    titleField.getText(),
                    usernameField.getText(),
                    encryptedPassword,
                    urlField.getText(),
                    notesField.getText()
                );
                
                passwordDAO.save(entry);
                refreshPasswordTable();
                JOptionPane.showMessageDialog(this, "Password saved successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving password: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showGeneratePasswordDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JSpinner lengthSpinner = new JSpinner(new SpinnerNumberModel(12, 8, 32, 1));
        JCheckBox specialCharsBox = new JCheckBox("Include Special Characters", true);

        panel.add(new JLabel("Password Length:"));
        panel.add(lengthSpinner);
        panel.add(new JLabel(""));
        panel.add(specialCharsBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Generate Password",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int length = (Integer) lengthSpinner.getValue();
            String password = PasswordGenerator.generatePassword(
                length,
                true,  // lowercase
                true,  // uppercase
                true,  // digits
                specialCharsBox.isSelected()
            );

            JPanel resultPanel = new JPanel(new BorderLayout(5, 5));
            JTextField passwordField = new JTextField(password);
            passwordField.setEditable(false);
            resultPanel.add(passwordField, BorderLayout.CENTER);
            
            JOptionPane.showMessageDialog(this, resultPanel, "Generated Password",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedPassword() {
        int selectedRow = passwordTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a password to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this password?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = (Integer) tableModel.getValueAt(selectedRow, 0);
            passwordDAO.delete(id);
            refreshPasswordTable();
        }
    }

    private void viewSelectedPassword() {
        int selectedRow = passwordTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a password to view.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        PasswordEntry entry = passwordDAO.findById(id);

        if (entry != null) {
            try {
                String decryptedPassword = AESEncryption.decrypt(entry.getEncryptedPassword(), masterPassword);
                
                JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
                JTextField titleField = new JTextField(entry.getTitle());
                JTextField usernameField = new JTextField(entry.getUsername());
                JPasswordField passwordField = new JPasswordField(decryptedPassword);
                JTextField urlField = new JTextField(entry.getUrl());
                JTextField notesField = new JTextField(entry.getNotes());
                JCheckBox showPassword = new JCheckBox("Show Password");

                titleField.setEditable(false);
                usernameField.setEditable(false);
                passwordField.setEditable(false);
                urlField.setEditable(false);
                notesField.setEditable(false);

                showPassword.addActionListener(e -> {
                    if (showPassword.isSelected()) {
                        passwordField.setEchoChar((char) 0);
                    } else {
                        passwordField.setEchoChar('•');
                    }
                });

                panel.add(new JLabel("Title:"));
                panel.add(titleField);
                panel.add(new JLabel("Username:"));
                panel.add(usernameField);
                panel.add(new JLabel("Password:"));
                JPanel passwordPanel = new JPanel(new BorderLayout(5, 0));
                passwordPanel.setBackground(BACKGROUND_COLOR);
                passwordPanel.add(passwordField, BorderLayout.CENTER);
                passwordPanel.add(showPassword, BorderLayout.EAST);
                panel.add(passwordPanel);
                panel.add(new JLabel("URL:"));
                panel.add(urlField);
                panel.add(new JLabel("Notes:"));
                panel.add(notesField);

                JOptionPane.showMessageDialog(this, panel, "View Password",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error decrypting password: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            PasswordManagerGUI gui = new PasswordManagerGUI();
            gui.setVisible(true);
        });
    }
}
