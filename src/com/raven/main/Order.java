
package com.raven.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    Database db;
    
    public Order(){
        db = new Database();
    }
    
    public ResultSet getAll(){
        try{
            String sql = "SELECT * FROM orders";
            db.stmt = db.con.createStatement();
            db.rs = db.stmt.executeQuery(sql);
            return db.rs;
        }catch(SQLException e){
            System.out.println(e);
            return null;
        }
    }
    
     public int getTotal(){
         try{
            String sql = "SELECT * FROM orders";
            db.stmt = db.con.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE, 
                     ResultSet.CONCUR_READ_ONLY);
            db.rs = db.stmt.executeQuery(sql);
            db.rs.last();
            
            return db.rs.getRow();      
        }catch(SQLException e){
            System.out.println(e);
            return -1;
        }
    }
     
    public ResultSet getOrdersToday(){
       try{
           String sql = "SELECT * FROM orders WHERE DATE(pickupDate) = CURDATE() AND status = 'pending'";
           db.stmt = db.con.createStatement();
           db.rs = db.stmt.executeQuery(sql);
           return db.rs;
       }catch(SQLException e){
           System.out.println(e);
           return null;
       }
    }
    
    public int getTotalOrdersToday(){
        try{
            String sql = "SELECT * FROM orders WHERE DATE(pickupDate) = CURDATE()";
            db.stmt = db.con.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE, 
                     ResultSet.CONCUR_READ_ONLY);
            db.rs = db.stmt.executeQuery(sql);
            db.rs.last();
            
            return db.rs.getRow();      
        }catch(SQLException e){
            System.out.println(e);
            return -1;
        }
    }
    
    public ResultSet getOrdersThisWeek(){
        try{
           String sql = "SELECT * FROM orders WHERE YEARWEEK(pickupDate, 0) = YEARWEEK(CURDATE(), 0)";
           db.stmt = db.con.createStatement();
           db.rs = db.stmt.executeQuery(sql);
           return db.rs;
        }catch(SQLException e){
           System.out.println(e);
           return null;
       }
    }
    
    public int getTotalOrdersThisWeek(){
        try{
            String sql = "SELECT * FROM orders WHERE YEARWEEK(pickupDate, 0) = YEARWEEK(CURDATE(), 0)";
            db.stmt = db.con.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE, 
                     ResultSet.CONCUR_READ_ONLY);
            db.rs = db.stmt.executeQuery(sql);
            db.rs.last();
            
            return db.rs.getRow();      
        }catch(SQLException e){
            System.out.println(e);
            return -1;
        }
    }
    
    public ResultSet getOrdersNextWeek(){
        try{
           String sql = "SELECT * FROM orders WHERE YEARWEEK(pickupDate, 0) = YEARWEEK(CURDATE() + INTERVAL 1 WEEK, 0)";
           db.stmt = db.con.createStatement();
           db.rs = db.stmt.executeQuery(sql);
           return db.rs;
        }catch(SQLException e){
           System.out.println(e);
           return null;
       }
    }
    
    public int getTotalOrdersNextWeek(){
        try{
            String sql = "SELECT * FROM orders WHERE YEARWEEK(pickupDate, 0) = YEARWEEK(CURDATE() + INTERVAL 1 WEEK, 0)";
            db.stmt = db.con.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE, 
                     ResultSet.CONCUR_READ_ONLY);
            db.rs = db.stmt.executeQuery(sql);
            db.rs.last();
            
            return db.rs.getRow();      
        }catch(SQLException e){
            System.out.println(e);
            return -1;
        }
    }
    
    public void add(String customer, String dish, String size, String pickupDate, String pickupTime, String status){
        String sql = "INSERT INTO orders (customer, dish, size, pickupDate, pickupTime, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = db.con.prepareStatement(sql)) {
            pst.setString(1, customer);
            pst.setString(2, dish);
            pst.setString(3, size);
            pst.setString(4, pickupDate);
            pst.setString(5, pickupTime);
            pst.setString(6, status);

            pst.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Failed to add ingredient");
            e.printStackTrace();
        }
    }
    
    public ResultSet search(String customer, String dish, String pickupDate, String pickupTime, String status){
        StringBuilder sql = new StringBuilder("SELECT * FROM orders WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (customer != null && !customer.isEmpty()) {
            sql.append(" AND customer = ?");
            parameters.add(customer);
        }
        if (dish != null && !dish.isEmpty()) {
            sql.append(" AND dish = ?");
            parameters.add(dish);
        }
        if (pickupDate != null && !pickupDate.isEmpty()) {
            sql.append(" AND pickupDate = ?");
            parameters.add(pickupDate);
        }
        if (pickupTime != null && !pickupTime.isEmpty()) {
            sql.append(" AND pickupTime = ?");
            parameters.add(pickupTime);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
            parameters.add(status);
        }

        try {
            PreparedStatement pst = db.con.prepareStatement(sql.toString());

            for (int i = 0; i < parameters.size(); i++) {
                pst.setObject(i + 1, parameters.get(i));
            }

            System.out.println(pst); // Debugging purposes

            return pst.executeQuery();

        } catch (SQLException e) {
            System.out.println("Error searching ingredient");
            System.out.println(e);
            return null;
        }
    }
 
    public void update(int id, String customer, String dish, String size, String pickupDate, String pickupTime, String status){
        String sql = "UPDATE orders SET customer=?, dish=?, size=?, pickupDate=?, pickupTime=?, status=? WHERE orderID=?";
        try{
            PreparedStatement pst = db.con.prepareStatement(sql);
            pst.setString(1, customer);
            pst.setString(2, dish);
            pst.setString(3, size);
            pst.setString(4, pickupDate);
            pst.setString(5, pickupTime);
            pst.setString(6, status);
            pst.setInt(7, id);
            pst.executeUpdate();
        }catch(SQLException e){
            System.out.println("Failed to update ingredient");
            System.out.println(e);
        }
    }
    
    public void delete(int id){
        String sql = "DELETE FROM orders WHERE orderID="+id+" LIMIT 1";
        
        try{
            PreparedStatement pst = db.con.prepareStatement(sql);
            pst.executeUpdate();
        }catch(SQLException e){
            System.out.println("Failed to delete ingredient");
            System.out.println(e);
        }
    }
}
