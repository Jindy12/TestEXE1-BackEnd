/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import dal.CartDAO;
import dal.ColorDAO;
import dal.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.Cart;
import model.Color;
import model.Product;

/**
 *
 * @author hiule
 */
@WebServlet(name = "AddToCart", urlPatterns = {"/addtocart"})
public class AddToCart extends HttpServlet {

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
        try {

           
            CartDAO cartDAO = new CartDAO();
            ProductDAO productDAO = new ProductDAO();
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");

            if (account == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            int userId = account.getUserId();
            String productId = request.getParameter("productId");
            String quantityStr = request.getParameter("quantity");
            int colorId = (request.getParameter("color") == null) ? 1 : Integer.parseInt(request.getParameter("color"));

            // Quantity check
            if (quantityStr == null || quantityStr.isEmpty()) {
                response.getWriter().write("Quantity is required.");
                return;
            }

            Product product = productDAO.getProductById(productId);
            int quantityCurrent = cartDAO.getQuantityByUserIdAndProductId(userId, productId);
            int quantity;
            String urlHistory = (String) session.getAttribute("urlHistory");
            urlHistory = (urlHistory == null) ? "home" : urlHistory;
            
            
            try {
                quantity = Integer.parseInt(quantityStr);
            } catch (NumberFormatException e) {
                response.getWriter().write("Invalid quantity format.");
                return;
            }
            
            if ((quantity + quantityCurrent) > product.getStock() ) {
                request.setAttribute("mesOfCart", "Quá số lượng");
                request.getRequestDispatcher(urlHistory).forward(request, response);
                return;
            }

            // Get cart ID based on productId and userId
            int cartId = cartDAO.getCartIdByUserIdAndProductId(userId, productId);

            if (cartId == -1) {
                // Item not in cart: add it directly and skip further checks
                cartDAO.addToCart(userId, productId, quantity, colorId);
            } else {
                // Item exists, check for color match
                List<Cart> carts = cartDAO.getCartsByUserId(userId);
                boolean itemExistsWithSameColor = false;

                for (Cart cartItem : carts) {
                    if (cartItem.getProduct().getProductId().equals(productId)) {
                        if (cartItem.getColorId() == colorId) {
                            // Item with same productId and colorId exists, update quantity
                            cartDAO.updateQuantityByCartId(cartItem.getCartId(), quantity);
                            itemExistsWithSameColor = true;
                            break;
                        }
                    }
                }

                // If no item with the same productId and colorId was found, add as a new cart item
                if (!itemExistsWithSameColor) {
                    cartDAO.addToCart(userId, productId, quantity, colorId);
                }
            }

            // Update cart size and set success message
            int sizeCart = cartDAO.countCartsByUserId(userId);
            session.setAttribute("sizeCart", sizeCart);
            request.setAttribute("mesOfCart", "Sản phẩm đã được thêm vào giỏ");
            request.getRequestDispatcher(urlHistory).forward(request, response);

        } catch (SQLException ex) {
            Logger.getLogger(AddToCart.class.getName()).log(Level.SEVERE, null, ex);
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
