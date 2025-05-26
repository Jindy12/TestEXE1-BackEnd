/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import dal.BrandDAO;
import dal.ProductDAO;
import dal.SegmentDAO;
import dal.StyleDAO;
import dal.SupplyDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import model.Brand;
import model.Segment;
import model.Style;
import model.Supply;

/**
 *
 * @author ADMIN
 */
public class SettingsListServlet extends HttpServlet {

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
            out.println("<title>Servlet SettingsListServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SettingsListServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    // Lấy các tham số tìm kiếm và trạng thái
    String search = request.getParameter("search");
    String statusString = request.getParameter("status");
    int status = -1; // Giá trị mặc định để kiểm tra

    // Chuyển đổi status từ String sang int
    if (statusString != null && !statusString.isEmpty()) {
        try {
            status = Integer.parseInt(statusString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Xác định view
    String view = request.getParameter("view");
    if (view == null || view.isEmpty()) {
        view = "brand"; // Mặc định là view brand nếu không có view nào được chỉ định
    }
    request.setAttribute("view", view);

    // Xử lý phân trang
    String pageString = request.getParameter("page");
    int page = (pageString != null && !pageString.isEmpty()) ? Integer.parseInt(pageString) : 1;
    int limit = 15; // Số lượng mục mỗi trang
    int offset = (page - 1) * limit; // Tính toán offset

    // Tạo các DAO để lấy dữ liệu
    BrandDAO bDAO = new BrandDAO();
    StyleDAO sDAO = new StyleDAO();
    SegmentDAO seDAO = new SegmentDAO();
    SupplyDAO suDAO = new SupplyDAO();
    ProductDAO pDAO = new ProductDAO();

    // Khai báo danh sách mục và tổng số mục
    List<Brand> brandList = null;
    List<Style> styleList = null;
    List<Segment> segmentList = null;
    List<Supply> supplyList = null;

    // Lấy danh sách có phân trang từ cơ sở dữ liệu dựa trên view
    switch (view) {
        case "brand":
            brandList = bDAO.getBrandListWithPagination(search, status, offset, limit);
            
            request.setAttribute("brandList", brandList);
            break;
        case "style":
            styleList = sDAO.getStyleListWithPagination(search, status, offset, limit);
            
            request.setAttribute("styleList", styleList);
            break;
        case "segment":
            segmentList = seDAO.getSegmentListWithPagination(search, status, offset, limit);
            request.setAttribute("segmentList", segmentList);
            break;
        case "supply":
            supplyList = suDAO.getSupplyListWithPagination(search, status, offset, limit);
            request.setAttribute("supplyList", supplyList);
            break;
        default:
            brandList = bDAO.getBrandListWithPagination(search, status, offset, limit);
            request.setAttribute("brandList", brandList);
            break;
    }

    // Lấy số lượng nhiên liệu
    Map<String, Integer> fuelCounts = pDAO.getFuelCounts();
    request.setAttribute("fuelCounts", fuelCounts);

    // Đặt tham số phân trang
    request.setAttribute("currentPage", page);
    request.setAttribute("search", search);
    request.setAttribute("status", statusString); // Để lưu lại trạng thái đã chọn
 
    // Forward đến trang JSP
    request.getRequestDispatcher("settingsList.jsp").forward(request, response);
}



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        BrandDAO bDAO = new BrandDAO();
        StyleDAO sDAO = new StyleDAO();
        SegmentDAO seDAO = new SegmentDAO();
        SupplyDAO suDAO = new SupplyDAO();
        ProductDAO pDAO = new ProductDAO();

        if ("updateFuelStatus".equals(action)) {
            String fuel = request.getParameter("fuel");
            String statusStr = request.getParameter("status");

            // Kiểm tra giá trị của fuel và statusStr
            if (fuel == null || statusStr == null) {
                request.setAttribute("errorMessage", "Tham số không hợp lệ.");
                request.getRequestDispatcher("admin").forward(request, response);
                return;
            }

            try {
                int newStatus = Integer.parseInt(statusStr);
                // Cập nhật trạng thái nhiên liệu
                boolean isUpdated = pDAO.updateFuelStatus(fuel, newStatus);
                response.sendRedirect("settingsList?view=fuel");
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Trạng thái không hợp lệ.");
                request.getRequestDispatcher("admin").forward(request, response);
            }
        } else if ("updateStyleStatus".equals(action)) {
            String styleIdStr = request.getParameter("styleId");
            String statusStr = request.getParameter("status");

            // Kiểm tra giá trị của styleIdStr và statusStr
            if (styleIdStr == null || statusStr == null) {
                request.setAttribute("errorMessage", "Tham số không hợp lệ.");
                request.getRequestDispatcher("settingsList.jsp").forward(request, response);
                return;
            }

            try {
                int styleId = Integer.parseInt(styleIdStr);
                int newStatus = Integer.parseInt(statusStr);

                // Cập nhật trạng thái style
                sDAO.updateStyleStatus(styleId, newStatus);
                pDAO.updateProductsStatusByStyleId(styleId, newStatus);
                response.sendRedirect("settingsList?view=style");
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Trạng thái không hợp lệ.");
                request.getRequestDispatcher("settingsList.jsp").forward(request, response);
            }
        } else if ("updateSegmentStatus".equals(action)) {
            String segmentIdStr = request.getParameter("segmentId");
            String statusStr = request.getParameter("status");

            // Kiểm tra giá trị của segmentIdStr và statusStr
            if (segmentIdStr == null || statusStr == null) {
                request.setAttribute("errorMessage", "Tham số không hợp lệ.");
                request.getRequestDispatcher("home").forward(request, response);
                return;
            }

            try {
                int segmentId = Integer.parseInt(segmentIdStr);
                int newStatus = Integer.parseInt(statusStr);

                // Cập nhật trạng thái segment
                seDAO.updateSegmentStatus(segmentId, newStatus);
                pDAO.updateProductsStatusBySegmentId(segmentId, newStatus);
                response.sendRedirect("settingsList?view=segment");
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Trạng thái không hợp lệ.");
                request.getRequestDispatcher("settingsList.jsp").forward(request, response);
            }
        } else if ("updateSupplyStatus".equals(action)) {
            String supplyIdStr = request.getParameter("supplyId");
            String statusStr = request.getParameter("status");

            // Kiểm tra giá trị của supplyIdStr và statusStr
            if (supplyIdStr == null || statusStr == null) {
                request.setAttribute("errorMessage", "Tham số không hợp lệ.");
                request.getRequestDispatcher("home").forward(request, response);
                return;
            }

            try {
                int supplyId = Integer.parseInt(supplyIdStr);
                int newStatus = Integer.parseInt(statusStr);

                // Cập nhật trạng thái supply
                suDAO.updateSupplyStatus(supplyId, newStatus);
                pDAO.updateProductsStatusBySupplyId(supplyId, newStatus);
                response.sendRedirect("settingsList?view=supply");
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Trạng thái không hợp lệ.");
                request.getRequestDispatcher("settingsList.jsp").forward(request, response);
            }
        } else {
            // Xử lý cập nhật trạng thái thương hiệu
            String brandIdStr = request.getParameter("brandId");
            String statusStr = request.getParameter("status");

            // Kiểm tra giá trị của brandIdStr và statusStr
            if (brandIdStr == null || statusStr == null) {
                request.setAttribute("errorMessage", "Tham số không hợp lệ.");
                request.getRequestDispatcher("home").forward(request, response);
                return;
            }

            try {
                int brandId = Integer.parseInt(brandIdStr);
                int newStatus = Integer.parseInt(statusStr);

                // Cập nhật trạng thái thương hiệu
                bDAO.updateBrandStatus(brandId, newStatus);
                // Cập nhật trạng thái cho tất cả sản phẩm theo brandId
                pDAO.updateProductsStatusByBrandId(brandId, newStatus);

                response.sendRedirect("settingsList?view=brand");
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Trạng thái không hợp lệ.");
                request.getRequestDispatcher("settingsList.jsp").forward(request, response);
            }
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
