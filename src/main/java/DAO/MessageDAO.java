package DAO;

import Model.Account;
import Model.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Util.ConnectionUtil;

public class MessageDAO {
    
    //Creates a message based on the input parameters. To be called when the POST /messages is hit
    // ACCEPTS: posted_by, message_text, and time_posted_epoch parameters
    // RETURNS: The message created if successful. NULL if unsuccessful
    public Message createMessage(int posted_by, String message_text, Long time_posted_epoch){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //sets up the prepared statement with the SQL string and the id and new message passed in to the method
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,posted_by);
            preparedStatement.setString(2, message_text);
            preparedStatement.setLong(3,time_posted_epoch);

            preparedStatement.executeUpdate();
            return new Message(posted_by,message_text,time_posted_epoch);
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;


    }
    //Receives all messages. To be called when the get /messages endpoint is hit.
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
    //returns all messages from the user with the matching userId
    // to be called when the GET /accounts/{account_id}/messages  endpoint is hit
    public List<Message> getAllMessagesFromUser(int userID){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            //selects all messages from the user passed into the method
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,userID);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    //Gets a message from the Database by ID. To be called when the GET /messages/{messageID} is hit
    // ACCEPTS: A messageID parameter
    // RETURNS: The message retrieved if successful. NULL if unsuccessful
    public Message getMessageById(int messageID){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //sets up the prepared statement with the SQL string and the id passed in to the method
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageID);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                                        rs.getInt("posted_by"), 
                                        rs.getString("message_text"), 
                                        rs.getLong("time_posted_epoch")
                                        );
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Gets a message from the Database by ID. To be called when the DELETE /messages/{messageID} is hit
    // ACCEPTS: A messageID parameter
    // RETURNS:  The message deleted if successful. NULL if unsuccessful. 
    // Note: If unscuessful, the response body should just not return the message body.
    public Message deleteMessageById(int messageID){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //gets the message to return if delete is successful
            Message deleted = getMessageById(messageID);
            //sets up the prepared statement with the SQL string and the id passed in to the method
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageID);

            preparedStatement.executeUpdate();
            return deleted;
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    //Gets a message from the Database by ID. To be called when the PATCH /messages/{messageID} is hit
    // ACCEPTS: A messageID parameter
    // RETURNS:  The updated message if successful. NULL if unsuccessful. 
    // Note: If unscuessful, the response body should have a status of 400
    public Message updateMessageById(int messageID, String message_text){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //sets up the prepared statement with the SQL string and the id and new message passed in to the method
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,message_text);
            preparedStatement.setInt(2, messageID);

            preparedStatement.executeUpdate();
            //returns the message, which will now have been updated
            return getMessageById(messageID);
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }



}
