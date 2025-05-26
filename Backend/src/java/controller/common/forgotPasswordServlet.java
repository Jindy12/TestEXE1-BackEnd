package controller.common;

import dal.AccountDAO;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Random;
import model.Account;

@WebServlet("/forgotPassword")
public class forgotPasswordServlet extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet forgotPasswordServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet forgotPasswordServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String email = request.getParameter("email");
        RequestDispatcher dispatcher = null;
        int otpvalue = 0;
        HttpSession mySession = request.getSession();

        // Tạo đối tượng AccountDAO và kiểm tra email bằng checkEmailExists
        AccountDAO accountDAO = new AccountDAO();
        Account account = accountDAO.checkEmailExists(email);

        if (account == null) {
            // Nếu email không tồn tại trong database, hiển thị thông báo lỗi
            request.setAttribute("errorMessage", "Email không tồn tại trong hệ thống.");
            dispatcher = request.getRequestDispatcher("forgotPassword.jsp");
            dispatcher.forward(request, response);
        } else {
            // Generate OTP
            Random rand = new Random();
            otpvalue = rand.nextInt(1255650);

            String to = email;
            // Configure mail server properties
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            
            // Create mail session with authentication
            Session session = Session.getDefaultInstance(props, new jakarta.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("carpipi1904@gmail.com", "fyvtiafvzyheblfw"); // Sử dụng email và password phù hợp
                }
            });

            // Compose and send OTP email
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("carpipi1904@gmail.com")); // Change sender's email if needed
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                
                message.setSubject("Xin Chào");
                message.setText("Mã OTP: " + otpvalue);

                // Send message
                Transport.send(message);
                System.out.println("OTP email sent successfully");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

            // Forward to EnterOtp.jsp with OTP message
            dispatcher = request.getRequestDispatcher("enterOTP.jsp");
            request.setAttribute("message", "OTP đã được gửi tới địa chỉ email của bạn.");
            mySession.setAttribute("otp", otpvalue);
            mySession.setAttribute("email", email);
            dispatcher.forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Forgot password servlet for sending OTP via email.";
    }
}
