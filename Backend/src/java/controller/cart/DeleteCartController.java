/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.cart;

import dal.CartDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Account;
import model.Cart;

/**
 *
 * @author Admin
 */
@WebServlet(name = "DeleteCartController", urlPatterns = {"/delete-cart"})
public class DeleteCartController extends HttpServlet {

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
            String productId = request.getParameter("productId");
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            int userId = account.getUserId(); // Lấy userId từ session
            int cartId = Integer.parseInt(request.getParameter("cartId"));
            // Tạo instance của CartDAO
            CartDAO cartDAO = new CartDAO();
            // Gọi phương thức deleteCartItem để thực hiện xóa mềm
            boolean isDeleted = cartDAO.deleteCartItem(cartId);

            // Ghi thông báo cho người dùng
            if (isDeleted) {
                out.write("Product removed from cart successfully."); // Ghi thông báo thành công
            } else {
                out.write("Failed to remove product from cart."); // Ghi thông báo thất bại
            }

            // Chuyển hướng về trang giỏ hàng
            response.sendRedirect("carts");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp"); // Chuyển hướng đến trang lỗi nếu có ngoại lệ
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
