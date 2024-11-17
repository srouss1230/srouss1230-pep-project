package DAO;

import Model.Account;
import Model.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Util.ConnectionUtil;

public class AccountDAO {
    
    // registers the account and then returns it
    // to be used when the POST /register endpoint is hit 
    // if registration is not successful we will return a status code of 400 (client error)
    public Account registerAccount(String username, String password){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO account (username,password) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2, password);

            preparedStatement.executeUpdate();
            return new Account(username,password);


        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    //will use to check whether a username is taken when a new user wishes to register
    public Account getAccountByUserName(String username){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(
                                        rs.getInt("account_id"),
                                        rs.getString("username"),
                                        rs.getString("password")
                                        );
                return account;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    //logs in a user and returns their account
    // to be used when the POST /login endpoint is hit 
    // if a login is unsuccesful, we should return a status code of 401 (not authorized)
    public Account login(String username, String password){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(
                                        rs.getInt("account_id"),
                                        rs.getString("username"),
                                        rs.getString("password")
                                        );
                return account;
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    
}
