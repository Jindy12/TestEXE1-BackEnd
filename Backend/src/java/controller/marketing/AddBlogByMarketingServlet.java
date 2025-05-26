/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.marketing;

import dal.BlogDAO;
import dal.BlogTopicDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import model.Account;
import model.Blog;
import model.BlogTopic;

/**
 *
 * @author Sonvu
 */
public class AddBlogByMarketingServlet extends HttpServlet {

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
            out.println("<title>Servlet AddBlogByMarketingServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddBlogByMarketingServlet at " + request.getContextPath() + "</h1>");
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

        BlogTopicDAO bTopicDao = new BlogTopicDAO();
        BlogDAO blogDao = new BlogDAO();

        List<BlogTopic> listTopic = bTopicDao.getAllTopic();
        request.setAttribute("listTopic", listTopic);

        request.getRequestDispatcher("addblogbymarketing.jsp").forward(request, response);

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
        // Thiết lập mã hóa để xử lý tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Lấy thông tin từ session
        HttpSession session = request.getSession(false);
        Account author = (Account) session.getAttribute("account");
        String userId = author != null ? String.valueOf(author.getUserId()) : "";  // userId dạng String

        // Lấy thông tin từ form
        String blogTopicId = request.getParameter("topic") != null ? request.getParameter("topic").trim() : "";  // blogTopicId dạng String
        String blogTitle = request.getParameter("title") != null ? request.getParameter("title").trim() : "";
        String openBlog = request.getParameter("open") != null ? request.getParameter("open").trim() : "";
        String bodyMain1 = request.getParameter("main1") != null ? request.getParameter("main1").trim() : "";
        String bodySp1 = request.getParameter("sp1") != null ? request.getParameter("sp1").trim() : "";
        String bodyMain2 = request.getParameter("main2") != null ? request.getParameter("main2").trim() : "";
        String bodySp2 = request.getParameter("sp2") != null ? request.getParameter("sp2").trim() : "";
        String bodyMain3 = request.getParameter("main3") != null ? request.getParameter("main3").trim() : "";
        String bodySp3 = request.getParameter("sp3") != null ? request.getParameter("sp3").trim() : "";
        String endBlog = request.getParameter("end") != null ? request.getParameter("end").trim() : "";

        // Lấy thời gian hiện tại để lưu thời gian tạo bài viết
        Timestamp blogTime = Timestamp.valueOf(LocalDateTime.now());

        // Lưu thông tin blog vào cơ sở dữ liệu qua BlogDAO
        BlogDAO blogDAO = new BlogDAO();
        boolean isSaved = blogDAO.insertTopic(userId, blogTopicId, blogTitle, blogTime, openBlog, bodyMain1, bodySp1, bodyMain2, bodySp2, bodyMain3, bodySp3, endBlog);

        // Kiểm tra kết quả và chuyển hướng
        if (isSaved) {
            // Thêm thông báo vào session
            session.setAttribute("message", "Thêm blog thành công!");
            response.sendRedirect("postlist"); // Điều hướng đến trang postList sau khi thêm thành công
        } else {
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi thêm bài viết.");
            request.getRequestDispatcher("addBlogForm.jsp").forward(request, response); // Trở lại form với thông báo lỗi
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
