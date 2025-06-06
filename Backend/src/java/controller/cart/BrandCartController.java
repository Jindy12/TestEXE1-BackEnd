/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import dal.CartDAO;
import dal.ColorDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Cart;
import java.sql.SQLException;
import model.Account;
import model.Color;

/**
 *
 * @author hiule
 */
@WebServlet(name = "BrandCartController", urlPatterns = {"/brandCart"})
public class BrandCartController extends HttpServlet {

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
            HttpSession session = request.getSession();

            int supplyId = Integer.parseInt(request.getParameter("supplyId")); // Nhận supplyId từ yêu cầu
            CartDAO cartDAO = new CartDAO();
            Account account = (Account) session.getAttribute("account");

            if (account == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            try {
                List<Cart> carts = cartDAO.getCartsBySupplyId(supplyId, account.getUserId()); // Lấy giỏ hàng theo supplyId
                for (Cart cartItem : carts) {

                    // Fetch color list for each product
                    ColorDAO colorDAO = new ColorDAO();
                    List<Color> colorList = colorDAO.getColorOfCar(cartItem.getProduct().getProductId());
                    cartItem.getProduct().setColorList(colorList); // Ensure Product class has a colorList property
                }

                session.setAttribute("carts", carts); // Truyền danh sách giỏ hàng vào request
                request.getSession().setAttribute("urlHistory", "brandCart?supplyId=" + supplyId);
                request.getRequestDispatcher("cart.jsp").forward(request, response); // Chuyển tiếp đến trang giỏ hàng
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp"); // Xử lý lỗi
            }
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
