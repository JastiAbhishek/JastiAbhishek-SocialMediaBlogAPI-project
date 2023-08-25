package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private final AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account createAccount(Account account) {
        // Check if the username is already taken
        if (isUsernameTaken(account.getUsername())) {
            return null; // Username already taken
        }
        
        // Create the account
        return accountDAO.createAccount(account);
    }

    public Account getAccountByUsername(String username) {
        return accountDAO.getAccountByUsername(username);
    }

    public boolean isUsernameTaken(String username) {
        return accountDAO.isUsernameTaken(username);
    }

    public boolean updateAccount(Account account) {
        return accountDAO.updateAccount(account);
    }

    public Account login(String username, String password) {
        // Implement logic to perform user login
        Account account = accountDAO.getAccountByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null; // Login failed
    }

    public Account register(String username, String password) {
        // Implement logic to register a new account
        Account newAccount = new Account(username, password); // Create a new Account object
        return accountDAO.createAccount(newAccount); // Call the createAccount method to insert the new account into the database
    }
}

