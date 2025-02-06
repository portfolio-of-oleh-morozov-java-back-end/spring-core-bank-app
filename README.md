# Console Banking Application in Java

## General Information
A console banking application implemented in Java using the Spring Framework. It provides basic functions for managing users and their bank accounts.

## Application Functionality
Once launched, the application waits for user input through the console. The following operations are available:

- `USER_CREATE` – create a new user
- `SHOW_ALL_USERS` – display a list of all users
- `ACCOUNT_CREATE` – create a new account for a user
- `ACCOUNT_CLOSE` – close an account
- `ACCOUNT_DEPOSIT` – deposit funds into an account
- `ACCOUNT_TRANSFER` – transfer funds between accounts
- `ACCOUNT_WITHDRAW` – withdraw funds from an account

### Command Descriptions

- `USER_CREATE` – prompts for a username, creates a new user, and automatically opens their first account with an initial balance. The system assigns a unique user ID.
- `SHOW_ALL_USERS` – displays a list of all users and their account information.
- `ACCOUNT_CREATE` – prompts for a user ID, creates a new account with a default balance, and assigns a unique account ID.
- `ACCOUNT_CLOSE` – prompts for an account ID, closes the account by transferring the remaining balance to the user's first account. If the user only has one account, closing is not allowed.
- `ACCOUNT_DEPOSIT` – prompts for an account ID and an amount, deposits the specified amount into the account.
- `ACCOUNT_TRANSFER` – prompts for a sender account ID, receiver account ID, and amount, and performs the transfer. A commission is charged if the transfer is made between different users.
- `ACCOUNT_WITHDRAW` – prompts for an account ID and an amount, and withdraws the specified amount from the account.

## Application Settings (`application.properties`)

The configuration file contains the following parameters:

- `account.default-amount` – the initial balance of each new account
- `account.transfer-commission` – the commission rate for transferring funds to another user (in percentage). No commission is charged for transfers between the user's own accounts.

Example of `application.properties` file content:

```properties
account.default-amount=500
account.transfer-commission=0.01

Requirements:
Java 17 or higher

Application Architecture
Data Models
User – contains the user ID, username, and a list of accounts (accountList).
Account – contains the account ID, user ID (userId), and current balance (balance).
Main Services
UserService – user management (creation, search, listing).
AccountService – account management (creation, deposits, withdrawals, transfers, closing).
OperationConsoleListener – handles user input from the console and performs corresponding operations.
The application is divided into separate classes with clearly defined areas of responsibility, implemented following the SOLID principles, and uses design patterns where necessary.

Error Handling
The application implements an error handling system that informs users of invalid actions. Possible errors include:

Attempting to create a user with an already existing username.
Accessing a non-existing account.
Entering incorrect data (e.g., negative amounts).
Trying to withdraw more funds than available in the account.

Project Structure:
org.bank
│   App.java                     // Entry point of the application
│   ApplicationConfiguration.java // Application configuration
│   ConsoleListenerStarter.java
│   OperationConsoleListener.java
│
├───account
│   │   Account.java              // Account model
│   │   AccountService.java       // Account service logic
│
├───operations
│   │   ConsoleOperationType.java // Operation types
│   │   OperationCommandProcessor.java // Command processor interface
│   ├───processors
│   │   AccountTransferProcessor.java  // Account transfer processor
│   │   AccountWithDrawProcessor.java  // Withdrawal processor
│   │   CloseAccountProcessor.java     // Account closing processor
│   │   CreateAccountProcessor.java    // Account creation processor
│   │   CreateUserProcessor.java       // User creation processor
│   │   DepositAccountProcessor.java   // Deposit processor
│   │   ShowAllUsersProcessor.java     // Show all users processor
│
├───user
│   │   User.java                // User model
│   │   UserService.java         // User service logic
│
│
└───resources/application.properties // Configuration file
     account.default-amount=500
     account.transfer-commission=0.01

Running the Application:
1. Clone the repository to your local machine.
2. Run the App file
3. Enter commands in the console to interact with the application.
