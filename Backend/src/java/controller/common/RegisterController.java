package controller.common;

import dal.AccountDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import util.HashPassword;
import jakarta.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

@WebServlet(name = "RegisterController", urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        int gender = Integer.parseInt(request.getParameter("gender").trim());
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String address = request.getParameter("address");

        AccountDAO dao = new AccountDAO();

        // Validate inputs
        boolean isEmailValid = dao.isValidEmail(email);
        boolean isMobileValid = dao.isValidMobile(mobile);
        boolean isPasswordValid = dao.isValidPassword(password);
        Account existingUser = dao.checkUserNameExists(userName);
        Account existingEmail = dao.checkEmailExists(email);
        Account existingMobile = dao.checkMobileExists(mobile);

        // Check for errors
        if (!isEmailValid) {
            request.setAttribute("errorMessage", "Email không hợp lệ.");
            forwardToRegisterPage(request, response, userName, password, firstName, lastName, gender, email, mobile, address);
            return;
        } else if (!isMobileValid) {
            request.setAttribute("errorMessage", "Số điện thoại phải có 10 số.");
            forwardToRegisterPage(request, response, userName, password, firstName, lastName, gender, email, mobile, address);
            return;
        } else if (!isPasswordValid) {
            request.setAttribute("errorMessage", "Mật khẩu phải có ít nhất 1 chữ cái viết hoa và 1 số.");
            forwardToRegisterPage(request, response, userName, password, firstName, lastName, gender, email, mobile, address);
            return;
        } else if (existingUser != null) {
            request.setAttribute("errorMessage", "Tên đăng nhập đã tồn tại.");
            forwardToRegisterPage(request, response, userName, password, firstName, lastName, gender, email, mobile, address);
            return;
        } else if (existingEmail != null) {
            request.setAttribute("errorMessage", "Email đã đăng ký.");
            forwardToRegisterPage(request, response, userName, password, firstName, lastName, gender, email, mobile, address);
            return;
        } else if (existingMobile != null) {
            request.setAttribute("errorMessage", "Số điện thoại đã đăng ký.");
            forwardToRegisterPage(request, response, userName, password, firstName, lastName, gender, email, mobile, address);
            return;
        }

        // Nếu tất cả các kiểm tra đều thành công
        password = HashPassword.toSHA1(password);

        // Tạo mã xác thực (có thể bỏ qua nếu không cần)
        String confirmationCode = java.util.UUID.randomUUID().toString();

        // Chèn thông tin vào database và thiết lập trạng thái là pending
        Account newAccount = new Account(userName, password, firstName, lastName, gender, email, mobile, address, 2); // 2 = pending
        dao.insertAccount(newAccount); // Chèn tài khoản vào bảng account với trạng thái pending

        // Gửi email xác nhận
        sendConfirmationEmail(email, confirmationCode);

        // Hiển thị thông báo cho người dùng
        request.setAttribute("message", "Một liên kết xác nhận đã được gửi đến email của bạn.");
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    private void sendConfirmationEmail(String toEmail, String confirmationCode) {
        // Cấu hình thuộc tính máy chủ gửi mail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        // Tạo phiên gửi mail với xác thực
        Session session = Session.getDefaultInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("carpipi1904@gmail.com", "fyvtiafvzyheblfw");
            }
        });

        // Soạn thảo email xác nhận
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("carpipi1904@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

//            message.setSubject("Xác nhận đăng ký tài khoản");
                message.setSubject(MimeUtility.encodeText("Xác nhận đăng ký tài khoản", "UTF-8", "B"));
            //String confirmationLink = "http://localhost:9999/Iter1_Test/confirm?email=" + toEmail; // Đảm bảo thay đổi 'yourapp' thành tên ứng dụng của bạn
//            message.setText("Vui lòng nhấp vào liên kết sau để xác nhận tài khoản của bạn: " + confirmationLink);
           // message.setContent("Vui lòng nhấp vào liên kết sau để xác nhận tài khoản của bạn: " + confirmationLink, "text/plain; charset=UTF-8");
            String confirmationLink = "http://localhost:9999/Iter1_Test/confirm?email=" + toEmail;
        String emailContent = "Vui lòng click vào link: " + confirmationLink;
        message.setContent(emailContent, "text/plain; charset=UTF-8");

            // Gửi email
            Transport.send(message);
            System.out.println("Email xác nhận đã được gửi thành công.");
        } catch (MessagingException| UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void forwardToRegisterPage(HttpServletRequest request, HttpServletResponse response,
            String userName, String password, String firstName,
            String lastName, int gender, String email,
            String mobile, String address)
            throws ServletException, IOException {
        // Set attributes for each field
        request.setAttribute("userName", userName);
        request.setAttribute("password", password);
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);
        request.setAttribute("gender", gender);
        request.setAttribute("email", email);
        request.setAttribute("mobile", mobile);
        request.setAttribute("address", address);

        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Register Controller";
    }
}
