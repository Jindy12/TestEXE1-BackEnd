/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.Account;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.GoogleAccount;
import context.DBContext;

/**
 *
 * @author Sonvu
 */
public class LoginDAO extends DBContext {

    public Account getUsernameAndPassword(String username, String password) {

        String sql = "SELECT * FROM account WHERE username = ? AND password = ? AND status = 1";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Account account = new Account();
                account.setUserId(rs.getInt("userId"));
                account.setUserName(rs.getString("userName"));
                account.setPassword(rs.getString("password"));
                account.setFirstName(rs.getString("firstName"));
                account.setLastName(rs.getString("lastName"));
                account.setGender(rs.getInt("gender"));
                account.setEmail(rs.getString("email"));
                account.setMobile(rs.getString("mobile"));
                account.setAddress(rs.getString("address"));
                account.setRoleId(rs.getInt("roleId"));
                account.setAvatar(rs.getString("avatar"));
                account.setStatus(rs.getInt("status"));
                account.setMoney(rs.getBigDecimal("money"));
                return account;
            } else {
                System.out.println("Đăng nhập thất bại: Sai thông tin hoặc tài khoản chưa kích hoạt.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi truy vấn CSDL: " + e.getMessage());
        }
        return null;

    }

    public Account getEmailAndPassword(String username, String password) {

        String sql = "select * from carpipi.account where email = ? and password = ? and status = 1";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Account account = new Account();
                account.setUserId(rs.getInt(1));
                account.setUserName(rs.getString(2));
                account.setPassword(rs.getString(3));
                account.setFirstName(rs.getString(4));
                account.setLastName(rs.getString(5));
                account.setGender(rs.getInt(6));
                account.setEmail(rs.getString(7));
                account.setMobile(rs.getString(8));
                account.setAddress(rs.getString(9));
                account.setRoleId(rs.getInt(10));

                return account;

            }

        } catch (SQLException e) {
        }

        return null;

    }

    public Account getByEmail(String email) {

        String sql = "select * from account where email = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Account account = new Account();
                account.setUserId(rs.getInt(1));
                account.setUserName(rs.getString(2));
                account.setPassword(rs.getString(3));
                account.setFirstName(rs.getString(4));
                account.setLastName(rs.getString(5));
                account.setGender(rs.getInt(6));
                account.setEmail(rs.getString(7));
                account.setMobile(rs.getString(8));
                account.setAddress(rs.getString(9));
                account.setRoleId(rs.getInt(10));
                account.setAvatar(rs.getString(11));
                account.setStatus(rs.getInt(12));

                return account;

            }

        } catch (SQLException e) {
        }

        return null;

    }

//     userId INT PRIMARY KEY,
//    userName VARCHAR(255),
//    password VARCHAR(255),
//    firstName VARCHAR(255),
//    lastName VARCHAR(255),
//    gender VARCHAR(10),
//    email VARCHAR(255),
//    mobile VARCHAR(20),
//    address VARCHAR(255),
//    roleId INT not null,
//    FOREIGN KEY (roleId) REFERENCES Roles(roleId)
    public void inserUserByTest(String id, String username, String password, String firstName, String lastName, String gender, String email, String phone, String address) {
        String sql = "INSERT INTO Account\n"
                + "           (userName\n"
                + "           ,password\n"
                + "           ,firstName\n"
                + "           ,lastName\n"
                + "           ,gender\n"
                + "           ,email\n"
                + "           ,mobile\n"
                + "           ,address\n"
                + "           ,roleId)\n"
                + "     VALUES\n"
                + "           (?,?,?,?,?,?,?,?,'4')";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, firstName);
            ps.setString(4, lastName);
            ps.setString(5, gender);
            ps.setString(6, email);
            ps.setString(7, "");
            ps.setString(8, "");

            ps.executeUpdate();
        } catch (Exception e) {
        }
    }

    public void inserUserByEmail(String username, String password, String firstName,
            String lastName, String gender, String email, String phone, String address, String avatar) {
        String sql = "INSERT INTO account (userName, password, firstName, lastName, gender, email, mobile, address, roleId, avatar, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, firstName);
            ps.setString(4, lastName);
            ps.setString(5, gender);
            ps.setString(6, email);
            ps.setString(7, "");
            ps.setString(8, "");
            ps.setString(9, "4");
            ps.setString(10, avatar);
            ps.setString(11, "1");

            ps.executeUpdate();
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        LoginDAO l = new LoginDAO();
        Account a = l.getUsernameAndPassword("sales_user1", "password123");

    }

}
