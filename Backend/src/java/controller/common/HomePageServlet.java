/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.common;

import dal.*;
import model.*;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;

/**
 *
 * @author nguye
 */
public class HomePageServlet extends HttpServlet {

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
        HttpSession session = request.getSession();
        ProductDAO productDAO = new ProductDAO();
        BrandDAO brandDao = new BrandDAO();
        StyleDAO styleDao = new StyleDAO();
        CartDAO cartDAO = new CartDAO();
        SliderDAO sliderDAO = new SliderDAO();
        AccountDAO accountDAO = new AccountDAO();
        Account account = (Account) session.getAttribute("account");
        if (account !=null) {
             int sizeCart = cartDAO.countCartsByUserId(account.getUserId());
             session.setAttribute("sizeCart", sizeCart);
        }
       
        List<Style> styleList = styleDao.getAllStyleCar();
        List<Brand> brandList = brandDao.getAllBrand();

        List<Product> featuredProducts = productDAO.getLastestProductsByProductIdPrefix("ME", 4);
        featuredProducts.addAll(productDAO.getProductsByProductIdPrefix("VO", 4));
        featuredProducts.addAll(productDAO.getProductsByProductIdPrefix("BM", 4));
        featuredProducts.addAll(productDAO.getProductsByProductIdPrefix("AU", 4));
        featuredProducts.addAll(productDAO.getProductsByProductIdPrefix("PO", 4));

        List<Slider> newProducts = sliderDAO.getAllActiveSlider();
        // Truyền danh sách sản phẩm đến JSP
        
        request.setAttribute("featuredProducts", featuredProducts); // Sử dụng tên đúng cho JSP
        request.setAttribute("newProducts", newProducts); // Sử dụng tên đúng cho JSP
        request.setAttribute("brandList", brandList);
        request.setAttribute("styleList", styleList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("HomePage.jsp");
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
