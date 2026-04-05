# 🏦 Mini Banking System

A desktop banking application built with **Core Java**, **JDBC**, and **Swing GUI** as part of the Advanced Java Programming (CSE4019) coursework.

---

## 📌 Tech Stack

- **Language:** Java (JDK 8+)
- **GUI:** Java Swing
- **Database:** MySQL
- **Connectivity:** JDBC (mysql-connector-java)

---

## 🗂 Project Structure

```
MiniBankingSystem/
├── src/
│   └── com/banking/
│       ├── Main.java
│       ├── db/
│       │   └── DatabaseConnection.java
│       ├── model/
│       │   ├── User.java
│       │   ├── Account.java
│       │   └── Transaction.java
│       ├── dao/
│       │   ├── UserDAO.java
│       │   ├── AccountDAO.java
│       │   └── TransactionDAO.java
│       ├── util/
│       │   ├── BankingService.java
│       │   └── UITheme.java
│       └── gui/
│           ├── LoginFrame.java
│           ├── RegisterFrame.java
│           └── DashboardFrame.java
├── lib/
│   └── mysql-connector-java.jar   ← (download separately, not committed)
├── schema.sql
└── README.md
```

---

## ✨ Features

- User Registration and Login
- Open Savings / Current / Fixed Deposit accounts
- Deposit and Withdraw funds
- Fund Transfer between accounts
- Transaction History per account
- Freeze / Activate account
- View all accounts in the system
- Dark-themed Swing GUI

---

## ⚙️ Setup Instructions

### 1. Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/mini-banking-system.git
cd mini-banking-system
```

### 2. Set up MySQL database
Open MySQL Workbench or CLI and run:
```sql
SOURCE schema.sql;
```

### 3. Download MySQL Connector JAR
- Download from: https://dev.mysql.com/downloads/connector/j/
- Select **Platform Independent** → extract the ZIP
- Copy the JAR into the `lib/` folder
- Rename it to: `mysql-connector-java.jar`

### 4. Update DB credentials
Open `src/com/banking/db/DatabaseConnection.java` and set:
```java
private static final String USERNAME = "your_mysql_username";
private static final String PASSWORD = "your_mysql_password";
```

### 5. Compile
```bash
javac -encoding UTF-8 -cp "lib/mysql-connector-java.jar" -d bin \
  src/com/banking/db/*.java \
  src/com/banking/model/*.java \
  src/com/banking/dao/*.java \
  src/com/banking/util/*.java \
  src/com/banking/gui/*.java \
  src/com/banking/Main.java
```

### 6. Run
```bash
# Linux / Mac
java -cp "bin:lib/mysql-connector-java.jar" com.banking.Main

# Windows
java -cp "bin;lib/mysql-connector-java.jar" com.banking.Main
```

---

## 🔐 Default Credentials

| Username | Password |
|----------|----------|
| admin    | admin123 |
| john_doe | pass123  |

---

## 📚 Course Info

| Field | Detail |
|-------|--------|
| Course | Advanced Java Programming |
| Code | CSE4019 |
| Units Covered | Unit 1 (OOP), Unit 3 (JDBC), Unit 4 (Swing) |

---

## 📄 License

This project is for educational purposes only.
