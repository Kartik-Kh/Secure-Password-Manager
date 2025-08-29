# Secure Password Manager

A modern, secure password management system built with Java, featuring strong encryption (AES-GCM) and a local SQLite database for storage. This application provides a user-friendly GUI for managing your passwords securely.

![Secure Password Manager](screenshots/app.png)

## Features

🔒 **Security**
- AES encryption with GCM mode for secure password storage
- Unique salt for each password
- Master password protection
- Passwords are never stored in plain text

🎨 **Modern User Interface**
- Clean, intuitive design
- Password visibility toggle
- Search functionality
- Easy-to-use forms
- Responsive layout

🛠️ **Password Management**
- Store and encrypt passwords
- Auto-generate strong passwords
- View/Edit/Delete password entries
- Store additional information (URLs, usernames, notes)
- Quick search through stored passwords

💾 **Local Storage**
- SQLite database for efficient storage
- No internet connection required
- Complete data privacy
- Portable database file

## Technology Stack

- Java 17
- SQLite for database
- AES-GCM encryption
- Swing GUI framework
- Maven for dependency management

## Security Features

### Encryption
- Uses AES (Advanced Encryption Standard) with GCM mode
- Implements PBKDF2 for key derivation
- Secure random salt generation
- Authenticated encryption using GCM

### Password Generation
- Configurable password length
- Mix of uppercase and lowercase letters
- Numbers and special characters
- Ensures strong password creation

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Git (for cloning the repository)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Kartik-Kh/Secure-Password-Manager.git
```

2. Navigate to the project directory:
```bash
cd Secure-Password-Manager
```

3. Compile the project:
```bash
javac -d target/classes -cp "lib/*" src/main/java/com/ibm/securepm/*/*.java src/main/java/com/ibm/securepm/*.java
```

4. Run the application:
```bash
java -cp "target/classes;lib/*" com.ibm.securepm.ui.PasswordManagerGUI
```

## Usage

1. **First Launch**
   - Enter your master password when prompted
   - This password will be used to encrypt/decrypt your stored passwords
   - Remember this password as it cannot be recovered

2. **Adding Passwords**
   - Click "Add Password"
   - Fill in the details (title, username, password, URL, notes)
   - Use the "Generate Password" button for strong passwords
   - Click "OK" to save

3. **Viewing Passwords**
   - Select a password entry
   - Click "View Password"
   - Use the "Show Password" checkbox to reveal the password

4. **Searching**
   - Use the search bar to filter entries
   - Searches through titles, usernames, and URLs

5. **Deleting Passwords**
   - Select a password entry
   - Click "Delete Password"
   - Confirm deletion

## Best Practices

1. Choose a strong master password
2. Regularly backup your database file
3. Use generated passwords when possible
4. Never share your master password
5. Keep your Java runtime updated

## Contributing

Feel free to contribute to this project. You can:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to your branch
5. Create a Pull Request

## Author

- **Kartik Khorwal** - [Kartik-Kh](https://github.com/Kartik-Kh)

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Features

- Store and encrypt passwords using AES encryption
- Auto-generate strong passwords with customizable parameters
- Secure storage using SQLite database
- Command-line interface for easy interaction
- Master password protection
- Store additional information like usernames, URLs, and notes

## Technical Stack

- Java 17
- SQLite for database
- BouncyCastle for encryption
- Maven for dependency management

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven

### Building the Project

1. Clone the repository
2. Navigate to the project directory
3. Build with Maven:
   ```bash
   mvn clean install
   ```

### Running the Application

After building, run the application using:
```bash
java -cp target/secure-password-manager-1.0-SNAPSHOT.jar com.ibm.securepm.SecurePasswordManager
```

## Security Features

### Password Encryption

- Uses AES (Advanced Encryption Standard) for password encryption
- Implements PBKDF2 (Password-Based Key Derivation Function 2) for key derivation
- Uses secure random salt generation
- Implements GCM (Galois/Counter Mode) for authenticated encryption

### Password Generation

- Configurable password length
- Includes uppercase and lowercase letters
- Includes numbers and special characters
- Ensures at least one character from each selected category
- Uses SecureRandom for cryptographically strong random generation

## Usage

1. Start the application
2. Enter your master password when prompted
3. Use the menu to:
   - Add new passwords
   - View stored passwords
   - Generate strong passwords
   - Delete stored passwords

## Best Practices

1. Use a strong master password
2. Regularly backup the database file
3. Never share your master password
4. Use generated passwords for maximum security

## Contributing

Feel free to submit issues and enhancement requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
