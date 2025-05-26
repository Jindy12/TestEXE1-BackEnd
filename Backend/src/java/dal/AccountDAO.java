/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import model.Account;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import util.HashPassword;

/**
 *
 * @author tuana
 */
public class AccountDAO extends DBContext {

    public boolean updateAccountStatus1(int userId, int status) {
        String sql = "UPDATE account SET status = ? WHERE userId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, status); // Set the new status (0, 1, or 2)
            stmt.setInt(2, userId);  // Set userId value

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //hieu
    public void insertAccountAdmin(Account acc) {

        String sql = "INSERT INTO account (userName, password, firstName, lastName, gender, email, mobile, address, roleId, avatar, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, acc.getUserName());
            stm.setString(2, acc.getPassword());
            stm.setString(3, acc.getFirstName());
            stm.setString(4, acc.getLastName());
            stm.setInt(5, acc.getGender());
            stm.setString(6, acc.getEmail());
            stm.setString(7, acc.getMobile());
            stm.setString(8, acc.getAddress());
            stm.setInt(9, 4); // Role mặc định - customer
            stm.setString(10, "avatar-trang-4.jpg"); // Avatar mặc định
            stm.setInt(11, 1); // Trạng thái 'pending'

            stm.executeUpdate();
            System.out.println("Tài khoản đã được thêm vào danh sách  thành công!");
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm tài khoản vào danh sách " + e.getMessage());
        }
    }
    //hieu

    public Account getAccountByEmail(String email) {
        // Truy vấn thông tin tài khoản từ bảng 'account' với trạng thái 'pending'
        String sql = "SELECT * FROM account WHERE email = ? ";
        Account pendingAccount = null;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                pendingAccount = new Account();
                pendingAccount.setUserName(rs.getString("userName"));
                pendingAccount.setPassword(rs.getString("password"));
                pendingAccount.setFirstName(rs.getString("firstName"));
                pendingAccount.setLastName(rs.getString("lastName"));
                pendingAccount.setGender(rs.getInt("gender"));
                pendingAccount.setEmail(rs.getString("email"));
                pendingAccount.setMobile(rs.getString("mobile"));
                pendingAccount.setAddress(rs.getString("address"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn tài khoản chờ: " + e.getMessage());
        }

        return pendingAccount;
    }

    //hieu
    public Account getAccountByPhone(String mobile) {
        // Truy vấn thông tin tài khoản từ bảng 'account' với trạng thái 'pending'
        String sql = "SELECT * FROM account WHERE mobile = ? ";
        Account pendingAccount = null;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, mobile);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                pendingAccount = new Account();
                pendingAccount.setUserName(rs.getString("userName"));
                pendingAccount.setPassword(rs.getString("password"));
                pendingAccount.setFirstName(rs.getString("firstName"));
                pendingAccount.setLastName(rs.getString("lastName"));
                pendingAccount.setGender(rs.getInt("gender"));
                pendingAccount.setEmail(rs.getString("email"));
                pendingAccount.setMobile(rs.getString("mobile"));
                pendingAccount.setAddress(rs.getString("address"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn tài khoản chờ: " + e.getMessage());
        }

        return pendingAccount;
    }

    //hieu
    public Account getAccountByUserName(String userName) {
        // Truy vấn thông tin tài khoản từ bảng 'account' với trạng thái 'pending'
        String sql = "SELECT * FROM account WHERE userName = ? ";
        Account pendingAccount = null;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, userName);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                pendingAccount = new Account();
                pendingAccount.setUserName(rs.getString("userName"));
                pendingAccount.setPassword(rs.getString("password"));
                pendingAccount.setFirstName(rs.getString("firstName"));
                pendingAccount.setLastName(rs.getString("lastName"));
                pendingAccount.setGender(rs.getInt("gender"));
                pendingAccount.setEmail(rs.getString("email"));
                pendingAccount.setMobile(rs.getString("mobile"));
                pendingAccount.setAddress(rs.getString("address"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn tài khoản chờ: " + e.getMessage());
        }

        return pendingAccount;
    }

    public void insertPendingAccount(Account acc) {
        // Chèn thông tin tài khoản vào bảng 'account' với trạng thái 'pending'
        String sql = "INSERT INTO account (userName, password, firstName, lastName, gender, email, mobile, address, roleId, avatar, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, acc.getUserName());
            stm.setString(2, acc.getPassword());
            stm.setString(3, acc.getFirstName());
            stm.setString(4, acc.getLastName());
            stm.setInt(5, acc.getGender());
            stm.setString(6, acc.getEmail());
            stm.setString(7, acc.getMobile());
            stm.setString(8, acc.getAddress());
            stm.setInt(9, 4); // Role mặc định - customer
            stm.setString(10, "avatar-trang-4.jpg"); // Avatar mặc định
            stm.setInt(11, 2); // Trạng thái 'pending'

            stm.executeUpdate();
            System.out.println("Tài khoản đã được thêm vào danh sách chờ thành công!");
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm tài khoản vào danh sách chờ: " + e.getMessage());
        }
    }

