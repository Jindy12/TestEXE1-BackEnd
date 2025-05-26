/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.VnPay;

import dal.CartDAO;
import dal.CheckOutDAO;
import dal.OrderDAO;
import dal.PaymentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import model.Account;
import model.Cart;
import model.EmailOrder;
import model.Payment;

/**
 *
 * @author admin
 */
@WebServlet(name = "ReturnResult", urlPatterns = {"/result"})
public class ReturnResult extends HttpServlet {

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
            out.println("<title>Servlet ReturnResult</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ReturnResult at " + request.getContextPath() + "</h1>");
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

        CartDAO cartDAO = new CartDAO();
        HttpSession session = request.getSession();
        Object thanhHieuObj = session.getAttribute("ThanhHieu");
        String thanhHieu = (thanhHieuObj instanceof String) ? (String) thanhHieuObj : thanhHieuObj.toString();

        if ("1".equalsIgnoreCase(thanhHieu)) {
            doPost(request, response);
            return;
        }
        Account account = (Account) session.getAttribute("account");
        List<Cart> cartList = cartDAO.getCartsSelectByUserId(account.getUserId()); // Get a list of carts

        OrderDAO odao = new OrderDAO();

        String name = (String) session.getAttribute("name");
        String phone = (String) session.getAttribute("phone");
        String email = (String) session.getAttribute("email");
        String address = (String) session.getAttribute("address");

