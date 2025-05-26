/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.marketing;

import dal.AccountDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Account;

/**
 *
 * @author tuana
 */
public class CustomerListServlet extends HttpServlet {

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
            out.println("<title>Servlet CustomerListServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CustomerListServlet at " + request.getContextPath() + "</h1>");
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

        // Nhận tham số tìm kiếm, sắp xếp và trạng thái từ request
        String search = request.getParameter("search");
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        String status = request.getParameter("status");

        List<Account> p = null;
        order = (order == null || order.isEmpty()) ? "asc" : order; // Mặc định là tăng dần

        // Kiểm tra tìm kiếm khách hàng
        if (search != null && !search.isEmpty()) {
            p = adao.searchCustomers(search); // Tìm kiếm khách hàng theo từ khóa
        } else if (status != null && !status.isEmpty()) {
            p = adao.getCustomersByStatus(Integer.parseInt(status)); // Lọc theo trạng thái
        } else {
            p = adao.getAllCustommer(); // Lấy tất cả khách hàng nếu không tìm kiếm hoặc lọc
        }

        // Xử lý sắp xếp danh sách khách hàng
        if (sort != null && !sort.isEmpty()) {
            switch (sort) {
                case "name":
                    p = adao.sortCustomerByName(order);
                    break;
                case "email":
                    p = adao.sortCustommerByEmail(order);
                    break;
                case "phone":
                    p = adao.sortCustommerByPhone(order);
                    break;
                case "userId":
                    p = adao.sortCustommerById(order);
                    break;
                default:
                    break;
            }
        }

        // Phân trang
        int page = 1;
        int numperpage = 5; // Số bản ghi mỗi trang (có thể điều chỉnh)
        String xpage = request.getParameter("page");
        if (xpage != null) {
            try {
                page = Integer.parseInt(xpage);
            } catch (NumberFormatException e) {
                page = 1; // Nếu có lỗi chuyển về trang 1
            }
        }

        int size = p.size();
        int num = (int) Math.ceil((double) size / numperpage); // Tính số trang
        int start = (page - 1) * numperpage;
        int end = Math.min(page * numperpage, size);

        // Lấy danh sách khách hàng theo trang
        List<Account> listAcc = adao.getCustomerListByPage(p, start, end);

        // Truyền giá trị về cho view (JSP)
        request.setAttribute("size", size);
        request.setAttribute("numperpage", numperpage);
        request.setAttribute("customerList", listAcc);
        request.setAttribute("page", page);
        request.setAttribute("num", num);
        request.setAttribute("sort", sort);
        request.setAttribute("order", order);
        request.setAttribute("status", status); // Truyền lại trạng thái lọc
        request.setAttribute("search", search); // Truyền lại từ khóa tìm kiếm
        request.getRequestDispatcher("customerList.jsp").forward(request, response);
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
