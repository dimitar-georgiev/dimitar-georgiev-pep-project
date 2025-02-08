package Service;

import java.util.List;

import Model.Message;
import DAO.MessageDAO;

public class MessageService {
    public MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message creatMessage(Message msg) {
        return messageDAO.createMessage(msg);
    }

    public List<Message> getAllMessage() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int msgId) {
        return messageDAO.getMessageById(msgId);
    }

    public Message deleteMessageById (int msgId) {
        return messageDAO.deleteMessageById(msgId);
    }

    public Message updateMessageById (Message msg) {
        return messageDAO.updatMessageById(msg);
    }

    public List<Message> getAllMessagesByAccountId (int accId) {
        return messageDAO.getAllMessagesByAccountId(accId);
    }
}