    public Account getPendingAccountByEmail(String email) {
        // Truy vấn thông tin tài khoản từ bảng 'account' với trạng thái 'pending'
        String sql = "SELECT * FROM account WHERE email = ? AND status = 2";
        Account pendingAccount = null;

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                pendingAccount = new Account();
                pendingAccount.setUserName(rs.getString("userName"));
                pendingAccount.setPassword(rs.getString("password"));
                pendingAccount.setFirstName(rs.getString("firstName"));
                pendingAccount.setLastName(rs.getString("lastName"));
                pendingAccount.setGender(rs.getInt("gender"));
                pendingAccount.setEmail(rs.getString("email"));
                pendingAccount.setMobile(rs.getString("mobile"));
                pendingAccount.setAddress(rs.getString("address"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn tài khoản chờ: " + e.getMessage());
        }

        return pendingAccount;
    }

    public void activateAccount(String email) {
        // Cập nhật trạng thái tài khoản từ 'pending' sang 'active'
        String sql = "UPDATE account SET status = 1 WHERE email = ? AND status = 2";

        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, email);
            int rowsUpdated = stm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Tài khoản đã được kích hoạt thành công!");
            } else {
                System.out.println("Không tìm thấy tài khoản để kích hoạt.");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kích hoạt tài khoản: " + e.getMessage());
        }
    }

    public void updateAccountStatus(Account account) {
        try {
            String sql = "UPDATE account SET status = ? WHERE userId = ?";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, account.getStatus());
            stm.setInt(2, account.getUserId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

//    public void insertAccount(Account acc) {
//        try {
//
//            String sql = "INSERT INTO account "
//                    + "(userName, password, firstName, lastName, gender, email, mobile, address, roleId, avatar,status) "
//                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//            PreparedStatement stm = connection.prepareStatement(sql);
//            stm.setString(1, acc.getUserName());
//            //stm.setString(2, hashedPassword);
//            stm.setString(2, acc.getPassword());
//            stm.setString(3, acc.getFirstName());
//            stm.setString(4, acc.getLastName());
//            stm.setInt(5, acc.getGender());
//            stm.setString(6, acc.getEmail());
//            stm.setString(7, acc.getMobile());
//            stm.setString(8, acc.getAddress());
//            stm.setInt(9, acc.getRoleId()); //role mac dinh - customer
//            stm.setString(10, acc.getAvatar()); //ava mac dinh
//            stm.setInt(11, 1);
//            stm.executeUpdate();
//            System.out.println("Account đã được thêm thành công!");
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi thêm account: " + e.getMessage());
//        }
//    }
    public void insertAccount(Account acc) {
        try {

            String sql = "INSERT INTO account "
                    + "(userName, password, firstName, lastName, gender, email, mobile, address, roleId, avatar) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, acc.getUserName());
            //stm.setString(2, hashedPassword);
            stm.setString(2, acc.getPassword());
            stm.setString(3, acc.getFirstName());
            stm.setString(4, acc.getLastName());
            stm.setInt(5, acc.getGender());
            stm.setString(6, acc.getEmail());
            stm.setString(7, acc.getMobile());
            stm.setString(8, acc.getAddress());
            stm.setInt(9, 4); //role mac dinh - customer
            stm.setString(10, "avatar-trang-4.jpg"); //ava mac dinh
            stm.executeUpdate();
            System.out.println("Account đã được thêm thành công!");
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm account: " + e.getMessage());
        }
    }

    public Account checkEmailExists(String email) {
        String sql = "SELECT * FROM account WHERE email = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11)
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error checking email", e);
        }
        return null;
    }

    // Kiểm tra nếu username đã tồn tại trong database
    public Account checkUserNameExists(String userName) {
        String sql = "SELECT * FROM account WHERE userName = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11)
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error checking username", e);
        }
        return null;
    }

    public Account checkMobileExists(String mobile) {
        String sql = "SELECT * FROM account WHERE mobile = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, mobile);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11)
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(AccountDAO.class.getName()).log(Level.SEVERE, "Error checking mobile", e);
        }
        return null;
    }

    // Kiểm tra email có đúng định dạng không
    public boolean isValidEmail(String email) {
        // Regex for a valid email format
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        return email != null && Pattern.compile(emailRegex).matcher(email).matches();
    }

    // Kiểm tra số điện thoại có đúng định dạng không (10 số)
    public boolean isValidMobile(String mobile) {
        System.out.println("Checking mobile validity: " + mobile);
        return mobile != null && mobile.matches("\\d{10}");
    }

    // Kiểm tra mật khẩu có ít nhất 1 chữ cái viết hoa và 1 số
    public boolean isValidPassword(String password) {
        return password != null && password.matches("^(?=.*[A-Z])(?=.*\\d).+$");
    }

    public List<Account> getAllAccount() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM Account";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account p = new Account(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getInt(12));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public void editAccountAdmin(String firstName, String lastName, int gender, String email, String mobile, String address, int roleId, String avatar, int status, int userId) {
        String sql = "UPDATE Account SET "
                + "firstName = ?, "
                + "lastName = ?, "
                + "gender = ?, "
                + "email = ?, "
                + "mobile = ?, "
                + "address = ?, "
                + "roleId = ?, "
                + "avatar = ?, "
                + "status = ? "
                + "WHERE userId = ?"; // Ensure this matches your database schema

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, firstName);
            st.setString(2, lastName);
            st.setInt(3, gender);
            st.setString(4, email);
            st.setString(5, mobile);
            st.setString(6, address);
            st.setInt(7, roleId);
            st.setString(8, avatar);
            st.setInt(9, status);
            st.setInt(10, userId);  // Note the correct parameter count here

            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public List<Account> getAllCustommer() {
        List<Account> list = new ArrayList<>();
        String sql = "select * from Account where roleId=4";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account p = new Account(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getInt(12));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Account> sortCustomerByName(String order) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM Account WHERE roleId = 4 ORDER BY firstName " + order + ", lastName " + order;
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account p = new Account(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getInt(12));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Account> sortCustommerByEmail(String order) {
        List<Account> list = new ArrayList<>();
        String sql = "select * from Account where roleId=4 order by email " + order;
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account p = new Account(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getInt(12));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Account> sortCustommerByPhone(String order) {
        List<Account> list = new ArrayList<>();
        String sql = "select * from Account where roleId=4 order by mobile " + order;
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account p = new Account(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getInt(12));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Account> sortCustommerById(String order) {
        List<Account> list = new ArrayList<>();
        String sql = "select * from Account where roleId=4 order by userId " + order;
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account p = new Account(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getInt(12));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public Account getAccountById(int userId) {
        String sql = "SELECT * FROM Account WHERE userId=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return new Account(
                        rs.getInt(1), // userId
                        rs.getString(2), // userName
                        rs.getString(3), // password
                        rs.getString(4), // firstName
                        rs.getString(5), // lastName
                        rs.getInt(6), // gender
                        rs.getString(7), // email
                        rs.getString(8), // mobile
                        rs.getString(9), // address
                        rs.getInt(10), // roleId
                        rs.getString(11), // avatar
                        rs.getInt(12), // status
                        rs.getBigDecimal(13) // money
                );
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Account> getAllAuthor() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT DISTINCT a.userId, a.userName, a.password, a.firstName, "
                + "a.lastName, a.gender, a.email, a.mobile, a.address, a.roleId, a.avatar, a.status "
                + "FROM Account a "
                + "JOIN Blog b ON a.userId = b.userId;";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account p = new Account(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getInt(12));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public Account checkAccountExits(String email) {
        String sql = "SELECT * FROM Account WHERE email = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String username = rs.getString(2);
                String password = rs.getString(3);
                String firstName = rs.getString(4);
                String lastName = rs.getString(5);
                int gender = rs.getInt(6);
                String mobile = rs.getString(7);
                String address = rs.getString(8);
                int roleId = rs.getInt(9);
                String avatar = rs.getString(11);
                return new Account(roleId, username, password, firstName, lastName, gender, email, mobile, address, roleId, avatar);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public void editAccount(String userName, String password, String firstName, String lastName, int gender, String email, String mobile, String address, int roleId, String avatar, int status, int userId) {
        String sql = "UPDATE Account SET "
                + "userName = ?, "
                + "password = ?, "
                + "firstName = ?, "
                + "lastName = ?, "
                + "gender = ?, "
                + "email = ?, "
                + "mobile = ?, "
                + "address = ?, "
                + "roleId = ?, "
                + "avatar = ?, "
                + "status = ? "
                + "WHERE userId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, userName);
            st.setString(2, password);
            st.setString(3, firstName);
            st.setString(4, lastName);
            st.setInt(5, gender);
            st.setString(6, email);
            st.setString(7, mobile);
            st.setString(8, address);
            st.setInt(9, roleId);
            st.setString(10, avatar);
            st.setInt(11, status);
            st.setInt(12, userId);

            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public List<Account> searchCustomers(String search) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM Account \n"
                + "WHERE roleId = 4 \n"
                + "  AND (firstName LIKE ? \n"
                + "       OR lastName LIKE ? \n"
                + "       OR email LIKE ? \n"
                + "       OR mobile LIKE ?);";
        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {
            String searchPattern = "%" + search + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern); // Thêm điều kiện tìm kiếm cho cột mobile
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Account p = new Account(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getInt(12));
                list.add(p);
            }
            rs.close(); // Đảm bảo đóng ResultSet
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Account> getCustomersByStatus(int status) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM Account WHERE status LIKE ? and roleId=4";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, status);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Account p = new Account(rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getInt(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(9),
                            rs.getInt(10),
                            rs.getString(11),
                            rs.getInt(12));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Account> getCustomerListByPage(List<Account> accounts, int start, int end) {
        ArrayList<Account> arr = new ArrayList<>();
        for (int i = start; i < end; i++) {
            arr.add(accounts.get(i));
        }
        return arr;
    }

    // Manhhuy check tk mk nguoi 
    public boolean checkCurrentPassword(String username, String currentPassword) {
        LoginDAO dao = new LoginDAO();

        // Mã hóa mật khẩu trước khi kiểm tra trong database
        String hashedPassword = HashPassword.toSHA1(currentPassword);

        // Gọi phương thức getUsernameAndPassword để kiểm tra thông tin tài khoản
        Account account = dao.getUsernameAndPassword(username, hashedPassword);

        // Nếu account không null, nghĩa là thông tin hợp lệ
        return account != null;
    }

    public List<Account> getAccountByRole() {

        List<Account> list = new ArrayList<>();

        String sql = "select * from Account where roleId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, "3");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getInt(12));

                list.add(account);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return list;
    }

    public int getCustomerCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM account where roleId=4";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1); // Lấy giá trị của cột đầu tiên
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public List<Account> getAccountByRoleId(String roleId) {

        List<Account> list = new ArrayList<>();

        String sql = "select * from Account where roleId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, roleId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getInt(12));

                list.add(account);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return list;
    }

    public int getStaffCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM account where roleId in (2,3,5)";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1); // Lấy giá trị của cột đầu tiên
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public List<Account> getStaff() {
        List<Account> list = new ArrayList<>();
        String sql = "select * from Account where roleId in (2,3,5)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getInt(12));

                list.add(account);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Account> getTop5Customer() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT a.* FROM Account a \n"
                + "JOIN (SELECT userId, SUM(totalPrice) as totalSpent \n"
                + "FROM `order`\n"
                + "GROUP BY userId \n"
                + "ORDER BY totalSpent DESC\n"
                + "LIMIT 5) AS topCustomers \n"
                + "ON a.userId = topCustomers.userId \n"
                + "WHERE a.roleId = 4";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10),
                        rs.getString(11),
                        rs.getInt(12));

                list.add(account);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public void payback(int userId, BigDecimal totalPrice) {
        String sql = "UPDATE `account` SET money = money + ? WHERE userId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Thiết lập tham số cho PreparedStatement
            statement.setBigDecimal(1, totalPrice); // Chuyển đổi giá trị thành BigDecimal
            statement.setInt(2, userId); // Thiết lập userId
            // Thực thi câu lệnh cập nhật
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cập nhật tiền thành công!");
            } else {
                System.out.println("Không tìm thấy người dùng.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public Map<String, Integer> getCustomersByGender() {
//        Map<String, Integer> genderCounts = new HashMap<>();
//        
//        String sql = "SELECT a.gender, COUNT(DISTINCT a.userId) AS totalCustomers "
//                     + "FROM account a "
//                     + "JOIN `order` o ON a.userId = o.userId "
//                     + "GROUP BY a.gender";
//        
//        try{ PreparedStatement st = connection.prepareStatement(sql);
//            ResultSet rs = st.executeQuery();
//
//            while (rs.next()) {
//                int gender = rs.getInt("gender"); // Get gender (0, 1, 2)
//                int totalCustomers = rs.getInt("totalCustomers");
//
//                // Map gender values to readable names
//                if (gender == 0) {
//                    genderCounts.put("male", totalCustomers);
//                } else if (gender == 1) {
//                    genderCounts.put("female", totalCustomers);
//                } else if (gender == 2) {
//                    genderCounts.put("other", totalCustomers);
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return genderCounts;
//    }
//    
    public Map<String, Double> getCustomersByGender() {
        Map<String, Double> genderPercentages = new HashMap<>();
        int totalCustomers = 0;

        String sql = "SELECT a.gender, COUNT(DISTINCT a.userId) AS totalCustomers "
                + "FROM account a "
                + "JOIN `order` o ON a.userId = o.userId "
                + "GROUP BY a.gender";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            // Get the gender counts
            while (rs.next()) {
                int gender = rs.getInt("gender"); // Get gender (0, 1, 2)
                int count = rs.getInt("totalCustomers");
                totalCustomers += count;

                // Map gender values to readable names
                if (gender == 0) {
                    genderPercentages.put("male", (double) count);
                } else if (gender == 1) {
                    genderPercentages.put("female", (double) count);
                } else if (gender == 2) {
                    genderPercentages.put("other", (double) count);
                }
            }

            // Calculate percentage for each gender
            if (totalCustomers > 0) {
                genderPercentages.put("male", (genderPercentages.get("male") / totalCustomers) * 100);
                genderPercentages.put("female", (genderPercentages.get("female") / totalCustomers) * 100);
                genderPercentages.put("other", (genderPercentages.get("other") / totalCustomers) * 100);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return genderPercentages;
    }

    public static void main(String[] args) {
        AccountDAO add = new AccountDAO();
        List<Account> acc = add.getAccountByRole();
        System.out.println(acc);

        int customerCount = add.getCustomerCount();
        System.out.println("Số lượng customer: " + customerCount);
        String email = "lanphhe181690@fpt.edu.vn";  // Test email
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        // Validate email using the regex
        if (email.matches(emailRegex)) {
            System.out.println("Valid email: " + email);
        } else {
            System.out.println("Invalid email: " + email);
        }
    }
}
