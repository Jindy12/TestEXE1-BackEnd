/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.marketing;

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
import model.Blog;

/**
 *
 * @author Sonvu
 */
public class EditBlogMarketingServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet EditBlogMarketingServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditBlogMarketingServlet at " + request.getContextPath () + "</h1>");
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

        Blog blog = bdao.getBlogById(blogId);
        String author = bdao.getUserFullNameById(blog.getUserId());
        String topic = btdao.getTopicById(blog.getBlogTopicId());

        System.out.println(blogId);

        session.setAttribute("blog", blog);
        request.setAttribute("author", author);
        request.setAttribute("topic", topic);
        request.getRequestDispatcher("editblogbymarketing.jsp").forward(request, response);
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

        BlogDAO blogDao = new BlogDAO();

        HttpSession session = request.getSession();
        Blog blog = (Blog) session.getAttribute("blog");
        int blogId = blog.getBlogId();
        int blogTopicId = blog.getBlogTopicId();

        String title = request.getParameter("title");
        String open = request.getParameter("open");

        String imageText1 = request.getParameter("imageText1");
        String main1 = request.getParameter("main1");
        String sp1 = request.getParameter("sp1");

        String imageText2 = request.getParameter("imageText2");
        String main2 = request.getParameter("main2");
        String sp2 = request.getParameter("sp2");

        String imageText3 = request.getParameter("imageText3");
        String main3 = request.getParameter("main3");
        String sp3 = request.getParameter("sp3");

        String end = request.getParameter("end");

        System.out.println("Title: " + title);
        System.out.println("Open: " + open);

        System.out.println("ImageText1: " + imageText1);
        System.out.println("Main1: " + main1);
        System.out.println("SP1: " + sp1);

        System.out.println("ImageText2: " + imageText2);
        System.out.println("Main2: " + main2);
        System.out.println("SP2: " + sp2);

        System.out.println("ImageText3: " + imageText3);
        System.out.println("Main3: " + main3);
        System.out.println("SP3: " + sp3);

        System.out.println("End: " + end);

        System.out.println("==================================");

        blogDao.updateBlog(blogId, blogTopicId, title, open, main1, sp1, main2, sp2, main3, sp3, end);
        request.getRequestDispatcher("postList.jsp").forward(request, response);

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
