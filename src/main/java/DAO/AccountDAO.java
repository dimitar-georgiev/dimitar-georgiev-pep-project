package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    // register
    public Account userRegistration(Account account) {
        Account existingUser = getUserByName(account.getUsername());
        boolean validInput = (
            !account.getUsername().isBlank() &&
            !account.getUsername().isEmpty() && 
            account.password.length() >= 4
        );

        if ( existingUser == null && validInput ) {
            try {
                Connection connection = ConnectionUtil.getConnection();
                String sql = "INSERT INTO account (username, password) VALUES (?,?)";
                PreparedStatement prepStmnt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                prepStmnt.setString(1, account.getUsername());
                prepStmnt.setString(2, account.getPassword());

                prepStmnt.executeUpdate();
                ResultSet rs = prepStmnt.getGeneratedKeys();

                if (rs.next()) {
                    int createdUserId = (int) rs.getLong(1);
                    
                    return new Account(
                        createdUserId,
                        account.getUsername(),
                        account.getPassword()
                    );
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        
        return null;
    }
    
    // login
    public Account userLogin (Account account) {
        
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE username = ?  AND password = ?";
            PreparedStatement perpStmnt = connection.prepareStatement(sql);

            perpStmnt.setString(1, account.getUsername());
            perpStmnt.setString(2, account.getPassword());

            ResultSet rs = perpStmnt.executeQuery();

            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    // get account by username
    private Account getUserByName (String username) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement prepStmnt = connection.prepareStatement(sql);

            prepStmnt.setString(1, username);

            ResultSet rs = prepStmnt.executeQuery();

            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // get account by id
    public Account getAccountById (int accId) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement prepStmnt = connection.prepareStatement(sql);

            prepStmnt.setInt(1, accId);

            ResultSet rs = prepStmnt.executeQuery();

            if (rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
