/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.common;

import context.DBContext;
import dal.AccountDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.HashPassword;

/**
 *
 * @author nguye
 */
public class ChangePasswordServlet extends HttpServlet {

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
            out.println("<title>Servlet ChangePasswordServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChangePasswordServlet at " + request.getContextPath() + "</h1>");
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
        // Lấy các tham số từ form
        String username = request.getParameter("username");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        AccountDAO dao = new AccountDAO();
        RequestDispatcher dispatcher = request.getRequestDispatcher("changePassWord.jsp");

        // Kiểm tra mật khẩu hiện tại có chính xác không
        if (!dao.checkCurrentPassword(username, currentPassword)) {
            request.setAttribute("mess", "Tài khoản hoặc mật khẩu hiện tại không chính xác.");
            dispatcher.forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu mới và xác nhận
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("mess", "Mật khẩu xác nhận không khớp.");
            dispatcher.forward(request, response);
            return;
        }

        // Kiểm tra độ mạnh của mật khẩu mới
        if (!dao.isValidPassword(newPassword)) {
            request.setAttribute("mess", "Mật khẩu mới phải có ít nhất 1 chữ cái viết hoa và 1 số.");
            dispatcher.forward(request, response);
            return;
        }

        // Kết nối cơ sở dữ liệu và cập nhật mật khẩu mới
        DBContext dbContext = new DBContext();
        Connection con = null;
        try {
            con = dbContext.getConnection();
            if (con != null) {
                String hashedPassword = HashPassword.toSHA1(newPassword);
                PreparedStatement pst = con.prepareStatement("UPDATE account SET password = ? WHERE username = ?");
                pst.setString(1, hashedPassword);
                pst.setString(2, username);

                int rowCount = pst.executeUpdate();
                if (rowCount > 0) {
                    request.setAttribute("message", "Đổi mật khẩu thành công.");
                    HttpSession session = request.getSession();
                    session.invalidate();
                    response.sendRedirect("login.jsp");
                    return;
                } else {
                    request.setAttribute("mess", "Đổi mật khẩu thất bại, vui lòng thử lại.");
                }
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("mess", "Không thể kết nối đến cơ sở dữ liệu.");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            Logger.getLogger(ChangePasswordServlet.class.getName()).log(Level.SEVERE, "SQL Error", e);
            request.setAttribute("mess", "Lỗi hệ thống, vui lòng thử lại sau.");
            dispatcher.forward(request, response);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    Logger.getLogger(ChangePasswordServlet.class.getName()).log(Level.SEVERE, "Failed to close connection", e);
                }
            }
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
