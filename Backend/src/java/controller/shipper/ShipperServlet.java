package controller.shipper;

import dal.OrderDetailDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Account;
import model.Order;
import model.OrderDetail;

public class ShipperServlet extends HttpServlet {

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
            out.println("<title>Servlet ShipperServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShipperServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession sesion = request.getSession();
        Account shipper = (Account) sesion.getAttribute("account");
        int shipperId = shipper.getUserId();
        OrderDetailDAO od1dao = new OrderDetailDAO();
        List<Order> order = new ArrayList<>();
        Map<Integer, List<OrderDetail>> orderDetailsMap = new HashMap<>();
        int statusId = 0;
        String statusIdParam = request.getParameter("statusId");
        String keyword = request.getParameter("keyword");
        if (statusIdParam != null && !statusIdParam.isEmpty()) {
            try {
                statusId = Integer.parseInt(statusIdParam);
            } catch (NumberFormatException e) {
                // Xử lý ngoại lệ khi chuyển đổi statusId
                e.printStackTrace(); // Log lỗi
                request.setAttribute("error", "Status ID không hợp lệ.");
            }
        }
        if (statusId > 0) {
            order = od1dao.getShipOrderByStatus(statusId);
        } else {
            order = od1dao.getShipOrder();
        }
        for (Order order1 : order) {
            List<OrderDetail> orderDetails = od1dao.getOrderDetail(order1.getOrderId());
            orderDetailsMap.put(order1.getOrderId(), orderDetails);
        }
        //phan trang
        int page, numperpage = 5;
        int size = order.size();
        int num = (int) Math.ceil((double) size / numperpage); // Số trang, làm tròn lên
        String xpage = request.getParameter("page");
        if (xpage == null) {
            page = 1;
        } else {
            page = Integer.parseInt(xpage);
        }
        int start = (page - 1) * numperpage;
        int end = Math.min(page * numperpage, size);
        order = od1dao.getMyOrderListByPage(order, start, end);

        request.setAttribute("order", order);
        request.setAttribute("orderDetailsMap", orderDetailsMap);
        request.setAttribute("page", page);
        request.setAttribute("statusId", statusId);
        request.setAttribute("keyword", keyword);
        request.setAttribute("num", num);
        request.getRequestDispatcher("shipper.jsp").forward(request, response);
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
        OrderDetailDAO od1dao = new OrderDetailDAO();
        int orderStatus = Integer.parseInt(request.getParameter("orderStatus"));
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        od1dao.updateOrderStatus(orderId, orderStatus);
        response.sendRedirect("shipper");
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
