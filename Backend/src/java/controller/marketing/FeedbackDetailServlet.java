/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.marketing;

import dal.FeedbackDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public class FeedbackDetailServlet extends HttpServlet {

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
            out.println("<title>Servlet FeedbackDetailServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FeedbackDetailServlet at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String feedbackIdParam = request.getParameter("id");

        if (feedbackIdParam != null) {
            try {
                int feedbackId = Integer.parseInt(feedbackIdParam);

                // Instantiate FeedbackDAO here
                FeedbackDAO feedbackDAO = new FeedbackDAO();
                Map<String, Object> feedbackDetail = feedbackDAO.getFeedbackDetailsById(feedbackId);

                if (feedbackDetail != null) {
                    // Get the feedback image from the database
                    String feedbackImg = feedbackDAO.getFeedbackImageById(feedbackId);
                    if (feedbackImg == null || feedbackImg.isEmpty()) {
                        feedbackImg = ""; // Đảm bảo gán chuỗi rỗng khi không có ảnh
                    }
                    // Add the image and feedback details to request attributes
                    request.setAttribute("feedbackdetail", feedbackDetail);
                    request.setAttribute("feedbackImg", feedbackImg); // Set feedbackImg for use in JSP

                    // Forward the request to the feedbackdetail.jsp page
                    request.getRequestDispatcher("feedbackdetail.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Feedback not found");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid feedback ID format");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Feedback ID is required");
        }
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String feedbackIdParam = request.getParameter("feedbackId");
        String newStatusParam = request.getParameter("status");

        // Ghi log các tham số nhận được
        System.out.println("Received feedbackId: " + feedbackIdParam);
        System.out.println("Received newStatus: " + newStatusParam);

        // Kiểm tra tham số
        if (feedbackIdParam == null || newStatusParam == null) {
            request.setAttribute("errorMessage", "Feedback ID and new status must be provided.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        try {
            int feedbackId = Integer.parseInt(feedbackIdParam);
            int status = Integer.parseInt(newStatusParam);

            // Tạo đối tượng FeedbackDAO
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            // Gọi phương thức cập nhật trạng thái
            feedbackDAO.updateFeedbackStatus(feedbackId, status);

            // Chuyển hướng về trang chi tiết phản hồi sau khi cập nhật
            response.sendRedirect("feedbackdetail?id=" + feedbackId);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid feedback ID or new status.");
            request.getRequestDispatcher("home").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("home").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
