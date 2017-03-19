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

    public void addUser(User user) {
        try {
            pst = conn.prepareStatement("INSERT INTO USERS VALUES (?,?)");
            pst.setString(1, user.getEmail());
            pst.setString(2, user.getPassword());
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
            return false;
        }
    }
    public void addTrip(Trip trip) {
        ArrayList<Notes> notes=new ArrayList<Notes>();
        try {
            pst = conn.prepareStatement("INSERT INTO TRIP VALUES(?,?,?,?,?,?,?,?,?)");
            pst.setInt(1, trip.getId());
            pst.setString(2, trip.getName());
            pst.setLong(3, trip.getDuration());
            pst.setLong(4, trip.getDate());
            pst.setString(5, trip.getStatus());
            pst.setString(6, trip.getAveSpeeed());
            pst.setString(7, trip.getSource());
            pst.setString(8, trip.getDestination());
            pst.executeUpdate();
            notes=trip.getNotes();
            addNotes(notes,trip.getId());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addNotes(ArrayList<Notes>notes, int tripId) {
        try {
            for (int i = 0; i < notes.size(); i++) {
                pst = conn.prepareStatement("INSERT INTO NOTES VALUES(?,?)");
                pst.setString(1, notes.get(i).getContent());
                pst.setInt(2, notes.get(i).getTripId());
                pst.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Trip> getUserTrips(User user) {
        ArrayList<Integer> tripId = new ArrayList<>();
        ArrayList<Trip> trips = new ArrayList<>();
        try {
            pst = conn.prepareStatement("SELECT * FROM users_Trips WHERE user_id=?");
            pst.setInt(1, user.getId());
            rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(2);
                tripId.add(id);
            }
            for (int i = 0; i < tripId.size(); i++) {
                pst = conn.prepareStatement("SELECT * FROM trips WHERE trip_id=?");
                pst.setInt(1, tripId.get(i));
                rs = pst.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String name = rs.getString(2);
                    long duration = rs.getLong(3);
                    long date = rs.getLong(4);
                    String status = rs.getString(5);
                    String speed = rs.getString(6);
                    String source = rs.getString(7);
                    String destination = rs.getString(8);
                    ArrayList<Notes> notes = getTripNotes(id);
                    Trip trip = new Trip(id, name, duration, date, status, speed, source, destination, notes);
                    trips.add(trip);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return trips;
    }

    public ArrayList<Notes> getTripNotes(int tripId) {
        ArrayList<Notes> notes = new ArrayList<>();
        try {
            pst = conn.prepareStatement("SELECT * FROM notes WHERE trip_id=?");
            pst.setInt(1, tripId);
            rs = pst.executeQuery();
            while (rs.next()) {
                int noteId = rs.getInt(1);
                String content = rs.getString(2);
                Notes note = new Notes(noteId, content);
                notes.add(note);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return notes;
    }

    public int getUserId(String email) {
        try {
            pst = conn.prepareStatement("SELECT user_id FROM USERS where user_email=?");
            pst.setString(1, email);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }

    }

    public void deleteAllUserTrip(String email) {
        ArrayList<Integer> tripIds = new ArrayList<>();
        try {
            int id = getUserId(email);
            pst = conn.prepareStatement("SELECT trip_id FROM  users_Trips where user_id=?");
            pst.setInt(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                tripIds.add(rs.getInt(1));
            }

            if (tripIds.size() > 0) {
                for (int i = 0; i < tripIds.size(); i++) {
                    pst = conn.prepareStatement("DELETE FROM notes where trip_id=?");
                    pst.setInt(1, tripIds.get(i));
                    pst.executeUpdate();

                    pst = conn.prepareStatement("DELETE FROM trips where trip_id=?");
                    pst.setInt(1, tripIds.get(i));
                    pst.executeUpdate();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

}
