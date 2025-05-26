package controller.common;

import dal.FeedbackDAO;
import dal.OrderDetailDAO;
import dal.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import model.Account;
import model.Product;

@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5, // 5 MB
        maxRequestSize = 1024 * 1024 * 10 // 10 MB
)
public class CustomerFeedbackServlet extends HttpServlet {

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
            out.println("<title>Servlet CustomerFeedbackServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CustomerFeedbackServlet at " + request.getContextPath() + "</h1>");
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
        ProductDAO pdao = new ProductDAO();
        Account acc = (Account) session.getAttribute("account");
        if (acc == null) {
            response.sendRedirect("login.jsp"); // Chuyển hướng về trang login nếu không có tài khoản
            return;
        }
        int userId = acc.getUserId();
        String productId = request.getParameter("productId");
        int orderDetailId = Integer.parseInt(request.getParameter("orderDetailId"));
        Product pro = pdao.getProductById(productId);
        request.setAttribute("acc", acc);
        request.setAttribute("pro", pro);
        request.setAttribute("orderDetailId", orderDetailId);
        request.getRequestDispatcher("customerFeedback.jsp").forward(request, response);
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
        FeedbackDAO fdao = new FeedbackDAO();
        OrderDetailDAO od1dao = new OrderDetailDAO();
        String feedbackInfor = request.getParameter("feedbackInfor"); // Đảm bảo đồng nhất tên biến
        int userId = Integer.parseInt(request.getParameter("userId"));
        String productId = request.getParameter("productId");
        int feedbackRate = Integer.parseInt(request.getParameter("feedbackRate"));
        int orderDetailId = Integer.parseInt(request.getParameter("orderDetailId"));
        String feedbackImg = "";

        Part file = request.getPart("feedbackImg"); // Đảm bảo tên trùng với input trong form
        if (file != null && file.getSize() > 0) {
            String fileName = Paths.get(file.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("/img") + File.separator + fileName;

            // Tạo thư mục nếu chưa tồn tại
            File uploadDir = new File(getServletContext().getRealPath("/img"));
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // Tạo thư mục
            }

            // Lưu file
            try (InputStream is = file.getInputStream(); FileOutputStream fos = new FileOutputStream(uploadPath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                feedbackImg = fileName; // Cập nhật đường dẫn ảnh mới
            } catch (Exception e) {
                e.printStackTrace(); // Log lỗi khi lưu file
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save image file.");
                return;
            }
        }

        // Gọi hàm tạo phản hồi trong DAO
        fdao.createFeedback(userId, productId, feedbackInfor, feedbackImg, feedbackRate, 1);
        od1dao.updateFeedbackOrder(orderDetailId);
        response.sendRedirect("myorder");
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
