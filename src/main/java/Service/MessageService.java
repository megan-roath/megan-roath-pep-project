package Service;

import Model.Account;
import Model.Message;
import DAO.MessageDAO;
import DAO.AccountDAO;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;
    AccountDAO accountDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }


    /*
     * Create a new message.
     * 
     * @param message the message to be created
     * @return message not conatining an id iff the message is not blank and is less than 255 characters.
     */
    public Message createMessage(Message message){
       int reference = message.getPosted_by();

            Account account = accountDAO.getAccountByID(reference);
            if(account != null){
               if(!message.getMessage_text().isBlank()){
                    if(message.getMessage_text().length() < 255){
                        return messageDAO.newMessage(message);
                    }
               } 
            }
        return null;
    }

    /*
     * Use the messageDAO to retrieve a message by its specific id.
     * 
     * @param message_id specific id linked to the message to be retrieved.
     * @return a message from message_id.
     */
    public Message getMessage(int message_id) {
        return messageDAO.getMessage(message_id);
    }

    /*
     * Use the messageDAO to retrieve all messages.
     * 
     * @return all messages in the database.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /*
     * Update an existing message from the database.
     * 
     * @param message_id the specific id that already exists.
     * @param message the message containing the message_text to be updated
     *                new message_text is not blank, and is not over 255 characters
     * @return the newly updated message.
     */
    public Message updateMessage(int message_id, Message message) {
        if(messageDAO.getMessage(message_id)!= null){
            if(message.message_text.length()<=255 && !message.message_text.isBlank()){
                messageDAO.updateMessage(message_id, message);
                return messageDAO.getMessage(message_id);
            }
        }
            return null;
        }

    /*
     * Delete a message from the database given it's specific message_id.
     * 
     * @param message_id unique id that determines the message to be deleted.
     */
    public void deleteMessage(int message_id){
        messageDAO.deleteMessage(message_id);
    }

    /*
     * Use the messageDAO to retrieve a List of all messages written by a particular user.
     * 
     * @param posted_by the foreign key linked to the user's account_id.
     * @return all messages from user assosciated with posted_by.
     */
    public List<Message> getMessageFromUser(int posted_by) {
        return messageDAO.getMessageFromUser(posted_by);
    }
}
