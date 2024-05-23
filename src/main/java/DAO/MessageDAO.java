package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    /* 
     * Persist a new message, posted by an already existing user.
     */
    public Message newMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                int message_id = rs.getInt(1);
                message.setMessage_id(message_id);
            return message;
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * Retrieve all messages in the database.
     */
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
            messages.add(message);
            }
            return messages;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * Retrieve a specific message by its ID.
     * 
     * @param message_id a message id, linked to a specific message post.
     */
    public Message getMessage(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                            rs.getInt("posted_by"),
                            rs.getString("message_text"),
                            rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /*
     * Delete a message given its message_id.
     * NEED: To check if message exisited.
     * 
     * @param message_id a message id, linked to a specific message post.
     */
    public boolean deleteMessage(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        boolean deleted = false;
        try{
            String sql = "DELETE FROM message WHERE message_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            int check = preparedStatement.executeUpdate();

            if(check > 0){
                deleted = true;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return deleted;
    }

    /*
     * Update a message's message_text given the message_id.
     * 
     * @param message_id a message id, linked to a specific message post.
     * @param message message containing a message_text to be updated.
     */
    public Message updateMessage(int message_id, Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, message.message_text);
            preparedStatement.setInt(2, message_id);

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
      }

    /*
     * Retrieve all messages posted by a particular user.
     * 
     * @param posted_by a specific id linked to a user posting messages.
     */
    public List<Message> getMessageFromUser(int posted_by){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, posted_by);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                            rs.getInt("posted_by"),
                            rs.getString("message_text"),
                            rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
      }
}
    
