package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private MessageService messageService;
    private AccountService accountService;

    public SocialMediaController() {
        AccountDAO accountDAO = new AccountDAO();
        MessageDAO messageDAO = new MessageDAO();

        messageService = new MessageService(messageDAO);
        accountService = new AccountService(accountDAO);
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/example-endpoint", this::exampleHandler);
        app.patch("/messages/{id}", this::updateMessageTextHandler);
        app.post("/login", this::userLoginHandler);
        app.post("/register", this::userRegistrationHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{id}", this::getMessageByIdHandler);
        app.delete("/messages/{id}", this::deleteMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);
    
        return app;
    }
    

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void userLoginHandler(Context context) {
        String requestBody = context.body();
        
        try {
        // Parse the request body JSON to get the username and password
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(requestBody);
        String username = jsonNode.get("username").asText();
        String password = jsonNode.get("password").asText();

        // Use the accountService to perform user login
        Account loggedInAccount = accountService.login(username, password);

        if (loggedInAccount != null) {
            context.json(loggedInAccount); // Respond with the logged-in account
        } else {
            context.status(401).result();
        }
    } catch (IOException e) {
        context.status(400).result("Invalid JSON payload.");
    }
    }

    private void userRegistrationHandler(Context context) {
        String requestBody = context.body();
        
        try {
            // Parse the request body JSON to get the username and password
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            String username = jsonNode.get("username").asText();
            String password = jsonNode.get("password").asText();
            
            // Check if the username is blank
            if (username.trim().isEmpty()) {
                context.status(400).result();
                return;
            }

            // Check if the password length is at least four characters
            if (password.length() < 4) {
                context.status(400).result();
                return;
            }
        
            // Use the accountService to perform user registration
            Account newAccount = accountService.register(username, password);
        
            if (newAccount != null) {
                context.json(newAccount); // Respond with the registered account
            } else {
                context.status(400).result();
            }
        } catch (IOException e) {
            context.status(400).result("Invalid JSON payload.");
        }
    }
    

    private void createMessageHandler(Context context) {
        String requestBody = context.body();
    
        try {
            // Parse the request body JSON to get the message text, posted_by ID, and time_posted_epoch
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            String messageText = jsonNode.get("message_text").asText().trim(); // Trim whitespace
            int postedBy = jsonNode.get("posted_by").asInt();
            long timePostedEpoch = jsonNode.get("time_posted_epoch").asLong(); // Extract time_posted_epoch value
    
            if (messageText.isEmpty()) {
                context.status(400).result();
                return;
            }
    
            // Create a new Message object
            Message newMessage = new Message();
            newMessage.setMessage_text(messageText);
            newMessage.setPosted_by(postedBy);
            newMessage.setTime_posted_epoch(timePostedEpoch);
    
            // Use the messageService to create a new message
            Message createdMessage = messageService.createMessage(newMessage);
    
            if (createdMessage != null) {
                context.json(createdMessage); // Respond with the created message
            } else {
                context.status(400).result();
            }
        } catch (IOException e) {
            context.status(400).result("Invalid JSON payload.");
        }
    }
    
    

    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();

        if (!messages.isEmpty()) {
            context.json(messages); // Respond with the list of messages
        } else {
            context.json(Collections.emptyList()); // Respond with an empty list
        }
    }

    private void getMessageByIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            context.json(message); // Respond with the retrieved message
        } else {
            context.status(200);
            // context.json(Collections.emptyMap()); // Respond with an empty map
        }
    }

    private void deleteMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("id"));
        Message deleted = messageService.deleteMessage(messageId);

        boolean isDeleted = true;
        if(deleted == null){
            isDeleted = false;
        }
        // boolean isDeleted = messageService.deleteMessage(messageId);
    
        if (isDeleted) {
            context.status(200);
            context.json(deleted);
        } else {
            context.status(200);
        }
    }    

    private void updateMessageTextHandler(Context context) {
        String requestBody = context.body();
        int messageId = Integer.parseInt(context.pathParam("id"));
    
        try {
            // Parse the request body JSON to get the updated message text
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            String updatedMessageText = jsonNode.get("message_text").asText();
    
            // Use the messageService to update the message text
            boolean isUpdated = messageService.updateMessageText(messageId, updatedMessageText);

           
            // Check if the update was successful
            if (isUpdated) {
                context.status(200); // Set success status code
                context.json(messageService.getMessageById(messageId)); // Respond with the updated message
            } else {
                context.status(400).result();
            }

        }catch (IOException e) {
            context.status(400).result("Invalid JSON payload.");
        }
    }
    

    private void getMessagesByUserHandler(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUser(accountId);

        context.status(200); // Respond with a success status
        context.json(messages); // Respond with the list of messages in JSON format
    }

}