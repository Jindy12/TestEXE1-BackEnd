/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import dal.AccountDAO;
import dal.AdminDao;
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
 * @author 84777
 */
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5, // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
@WebServlet(name = "AddUser", urlPatterns = {"/addUser"})
public class AddUser extends HttpServlet {

    /**
     * Processes sessions for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param session servlet session
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("addUserList.jsp");
    }

    private static boolean isValidUsername(String input) {
        return input != null && input.matches("^(?=.*[a-zA-Z])[a-zA-Z0-9]{3,30}$");
    }

    public static boolean isGmail(String input) {
        return input != null && input.matches("^[a-zA-Z][a-zA-Z0-9._%+-]*[a-zA-Z0-9]@[a-zA-Z0-9]{2,}(\\.[a-zA-Z0-9]{2,})+$");
    }

    public static boolean isPhoneNum(String input) {
        return input != null && input.matches("\\d{10,11}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        AccountDAO accountDAO = new AccountDAO();
        AdminDao adminDao = new AdminDao();
        // Retrieve form data
//        String avatar = request.getParameter("avatar");
        String firstname = request.getParameter("firstName");
        String lastname = request.getParameter("lastName");
        String address = request.getParameter("address");
        String role_raw = request.getParameter("role");
        String gender_raw = request.getParameter("gender");
        String userName = request.getParameter("userName");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");

        int role = Integer.parseInt(role_raw);
        int gender = Integer.parseInt(gender_raw);
        session.setAttribute("input_firstName", firstname);
        session.setAttribute("input_lastName", lastname);
        session.setAttribute("input_userName", userName);
        session.setAttribute("input_email", email);
        session.setAttribute("input_mobile", mobile);
        session.setAttribute("input_address", address);
        session.setAttribute("input_gender", gender_raw);
        session.setAttribute("input_role", role_raw);
        boolean flag = true;

        String avatar = "avatar-trang-4.jpg";
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
        if (role == -1) {
            session.setAttribute("msg_role", "Chọn vai trò");
            flag = false;
        }

        if (userName == null || !isValidUsername(userName)) {
            session.setAttribute("msg_username", "Username phải từ 3-30 ký tự");
            flag = false;
        }

        if (email == null || !isGmail(email)) {
            session.setAttribute("msg_gmail", "Gmail không hợp lệ");
            flag = false;
        }

        if (mobile == null || !isPhoneNum(mobile)) {
            session.setAttribute("msg_phone", "Số điện thoại không hợp lệ");
            flag = false;
        }

        if (email != null && accountDAO.getAccountByEmail(email) != null) {
            session.setAttribute("msg_gmail_ex", "Gmail đã tồn tại");
            flag = false;
        }

        if (userName != null && accountDAO.getAccountByUserName(userName) != null) {
            session.setAttribute("msg_username_ex", "Tài khoản đã tồn tại");
            flag = false;
        }

        if (mobile != null && accountDAO.getAccountByPhone(mobile) != null) {
            session.setAttribute("msg_phone_ex", "Số điện thoại đã tồn tại");
            flag = false;
        }

        if (firstname == null || firstname.isBlank()) {
            session.setAttribute("msg_firstname", "Tên đầu không được để trống");
            flag = false;
        }

        if (lastname == null || lastname.isBlank()) {
            session.setAttribute("msg_lastname", "Tên sau không được để trống");
            flag = false;
        }

        if (flag) {

            session.setAttribute("msg_suc", "Thêm người dùng thành công");
            Account account = new Account(userName, "UserPassword@123", firstname, lastname, gender, email, mobile, address, role, avatar);
            adminDao.insertAccountAdmin(account);
            response.sendRedirect("userlist");
            return;
        }

        session.setAttribute("msg_err", "Thêm người dùng thất bại");
        request.getRequestDispatcher("addUserList.jsp").forward(request, response);
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
