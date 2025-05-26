/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.marketing;

import dal.AccountDAO;
import dal.RoleDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
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
@MultipartConfig()
public class ViewCustomerServlet extends HttpServlet {

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
            out.println("<title>Servlet ViewCustomerServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ViewCustomerServlet at " + request.getContextPath() + "</h1>");
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
        int userId = Integer.parseInt(request.getParameter("userId"));
        HttpSession session = request.getSession();
        AccountDAO adao = new AccountDAO();
        Account acc = adao.getAccountById(userId);
        //List<Account> listCustomer = adao.getAllAccount();
        RoleDAO rdao = new RoleDAO();
        int roleId = acc.getRoleId();
        String role = rdao.getRoleNameById(roleId);
        session.setAttribute("role1", role);
        session.setAttribute("acc", acc);
        request.getRequestDispatcher("editCustomer.jsp").forward(request, response);
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
        AccountDAO adao = new AccountDAO();
        HttpSession session = request.getSession();
        Account customer = (Account) session.getAttribute("acc");
        
        // Lấy thông tin cũ
        int userId = customer.getUserId();
        String userName = customer.getUserName();
        String password = customer.getPassword();
        String avatar = customer.getAvatar();
        int roleId = customer.getRoleId();
        // Lấy giá trị từ JSP
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        int gender = Integer.parseInt(request.getParameter("gender"));
        String mobile = request.getParameter("mobile");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        int status = Integer.parseInt(request.getParameter("status"));
        
        //Verifile moblie
        boolean isMobileValid = adao.isValidMobile(mobile);
        if (!isMobileValid) {
            request.setAttribute("errorMessage", "Số điện thoại phải có 10 số.");
            request.getRequestDispatcher("editCustomer.jsp").forward(request, response);
            return;
        }

        // Xử lý upload avatar nếu có file mới
        Part file = request.getPart("avatar");
        if (file != null && file.getSize() > 0) {
            //lấy ten file
            String fileName = Paths.get(file.getSubmittedFileName()).getFileName().toString();
            //lấy địa chỉ lưu file
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
        adao.editAccount(userName, password, firstName, lastName, gender, email, mobile, address, roleId, avatar, status, userId);
        response.sendRedirect("customerlist");
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
