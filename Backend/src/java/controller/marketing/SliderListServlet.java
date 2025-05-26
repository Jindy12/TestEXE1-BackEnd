/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.marketing;

import dal.SliderDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Slider;

/**
 *
 * @author ADMIN
 */
public class SliderListServlet extends HttpServlet {
   
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
            out.println("<title>Servlet SliderListServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SliderListServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//        // Lấy thông tin từ request (search và status)
//String search = request.getParameter("search"); // Đảm bảo biến này là kiểu String
//String statusString = request.getParameter("status"); // Đảm bảo biến này là kiểu String
//int status = -1; // Giá trị mặc định để kiểm tra
//
//// Chuyển đổi status từ String sang int
//if (statusString != null && !statusString.isEmpty()) {
//    try {
//        status = Integer.parseInt(statusString); // Chuyển đổi kiểu dữ liệu
//    } catch (NumberFormatException e) {
//        // Xử lý lỗi nếu giá trị không hợp lệ
//        e.printStackTrace(); // In ra lỗi để kiểm tra
//    }
//}

// Lấy danh sách slider từ SliderDAO
//SliderDAO sliderDAO = new SliderDAO();
//List<Slider> sliders = sliderDAO.getFilteredSliders(search, status);
//
//// Gửi danh sách slider về cho view (JSP)
//request.setAttribute("sliders", sliders);
//request.getRequestDispatcher("SliderList.jsp").forward(request, response);
    
    String search = request.getParameter("search");
    String statusString = request.getParameter("status");
    int status = -1; // Giá trị mặc định để kiểm tra

    // Chuyển đổi status từ String sang int
    if (statusString != null && !statusString.isEmpty()) {
        try {
            status = Integer.parseInt(statusString);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Xử lý phân trang
    String pageString = request.getParameter("page");
    int page = (pageString != null && !pageString.isEmpty()) ? Integer.parseInt(pageString) : 1;
    int limit = 15; // Số lượng slider mỗi trang
    int offset = (page - 1) * limit; // Tính toán offset

    // Lấy danh sách slider từ SliderDAO
    SliderDAO sliderDAO = new SliderDAO();
    List<Slider> sliders = sliderDAO.getFilteredSliders(search, status, offset, limit);

    // Gửi danh sách slider về cho view (JSP)
    request.setAttribute("sliders", sliders);
    request.setAttribute("currentPage", page);
    request.setAttribute("search", search);
    request.setAttribute("status", statusString); // Để lưu lại trạng thái đã chọn

    request.getRequestDispatcher("SliderList.jsp").forward(request, response);

    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // Lấy thông tin từ request (sliderId và status)
        String sliderIdParam = request.getParameter("sliderId");
        String statusParam = request.getParameter("status");

        if (sliderIdParam != null && statusParam != null) {
            int sliderId = Integer.parseInt(sliderIdParam);
            int status = Integer.parseInt(statusParam);

            // Cập nhật trạng thái slider
            SliderDAO sliderDAO = new SliderDAO();
            sliderDAO.updateSliderStatus(sliderId, status);
        }

        // Sau khi cập nhật, reload lại trang danh sách slider
        response.sendRedirect("SliderList");
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
