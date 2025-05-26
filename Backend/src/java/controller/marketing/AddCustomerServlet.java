/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.marketing;

import dal.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;

/**
 *
 * @author tuana
 */
public class AddCustomerServlet extends HttpServlet {

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
            out.println("<title>Servlet AddCustomerServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddCustomerServlet at " + request.getContextPath() + "</h1>");
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
        String userName = request.getParameter("userName").trim();
        String password = request.getParameter("password").trim();
        String firstName = request.getParameter("firstName").trim();
        String lastName = request.getParameter("lastName").trim();
        int gender = Integer.parseInt(request.getParameter("gender").trim());
        String email = request.getParameter("email").trim();
        String mobile = request.getParameter("mobile").trim();
        String address = request.getParameter("address").trim();
        
        //check xem co ton tai account ko
        AccountDAO adao = new AccountDAO();
        boolean isEmailValid = adao.isValidEmail(email);
        boolean isMobileValid = adao.isValidMobile(mobile);
        boolean isPasswordValid = adao.isValidPassword(password);
        Account existingUser = adao.checkUserNameExists(userName);
        Account existingEmail = adao.checkEmailExists(email);

        // Check for errors
        if (!isEmailValid) {
            request.setAttribute("errorMessage", "Email không hợp lệ.");
            forwardToCustomerListPage(request, response);
            return;
        }

        else if (!isMobileValid) {
            request.setAttribute("errorMessage", "Số điện thoại phải có 10 số.");
            forwardToCustomerListPage(request, response);
            return;
        }

        else if (existingUser != null) {
            request.setAttribute("errorMessage", "Tên đăng nhập đã tồn tại.");
            forwardToCustomerListPage(request, response);
            return;
        }

        else if (!isPasswordValid) {
            request.setAttribute("errorMessage", "Mật khẩu phải có ít nhất 1 chữ cái viết hoa và 1 số.");
            forwardToCustomerListPage(request, response);
            return;
        }

        else if (existingEmail != null) {
            request.setAttribute("errorMessage", "Email đã đăng ký.");
            forwardToCustomerListPage(request, response);
            return;
        }
        Account acc = new Account(userName, password, firstName, lastName, gender, email, mobile, address, 4,"avatar-trang-4.jpg");
        adao.insertAccount(acc);
        response.sendRedirect("customerlist");
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
    
    private void forwardToCustomerListPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("customerlist").forward(request, response);
    }
}
