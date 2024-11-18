package Service;

import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO; 
    private AccountDAO accountDAO;
    
    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }
    public Message createMessage(Message message){
        //check to make sure the conditions for registering are met
        boolean postedByIsNotValid = (accountDAO.getAccountByID(message.posted_by) == null);
        boolean messageTooLong = (message.message_text.length() > 255);
        boolean messageIsBlank = (message.message_text == "");

        if(postedByIsNotValid || messageTooLong || messageIsBlank){
            return null;
        }

        return messageDAO.createMessage(message);
    }
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }


    public Message deleteMessageByID(int message_id){
        return messageDAO.deleteMessageById(message_id);
    }

    public Message getMessageByID(int message_id){
        return messageDAO.getMessageById(message_id);
    }
    public List<Message> getAllMessagesByUser(int userID) {
        return messageDAO.getAllMessagesFromUser(userID);
    }
     
    public Message updateMessage(int message_id, String message_text){
        boolean messageTooLong = (message_text.length() > 255);
        boolean messageIsBlank = (message_text == "");
        if(messageTooLong || messageIsBlank){
            return null;
        }
        return messageDAO.updateMessageById(message_id, message_text);
    }
}
