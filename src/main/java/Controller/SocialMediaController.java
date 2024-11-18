package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
        app.get("example-endpoint", this::exampleHandler);
        app.post("/login", this::loginHandler);
        app.post("/register",this::registerAccountHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    
    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInUser = accountService.login(account.username, account.password);
        if(loggedInUser != null){
            ctx.json(mapper.writeValueAsString(loggedInUser));
        }
        else{
            ctx.status(401);
        }
    }

    private void registerAccountHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account registeredUser = accountService.registerAccount(account.username, account.password);
        if(registeredUser != null){
            ctx.json(mapper.writeValueAsString(registeredUser));
        }
        else{
            ctx.status(400);
        }

    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        if(createdMessage!=null){
            ctx.json(mapper.writeValueAsString(createdMessage));
        }
        else{
            ctx.status(400);
        }
    }
    private void getAllMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);

    }

    private void deleteMessageByIDHandler(Context ctx){
        Message deleted = messageService.deleteMessageByID(Integer.parseInt(ctx.pathParam("message_id")));
        if(deleted != null){
            ctx.json(deleted);
        }
    }

    private void getMessageByIDHandler(Context ctx){
        Message found = messageService.getMessageByID(Integer.parseInt(ctx.pathParam("message_id")));
        if(found!=null){
            ctx.json(found);
        }
    }

    private void getAllMessagesByUserHandler(Context ctx){
        List<Message> messages = messageService.getAllMessagesByUser(Integer.parseInt(ctx.pathParam("account_id")));
        ctx.json(messages);
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException{
       
        ObjectMapper mapper = new ObjectMapper();

        String jsonData = ctx.body();
        JsonNode new_message = mapper.readTree(jsonData);
        String new_message_text = new_message.get("message_text").asText();

        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        
        Message updatedMessage = messageService.updateMessage(message_id, new_message_text);

        if(updatedMessage!=null){
            ctx.json(updatedMessage);
        }
        else{
            ctx.status(400);
        }
    }

}