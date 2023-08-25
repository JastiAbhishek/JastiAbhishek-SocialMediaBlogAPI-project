package Service;

import java.util.List;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {

    private final MessageDAO messageDAO;

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message) {
        if(!message.getMessage_text().isEmpty() && message.getMessage_text().length() < 255){
            return messageDAO.createMessage(message);
        }
        return null;
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public List<Message> getMessagesByUser(int userId) {
        return messageDAO.getMessagesByUser(userId);
    }

    public Message deleteMessage(int messageId) {
        if(messageDAO.getMessageById(messageId) == null){
            return null;
        }
        return messageDAO.deleteMessage(messageId);
    }

    public boolean updateMessageText(int messageId, String updatedMessageText) {
        // Retrieve the existing message by messageId
        Message existingMessage = messageDAO.getMessageById(messageId);


        if(updatedMessageText.isEmpty() || updatedMessageText.length() >= 255){
            return false;
        }
    
        if (existingMessage != null) {
            // Update the message text
            existingMessage.setMessage_text(updatedMessageText);
    
            // Update the message using the DAO and return the result
            return messageDAO.updateMessageText(messageId, updatedMessageText);
        } else {
            return false; // Message not found
        }
    }
    
}

