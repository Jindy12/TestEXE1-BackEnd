/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.common;

import dal.BrandDAO;
import dal.ColorDAO;
import dal.ProductDAO;
import dal.StyleDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Brand;
import model.Color;
import model.OrderDetail;
import model.Product;
import model.Style;

/**
 *
 * @author nguye
 */
public class ProductListServlet extends HttpServlet {

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
        BrandDAO brandDao = new BrandDAO();
        ProductDAO productDao = new ProductDAO();
        StyleDAO styleDao = new StyleDAO();
        ColorDAO cdao = new ColorDAO();
        HttpSession session = request.getSession();
        Map<String, List<Color>> colorsMap = new HashMap<>();
        // Lấy số trang từ yêu cầu (mặc định là 1)
        int page = 1;
        String pageParam = request.getParameter("page");
        String brandId = request.getParameter("brandId");
        String styleId = request.getParameter("styleId");
        String keyword = request.getParameter("keyword");
            String sort = request.getParameter("sort");

        
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }
        String prefix;
        List<Product> allPro;

        if (keyword != null && !keyword.isEmpty()) {
            allPro = productDao.searchProductsByKeyword(keyword);
            prefix = "po";
        } else if (brandId != null && !brandId.isEmpty()) {
            allPro = productDao.getAllProductByBrandId(brandId);
            String brandName = brandDao.getBrandById(Integer.parseInt(brandId));
            prefix = (brandName != null && brandName.length() >= 2) ? brandName.substring(0, 2) : null;

        } else if (styleId != null && !styleId.isEmpty()) {
            allPro = productDao.getAllProductByStyleId(styleId); 
            prefix = "au"; // Có thể thay đổi nếu bạn muốn
        }
        else {
            // Nếu không có brandId, lấy tất cả sản phẩm
            allPro = productDao.getAllProductsCommon();
            prefix = "Me";
        }
        if ("asc".equals(sort)) {
        allPro.sort(Comparator.comparing(Product::getPrice));  // Sắp xếp tăng dần theo giá
    } else if ("desc".equals(sort)) {
        allPro.sort(Comparator.comparing(Product::getPrice).reversed());  // Sắp xếp giảm dần theo giá
    }
        for (Product color : allPro) {
            List<Color> colors = cdao.getColorOfCar(color.getProductId());
            colorsMap.put(color.getProductId(), colors);
        }
        int totalPro = allPro.size();
        // Tính toán chỉ số sản phẩm bắt đầu và kết thúc cho trang hiện tại
        int start = (page - 1) * 12;
        int end = Math.min(start + 12, totalPro);
        // Lấy danh sách sản phẩm cho trang hiện tại
        List<Product> productsForCurrentPage = allPro.subList(start, end);
        int totalPages = (int) Math.ceil((double) totalPro / 12);

        List<Product> newProduct = productDao.getLastestProductsByProductIdPrefix(prefix, 1);
        List<Style> styList = styleDao.getAllStyleCar();
        List<Brand> braList = brandDao.getAllBrand();

        request.setAttribute("newProduct", newProduct);
        request.setAttribute("productList", productsForCurrentPage);
        request.setAttribute("brandList", braList);
        request.setAttribute("styleList", styList);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("selectedBrandId", brandId);
        request.setAttribute("selectedStyleId", styleId);
        request.setAttribute("keyword", keyword);
        request.setAttribute("sort", sort);
        request.setAttribute("colorsMap", colorsMap);
        session.setAttribute("urlHistory", "productlist");
        RequestDispatcher dispatcher = request.getRequestDispatcher("productList.jsp");
        dispatcher.forward(request, response);
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
