/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.marketing;

import dal.AccountDAO;
import dal.BlogDAO;
import dal.BrandDAO;
import dal.OrderDAO;
import dal.ProductDAO;
import dal.SliderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import model.Brand;
import model.Product;

/**
 *
 * @author ADMIN
 */
public class MktDashboardServlet extends HttpServlet {

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
            out.println("<title>Servlet MktDashboardServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MktDashboardServlet at " + request.getContextPath() + "</h1>");
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
        BlogDAO blogDAO = new BlogDAO();
        ProductDAO productDAO = new ProductDAO();
        AccountDAO accountDAO = new AccountDAO();
        SliderDAO sliderDAO = new SliderDAO();
        //OrderDAO orderDAO = new OrderDAO();
        BrandDAO brandDAO = new BrandDAO();
        
    
    // Fetch gender distribution data: Map<String, Integer> where keys are gender names ("male", "female", "other")
//    Map<String, Integer> genderData = accountDAO.getCustomersByGender();
//
//    // Set the data to the request so it's accessible in the JSP
//    request.setAttribute("genderData", genderData);
//  Map<String, Double> genderData = accountDAO.getCustomersByGender();

    // Set the data to the request so it's accessible in the JSP
//    request.setAttribute("genderData", genderData);

        // Get the date range from the request
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;

        try {
            // If start date is provided, parse it
            if (startDateStr != null && !startDateStr.isEmpty()) {
                java.util.Date parsedStartDate = sdf.parse(startDateStr);
                startDate = new Date(parsedStartDate.getTime());
            } else {
                // Default to 30 days ago
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -30);
                startDate = new Date(cal.getTimeInMillis());
            }

            // If end date is provided, parse it
            if (endDateStr != null && !endDateStr.isEmpty()) {
                java.util.Date parsedEndDate = sdf.parse(endDateStr);
                endDate = new Date(parsedEndDate.getTime());
            } else {
                // Default to current date
                endDate = new Date(System.currentTimeMillis());
            }

            // Ensure the start date is before the end date
            if (startDate.after(endDate)) {
                Date temp = startDate;
                startDate = endDate;
                endDate = temp;
            }

        } catch (Exception e) {
            // Handle parsing errors (e.g., invalid date format)
            e.printStackTrace();
            // Set default dates in case of error
            startDate = new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000); // 30 days ago
            endDate = new Date(System.currentTimeMillis()); // current date
        }

        // Fetch the products within the date range
        List<Product> productSale = productDAO.getTop5ProductsBySales(startDate, endDate);

        // Lấy số lượng blog từ BlogDAO
        int blogCount = blogDAO.getActiveBlogCount();
        int productCount = productDAO.getActiveProductCount();
        int customerCount = accountDAO.getCustomerCount();
        // List<Product> productSale = productDAO.getTop5ProductsByTotalQuantitySold();
        int totalQuantitySold = productDAO.getTotalQuantitySold();
        int sliderCount = sliderDAO.getActiveSliderCount();
        List<Brand> totalBrandRevenue = brandDAO.getTotalRevenueByBrand();
        //int orderCount = orderDAO.getOrderCount();
        // Đưa số lượng blog vào request attribute
        request.setAttribute("blogCount", blogCount);
        request.setAttribute("productCount", productCount);
        request.setAttribute("customerCount", customerCount);
        //  request.setAttribute("productSale", productSale);
        request.setAttribute("totalQuantitySold", totalQuantitySold);
        request.setAttribute("sliderCount", sliderCount);
        request.setAttribute("totalBrandRevenue", totalBrandRevenue);
        request.setAttribute("productSale", productSale);
        request.setAttribute("startDate", startDateStr);
        request.setAttribute("endDate", endDateStr);

        //request.setAttribute("orderCount", orderCount);
        // Chuyển tiếp tới trang JSP để hiển thị
        request.getRequestDispatcher("marketingdashboard.jsp").forward(request, response);
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
