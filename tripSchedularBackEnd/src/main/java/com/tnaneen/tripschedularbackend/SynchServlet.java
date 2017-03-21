/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tnaneen.tripschedularbackend;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect.Type;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ehabm
 */
@WebServlet(name = "SynchServlet", urlPatterns = {"/SynchServlet"})
public class SynchServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            // ArrayList<Trip> trips = (ArrayList<Trip>)request.getAttribute("trips");
            String email = request.getParameter("email");
            String jsonString = request.getParameter("trips");
            Gson gson = new Gson();
//            ArrayList<Notes> notes=new ArrayList<>();
//            notes.add(new Notes(1,12, "bel ganzbeel"));
//            notes.add(new Notes(2,12, "bel eshta"));
//            notes.add(new Notes(3,12, "bel fol"));
//            ArrayList<Trip> test=new ArrayList<>();
//            test.add(new Trip(12,"hamada",null,null,"done","123","bolaq","faisl",notes,"ehab@gmail.com"));
//            test.add(new Trip(13,"roma",123123L,12321L,"done","123","bolaq","haram",notes,"ehab@gmail.com"));
//            test.add(new Trip(15,"ehab",123123L,12321L,"done","123","bolaq","october",notes,"ehab@gmail.com"));
//            System.out.println(gson.toJson(test));
            //parse json to arraylist of trips
            ArrayList<Trip> trips;
            java.lang.reflect.Type type = new TypeToken<ArrayList<Trip>>() {}.getType();
            trips = gson.fromJson(jsonString, type);
            System.out.println("----jsonString: " + jsonString);
            System.out.println("-----Arr length: " + trips.size());
            System.out.println(email);    
            DatabaseHandler db = new DatabaseHandler();
            db.deleteAllUserTrip(email);
            for (int i = 0; i < trips.size(); i++) {
                db.addTrip(trips.get(i),email);
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
