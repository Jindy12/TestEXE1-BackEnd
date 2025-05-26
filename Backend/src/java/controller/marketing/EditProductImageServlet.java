/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.marketing;

import dal.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author ADMIN
 */
public class EditProductImageServlet extends HttpServlet {

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
            out.println("<title>Servlet EditProductImageServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditProductImageServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();

        String id = (String) session.getAttribute("productIdSession"); // Không cập nhật productId

        String action = request.getParameter("action");
        ProductDAO productDAO = new ProductDAO();
//        if ("delete".equals(action)) {
//            String imageUrl = request.getParameter("imageUrl");
//            int imageId = Integer.parseInt(request.getParameter("imageId"));
//            //productDAO.deleteImage(imageUrl);  // Gọi phương thức xóa hình ảnh
//            productDAO.deleteImageUrl(imageId);  // Gọi phương thức xóa hình ảnh
//            response.sendRedirect("editproductbymarketing?id=" + id); // Redirect sau khi xóa
        if ("delete".equals(action)) {
            String imageIdStr = request.getParameter("imageId");

            // Check if imageId is valid before parsing
            if (imageIdStr != null && !imageIdStr.isEmpty()) {
                try {
                    int imageId = Integer.parseInt(imageIdStr);
                    // Call the DAO method to delete the image
                    productDAO.deleteImageUrl(imageId);
                    response.sendRedirect("editproductbymarketing?id=" + id); // Redirect after deletion
                } catch (NumberFormatException e) {
                    // Handle the case where the imageId is invalid (e.g., not a valid integer)
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid image ID format");
                }
            } else {
                // Handle the case where imageId is missing
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing image ID");
            }

        } else if ("update".equals(action)) {
            String imageIdStr = request.getParameter("imageId");
            String newImageUrl = request.getParameter("newImageUrl");
            if (imageIdStr != null && !imageIdStr.isEmpty()) {
                try {
                    int imageId = Integer.parseInt(imageIdStr);
                    // Call the DAO method to delete the image
                    productDAO.updateImage(imageId, newImageUrl); // Gọi phương thức cập nhật hình ảnh
                    response.sendRedirect("editproductbymarketing?id=" + id); // Redirect after deletion
                } catch (NumberFormatException e) {
                    // Handle the case where the imageId is invalid (e.g., not a valid integer)
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid image ID format");
                }
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
