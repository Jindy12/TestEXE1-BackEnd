/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import context.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.security.MessageDigest;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 *
 * @author ADMIN
 */
public class HashPassword {
    //md5
    //sha1

    public static String toSHA1(String pass) {
        String salt = "khongthecongpha"; //salt : them vao str ma hoa de pass phuc tap hon
        String result = null;
        pass = pass + salt;
        try {
            byte[] dataBytes = pass.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            result = Base64.encodeBase64String(md.digest(dataBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    

    // Phương thức lấy và mã hóa lại mật khẩu chưa được mã hóa
    // Phương thức lấy và mã hóa lại mật khẩu chưa được mã hóa
public static void encryptPasswords() {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    PreparedStatement pstmt = null;

    try {
        DBContext dbContext = new DBContext();

        // Sử dụng kết nối từ DBContext
        conn = dbContext.getConnection();  // Giả sử bạn có phương thức getConnection() trong DBContext
        stmt = conn.createStatement();
        String selectQuery = "SELECT userId, password FROM account WHERE LENGTH(password) <=20"; // Không kiểm tra cột 'encrypted'

        rs = stmt.executeQuery(selectQuery);

        while (rs.next()) {
            int id = rs.getInt("userId");
            String password = rs.getString("password");

            // Mã hóa mật khẩu
            String encryptedPassword = HashPassword.toSHA1(password);

            // Cập nhật mật khẩu đã mã hóa vào CSDL
            String updateQuery = "UPDATE account SET password = ? WHERE userId = ?"; // Loại bỏ cột 'encrypted'
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, encryptedPassword);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        }

        System.out.println("Mã hóa mật khẩu thành công!");

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


    public static void main(String[] args) {
        System.out.println(toSHA1("123456"));
                encryptPasswords();  // Gọi hàm để mã hóa lại mật khẩu

    }
}
