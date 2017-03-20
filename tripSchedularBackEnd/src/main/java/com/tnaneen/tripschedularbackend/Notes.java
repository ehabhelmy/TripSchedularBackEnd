/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tnaneen.tripschedularbackend;

/**
 *
 * @author ehabm
 */
public class Notes {
    int noteId;

    public Notes() {
    }

    public Notes(int noteId, int tripId, String content) {
        this.noteId = noteId;
        this.tripId = tripId;
        this.content = content;
    }

    public Notes(int noteId, String content) {

        this.noteId = noteId;
        this.content = content;
    }
    
    public void setTripId(int tripId) {

        this.tripId = tripId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getTripId() {
        return tripId;
    }

    public int getNoteId() {
        return noteId;
    }

    int tripId;
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
