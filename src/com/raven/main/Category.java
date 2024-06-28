
package com.raven.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Category {
    Database db;
    
    public Category(){
        db=new Database();
    }
    
    public ResultSet getAll(){
        try{
            String sql = "SELECT * FROM categories";
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
           String sql = "SELECT * FROM categories";
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
        
    public boolean checkDuplicate(String category) {
        String sql = "SELECT * FROM categories WHERE category = ?";
        try (PreparedStatement pstmt = db.con.prepareStatement(sql)) {
            pstmt.setString(1, category);
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
    
    public void add(String category){
        String sql = "INSERT INTO categories(category) VALUE (?)";

        try (PreparedStatement pst = db.con.prepareStatement(sql)) {
            pst.setString(1, category);
            pst.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Failed to add category");
            e.printStackTrace();
        }
    }
    
    public ResultSet search(String category){
        String sql = "SELECT * FROM categories WHERE category='"+category+"'";
        try{
            db.stmt = db.con.createStatement();
            db.rs = db.stmt.executeQuery(sql);
            System.out.println(sql);
            return db.rs;
            
        }catch(SQLException e){
            System.out.println("Failed to search category");
            System.out.println(e);
            return null;
        }
    }
    
    public void update(int id, String category){
        String sql = "UPDATE categories SET category=? WHERE categoryID=?";
        try{
            PreparedStatement pst = db.con.prepareStatement(sql);
            pst.setString(1, category);
            pst.setInt(2, id);
            pst.executeUpdate();
        }catch(SQLException e){
            System.out.println("Failed to update category");
            System.out.println(e);
        }
    }
    
    public void delete(int id){
        String sql = "DELETE FROM categories WHERE categoryID="+id+" LIMIT 1";
        
        try{
            PreparedStatement pst = db.con.prepareStatement(sql);
            pst.executeUpdate();
        }catch(SQLException e){
            System.out.println("Failed to delete ingredient");
            System.out.println(e);
        }
    }
}
