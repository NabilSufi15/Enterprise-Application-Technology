/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.sql.PreparedStatement;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author bastinl
 */
public class Users {
  
    private ResultSet rs = null;
    private PreparedStatement pstmt = null;
    DataSource ds = null;
   
    public Users() {
        
        // You don't need to make any changes to the try/catch code below
        try {
            // Obtain our environment naming context
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            // Look up our data source
            ds = (DataSource)envCtx.lookup("jdbc/LessonDatabase");
        }
            catch(Exception e) {
            System.out.println("Exception message is " + e.getMessage());
        }
        
    }

    public int isValid(String name, String pwd) {
       
        try {
            
            Connection connection = ds.getConnection();

            if (connection != null) {
                                

               //TODO: implement this method so that if the user does not exist, it returns -1.
               // If the username and password are correct, it should return the 'clientID' value from the database.
               
               if(name != null && pwd !=null){
                   
                   String query = "SELECT * FROM clients WHERE username ='" + name + "'";
                   
                   PreparedStatement pstmt = connection.prepareStatement(query);
                   ResultSet rs = pstmt.executeQuery(query);
                   
                   if(rs.next()){
                       String password = rs.getString(3);
                       
                       if(password.equals(pwd)){
                           return rs.getInt(1);
                       }else{
                           return -1;
                       }
                       
                   }
               }
                
  

            }
            else {
                return -1;
            }
        } catch(SQLException e) {
                    
            System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
            return -1;
        }
        
        return 0;
    }
    
    // TODO (Optional steps 3 and 4) add a user with specified username and password
    public void addUser(String name, String pwd) {
       
        //TODO: implement this method so that the specified username and password are inserted into the database.

         try {
            
            Connection connection = ds.getConnection();

            if (connection != null) {
                
                
                
            
                // todo check success
                
                //checks if input of new username is already in the database
                String query = "select * from clients where username = '" + name + "'";
                
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);
                
                //loops through and checks if it already exists in the database
                if(!rs.next()){
                    
                    pstmt = connection.prepareStatement("INSERT INTO clients ( username, password) VALUES (?,?)");
                    pstmt.setString(1, name);
                    pstmt.setString(2, pwd);
                    pstmt.executeUpdate();
                }else{
                
                }
                    
                
                
            
            }
            
         }
            catch(SQLException e) {
                System.out.println("Exception is ;"+e + ": message is " + e.getMessage());
               
         }
        
    }
}
