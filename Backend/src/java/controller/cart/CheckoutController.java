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
import model.AddressUser;
import model.Cart;

/**
 *
 * @author hiule
 */
@WebServlet(name = "CheckoutController", urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

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
        CheckOutDAO checkout = new CheckOutDAO();
        HttpSession session = request.getSession();
        Account acc = (Account) session.getAttribute("account");
        if (acc != null) {
            List<AddressUser> addressUsers = checkout.getAddressByUserId(acc.getUserId());
            if (addressUsers != null && !addressUsers.isEmpty()) {
                System.out.println("Address list size: " + addressUsers.size());
                session.setAttribute("userAddress", addressUsers);
            } else {
                System.out.println("No addresses found.");
            }

        } else {
            // Handle the case where the account is null (e.g., redirect to login)
            response.sendRedirect("login.jsp");
            return;
        }

        CartDAO cartDAO = new CartDAO();
        List<Cart> carts = cartDAO.getSelectedCarts(acc.getUserId());
        if (carts == null || carts.isEmpty()) {
            session.setAttribute("messUpdateCart", "Bạn chưa chọn sản phẩm");
            response.sendRedirect("carts");
            return;
        }

        // For testing, forward to the test JSP
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
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
        CartDAO cartDAO = new CartDAO();
        List<Cart> cartList = cartDAO.getCartsSelectByUserId(acc.getUserId());
        // Retrieve selected address ID from the form
        String selectedAddressIdStr = request.getParameter("address");
        int selectedAddressId = -1;
        if (selectedAddressIdStr != null) {
            try {
                selectedAddressId = Integer.parseInt(selectedAddressIdStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid address selected.");
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
                return;
            }
        } else {
            // No address selected
            request.setAttribute("error", "Please select a shipping address.");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        // Retrieve selected payment method from the form
        String payMethod = request.getParameter("pay-method");
        if (payMethod == null) {
            // No payment method selected
            request.setAttribute("error", "Please select a payment method.");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        // Fetch the selected address details
        CheckOutDAO checkoutDAO = new CheckOutDAO();
        AddressUser selectedAddress = checkoutDAO.getAddressById(selectedAddressId);
        if (selectedAddress == null) {
            request.setAttribute("error", "Selected address not found.");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        String name = selectedAddress.getName();
        String phone = selectedAddress.getPhone();
        String email = selectedAddress.getEmail();
        String address = selectedAddress.getAddress();
        // Store selected address and payment method in session or process accordingly
        session.setAttribute("selectedAddress", selectedAddress);
        session.setAttribute("payMethod", payMethod);

        // Proceed based on the selected payment method
        if (payMethod.equals("online")) {
            session.setAttribute("name", name);
            session.setAttribute("phone", phone);
            session.setAttribute("email", email);
            session.setAttribute("address", address);
            session.setAttribute("ThanhHieu", "2");
            // Redirect to online payment page
            response.sendRedirect("vnpay_pay.jsp");
        } else if (payMethod.equals("cod")) {
            // Process COD order
            double totalmoney = (double) session.getAttribute("totalFinal");// Ensure to implement a method to calculate total money if needed
            int orderId = cartDAO.addOrder(acc.getUserId(), name, email, phone, totalmoney, address, 1,2);

            for (Cart cart : cartList) {
                // Add each cart's details to the order
                cartDAO.addOrderDetail(orderId, cart.getProduct().getProductId(), cart.getQuantity(),  cart.getColorId(), 0);

                // Update stock for the product associated with the cart item
                cartDAO.updateStockByCartId(cart.getCartId());

                // Remove the cart after processing
                cartDAO.deleteCar(cart.getCartId());
            }

            // Redirect to order confirmation page after processing
            response.sendRedirect("thanks.jsp");
        } else if (payMethod.equals("balance")) {
            // Process COD order
            double totalmoney = (double) session.getAttribute("totalFinal");// Ensure to implement a method to calculate total money if needed

            double balance = checkoutDAO.getMoneyByUserId(acc.getUserId());
            if (totalmoney > balance) {
                session.setAttribute("messCheckOut", "Số dư không đủ");
                response.sendRedirect("checkout");
                return;
            }else{
            checkoutDAO.updateMoneyAfterPurchase(acc.getUserId(), totalmoney);
            int orderId = cartDAO.addOrder(acc.getUserId(), name, email, phone, totalmoney, address, 1,3);
            
            for (Cart cart : cartList) {
                // Add each cart's details to the order
                             cartDAO.addOrderDetail(orderId, cart.getProduct().getProductId(), cart.getQuantity(),  cart.getColorId(), 0);


                // Update stock for the product associated with the cart item
                cartDAO.updateStockByCartId(cart.getCartId());

                // Remove the cart after processing
                cartDAO.deleteCar(cart.getCartId());
            }

            // Redirect to order confirmation page after processing
            response.sendRedirect("thanks.jsp");}
        } else {
            // Handle other payment methods if any
            request.setAttribute("error", "Invalid payment method selected.");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
        }
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
