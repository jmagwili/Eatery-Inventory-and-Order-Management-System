
package com.raven.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class Database {
    public Connection con;
    public Statement stmt = null;
    public ResultSet rs = null;
    
     PreparedStatement pst;
    
    public Database(){
        connect();
    }
    
    private void connect(){
        try{
            this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory?zeroDateTimeBehavior=CONVERT_TO_NULL","root","myDbPassword123");
            System.out.println("Connected to Database");
            
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users");
            
                // Print the column names
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnLabel(i) + "\t");
            }
            System.out.println();

            // Print the values of the result set
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void authenticate(String username, String password){
        String query = "SELECT * FROM users WHERE userName = '"+username+"' AND userPassword = '"+password+"'";
        
        try{
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            
            if (!rs.next()) {
                System.out.println("User not found");
            }else{
                System.out.println("Welcome " + rs.getString("userName"));
            }
            
        }catch(Exception e){
            System.out.println("user not found");
            System.out.println(e);
        }
        

    }
}
