package DAO;

import Model.Message;
import Model.Account;
import Util.ConnectionUtil;
import DAO.AccountDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    // create message
    public Message createMessage(Message msg) {
        Account existingAccount = getAccountById(msg.getPosted_by());
        boolean validMsg = (
            !msg.getMessage_text().isBlank() && 
            !msg.getMessage_text().isEmpty() && 
            msg.getMessage_text().length() < 255);

        if (existingAccount != null && validMsg) {
            try {
                Connection connection = ConnectionUtil.getConnection();
                String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
                PreparedStatement prepStmnt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                
                prepStmnt.setInt(1, msg.getPosted_by());
                prepStmnt.setString(2, msg.getMessage_text());
                prepStmnt.setLong(3, msg.getTime_posted_epoch()); // set type!!!
                
                prepStmnt.executeUpdate();

                ResultSet rs = prepStmnt.getGeneratedKeys();

                if (rs.next()) {
                    int createdMsgId = (int)rs.getLong(1);

                    return new Message(
                        createdMsgId,
                        msg.getPosted_by(),
                        msg.getMessage_text(),
                        msg.getTime_posted_epoch()
                    );
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        
        return null;
    }

    // get all messages
    public List<Message> getAllMessages() {
        List<Message> retrievedMessages = new ArrayList<>();
        
        try {
            Connection connection = ConnectionUtil.getConnection();
            
            String sql = "SELECT * FROM message";
            PreparedStatement prepStmnt = connection.prepareStatement(sql);

            ResultSet rs = prepStmnt.executeQuery();

            while(rs.next()) {
                Message msg = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );

                retrievedMessages.add(msg);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return retrievedMessages;
    }

    // get message by id
    public Message getMessageById (int msgId) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement prepStmnt = connection.prepareStatement(sql);

            prepStmnt.setInt(1, msgId);

            ResultSet rs = prepStmnt.executeQuery();

            while (rs.next()) {
                Message msg = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );

                return msg;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // delete message by id
    public Message deleteMessageById (int msgId) {
        Message existingMessage = getMessageById(msgId);

        if (existingMessage != null) {
            try {
                Connection connection = ConnectionUtil.getConnection();
    
                String sql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement prepStmnt = connection.prepareStatement(sql);
    
                prepStmnt.setInt(1, msgId);
    
                prepStmnt.executeUpdate();
    
                return existingMessage;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        
        return null;
    }

    // update message by id
    public Message updatMessageById (Message msg) {
        Message existinMessage = getMessageById(msg.getMessage_id());
        boolean validMsg = (
            !msg.getMessage_text().isBlank() &&
            !msg.getMessage_text().isEmpty() &&
            msg.getMessage_text().length() <= 255
        );

        if (existinMessage != null && validMsg) {
            try {
                Connection connection = ConnectionUtil.getConnection();
                String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
                PreparedStatement prepStmnt = connection.prepareStatement(sql);

                prepStmnt.setString(1, msg.getMessage_text());
                prepStmnt.setInt(2, existinMessage.getMessage_id());

                int updatedRows = prepStmnt.executeUpdate();

                if (updatedRows > 0) {
                    return new Message(
                        existinMessage.getMessage_id(),
                        existinMessage.getPosted_by(),
                        msg.getMessage_text(),
                        existinMessage.getTime_posted_epoch()
                    );
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

        return null;
        
    }

    // get all messages by account id
    public List<Message> getAllMessagesByAccountId (int accId) {
        List<Message> retrievedMessages = new ArrayList<>();

        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement perpStmnt = connection.prepareStatement(sql);

            perpStmnt.setInt(1, accId);

            ResultSet rs = perpStmnt.executeQuery();

            while (rs.next()) {
                Message msg = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );

                retrievedMessages.add(msg);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return retrievedMessages;
    }

    public Account getAccountById (int accId) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement prepStmnt = connection.prepareStatement(sql);

            prepStmnt.setInt(1, accId);

            ResultSet rs = prepStmnt.executeQuery();

            while (rs.next()) {
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
