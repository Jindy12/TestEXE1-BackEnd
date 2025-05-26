/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.marketing;

import dal.SliderDAO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.PrintWriter;
import model.Slider;
/**
 *
 * @author nguye
 */

@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5, // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class SliderDetailServlet extends HttpServlet {
   
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
            out.println("<title>Servlet SilderDetailServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SilderDetailServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String sliderId = request.getParameter("id"); // Lấy id từ query string
        SliderDAO sdao = new SliderDAO();
        Slider slider = sdao.getSliderById(Integer.parseInt(sliderId));
        
        if (slider != null) {
            request.setAttribute("slider", slider);
            request.getRequestDispatcher("sliderDetail.jsp").forward(request, response);
        } else {
            response.sendRedirect("SliderList"); // Chuyển hướng nếu không tìm thấy slider
        }
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession();
        SliderDAO sdao = new SliderDAO();
        
        // Lấy thông tin từ form
        String title = request.getParameter("title");
        String backlink = request.getParameter("backlink");
        int status = Integer.parseInt(request.getParameter("status"));
        String notes = request.getParameter("description");
        String sliderId = request.getParameter("id");
        String productId = request.getParameter("proId");
        
        // Xử lý upload ảnh nếu có file mới
        Part filePart = request.getPart("image");
        String imagePath = null;
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("/img") + File.separator + fileName;

            File uploadDir = new File(getServletContext().getRealPath("/img"));
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
            }

            try (InputStream is = filePart.getInputStream(); FileOutputStream fos = new FileOutputStream(uploadPath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                imagePath = fileName;
            }
        }
        
        boolean isUpdated = sdao.updateSlider(Integer.parseInt(sliderId), productId, title, backlink, status, notes, imagePath != null ? "img/" + imagePath : null);
        if (isUpdated) {
            request.setAttribute("successMessage", "Cập nhật thông tin thành công!");
        } else {
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật thông tin.");
        }
        Slider updatedSlider = sdao.getSliderById(Integer.parseInt(sliderId));
        request.setAttribute("slider", updatedSlider);
        request.getRequestDispatcher("sliderDetail.jsp").forward(request, response);
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
