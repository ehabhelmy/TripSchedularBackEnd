package com.tnaneen.tripschedularbackend;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

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
        response.setContentType("application/json");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String flag = request.getParameter("flag");
        PrintWriter out = response.getWriter();
        if (flag != null && flag.equals("app")) {
            if (new DatabaseHandler().checkUserExisting(email)) {
                //Return User Trips In Json Format
                ArrayList<Trip> userTrips = new DatabaseHandler().getUserTrips(email);
                Gson gson = new Gson();
                String userTripsInJsonFormat = gson.toJson(userTrips);
                out.print(userTripsInJsonFormat);
                out.flush();
            } else {
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                new DatabaseHandler().addUser(user);
                //Return User Trips In Json Format
                ArrayList<Trip> userTrips = new DatabaseHandler().getUserTrips(email);
                Gson gson = new Gson();
                String userTripsInJsonFormat = gson.toJson(userTrips);
                out.print(userTripsInJsonFormat);
                out.flush();
            }
        } else {
            if (new DatabaseHandler().checkLogin(email, password)) {
                //Return User Trips In Json Format
                ArrayList<Trip> userTrips = new DatabaseHandler().getUserTrips(email);
                Gson gson = new Gson();
                String userTripsInJsonFormat = gson.toJson(userTrips);
                out.print(userTripsInJsonFormat);
                out.flush();
            } else {
                out.print("not exist");
                out.flush();
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
