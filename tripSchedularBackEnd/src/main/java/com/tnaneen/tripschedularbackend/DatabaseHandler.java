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
    private final String password = "admin";
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
            ex.printStackTrace();
        }
    }
    
    public boolean checkUser(User user){
        try {
            pst=conn.prepareStatement("SELECT * FROM USER WHERE ID=?");
            pst.setInt(1, user.getId());
            rs=pst.executeQuery();
            if(rs.next()){
                return true;
            }
            else
            {
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public void addTrip(Trip trip){
        try {
            pst=conn.prepareStatement("INSERT INTO TRIP VALUES(?,?,?,?,?,?,?,?,?)");
            pst.setInt(1, trip.getId());
            pst.setString(2, trip.getName());
            pst.setLong(3, trip.getDuration());
            pst.setLong(4, trip.getDate());
            pst.setString(5, trip.getStatus());
            pst.setString(6, trip.getAveSpeeed());
            pst.setString(7, trip.getSource());
            pst.setString(8, trip.getDestination());
            pst.executeUpdate();   
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void addNotes(Notes[] notes,int tripId){
        try {
            for(int i=0;i<notes.length;i++){
                 pst=conn.prepareStatement("INSERT INTO NOTES VALUES(?,?,?)");
                 pst.setInt(1,notes[i].getNoteId());
                 pst.setString(2,notes[i].getContent());
                 pst.setInt(3,notes[i].getTripId());
                 pst.executeUpdate(); 
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public ArrayList<Trip> getUserTrips(User user){
         ArrayList<Integer>tripId = new ArrayList<>();
         ArrayList<Trip>trips = new ArrayList<>();
         try {
            pst=conn.prepareStatement("SELECT * FROM users_Trips WHERE user_id=?");
            pst.setInt(1, user.getId());
            rs=pst.executeQuery();
            while(rs.next()){
                int id=rs.getInt(2);
                tripId.add(id);
            }
            for(int i=0;i<tripId.size();i++){
               pst=conn.prepareStatement("SELECT * FROM trips WHERE trip_id=?");
               pst.setInt(1,tripId.get(i));
               rs=pst.executeQuery();
               while(rs.next()){
                int id=rs.getInt(1);
                String name=rs.getString(2);
                long duration=rs.getLong(3);
                long date=rs.getLong(4);
                String status=rs.getString(5);
                String speed=rs.getString(6);
                String source=rs.getString(7);
                String destination=rs.getString(8);
                ArrayList<Notes>notes=getTripNotes(id);
                Trip trip=new Trip(id,name,duration,date,status,speed,source,destination,notes);
                trips.add(trip);
            }         
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return trips;
    }
    
    
    
    public ArrayList<Notes> getTripNotes(int tripId){
        ArrayList<Notes>notes=new ArrayList<>();
         try {
            pst=conn.prepareStatement("SELECT * FROM notes WHERE trip_id=?");
            pst.setInt(1, tripId);
            rs=pst.executeQuery();
            while(rs.next()){
                int noteId=rs.getInt(1);
                String content=rs.getString(2);
                Notes note=new Notes(noteId,content);
                notes.add(note);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return notes;
    }
    
    
}
