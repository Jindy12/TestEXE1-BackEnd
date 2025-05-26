/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.marketing;

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
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Brand;
import model.Product;
import model.ProductImage;
import model.Segment;
import model.Style;
import model.Supply;

/**
 *
 * @author Sonvu
 */
public class EditByMarketingServlet extends HttpServlet {

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
            out.println("<title>Servlet EditByMarketingServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditByMarketingServlet at " + request.getContextPath() + "</h1>");
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
        String id = request.getParameter("id");
        HttpSession session = request.getSession();

        ProductDAO productDao = new ProductDAO();
        BrandDAO brandDao = new BrandDAO();
        SegmentDAO segmentDao = new SegmentDAO();
        StyleDAO styleDao = new StyleDAO();
        SupplyDAO supplyDao = new SupplyDAO();

        // Lấy thông tin sản phẩm
        Product product = productDao.getProductById(id);
        request.setAttribute("car", product);
        session.setAttribute("productIdSession", id);

        // Lấy danh sách hình ảnh của sản phẩm
        List<ProductImage> imageList = productDao.getImagesByProductId(id);
        request.setAttribute("imageList", imageList);

        // Lấy danh sách thương hiệu, phân khúc, kiểu dáng và nhà cung cấp
        request.setAttribute("brandList", brandDao.getAllBrand());
        request.setAttribute("segmentList", segmentDao.getAllSegment());
        request.setAttribute("styleList", styleDao.getAllStyleCar());
        request.setAttribute("supplyList", supplyDao.getAllSupplyCar());

        // Chuyển tiếp đến JSP để hiển thị thông tin
        request.getRequestDispatcher("editproductbymarketing.jsp").forward(request, response);
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
    // Lấy dữ liệu từ form
    HttpSession session = request.getSession();
    String id =(String) session.getAttribute("productIdSession"); // Không cập nhật productId
    String name = request.getParameter("name");
    int seatNumber = Integer.parseInt(request.getParameter("seatNumber"));
    double price = Double.parseDouble(request.getParameter("price"));
    String fuel = request.getParameter("fuel");
    int stock = Integer.parseInt(request.getParameter("stock"));
    String description = request.getParameter("des");
    int supplyId = Integer.parseInt(request.getParameter("supply"));
    int brandId = Integer.parseInt(request.getParameter("brandId"));
    int segmentId = Integer.parseInt(request.getParameter("segmentId"));
    int styleId = Integer.parseInt(request.getParameter("styleId"));
    int status = request.getParameter("status").equals("active") ? 1 : 0;
    String newImageUrl = request.getParameter("newImageUrl"); // Lấy URL ảnh mới
    
    // Kiểm tra nếu người dùng đã nhập URL mới
    if (newImageUrl != null && !newImageUrl.trim().isEmpty()) {
        // Giả sử bạn đã có phương thức thêm ảnh sản phẩm mới trong DAO (ProductDAO)
        ProductDAO productDAO = new ProductDAO();
        try {
            // Gọi hàm thêm ảnh mới với ID sản phẩm
            productDAO.addProductImage(id, newImageUrl); 
            System.out.println("Thêm ảnh mới thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Có lỗi xảy ra khi thêm ảnh sản phẩm mới: " + e.getMessage());
        }
    }
    // Tạo đối tượng ProductDAO
    ProductDAO productDao = new ProductDAO();
    
    
    try {
        // Cập nhật sản phẩm
        productDao.updateProduct1(id, name, seatNumber, price, fuel, stock, description, 10, 
                                  supplyId, brandId, segmentId, styleId, status);

        // Nếu cập nhật thành công, chuyển hướng đến trang chi tiết sản phẩm
        response.sendRedirect("editproductbymarketing?id=" + id);
    } catch (Exception e) {
        e.printStackTrace();
        // Nếu cập nhật thất bại, thông báo lỗi
        request.setAttribute("error", "Cập nhật sản phẩm thất bại: " + e.getMessage());
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }}

    // Xử lý cập nhật hình ảnh nếu cần thiết
//    String[] imageUrls = request.getParameterValues("imageUrls");
//    if (imageUrls != null) {
//        for (String imageUrl : imageUrls) {
//            // Thêm logic để lưu hình ảnh vào cơ sở dữ liệu
//        }
//    }


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