        LocalDateTime currentDateTime = LocalDateTime.now();
        if (cartList != null && !cartList.isEmpty()) {
            if (account != null) {
                String vnp_TxnRef = request.getParameter("vnp_TxnRef");
                String vnp_Amount = request.getParameter("vnp_Amount");
                vnp_Amount = vnp_Amount.substring(0, vnp_Amount.length() - 2);
                String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
                String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
                String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
                String vnp_BankCode = request.getParameter("vnp_BankCode");
                String vnp_PayDate_raw = request.getParameter("vnp_PayDate");
                String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");

                PaymentDAO padao = new PaymentDAO();
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat outputFormatDTB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat outputFormatWeb = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                try {
                    Date date = inputFormat.parse(vnp_PayDate_raw);
                    String vnp_PayDateWeb = outputFormatWeb.format(date);
                    String vnp_PayDateDTB = outputFormatDTB.format(date);
                    request.setAttribute("vnp_PayDate", vnp_PayDateWeb);
                    double totalmoney = (double) session.getAttribute("totalFinal");
                    Payment p = new Payment("", vnp_TxnRef, vnp_Amount, vnp_OrderInfo, vnp_ResponseCode, vnp_TransactionNo, vnp_BankCode, vnp_PayDateDTB, vnp_TransactionStatus, account.getUserId());
                    padao.addPayment(p);

                    if (vnp_TransactionStatus.equals("00")) {
                        // Iterate over the list of carts and process them as needed
                        int orderId = cartDAO.addOrder(account.getUserId(), name, email, phone, totalmoney, address, 1, 1);
                        for (Cart cart : cartList) {
                            // Example: Call addOrder method for each cart or handle cart processing logic
                            // odao.addOrder(account, cart, email, phone, address, note, dateShip, timeShip, "0");
                            cartDAO.addOrderDetail(orderId, cart.getProduct().getProductId(), cart.getQuantity(), cart.getColorId(), 0);

                            // Update stock for the product associated with the cart item
                            cartDAO.updateStockByCartId(cart.getCartId());

                            // Remove the cart after processing
                            cartDAO.deleteCar(cart.getCartId());

                        }

                        EmailOrder handleEmail = new EmailOrder();
                        String sub = handleEmail.subjectOrder(name);
                        // Uncomment and modify as needed to create email content
                        // String msg = handleEmail.messageOrder(currentDateTime, formatNumber(totalmoney), phone, name, address, note, cartList);
                        session.removeAttribute("cartList");
                        session.removeAttribute("name");
                        session.removeAttribute("phone");
                        session.removeAttribute("email");
                        session.removeAttribute("address");
                        session.removeAttribute("note");
                        session.setAttribute("size", 0);

                        request.setAttribute("vnp_TxnRef", vnp_TxnRef);
                        request.setAttribute("vnp_Amount", vnp_Amount);
                        request.setAttribute("vnp_OrderInfo", vnp_OrderInfo);
                        request.setAttribute("vnp_ResponseCode", vnp_ResponseCode);
                        request.setAttribute("vnp_TransactionNo", vnp_TransactionNo);
                        request.setAttribute("vnp_BankCode", vnp_BankCode);
                        request.setAttribute("vnp_TransactionStatus", vnp_TransactionStatus);
                        request.getRequestDispatcher("thanks.jsp").forward(request, response);

                        // Uncomment if needed
                        // executorService.submit(() -> handleEmail.sendEmail(sub, msg, email));
                    } else {
                        int orderId = cartDAO.addOrder(account.getUserId(), name, email, phone, totalmoney, address, 5, 1);
                        for (Cart cart : cartList) {
                            // Example: Call addOrder method for each cart or handle cart processing logic
                            // odao.addOrder(account, cart, email, phone, address, note, dateShip, timeShip, "0");
                            // odao.updateBought(account, cart);
                            cartDAO.addOrderDetail(orderId, cart.getProduct().getProductId(), cart.getQuantity(), cart.getColorId(), 0);

                            // Update stock for the product associated with the cart item
                            cartDAO.updateStockByCartId(cart.getCartId());

                            // Remove the cart after processing
                            cartDAO.deleteCar(cart.getCartId());

                        }
                        request.setAttribute("vnp_TxnRef", vnp_TxnRef);
                        request.setAttribute("vnp_Amount", vnp_Amount);
                        request.setAttribute("vnp_OrderInfo", vnp_OrderInfo);
                        request.setAttribute("vnp_ResponseCode", vnp_ResponseCode);
                        request.setAttribute("vnp_TransactionNo", vnp_TransactionNo);
                        request.setAttribute("vnp_BankCode", vnp_BankCode);
                        request.setAttribute("vnp_TransactionStatus", vnp_TransactionStatus);
                        request.getRequestDispatcher("thanks.jsp").forward(request, response);
                    }

                } catch (ParseException e) {
                    e.printStackTrace(); // Handle exception as needed
                }

            } else {
                response.sendRedirect("login");
            }
        }

    }

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static String formatNumber(double number) {
        DecimalFormat formatter = new DecimalFormat("#,###,###.###");
        return formatter.format(number);
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
        String thanhHieu = (String) session.getAttribute("ThanhHieu");

        CheckOutDAO checkOutDAO = new CheckOutDAO();
        Account account = (Account) session.getAttribute("account");

        OrderDAO odao = new OrderDAO();

        LocalDateTime currentDateTime = LocalDateTime.now();

        if (account != null) {
            String vnp_TxnRef = request.getParameter("vnp_TxnRef");
            String vnp_Amount = request.getParameter("vnp_Amount");
            vnp_Amount = vnp_Amount.substring(0, vnp_Amount.length() - 2);
            String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
            String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
            String vnp_BankCode = request.getParameter("vnp_BankCode");
            String vnp_PayDate_raw = request.getParameter("vnp_PayDate");
            String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");

            PaymentDAO padao = new PaymentDAO();
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat outputFormatDTB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputFormatWeb = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            try {
                Date date = inputFormat.parse(vnp_PayDate_raw);
                String vnp_PayDateWeb = outputFormatWeb.format(date);
                String vnp_PayDateDTB = outputFormatDTB.format(date);
                request.setAttribute("vnp_PayDate", vnp_PayDateWeb);
                double balance = (double) session.getAttribute("balance");
                Payment p = new Payment("", vnp_TxnRef, vnp_Amount, vnp_OrderInfo, vnp_ResponseCode, vnp_TransactionNo, vnp_BankCode, vnp_PayDateDTB, vnp_TransactionStatus, account.getUserId());
                padao.addPayment(p);

                if (vnp_TransactionStatus.equals("00")) {
                    checkOutDAO.addMoneyToBalance(account.getUserId(), balance);
                    response.sendRedirect("userprofile");
                    // Uncomment if needed
                    // executorService.submit(() -> handleEmail.sendEmail(sub, msg, email));
                } else {

                }

            } catch (ParseException e) {
                e.printStackTrace(); // Handle exception as needed
            }

        } else {
            response.sendRedirect("login");
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
