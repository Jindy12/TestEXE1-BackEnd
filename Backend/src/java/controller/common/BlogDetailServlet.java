/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.common;

import dal.BlogDAO;
import dal.BlogTopicDAO;
import dal.CommentBlogDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Account;
import model.Blog;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import model.CommentBlog;

/**
 *
 * @author tuana
 */
public class BlogDetailServlet extends HttpServlet {

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
            out.println("<title>Servlet BlogDetailServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BlogDetailServlet at " + request.getContextPath() + "</h1>");
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
        BlogDAO bdao = new BlogDAO();
        CommentBlogDAO cbdao = new CommentBlogDAO();
        BlogTopicDAO btdao = new BlogTopicDAO();

        int blogId = Integer.parseInt(request.getParameter("blogId"));
        int commentRating = 0;

        try {
            commentRating = Integer.parseInt(request.getParameter("rating"));
        } catch (NumberFormatException e) {
            commentRating = 0;
        }

        // Lấy danh sách bình luận dựa vào rating
        List<CommentBlog> comment;
        if (commentRating > 0) {
            comment = cbdao.getCommentByRating(blogId, commentRating);
        } else {
            comment = cbdao.getCommentBlogByBlogId(blogId);
        }

        // Lấy thông tin blog và các thông tin liên quan
        Blog blog = bdao.getBlogById(blogId);
        List<Account> acc = cbdao.getUserNameByBlogId(blogId);
        String author = bdao.getUserFullNameById(blog.getUserId());
        String topic = btdao.getTopicById(blog.getBlogTopicId());
        List<Blog> top5 = bdao.getTop5NewBlog();

        // Phân trang
        int page, numperpage = 1;
        int size = comment.size();
        int num = (int) Math.ceil((double) size / numperpage); // Số trang, làm tròn lên
        String xpage = request.getParameter("page");
        if (xpage == null) {
            page = 1;
        } else {
            page = Integer.parseInt(xpage);
        }
        int start = (page - 1) * numperpage;
        int end = Math.min(page * numperpage, size);
        comment = cbdao.getCommentListByPage(comment, start, end);

        // Đặt các thuộc tính để chuyển đến trang JSP
        request.setAttribute("author", author);
        request.setAttribute("topic", topic);
        request.setAttribute("comment", comment);
        session.setAttribute("blog", blog);
        request.setAttribute("top5", top5);
        request.setAttribute("listacc", acc);
        request.setAttribute("commentRating", commentRating);
        request.setAttribute("size", size);
        request.setAttribute("numperpage", numperpage);
        request.setAttribute("page", page);
        request.setAttribute("num", num);

        // Chuyển hướng tới blogDetail.jsp
        request.getRequestDispatcher("blogDetail.jsp").forward(request, response);
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
        Blog blog = (Blog) session.getAttribute("blog");
        int blogId = blog.getBlogId();
        CommentBlogDAO cbdao = new CommentBlogDAO();
        String commentInfor = request.getParameter("commentInfor");
        int commentRating = Integer.parseInt(request.getParameter("rating"));
        Timestamp commentDate = Timestamp.valueOf(LocalDateTime.now());
        Account user = (Account) session.getAttribute("account");
        int userId = user.getUserId();
        cbdao.creatComment(userId, blogId, commentInfor, commentDate, commentRating);
        response.sendRedirect("blogdetail?blogId=" + blogId);
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
