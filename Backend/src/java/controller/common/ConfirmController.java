package controller.common;

import dal.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
@WebServlet(name = "ConfirmController", urlPatterns = {"/confirm"})

public class ConfirmController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConfirmController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConfirmController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email"); // Lấy email từ tham số yêu cầu

        AccountDAO dao = new AccountDAO();
        Account newAccount = dao.getPendingAccountByEmail(email);

        if (newAccount != null) {
            // Cập nhật trạng thái của tài khoản thành active (1)
        newAccount.setStatus(1);
      //  dao.updateAccountStatus(newAccount); // Cập nhật vào database
            // Kích hoạt tài khoản trong cơ sở dữ liệu
            dao.activateAccount(email);
            request.setAttribute("message", "Đăng ký thành công! Bạn có thể đăng nhập ngay bây giờ.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Liên kết xác nhận không hợp lệ hoặc đã hết hạn.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
