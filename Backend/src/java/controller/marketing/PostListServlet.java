/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.marketing;

import dal.AccountDAO;
import dal.BlogDAO;
import dal.BlogTopicDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Account;
import model.Blog;
import model.BlogTopic;

/**
 *
 * @author tuana
 */
public class PostListServlet extends HttpServlet {

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
            out.println("<title>Servlet PostListServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PostListServlet at " + request.getContextPath() + "</h1>");
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
        BlogDAO bdao = new BlogDAO();
        BlogTopicDAO btdao = new BlogTopicDAO();
        AccountDAO adao = new AccountDAO();
        List<Account> listAuthor = adao.getAllAuthor();
        List<BlogTopic> listTopic = btdao.getAllTopic();
        // Lấy các tham số từ request
        String search = request.getParameter("search");
        String statusParam = request.getParameter("status");
        String topicParam = request.getParameter("topic");
        String userIdParam = request.getParameter("author");

        int status = -1;  // Giá trị mặc định khi status không hợp lệ
        int topic = 0, userId = 0;
        // Kiểm tra và chuyển đổi status
        if (statusParam != null && !statusParam.isEmpty()) {
            try {
                status = Integer.parseInt(statusParam);
            } catch (NumberFormatException e) {
                System.out.println("Lỗi chuyển đổi status: " + e);
            }
        }

        if (topicParam != null && !topicParam.isEmpty()) {
            try {
                topic = Integer.parseInt(topicParam);
            } catch (NumberFormatException e) {
                System.out.println("Lỗi chuyển đổi topic: " + e);
            }
        }

        if (userIdParam != null && !userIdParam.isEmpty()) {
            try {
                userId = Integer.parseInt(userIdParam);
            } catch (NumberFormatException e) {
                System.out.println("Lỗi chuyển đổi userId: " + e);
            }
        }

        // Lấy danh sách blog
        List<Blog> blog;
        if ((search == null || search.trim().isEmpty()) && status == -1 && topic == 0 && userId == 0) {
            blog = bdao.getAllBlog();
        } else if (status == 1 || status == 0) {
            blog = bdao.getBlogByStatus(status);
        } else if (search != null && !search.trim().isEmpty()) {
            blog = bdao.getBlogBySearch(search);
        } else if (topic >= 1 && topic <= 5) {
            blog = bdao.getBlogByTopic(topic);
        } else if (userId>0){
            blog = bdao.getBlogByAuthor(userId);
        } else {
            blog = bdao.getAllBlog();
        }

        //phân trang
        int page, numperpage = 4;
        int size = blog.size();
        int num = (int) Math.ceil((double) size / numperpage); // Số trang, làm tròn lên
        String xpage = request.getParameter("page");
        if (xpage == null) {
            page = 1;
        } else {
            page = Integer.parseInt(xpage);
        }
        int start = (page - 1) * numperpage;
        int end = Math.min(page * numperpage, size);
        List<Blog> listBlog = bdao.getBlogListByPage(blog, start, end);

        request.setAttribute("size", size);
        request.setAttribute("numperpage", numperpage);
        request.setAttribute("blogList", listBlog);
        request.setAttribute("author", listAuthor);
        request.setAttribute("search", search);
        request.setAttribute("page", page);
        request.setAttribute("num", num);
        request.setAttribute("topicId", topic);
        request.setAttribute("status", status);
        request.setAttribute("userId", userId);
        request.setAttribute("topic", listTopic);
        request.getRequestDispatcher("postList.jsp").forward(request, response);
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
