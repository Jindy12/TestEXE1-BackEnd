/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.common;

import controller.common.GoogleLogin;
import Constain.Iconstant;
import dal.LoginDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.GoogleAccount;
import util.HashPassword;

/**
 *
 * @author Sonvu
 */
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
        String code = request.getParameter("code");
        GoogleLogin gg = new GoogleLogin();

        try {
            String accessToken = gg.getToken(code);
            GoogleAccount ggAccount = gg.getUserInfo(accessToken);

            String firstName = ggAccount.getGiven_name();
            String lastName = ggAccount.getFamily_name();
            String gender = "2";
            String email = ggAccount.getEmail();
            String picture = ggAccount.getPicture();
            String password = Iconstant.generateRandomPassword(8);
            String cpass = HashPassword.toSHA1(password);

            HttpSession session = request.getSession();
            LoginDAO login = new LoginDAO();

            Account account = login.getByEmail(email);

            if (account == null) {
                login.inserUserByEmail(email, cpass, firstName, lastName, gender, email, "", "", picture);
                account = login.getByEmail(email);
            }

            session.setAttribute("account", account);
            session.setMaxInactiveInterval(60 * 600);
            request.getRequestDispatcher("home").forward(request, response);
        } catch (Exception e) {
            // Ghi lại lỗi để dễ dàng debug
            e.printStackTrace();

            // Chuyển hướng đến trang lỗi tùy chỉnh
            response.sendRedirect("error.jsp");
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



