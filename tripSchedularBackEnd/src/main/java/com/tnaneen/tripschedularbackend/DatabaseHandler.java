/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tnaneen.tripschedularbackend;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author ehabm
 */
public class DatabaseHandler {

    private final String URL = "jdbc:mysql://localhost/TripSchedular";
    private final String userName = "root";
    private final String password = "";
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;

    private void openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(URL, userName, password);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            pst.close();
            //rs.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
   
    public void addUser(User user){
        try {
            pst=conn.prepareStatement("INSERT INTO USERS VALUES (?,?)");
            pst.setString(1, user.getEmail());
            pst.setString(2, user.getPassword());
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean checkUser(String email,String password){
        try {
            pst=conn.prepareStatement("SELECT * FROM USERS WHERE EMAIL=? AND PASSWORD=?");
            pst.setString(1, email);
            pst.setString(2, password);
            rs=pst.executeQuery();
            if(rs.next()){
                return true;
            }
            else
            {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public void addTrip(Trip trip){
        try {
            pst=conn.prepareStatement("INSERT INTO TRIP VALUES(?,?,?,?,?,?,?,?,?)");
            pst.setInt(1, trip.getId());
            pst.setString(2, trip.getAveSpeeed());
            pst.setString(3, trip.getDestination());
            pst.setString(4, trip.getSource());
            pst.setString(5, trip.getName());
            pst.setString(6, trip.getStatus());
            pst.setLong(7, trip.getDate());
            pst.setLong(8, trip.getDuration());
            pst.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void addNotes(Notes[] notes,int tripId){
        try {
            pst=conn.prepareStatement("INSERT INTO NOTES VALUES(?,?,?)");
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
