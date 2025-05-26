/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import dal.AccountDAO;
import dal.CartDAO;
import dal.CheckOutDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Account;
import model.Cart;

/**
 *
 * @author hiule
 */
@WebServlet(name = "AddAddressUser", urlPatterns = {"/addAddressUser"})
public class AddAddressUser extends HttpServlet {
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
            out.println("<title>Servlet CheckoutController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CheckoutController at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("account");
        if (acc != null) {
            Account user = adao.getAccountById(acc.getUserId());
            session.setAttribute("accCheckout", user);
        } else {
            response.sendRedirect("login.jsp");
            return;
        }

        request.getRequestDispatcher("addUserAddress.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        Account acc = (Account) session.getAttribute("account");
        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = acc.getUserId();
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String userName = request.getParameter("userName");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String tinh = request.getParameter("tinh");
        String quan = request.getParameter("quan");
        String phuong = request.getParameter("phuong");
        String address2 = request.getParameter("address2");

        String shippingAddress = String.join(", ", address2, phuong, quan, tinh);

        boolean flag = true;
        if (mobile == null || !isPhoneNum(mobile)) {
            session.setAttribute("msg_phoneCart", "Số điện thoại không hợp lệ");
            flag = false;
        }
        if (email == null || !isGmail(email)) {
            session.setAttribute("msg_gmailCart", "Gmail không hợp lệ");
            flag = false;
        }

        if (flag) {
            CheckOutDAO checkOutDAO = new CheckOutDAO();
            boolean success = checkOutDAO.insertAddress(userId, shippingAddress, firstName + " " + lastName, email, mobile);

            if (success) {
                response.sendRedirect("checkout");
            } else {
                request.setAttribute("error", "Thêm địa chỉ thất bại. Vui lòng thử lại.");
                setFormAttributes(request, firstName, lastName, userName, email, mobile, tinh, quan, phuong, address2);
                request.getRequestDispatcher("addUserAddress.jsp").forward(request, response);
            }
        } else {
            setFormAttributes(request, firstName, lastName, userName, email, mobile, tinh, quan, phuong, address2);
            request.getRequestDispatcher("addUserAddress.jsp").forward(request, response);
        }
    }

    private void setFormAttributes(HttpServletRequest request, String firstName, String lastName, String userName, String email, String mobile, String tinh, String quan, String phuong, String address2) {
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);
        request.setAttribute("userName", userName);
        request.setAttribute("email", email);
        request.setAttribute("mobile", mobile);
        request.setAttribute("tinh", tinh);
        request.setAttribute("quan", quan);
        request.setAttribute("phuong", phuong);
        request.setAttribute("address2", address2);
    }

    public static boolean isGmail(String input) {
        return input != null && input.matches("^[a-zA-Z][a-zA-Z0-9._%+-]*[a-zA-Z0-9]@[a-zA-Z0-9]{2,}(\\.[a-zA-Z0-9]{2,})+$");
    }

    public static boolean isPhoneNum(String input) {
        return input != null && input.matches("\\d{10,11}");
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
