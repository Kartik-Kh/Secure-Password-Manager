# Secure Password Manager

A secure password management system built with Java, featuring strong encryption (AES) and a local SQLite database for storage.

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
