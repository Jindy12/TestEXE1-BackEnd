/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import dal.AccountDAO;
import dal.CheckOutDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.AddressUser;

/**
 *
 * @author hiule
 */
@WebServlet(name = "EditAddressUser", urlPatterns = {"/editAddressUser"})
public class EditAddressUser extends HttpServlet {

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
            out.println("<title>Servlet EditAddressUser</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditAddressUser at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("account");
        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Retrieve the addressId from the request parameter
        String addressIdStr = request.getParameter("addressId");
        int addressId = -1;
        if (addressIdStr != null) {
            try {
                addressId = Integer.parseInt(addressIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid address ID.");
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
                return;
            }
        } else {
            request.setAttribute("error", "Address ID not provided.");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        // Fetch the address details using the addressId
        CheckOutDAO checkoutDAO = new CheckOutDAO();
        AddressUser addressUser = checkoutDAO.getAddressById(addressId);
        
        // Verify that the address belongs to the logged-in user
        if (addressUser == null || addressUser.getUserId() != acc.getUserId()) {
            request.setAttribute("error", "You do not have permission to edit this address.");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        // Set the addressUser object in the request scope
        request.setAttribute("addressUser", addressUser);

        // Forward to the edit address JSP
        request.getRequestDispatcher("editUserAddress.jsp").forward(request, response);
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

        Account acc = (Account) session.getAttribute("account");
        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = acc.getUserId();
        String addressIdStr = request.getParameter("addressId");
        int addressId = Integer.parseInt(addressIdStr);

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // Validate input data
        boolean flag = true;
        if (phone == null || !isPhoneNum(phone)) {
            request.setAttribute("msg_phoneCart", "Số điện thoại không hợp lệ");
            flag = false;
        }
        if (email == null || !isGmail(email)) {
            request.setAttribute("msg_gmailCart", "Gmail không hợp lệ");
            flag = false;
        }

        if (flag) {
            CheckOutDAO checkOutDAO = new CheckOutDAO();
            boolean success = checkOutDAO.updateAddress(addressId, name, email, phone, address);

            if (success) {
                response.sendRedirect("checkout");
            } else {
                request.setAttribute("error", "Cập nhật địa chỉ thất bại. Vui lòng thử lại.");
                setFormAttributes(request, name, email, phone, address);
                request.getRequestDispatcher("editUserAddress.jsp").forward(request, response);
            }
        } else {
            setFormAttributes(request, name, email, phone, address);
            request.getRequestDispatcher("editUserAddress.jsp").forward(request, response);
        }
    }

    private void setFormAttributes(HttpServletRequest request, String name, String email, String phone, String address) {
        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("address", address);
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
