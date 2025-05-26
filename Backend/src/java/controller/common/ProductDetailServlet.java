/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.common;

import dal.BrandDAO;
import dal.ColorDAO;
import dal.FeedbackDAO;
import dal.ProductDAO;
import dal.SegmentDAO;
import dal.StyleDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Account;
import model.Color;
import model.Product;
import model.Feedback;

/**
 *
 * @author tuana
 */
public class ProductDetailServlet extends HttpServlet {

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
            out.println("<title>Servlet ProductDetailServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductDetailServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        String productId = request.getParameter("productId");
        String rateParam = request.getParameter("rate");
        int rate = 0;
        try {
            rate = Integer.parseInt(rateParam);
        } catch (Exception e) {
        }
        ColorDAO cdao = new ColorDAO();
        ProductDAO pdao = new ProductDAO();
        BrandDAO bdao = new BrandDAO();
        StyleDAO sdao = new StyleDAO();
        SegmentDAO sedao = new SegmentDAO();
        FeedbackDAO fdao = new FeedbackDAO();

        List<Feedback> feedback;
        if (rate == 0) {
            feedback = fdao.getFeedbackByProductId(productId);
        } else {
            feedback = fdao.getFeedbackByRate(productId, rate);
        }
        List<Color> color = cdao.getColorOfCar(productId);
        Product pro = (Product) pdao.getProductById(productId);
        List<Product> pro2 = pdao.getProductByPrice(pro.getPrice());
        String segmentName = sedao.getSegmentNameBySegmentId(pro.getSegmentId());
        String brand = bdao.getBrandById(pro.getBrandId());
        String style = sdao.getStyleNameByStyleId(pro.getStyleId());
        String supply = pdao.getSupplyNameById(pro.getSupplyId());
        List<Account> listAcc = fdao.getUserNameByProductId(productId);
        int rateCar = fdao.getRateProduct(productId);

        // Phân trang
        int page, numperpage = 2;
        int size = feedback.size();
        int num = (int) Math.ceil((double) size / numperpage); // Số trang, làm tròn lên
        String xpage = request.getParameter("page");
        if (xpage == null) {
            page = 1;
        } else {
            page = Integer.parseInt(xpage);
        }
        int start = (page - 1) * numperpage;
        int end = Math.min(page * numperpage, size);
        feedback = fdao.getFeedbackListByPage(feedback, start, end);

        request.setAttribute("pro", pro);
        request.setAttribute("color", color);
        request.setAttribute("brand", brand);
        request.setAttribute("pro2", pro2);
        request.setAttribute("style", style);
        request.setAttribute("supply", supply);
        request.setAttribute("feedback", feedback);
        request.setAttribute("segment", segmentName);
        request.setAttribute("acc", listAcc);
        request.setAttribute("rate", rate);
        request.setAttribute("page", page);
        request.setAttribute("num", num);
        request.setAttribute("size", size);
        request.setAttribute("rateCar", rateCar);
        session.setAttribute("urlHistory", "productdetail?productId=" + productId);
        request.getRequestDispatcher("productDetail.jsp").forward(request, response);
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
