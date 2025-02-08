package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

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
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::userRegistrationHandler);
        app.post("/login", this::userLoginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountId);

        return app;
    }


    private void userRegistrationHandler (Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account createdAccount = accountService.userRegistration(account);

        if (createdAccount != null) {
            context.json(createdAccount);
        } else {
            context.status(400);
        }
    }

    private void userLoginHandler (Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account loggedUser = accountService.userLogin(account);

        if (loggedUser != null) {
            context.json(loggedUser);
        } else {
            context.status(401);
        }
    }

    private void createMessageHandler (Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(context.body(), Message.class);
        Message createdMessage = messageService.creatMessage(msg);

        if (createdMessage != null) {
            context.json(createdMessage);
        } else {
            context.status(400);
        }
    }

    private void getAllMessagesHandler (Context context) {
        List<Message> messages = messageService.getAllMessage();
        context.json(messages);
    }

    private void getMessageByIdHandler (Context context) {
        int msgId = Integer.parseInt(Objects.requireNonNull(context.pathParam("message_id")));
        Message msg = messageService.getMessageById(msgId);

        if (msg != null) {
            context.json(msg);
        } else {
            context.result();
        }
    }

    private void deleteMessageByIdHandler (Context context) {
        int msgId = Integer.parseInt(Objects.requireNonNull(context.pathParam("message_id")));

        Message msg = messageService.deleteMessageById(msgId);

        if (msg != null) {
            context.status(200).json(msg);
        } else {
            context.status(200).result();
        }
    }

    private void updateMessage (Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(context.body(), Message.class);
        Message updatedMessage = messageService.updateMessageById(msg);

        if (updatedMessage != null) {
            context.json(updatedMessage);
        } else {
            context.status(400);
        }
    }

    private void getAllMessagesByAccountId (Context context) {
        int accId = Integer.parseInt(Objects.requireNonNull(context.pathParam("account_id")));

        List<Message> messages = messageService.getAllMessagesByAccountId(accId);

        context.json(messages);
    }

}