    
package com.raven.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Ingredient {
    Database db;
    
    public Ingredient(){
        db = new Database();
    }
    
    public ResultSet getAll(){
        try{
            String sql = "SELECT * FROM ingredients";
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
            String sql = "SELECT * FROM ingredients";
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
    
    public ResultSet getLastUpdated(){
        try{
            String sql = "SELECT * FROM ingredients ORDER BY update_timestamp DESC LIMIT 10";
            db.stmt = db.con.createStatement();
            db.rs = db.stmt.executeQuery(sql);
            return db.rs;
        }catch(SQLException e){
            System.out.println(e);
            return null;
        }
    }
    
    public boolean checkDuplicate(String ingredient) {
        String sql = "SELECT * FROM ingredients WHERE ingredient = ?";
        try (PreparedStatement pstmt = db.con.prepareStatement(sql)) {
            pstmt.setString(1, ingredient);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println(sql);
                boolean hasNoDuplicates = !rs.next();
                return hasNoDuplicates;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
    
    public void add(String ingredient, String category, String quantity, List<String> menuList){
        String sql = "INSERT INTO ingredients (ingredient, category, quantity, menuList) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = db.con.prepareStatement(sql)) {
            pst.setString(1, ingredient);
            pst.setString(2, category);
            pst.setString(3, quantity);

            // Convert the list of strings to a JSON array string
            String jsonArrayString = "[\"" + String.join("\",\"", menuList) + "\"]";
            pst.setString(4, jsonArrayString);

            pst.executeUpdate();
            System.out.println("Ingredient added successfully");
        } catch(SQLException e) {
            System.out.println("Failed to add ingredient");
            e.printStackTrace();
        }
    }
    
    public ResultSet search(String ingredient, String category, List<String> menuList) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ingredients WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (ingredient != null && !ingredient.isEmpty()) {
            sql.append(" AND ingredient = ?");
            parameters.add(ingredient);
        }
        if (category != null && !category.isEmpty()) {
            sql.append(" AND category = ?");
            parameters.add(category);
        }
        if (menuList != null && !menuList.isEmpty()) {
            for (String searchItem : menuList) {
                sql.append(" AND JSON_CONTAINS(menuList, CAST(? AS JSON))");
                parameters.add("\"" + searchItem + "\"");
            }
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
            e.printStackTrace();
            return null;
        }
    }

//    public void update(int id, String customer, String dish, String size, String pickupDate, String pickupTime){
//        String sql = "UPDATE orders SET customer=?, dish=?, size=?, pickupDate=?, pickupTime=? WHERE orderID=?";
//        try{
//            PreparedStatement pst = db.con.prepareStatement(sql);
//            pst.setString(1, customer);
//            pst.setString(2, dish);
//            pst.setString(3, size);
//            pst.setString(4, pickupDate);
//            pst.setString(5, pickupTime);
//            pst.setInt(6, id);
//            pst.executeUpdate();
//        }catch(SQLException e){
//            System.out.println("Failed to update ingredient");
//            System.out.println(e);
//        }
//    }
    
    public void update(int id, String ingredient, String category, int quantity, List<String> menuList){
        String sql = "UPDATE ingredients SET ingredient=?, category=?, quantity=?, menuList=? WHERE ingredientID=?";
        try{
            PreparedStatement pst = db.con.prepareStatement(sql);
            pst.setString(1, ingredient);
            pst.setString(2, category);
            pst.setInt(3, quantity);
            
            String jsonArrayString = "[\"" + String.join("\",\"", menuList) + "\"]";
            pst.setString(4, jsonArrayString);
            
            pst.setInt(5, id);
            pst.executeUpdate();
        }catch(SQLException e){
            System.out.println("Failed to update ingredient");
            System.out.println(e);
        }
    }
    
    public void delete(int id){
        String sql = "DELETE FROM ingredients WHERE ingredientID="+id+" LIMIT 1";
        
        try{
            PreparedStatement pst = db.con.prepareStatement(sql);
            pst.executeUpdate();
        }catch(SQLException e){
            System.out.println("Failed to delete ingredient");
            System.out.println(e);
        }
    }
}
