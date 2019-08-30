/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.LessonTimetable;
import model.Users;
import model.LessonSelection;

/**
 *
 * @author bastinl
 */
public class Controller extends HttpServlet {

   private Users users;
   private LessonTimetable availableLessons;
   private LessonSelection selected;

    public void init() {
         users = new Users();
         availableLessons = new LessonTimetable();
         //selection = new LessonSelection();
         // TODO Attach the lesson timetable to an appropriate scope
        
    }
    
    public void destroy() {
        
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        RequestDispatcher dispatcher = null;
        HttpSession session = request.getSession(false);
        if(action.equals("/login")){
            
            //Get's username and password string from the login
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            //assigns the isValid function from the Users model class to int to check whether the username and password is correct
            int id = users.isValid(username, password);
            
            
            // if the username and password is correct 
            if(id != -1){
                
                //redirects to LessonTimeTableViewPage
                dispatcher =
                this.getServletContext().getRequestDispatcher("/LessonTimetableView.jspx");
                
                //sets the user's id into the session
                session.setAttribute("id", id);
                
                //sets the user's name into the session
                session.setAttribute("username", username);
                
                //Passes the users id to the lesson selection
                selected = new LessonSelection(id);
                
                //sets the lesson selected into the session
                session.setAttribute("LessonSelection", selected);

                
            }else{
                
                //redirects to the login page if username and password is incorrect
                dispatcher =
                     this.getServletContext().getRequestDispatcher("/login.jsp");
            
            }
        }else if(action.equals("/chooseLesson")){
            
            //get's the id of the lesson that the user selected
            String lessonID = request.getParameter("lessonID");
            
            //displays the information about the lesson that the user selected
            selected.showLesson(lessonID);
            
            dispatcher =
                     this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");
            
            
        }else if(action.equals("/finaliseBooking")){
            
            //Get's the id of the user
            Integer ownerID = (Integer) session.getAttribute("id");
            
            //assigns the user id to the lessons that he/she has selected and updates to the database
            selected.updateBooking(ownerID);
            
            //redirects to the lesson timetable view
            dispatcher =
                this.getServletContext().getRequestDispatcher("/LessonTimetableView.jspx");
            
        }else if(action.equals("/lessons")){
            
            //redirects user to lesson timetable view page
            dispatcher =
                this.getServletContext().getRequestDispatcher("/LessonTimetableView.jspx");
        
        }else if(action.equals("/selected")){
            
            //redirects the user to the lesson selection view page
            dispatcher =
                     this.getServletContext().getRequestDispatcher("/LessonSelectionView.jspx");
            
        }else if(action.equals("/addUser")){
            
            //get's new username and password from login 
            String username = request.getParameter("newUsername");
            String password = request.getParameter("newPassword");
            
            //adds the new username and password into the database
            users.addUser(username, password);
            
            dispatcher =
                this.getServletContext().getRequestDispatcher("/login.jsp");
            
        }else if(action.equals("/logout")){
            session.invalidate();
            dispatcher =
                     this.getServletContext().getRequestDispatcher("/login.jsp");
        }
        
        
        dispatcher.forward(request, response);
        
       

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
