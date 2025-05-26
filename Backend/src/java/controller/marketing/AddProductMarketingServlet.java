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
public class AddProductMarketingServlet extends HttpServlet {

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
            out.println("<title>Servlet AddProductMarketingServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddProductMarketingServlet at " + request.getContextPath() + "</h1>");
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

        ProductDAO pDao = new ProductDAO();
        BrandDAO bDao = new BrandDAO();
        SegmentDAO sDao = new SegmentDAO();
        StyleDAO styleDao = new StyleDAO();
        SupplyDAO supplyDao = new SupplyDAO();

        List<Brand> brandList = bDao.getAllBrand();
        request.setAttribute("brandList", brandList);

        List<Segment> segmentList = sDao.getAllSegment();
        request.setAttribute("segmentList", segmentList);

        List<Style> styleList = styleDao.getAllStyleCar();
        request.setAttribute("styleList", styleList);

        List<Supply> supplyList = supplyDao.getAllSupplyCar();
        request.setAttribute("supplyList", supplyList);

        request.getRequestDispatcher("addproductbymarketing.jsp").forward(request, response);
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

        System.out.println("Dopost addproduct");
        Product product = new Product();
        ProductDAO pDao = new ProductDAO();

        String name = request.getParameter("name");
        String imageUrl = request.getParameter("imageUrl");
        String brand = request.getParameter("brand");
        String style = request.getParameter("style");
        String segment = request.getParameter("segment");
        String supply = request.getParameter("supply");
        String seatNumber = request.getParameter("seatNumber");
        String price = request.getParameter("price");
        String fuel = request.getParameter("fuel");
        String stock = request.getParameter("stock");
        String des = request.getParameter("des");

        
        

        System.out.println("name: " + name);
        System.out.println("imageUrl: " + imageUrl);
        System.out.println("seat: " + seatNumber);
        System.out.println("price: " + price);
        System.out.println("brand: " + brand);
        System.out.println("style: " + style);
        System.out.println("segment: " + segment);
        System.out.println("supply: " + supply);
        System.out.println("fuel: " + fuel);
        System.out.println("stock:" + stock);
        System.out.println("des: " + des); 
        
        int brandId = 0;
        double priceP = 0;
        if(brand != null && !brand.isEmpty()){
            brandId = Integer.parseInt(brand);
        }
        if(price != null && !price.isEmpty()){
            priceP = Integer.parseInt(price);
        }
        String id = pDao.checkBrand(brandId);
        String productId = pDao.getProductToScanId(id);
        
        System.out.println("id: " + productId);
        
        pDao.insertProduct(productId, name, seatNumber, priceP, fuel, stock, des, "10", supply, brand, segment, style);
        
        System.out.println("-------------------------------------");

        response.sendRedirect("proformarketing");
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
