/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.sale;

import dal.AccountDAO;
import dal.ColorDAO;
import dal.OrderDAO;
import dal.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.Account;
import model.Order;
import model.OrderDetail;
import model.OrderStatus;
import model.Product;
import model.ProductImage;

/**
 *
 * @author Sonvu
 */
public class OrderListForSaleServlet extends HttpServlet {

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

        AccountDAO accountDao = new AccountDAO();
        Order order = new Order();
        OrderDAO orderDao = new OrderDAO();
        ColorDAO colorDao = new ColorDAO();
        ProductDAO p = new ProductDAO();
        Product product = new Product();
        ProductImage image = new ProductImage();

        String indexPage = request.getParameter("index");

        if (indexPage == null || indexPage.isEmpty()) {
            indexPage = "1";
        }

        int index = Integer.parseInt(indexPage);

        List<OrderDetail> orderList = orderDao.getAllOrderList(index);

        List<OrderStatus> listStatusOrder = orderDao.getListOrderStatus();

        List<Account> allSaleName = accountDao.getAccountByRoleId("3");

        for (Account account : allSaleName) {
            System.out.println(account.getUserId());
            System.out.println(account.getLastName());

        }

        String status = request.getParameter("status");
        String search = request.getParameter("search");
        String sort = request.getParameter("sort");
        String orderforSort = request.getParameter("order");

        System.out.println("order: " + order);
        System.out.println("sort: " + sort);
        System.out.println("status: " + status);
        System.out.println("search: " + search);

        orderList = orderDao.getAllOrderList(index);

        // Kiểm tra điều kiện cho cả search và status
        if (search != null && !search.isEmpty() && status != null && status.equals("10")) {
            orderList = orderDao.getListOrderWithSearch(search, index); // Tìm kiếm tất cả đơn hàng theo tên
        } else if (search != null && !search.isEmpty() && status != null && !status.isEmpty()) {
            orderList = orderDao.getListOrderWithSearchAndStatus(search, status, index); // Tìm kiếm theo tên và trạng thái
        } else if (status != null && status.equals("10")) {
            orderList = orderDao.getAllOrderList(index); // Lấy tất cả đơn hàng
        } else if (status != null && !status.isEmpty()) {
            orderList = orderDao.getListOrderWithStatus(status, index); // Lọc theo trạng thái
        } else if (search != null && !search.isEmpty()) {
            orderList = orderDao.getListOrderWithSearch(search, index); // Lọc theo tên sản phẩm
        } else {
            orderList = orderDao.getAllOrderList(index); // Mặc định lấy tất cả
        }

