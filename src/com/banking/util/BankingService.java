package com.banking.util;

import com.banking.dao.AccountDAO;
import com.banking.dao.TransactionDAO;
import com.banking.dao.UserDAO;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class BankingService {

    private final UserDAO userDAO = new UserDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    // ── Auth ──────────────────────────────────────────────────────────────────

    public User login(String username, String password) {
        return userDAO.authenticate(username, password);
    }

    public String register(String username, String password, String fullName, String email) {
        if (username == null || username.trim().isEmpty()) return "Username cannot be empty.";
        if (password == null || password.length() < 4)     return "Password must be at least 4 characters.";
        if (fullName == null || fullName.trim().isEmpty())  return "Full name cannot be empty.";
        if (userDAO.usernameExists(username))               return "Username already taken.";

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password);
        user.setFullName(fullName.trim());
        user.setEmail(email != null ? email.trim() : "");

        return userDAO.register(user) ? "SUCCESS" : "Registration failed. Try again.";
    }

    // ── Account ───────────────────────────────────────────────────────────────

    public String createAccount(int userId, String accountType, BigDecimal initialDeposit) {
        if (initialDeposit == null || initialDeposit.compareTo(BigDecimal.ZERO) < 0)
            return "Initial deposit cannot be negative.";

        String accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, userId, accountType, initialDeposit);

        if (!accountDAO.createAccount(account)) return "Failed to create account.";

        if (initialDeposit.compareTo(BigDecimal.ZERO) > 0) {
            recordTransaction(accountNumber, "DEPOSIT", initialDeposit, initialDeposit,
                              "Initial deposit", null);
        }
        return "SUCCESS:" + accountNumber;
    }

    public Account getAccount(String accountNumber) {
        return accountDAO.getAccountByNumber(accountNumber);
    }

    public List<Account> getUserAccounts(int userId) {
        return accountDAO.getAccountsByUser(userId);
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    // ── Deposit ───────────────────────────────────────────────────────────────

    public String deposit(String accountNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            return "Amount must be greater than 0.";

        Account account = accountDAO.getAccountByNumber(accountNumber);
        if (account == null)           return "Account not found.";
        if (!"ACTIVE".equals(account.getStatus())) return "Account is " + account.getStatus() + ".";

        BigDecimal newBalance = account.getBalance().add(amount);
        accountDAO.updateBalance(accountNumber, newBalance);
        recordTransaction(accountNumber, "DEPOSIT", amount, newBalance, "Cash deposit", null);
        return "SUCCESS:Deposited ₹" + amount + " | New Balance: ₹" + newBalance;
    }

    // ── Withdrawal ────────────────────────────────────────────────────────────

    public String withdraw(String accountNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            return "Amount must be greater than 0.";

        Account account = accountDAO.getAccountByNumber(accountNumber);
        if (account == null)           return "Account not found.";
        if (!"ACTIVE".equals(account.getStatus())) return "Account is " + account.getStatus() + ".";
        if (account.getBalance().compareTo(amount) < 0)
            return "Insufficient balance. Available: ₹" + account.getBalance();

        BigDecimal newBalance = account.getBalance().subtract(amount);
        accountDAO.updateBalance(accountNumber, newBalance);
        recordTransaction(accountNumber, "WITHDRAWAL", amount, newBalance, "Cash withdrawal", null);
        return "SUCCESS:Withdrawn ₹" + amount + " | New Balance: ₹" + newBalance;
    }

    // ── Transfer ──────────────────────────────────────────────────────────────

    public String transfer(String fromAccount, String toAccount, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            return "Amount must be greater than 0.";
        if (fromAccount.equals(toAccount))
            return "Cannot transfer to the same account.";

        Account from = accountDAO.getAccountByNumber(fromAccount);
        Account to   = accountDAO.getAccountByNumber(toAccount);

        if (from == null) return "Source account not found.";
        if (to == null)   return "Destination account not found.";
        if (!"ACTIVE".equals(from.getStatus())) return "Source account is " + from.getStatus() + ".";
        if (!"ACTIVE".equals(to.getStatus()))   return "Destination account is " + to.getStatus() + ".";
        if (from.getBalance().compareTo(amount) < 0)
            return "Insufficient balance. Available: ₹" + from.getBalance();

        BigDecimal newFromBalance = from.getBalance().subtract(amount);
        BigDecimal newToBalance   = to.getBalance().add(amount);

        accountDAO.updateBalance(fromAccount, newFromBalance);
        accountDAO.updateBalance(toAccount, newToBalance);

        recordTransaction(fromAccount, "TRANSFER_OUT", amount, newFromBalance,
                          "Transfer to " + toAccount, toAccount);
        recordTransaction(toAccount, "TRANSFER_IN", amount, newToBalance,
                          "Transfer from " + fromAccount, fromAccount);

        return "SUCCESS:Transferred ₹" + amount + " to " + toAccount;
    }

    // ── Transactions ──────────────────────────────────────────────────────────

    public List<Transaction> getTransactions(String accountNumber) {
        return transactionDAO.getTransactionsByAccount(accountNumber);
    }

    public List<Transaction> getAllTransactions() {
        return transactionDAO.getAllTransactions();
    }

    // ── Account Status ────────────────────────────────────────────────────────

    public String freezeAccount(String accountNumber) {
        Account acc = accountDAO.getAccountByNumber(accountNumber);
        if (acc == null) return "Account not found.";
        accountDAO.updateStatus(accountNumber, "FROZEN");
        return "SUCCESS:Account frozen.";
    }

    public String activateAccount(String accountNumber) {
        Account acc = accountDAO.getAccountByNumber(accountNumber);
        if (acc == null) return "Account not found.";
        accountDAO.updateStatus(accountNumber, "ACTIVE");
        return "SUCCESS:Account activated.";
    }

    // ── Users (admin) ─────────────────────────────────────────────────────────

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String generateAccountNumber() {
        String prefix = "MBA";
        String ts     = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String rand   = String.format("%04d", new Random().nextInt(10000));
        return prefix + ts + rand;
    }

    private void recordTransaction(String accountNumber, String type, BigDecimal amount,
                                   BigDecimal balanceAfter, String description, String related) {
        Transaction tx = new Transaction();
        tx.setTransactionId("TXN" + System.currentTimeMillis() + new Random().nextInt(999));
        tx.setAccountNumber(accountNumber);
        tx.setTransactionType(type);
        tx.setAmount(amount);
        tx.setBalanceAfter(balanceAfter);
        tx.setDescription(description);
        tx.setRelatedAccount(related);
        transactionDAO.saveTransaction(tx);
    }
}
