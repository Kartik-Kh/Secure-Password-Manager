package com.ibm.securepm;

import com.ibm.securepm.dao.PasswordDAO;
import com.ibm.securepm.encryption.AESEncryption;
import com.ibm.securepm.generator.PasswordGenerator;
import com.ibm.securepm.model.PasswordEntry;

import java.util.List;
import java.util.Scanner;

public class SecurePasswordManager {
    private static final PasswordDAO passwordDAO = new PasswordDAO();
    private static final Scanner scanner = new Scanner(System.in);
    private static String masterPassword;

    public static void main(String[] args) {
        try {
            initializeMasterPassword();
            while (true) {
                showMenu();
                processUserChoice();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initializeMasterPassword() {
        System.out.print("Enter your master password: ");
        masterPassword = scanner.nextLine();
    }

    private static void showMenu() {
        System.out.println("\n=== Secure Password Manager ===");
        System.out.println("1. Add new password");
        System.out.println("2. View all passwords");
        System.out.println("3. Generate strong password");
        System.out.println("4. Delete password");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    private static void processUserChoice() throws Exception {
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                addNewPassword();
                break;
            case 2:
                viewAllPasswords();
                break;
            case 3:
                generateStrongPassword();
                break;
            case 4:
                deletePassword();
                break;
            case 5:
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option!");
        }
    }

    private static void addNewPassword() throws Exception {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        System.out.print("Enter URL: ");
        String url = scanner.nextLine();
        
        System.out.print("Enter notes: ");
        String notes = scanner.nextLine();

        String encryptedPassword = AESEncryption.encrypt(password, masterPassword);
        PasswordEntry entry = new PasswordEntry(title, username, encryptedPassword, url, notes);
        passwordDAO.save(entry);
        
        System.out.println("Password saved successfully!");
    }

    private static void viewAllPasswords() throws Exception {
        List<PasswordEntry> entries = passwordDAO.findAll();
        
        if (entries.isEmpty()) {
            System.out.println("No passwords stored yet.");
            return;
        }

        System.out.println("\nStored Passwords:");
        System.out.println("----------------");
        
        for (PasswordEntry entry : entries) {
            System.out.println("Title: " + entry.getTitle());
            System.out.println("Username: " + entry.getUsername());
            String decryptedPassword = AESEncryption.decrypt(entry.getEncryptedPassword(), masterPassword);
            System.out.println("Password: " + decryptedPassword);
            System.out.println("URL: " + entry.getUrl());
            System.out.println("Notes: " + entry.getNotes());
            System.out.println("----------------");
        }
    }

    private static void generateStrongPassword() {
        System.out.print("Enter desired password length (minimum 12 recommended): ");
        int length = scanner.nextInt();
        
        String password = PasswordGenerator.generatePassword(
            length,
            true,  // use lowercase
            true,  // use uppercase
            true,  // use digits
            true   // use special characters
        );
        
        System.out.println("Generated Password: " + password);
    }

    private static void deletePassword() {
        System.out.print("Enter the ID of the password to delete: ");
        int id = scanner.nextInt();
        
        passwordDAO.delete(id);
        System.out.println("Password deleted successfully!");
    }
}
