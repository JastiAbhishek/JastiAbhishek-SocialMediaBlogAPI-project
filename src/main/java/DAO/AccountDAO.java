package DAO;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    public Account createAccount(Account account) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
    
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                return null; // Insertion failed
            }
    
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedAccountId = generatedKeys.getInt(1);
                account.setAccount_id(generatedAccountId); // Set the generated account_id
                return account; // Return the account with the generated account_id
            } else {
                return null; // Couldn't get the generated account_id
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            // if (connection != null) {
            //     try {
            //         connection.close();
            //     } catch (SQLException e) {
            //         e.printStackTrace();
            //     }
            // }
        }
    }
    
    
    public Account getAccountByUsername(String username) {
        String sql = "SELECT * FROM Account WHERE username = ?";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
    
        try {
            connection = ConnectionUtil.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int accountId = resultSet.getInt("account_id");
                String password = resultSet.getString("password");
                return new Account(accountId, username, password);
            } else {
                return null; // Account not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred while fetching account
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                // if (connection != null) {
                //     connection.close();
                // }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    

    public boolean updateAccount(Account account) {
        String sql = "UPDATE Account SET username = ?, password = ? WHERE account_id = ?";
        Connection connection = null;
        PreparedStatement statement = null;
    
        try {
            connection = ConnectionUtil.getConnection();
            statement = connection.prepareStatement(sql);
    
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setInt(3, account.getAccount_id());
    
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Updating account failed
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                // if (connection != null) {
                //     connection.close();
                // }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    

    public boolean isUsernameTaken(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
    
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Account WHERE username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
    
            resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // Returns true if username exists, false otherwise
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                // if (connection != null) {
                //     connection.close();
                // }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
}
