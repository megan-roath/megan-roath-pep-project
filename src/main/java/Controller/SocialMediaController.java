package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.start(8080);
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("messages", this::messageHandler);
        app.get("/messages", this::allMessageHandler);
        app.get("/messages/{message_id}", this::messageIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.get("/accounts/{account_id}/messages", this::userMessageHandler);
        return app;
    }

    /**
     * Handler to process new User Registration.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * If this is succesful the JSON will contain the account and return a 200 response. If unsuccessful the response will be 400 (client-error).
     * 
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Account account = mapper.readValue(context.body(), Account.class);
        Account registerAccount = accountService.addAccount(account);
        if(registerAccount!=null){
            context.json(mapper.writeValueAsString(registerAccount));
            context.status(200);
        } else{
            context.status(400);
        }
    }

    /*
     * Handler to process User logins.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an Account object.
     * If the login is succesful the response will contain the account and its account_id returning a successful status(200).
     * If not the user will be unauthorized, (401).
     */
    private void loginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();

        Account account = mapper.readValue(context.body(), Account.class);
        Account userLogin = accountService.login(account);

        if(userLogin!=null){
            context.json(mapper.writeValueAsString(userLogin));
            context.status(200);
        } else{
            context.status(401);
        }

    }

    /*
     * Handler to create new messages.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into a message object.
     * If the message is successful, the response body will contain a JSON of the message. Status will be 200 (successful)
     * Client error (400) response will be given if creation is not successful.
     */
    private void messageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();

        Message message = mapper.readValue(context.body(), Message.class);
        Message newMessage = messageService.createMessage(message);

        if (newMessage==null){
            context.status(400);
        } else{
            context.json(mapper.writeValueAsString(message));
            context.status(200);
        }

    }

    /*
     * Handler to retrieve all messages.
     * Response should always be 200 by default.
     * 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void allMessageHandler(Context context){
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
        context.status(200);
    }

    /*
     * Handler to retrieve a message given a specific ID.
     * The response should always be 200, by default.
     * 
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin.
     */
    private void messageIdHandler(Context context) throws JsonProcessingException{
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        if(messageService.getMessage(message_id) != null){
            context.json(messageService.getMessage(message_id));
            context.status(200);
        } else {
            context.result("");
            context.status(200);
        }

    }

    /*
     * Handler to delete a message identified by a message ID.
     * If the deletion of the message is successful, the response body will contain the now deleted message and return 200.
     * If the message did not exist the response will be 200, with no response body.
     */
    private void deleteMessageHandler(Context context) throws JsonProcessingException{
       //ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        Message deletedMessage = messageService.getMessage(message_id);

        if(deletedMessage != null){
            messageService.deleteMessage(message_id);
            context.json(deletedMessage);
            context.status(200);
        } else {
            context.result("");
            context.status(200);
        }

    }

    /*
     * Handler to update a message.
     * 
     * If update is unsuccessful, given the message is blank or parameter are not met, response will give a client error(400)
     * If all parameters are met, the response body will contain the now updated message and return 200.
     */
    private void patchMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();

        Message message = mapper.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);

        if (updatedMessage == null){
            context.status(400);
        }else{
            context.json(mapper.writeValueAsString(updatedMessage));
            context.status(200);
        }

    }

    /*
     * Handler to retrieve all messages written by a specific user.
     * 
     * The response body will contain a JSON representation of the list of messages posted by a specific user.
     * If no user exists, or has posted no messages, the response body will just be empty.
     * Both will return 200.
     */
    private void userMessageHandler(Context context){
        int posted_by= Integer.parseInt(context.pathParam("account_id"));

        context.json(messageService.getMessageFromUser(posted_by));
        context.status(200);
    }

}