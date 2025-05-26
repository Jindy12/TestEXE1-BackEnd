/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import dal.CartDAO;
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
import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 *
 * @author hiule
 */
@WebServlet(name = "TaxController", urlPatterns = {"/tax"})
public class TaxController extends HttpServlet {

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
        Account account = (Account) session.getAttribute("account");

        if (account == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = account.getUserId();
        CartDAO cartDAO = new CartDAO();
        List<Cart> cartList = cartDAO.getCartsByUserId(userId); // Return as List<Cart>

        // Check if the list is empty or if all cart items have quantity 0
        if (cartList == null || cartList.isEmpty() || cartList.stream().allMatch(cart -> cart.getQuantity() == 0)) {
            session.setAttribute("messUpdateCart", "San pham bang 0");
            response.sendRedirect("carts");
            return;
        }

        List<Cart> carts = cartDAO.getSelectedCarts(userId);

        if (carts == null || carts.isEmpty()) {
            session.setAttribute("messUpdateCart", "Bạn chưa chọn sản phẩm");
            response.sendRedirect("carts");
            return;
        }

        double totalFinal = 0;
        for (Cart cartItem : carts) {
            totalFinal += (cartItem.getQuantity() * cartItem.getProduct().getPrice()) * 2.2;
        }
    
        
        session.setAttribute("totalFinal", totalFinal);
        session.setAttribute("cartsSelect", carts);
        request.getRequestDispatcher("checkout_1.jsp").forward(request, response);
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
