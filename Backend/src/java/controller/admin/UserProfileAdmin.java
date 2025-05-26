/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import controller.common.*;
import dal.AccountDAO;
import dal.RoleDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import model.Account;

/**
 *
 * @author tuana
 */
@WebServlet(name = "UserProfileAdmin", urlPatterns = {"/userprofileadmin"})
public class UserProfileAdmin extends HttpServlet {

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
            out.println("<title>Servlet EditUserProfileServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditUserProfileServlet at " + request.getContextPath() + "</h1>");
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
        AccountDAO adao = new AccountDAO();
        int userId = Integer.parseInt(request.getParameter("userId"));
        HttpSession session = request.getSession();

        Account userUpdate = adao.getAccountById(userId);

       
        request.setAttribute("userUpdate", userUpdate);
        request.getRequestDispatcher("userProfile_1.jsp").forward(request, response);
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
        AccountDAO adao = new AccountDAO();
        Account user = (Account) session.getAttribute("accountProfile");
        // Lấy thông tin cũ

        String userIdStr = request.getParameter("userId");
        String genderStr = request.getParameter("gender");
        String roleStr = request.getParameter("role");

        String email = request.getParameter("email");
        String avatar = request.getParameter("avatar");

        // Lấy giá trị từ JSP
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        String mobile = request.getParameter("mobile");
        String address = request.getParameter("address");
        boolean flag = true;
        if (userIdStr == null || userIdStr.isEmpty()) {
            request.setAttribute("errorMessage", "Missing required field: userId.");
            request.getRequestDispatcher("userProfile_1.jsp").forward(request, response);
            return;
        }
          if (genderStr == null || genderStr.isEmpty()) {
            request.setAttribute("errorMessage", "Missing required field: gender.");
            request.getRequestDispatcher("userProfile_1.jsp").forward(request, response);
            return;
        }
// Validate role
        if (roleStr == null || roleStr.isEmpty()) {
            request.setAttribute("errorMessage", "Missing required field: role.");
            request.getRequestDispatcher("userProfile_1.jsp").forward(request, response);
            return;
        }
// Validate gender
      



        int userId = Integer.parseInt(userIdStr);
        int gender = Integer.parseInt(genderStr);
        int roleId = Integer.parseInt(roleStr);
        //Verifile moblie
        boolean isMobileValid = adao.isValidMobile(mobile);
        if (!isMobileValid) {
            request.setAttribute("errorMessage", "Số điện thoại phải có 10 số.");
            
        }

        // Xử lý upload avatar nếu có file mới
        Part file = request.getPart("avatar");
        if (file != null && file.getSize() > 0) {
            String fileName = Paths.get(file.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("/img") + File.separator + fileName;

            // Tạo thư mục nếu chưa tồn tại
            File uploadDir = new File(getServletContext().getRealPath("/img"));
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // Tạo thư mục
            }

            // Lưu file
            try (InputStream is = file.getInputStream(); FileOutputStream fos = new FileOutputStream(uploadPath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                avatar = fileName; // Cập nhật đường dẫn avatar mới
            } catch (Exception e) {
                return;
            }
        }

        // Cập nhật account
        adao.editAccountAdmin(firstName, lastName, 1, email, mobile, address, 4, avatar, 1, userId);
        Account acc = adao.getAccountById(userId);
        request.setAttribute("userUpdate", acc);
        request.getRequestDispatcher("userProfile_1.jsp").forward(request, response);
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
