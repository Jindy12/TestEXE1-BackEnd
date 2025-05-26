/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.common;

import dal.LoginDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import util.HashPassword;

/**
 *
 * @author Sonvu
 */
public class LoginByAccountServlet extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginByAccountServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginByAccountServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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

        Cookie[] arr = request.getCookies();
        System.out.println(arr);
        if (arr != null) {
            for (Cookie cookie : arr) {
                System.out.println("Cookie Name: " + cookie.getName());
                System.out.println("Cookie Value: " + cookie.getValue());
                if (cookie.getName().equalsIgnoreCase("userCookie")) {
                    request.setAttribute("userName", cookie.getValue());
                }
                if (cookie.getName().equalsIgnoreCase("passCookie")) {
                    request.setAttribute("passWord", cookie.getValue());
                }
            }
        }

        request.getRequestDispatcher("login.jsp").forward(request, response);
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
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String pass = request.getParameter("password");
        String cpass = HashPassword.toSHA1(pass);
        String remember = request.getParameter("remember");

        LoginDAO loginDao = new LoginDAO();
        Account account = loginDao.getUsernameAndPassword(username, cpass);
        if(account == null){
            account = loginDao.getEmailAndPassword(username, pass);
        }

        if (account != null) // login successfully!
        {
            if (remember != null) {
                Cookie c_user = new Cookie("userCookie", username);
                Cookie c_pass = new Cookie("passCookie", pass);
                c_user.setMaxAge(3600 * 24 * 30);
                c_pass.setMaxAge(3600 * 24 * 30);
                response.addCookie(c_user);
                response.addCookie(c_pass);

            }
            session.setAttribute("account", account);
            session.setMaxInactiveInterval(60 * 600);
            response.sendRedirect("home");
        } else //login fail
        {
            request.setAttribute("userName", username);
            request.setAttribute("passWord", pass);           
            request.setAttribute("mess", "Sai tên đăng nhập hoặc mật khẩu");
            request.setAttribute("user", username);
            request.setAttribute("pass", pass);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
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
