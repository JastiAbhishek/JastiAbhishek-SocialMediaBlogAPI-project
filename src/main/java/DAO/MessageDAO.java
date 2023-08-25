package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    public Message createMessage(Message message) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
    
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                return null; // Creating message failed
            }
    
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                message.setMessage_id(generatedKeys.getInt(1));
            } else {
                return null; // Creating message failed
            }
    
            return message;
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Creating message failed
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

    public Message getMessageById(int messageId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
    
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");
                return new Message(messageId, postedBy, messageText, timePostedEpoch);
            } else {
                return null; // Message not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred while fetching message
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
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
    

    public List<Message> getAllMessages() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Message> messages = new ArrayList<>();
    
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Message";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                int messageId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");
                messages.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        return messages;
    }
    

    public List<Message> getMessagesByUser(int userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Message> messages = new ArrayList<>();
    
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                int messageId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");
                messages.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        return messages;
    }
    
    // public boolean updateMessage(Message message) {
    //     Connection connection = null;
    //     PreparedStatement preparedStatement = null;
    
    //     try {
    //         connection = ConnectionUtil.getConnection();
    //         String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
    //         preparedStatement = connection.prepareStatement(sql);
    //         preparedStatement.setString(1, message.getMessage_text());
    //         preparedStatement.setInt(2, message.getMessage_id());
    

    //         int rowsAffected = preparedStatement.executeUpdate();
        
    //         return rowsAffected > 0;
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         return false;
    //     } finally {
    //         try {
    //             if (preparedStatement != null) {
    //                 preparedStatement.close();
    //             }
    //             if (connection != null) {
    //                 connection.close();
    //             }
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }

    public boolean updateMessageText(int messageId, String updatedMessageText) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
    
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, updatedMessageText);
            preparedStatement.setInt(2, messageId);
    
            int rowsUpdated = preparedStatement.executeUpdate();
    
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
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
    

    public Message deleteMessage(int messageId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        Message deleted = getMessageById(messageId);
    
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "DELETE FROM Message WHERE message_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
    
            int rowsUpdated = preparedStatement.executeUpdate();
            if(rowsUpdated>0){
                return deleted;
            }
            else{
                return null;
            }
            // return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Deleting message failed
        } finally {
            try {
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
