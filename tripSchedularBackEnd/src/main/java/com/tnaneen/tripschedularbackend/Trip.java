/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tnaneen.tripschedularbackend;

import java.util.ArrayList;

/**
 *
 * @author ehabm
 */
public class Trip {
    int id;
    String name;
    String status;
    String aveSpeeed;
    String source;
    String destination;
    Long date;
    Long duration;
    ArrayList<Notes>notes;

    public Trip() {
    }
    public Trip(int id,String name,Long duration,Long date,String status,String avespeed,String source,String destination,ArrayList<Notes>notes) {    
        this.id=id;
        this.name=name;
        this.duration=duration;
        this.date=date; 
        this.status=status;
        this.aveSpeeed=avespeed;
        this.source=source;
        this.destination=destination;
        this.notes=notes;
    }
    
    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public ArrayList<Notes> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Notes> notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAveSpeeed() {
        return aveSpeeed;
    }

    public void setAveSpeeed(String aveSpeeed) {
        this.aveSpeeed = aveSpeeed;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
