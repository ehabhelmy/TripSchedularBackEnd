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
//    private ResultSet rs;

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

    public DatabaseHandler() {
        openConnection();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize(); //To change body of generated methods, choose Tools | Templates.
        closeConnection();
    }

    
    
    public void addUser(User user) {
        try {
            pst = conn.prepareStatement("INSERT INTO USERS (user_email,user_password) VALUES (?,?)");
            pst.setString(1, user.getEmail().toLowerCase());
            pst.setString(2, user.getPassword());
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean checkLogin(String email,String password){
        try {
            pst=conn.prepareStatement("SELECT * FROM USERS WHERE user_email=? AND user_password=?");
            pst.setString(1, email.toLowerCase());
            pst.setString(2, password);
            
            ResultSet rs=pst.executeQuery();
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
    
    public boolean checkUserExisting(String email){
        try {
            pst=conn.prepareStatement("SELECT * FROM USERS WHERE user_email=?");
            pst.setString(1, email.toLowerCase());            
            ResultSet rs=pst.executeQuery();
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
    public void addTrip(Trip trip) {
        ArrayList<Notes> notes=new ArrayList<Notes>();
        try {
            System.out.println("added trip");
            pst = conn.prepareStatement("INSERT INTO TRIPS VALUES(?,?,?,?,?,?,?,?,?)");
            pst.setInt(1, trip.getId());
            pst.setString(2, trip.getName());
            pst.setLong(3, trip.getDuration());
            pst.setLong(4, trip.getDate());
            pst.setString(5, trip.getStatus());
            pst.setString(6, trip.getAveSpeeed());
            pst.setString(7, trip.getSource());
            pst.setString(8, trip.getDestination());
            pst.setString(9, trip.getUserEmail());
            pst.executeUpdate();
            System.out.println("trip added success");
            notes=trip.getNotes();
            addNotes(notes,trip.getId(),trip.getUserEmail());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    

    public void addNotes(ArrayList<Notes>notes, int tripId, String email) {
        try {
            for (int i = 0; i < notes.size(); i++) {
                System.out.println("added notes");
                pst = conn.prepareStatement("INSERT INTO NOTES (note_id,content,trip_id,user_email) VALUES(?,?,?,?)");
                pst.setInt(1, notes.get(i).getNoteId());
                pst.setString(2, notes.get(i).getContent());
                pst.setInt(3, notes.get(i).getTripId());
                pst.setString(4,email);
                pst.executeUpdate();
                System.out.println("notes add success");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public ArrayList<Trip> getUserTrips(String email){
//         ArrayList<Integer>tripId = new ArrayList<>();
         ArrayList<Trip>trips = new ArrayList<>();
         try {
            System.out.println("getting user trips");
            pst=conn.prepareStatement("SELECT * FROM Trips WHERE user_email=?");
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                    int id = rs.getInt(1);
                    String name = rs.getString(2);
                    long duration = rs.getLong(3);
                    long date = rs.getLong(4);
                    String status = rs.getString(5);
                    String speed = rs.getString(6);
                    String source = rs.getString(7);
                    String destination = rs.getString(8);
                    String emaill=rs.getString(9);
                    ArrayList<Notes> notes = getTripNotes(id,email);
                    Trip trip = new Trip(id, name, duration, date, status, speed, source, destination, notes,emaill);
                    trips.add(trip);
                }
             return trips;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
       
        
    }

    public ArrayList<Notes> getTripNotes(int tripId,String email) {
        ArrayList<Notes> notes = new ArrayList<>();
        try {
            pst = conn.prepareStatement("SELECT * FROM notes WHERE trip_id=? AND user_email=?");
            pst.setInt(1, tripId);
            pst.setString(2, email);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int noteId = rs.getInt(1);
                String content = rs.getString(2);
                Notes note = new Notes(noteId,tripId, content);
                notes.add(note);
            }
            return notes;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
      
    }

    public void deleteAllUserTrip(String email){
        ArrayList<Trip> trips=getUserTrips(email);
        for (Trip trip : trips) {
            System.out.println("deleting");
            deleteAllNotes(trip.getId(),email);
            deleteTrip(trip.getId(),email);
        }
    }
    
    public void deleteAllNotes(int tripId, String email){
        try {
            pst=conn.prepareStatement("DELETE FROM NOTES WHERE trip_id=? AND user_email=?");
            pst.setInt(1, tripId);
            pst.setString(2,email);
            pst.executeUpdate();
            System.out.println("deleting notes");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void deleteTrip(int tripId, String email){
         try {
            pst=conn.prepareStatement("DELETE FROM TRIPS WHERE trip_id=? AND user_email=?");
            pst.setInt(1, tripId);
            pst.setString(2,email);
            pst.executeUpdate();
             System.out.println("deleting trips");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
  
}
