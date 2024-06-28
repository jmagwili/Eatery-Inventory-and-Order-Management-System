
package com.raven.main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    Database db;
    
    public Menu(){
        db=new Database();
//        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public ResultSet getAll(){
        try{
            String sql = "SELECT * FROM menu";
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
           String sql = "SELECT * FROM menu";
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
        
    public boolean checkDuplicate(String menu) {
        String sql = "SELECT * FROM menu WHERE menu = ?";
        try (PreparedStatement pstmt = db.con.prepareStatement(sql)) {
            pstmt.setString(1, menu);
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
    
    public void add(String menu, String type){
        String sql = "INSERT INTO menu(menu, type) VALUE (?, ?)";

        try (PreparedStatement pst = db.con.prepareStatement(sql)) {
            pst.setString(1, menu);
            pst.setString(2, type);
            pst.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Failed to add menu");
            e.printStackTrace();
        }
    }
    
//    public ResultSet search(String menu, String type){
//        String sql = "SELECT * FROM menu WHERE menu='"+menu+"'";
//        try{
//            db.stmt = db.con.createStatement();
//            db.rs = db.stmt.executeQuery(sql);
//            System.out.println(sql);
//            return db.rs;
//            
//        }catch(SQLException e){
//            System.out.println("Failed to search menu");
//            System.out.println(e);
//            return null;
//        }
//    }
    
    public ResultSet search(String menu, String type){
        StringBuilder sql = new StringBuilder("SELECT * FROM menu WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (menu != null && !menu.isEmpty()) {
            sql.append(" AND menu = ?");
            parameters.add(menu);
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND type = ?");
            parameters.add(type);
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
 
    
    public void update(int id, String menu, String type){
        String sql = "UPDATE menu SET menu=?, type=? WHERE menuID=?";
        try{
            PreparedStatement pst = db.con.prepareStatement(sql);
            pst.setString(1, menu);
            pst.setString(2, type);
            pst.setInt(3, id);
            pst.executeUpdate();
        }catch(SQLException e){
            System.out.println("Failed to update menu");
            System.out.println(e);
        }
    }
    
    public void delete(int id){
        String sql = "DELETE FROM menu WHERE menuID="+id+" LIMIT 1";
        
        try{
            PreparedStatement pst = db.con.prepareStatement(sql);
            pst.executeUpdate();
        }catch(SQLException e){
            System.out.println("Failed to delete menu");
            System.out.println(e);
        }
    }
}