        if (sort != null && !sort.isEmpty()) {
            String sortBy = "";
            switch (sort) {
                case "oid":

                    sortBy = " orr.orderId ";

                    if (search != null && !search.isEmpty() && status != null && status.equals("10")) {
                        orderList = orderDao.getListOrderWithSearchAndSort(search, sortBy, orderforSort, index); // Tìm kiếm tất cả đơn hàng theo tên
                        System.out.println("sort searh , all");
                    } else if (search != null && !search.isEmpty() && status != null && !status.isEmpty()) {
                        orderList = orderDao.getListOrderWithSearchAndStatusAndSort(search, status, sortBy, orderforSort, index); // Tìm kiếm theo tên và trạng thái
                        System.out.println("search status");
                    } else if (status != null && status.equals("10")) {
                        orderList = orderDao.getAllOrderListAndSort(sortBy, orderforSort, index); // Lấy tất cả đơn hàng
                        System.out.println("alll");
                    } else if (status != null && !status.isEmpty()) {
                        orderList = orderDao.getListOrderWithStatusAndSort(status, sortBy, orderforSort, index); // Lọc theo trạng thái
                        System.out.println(" status");
                    } else if (search != null && !search.isEmpty()) {
                        orderList = orderDao.getListOrderWithSearchAndSort(search, sortBy, orderforSort, index); // Lọc theo tên sản phẩm
                        System.out.println("search ");
                    } else {
                        orderList = orderDao.getAllOrderListAndSort(sortBy, orderforSort, index); // Mặc định lấy tất cả
                        System.out.println("all down");
                    }
                    break;

                case "nameC":
                    sortBy = " orr.orderName ";

                    if (search != null && !search.isEmpty() && status != null && status.equals("10")) {
                        orderList = orderDao.getListOrderWithSearchAndSort(search, sortBy, orderforSort, index); // Tìm kiếm tất cả đơn hàng theo tên
                        System.out.println("sort searh , all");
                    } else if (search != null && !search.isEmpty() && status != null && !status.isEmpty()) {
                        orderList = orderDao.getListOrderWithSearchAndStatusAndSort(search, status, sortBy, orderforSort, index); // Tìm kiếm theo tên và trạng thái
                        System.out.println("search status");
                    } else if (status != null && status.equals("10")) {
                        orderList = orderDao.getAllOrderListAndSort(sortBy, orderforSort, index); // Lấy tất cả đơn hàng
                        System.out.println("alll");
                    } else if (status != null && !status.isEmpty()) {
                        orderList = orderDao.getListOrderWithStatusAndSort(status, sortBy, orderforSort, index); // Lọc theo trạng thái
                        System.out.println(" status");
                    } else if (search != null && !search.isEmpty()) {
                        orderList = orderDao.getListOrderWithSearchAndSort(search, sortBy, orderforSort, index); // Lọc theo tên sản phẩm
                        System.out.println("search ");
                    } else {
                        orderList = orderDao.getAllOrderListAndSort(sortBy, orderforSort, index); // Lấy tất cả đơn hàng
                        System.out.println("all down");
                    }
                    break;
                case "nameP":
                    sortBy = " firstProductName ";

                    if (search != null && !search.isEmpty() && status != null && status.equals("10")) {
                        orderList = orderDao.getListOrderWithSearchAndSort(search, sortBy, orderforSort, index); // Tìm kiếm tất cả đơn hàng theo tên
                        System.out.println("sort searh , all");
                    } else if (search != null && !search.isEmpty() && status != null && !status.isEmpty()) {
                        orderList = orderDao.getListOrderWithSearchAndStatusAndSort(search, status, sortBy, orderforSort, index); // Tìm kiếm theo tên và trạng thái
                        System.out.println("search status");
                    } else if (status != null && status.equals("10")) {
                        orderList = orderDao.getAllOrderListAndSort(sortBy, orderforSort, index); // Lấy tất cả đơn hàng
                        System.out.println("alll");
                    } else if (status != null && !status.isEmpty()) {
                        orderList = orderDao.getListOrderWithStatusAndSort(status, sortBy, orderforSort, index); // Lọc theo trạng thái
                        System.out.println(" status");
                    } else if (search != null && !search.isEmpty()) {
                        orderList = orderDao.getListOrderWithSearchAndSort(search, sortBy, orderforSort, index); // Lọc theo tên sản phẩm
                        System.out.println("search ");
                    } else {
                        orderList = orderDao.getAllOrderListAndSort(sortBy, orderforSort, index); // Lấy tất cả đơn hàng
                        System.out.println("all down");
                    }
                    break;

                default:
                    break;
            }
            request.setAttribute("sort", sort);
            request.setAttribute("order", order);
        }

        int endPage = numberOfPage(orderDao.getOrderCount());
        String sstatus = status;

        request.setAttribute("search", search);
        request.setAttribute("status", status);
        request.setAttribute("statusSelect", status);
        request.setAttribute("orderList", orderList);

        request.setAttribute("index", index);
        request.setAttribute("endP", endPage);
        request.setAttribute("allSaleName", allSaleName);
        request.setAttribute("listStatusOrder", listStatusOrder);

        System.out.println("===================================================");
        request.getRequestDispatcher("orderlistforsale.jsp").forward(request, response);

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

        request.getRequestDispatcher("orderlistforsale.jsp").forward(request, response);
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

    private int numberOfPage(int total) {

        int endPage = total / 5;
        if (total % 5 != 0) {
            endPage++;
        }

        return endPage;
    }

}
