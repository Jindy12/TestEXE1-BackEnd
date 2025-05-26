/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import model.Account;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 *
 * @author hiule
 */
public class AdminDao extends DBContext {

    
    // Method to update roleId for a specific user
// Method to search accounts by keyword
    public List<Account> searchAccounts(String keyword) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE userName LIKE ? OR firstName LIKE ? OR lastName LIKE ? OR email LIKE ? "
                + "OR mobile LIKE ? OR address LIKE ? OR gender LIKE ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            ps.setString(5, searchPattern);
            ps.setString(6, searchPattern);
            ps.setString(7, searchPattern); // Gender search, though gender may not be suitable for LIKE

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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
                // Add other fields as needed
                accounts.add(account);
            }
        } catch (SQLException e) {
            // Log the exception (you may want to use a logging framework)
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return accounts;
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
            stm.setInt(9, acc.getRoleId()); // Role mặc định - customer
            stm.setString(10, acc.getAvatar()); // Avatar mặc định
            stm.setInt(11, 1); // Trạng thái 'pending'

            stm.executeUpdate();
            System.out.println("Tài khoản đã được thêm vào danh sách  thành công!");
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm tài khoản vào danh sách " + e.getMessage());
        }
    }

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

    public boolean updateRoleId(int userId, int roleId) {
        String sql = "UPDATE account SET roleId = ? WHERE userId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            // Set the parameters for the prepared statement
            ps.setInt(1, roleId);
            ps.setInt(2, userId);

            // Execute the update
            int affectedRows = ps.executeUpdate();

            // Check if the update was successful
            return affectedRows > 0;
        } catch (SQLException e) {
            // Log the exception (you may want to use a logging framework)
            System.err.println("SQL Exception: " + e.getMessage());
            return false;
        }
    }

    public List<Account> getAccountsWithPaging(int page, int PAGE_SIZE) {
        List<Account> list = new ArrayList<>();
        try {
            // Update the SQL query for MySQL pagination
            String sql = "SELECT * FROM account ORDER BY userId LIMIT ? OFFSET ?";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, PAGE_SIZE); // Number of records to fetch
            stm.setInt(2, (page - 1) * PAGE_SIZE); // Calculate the offset

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
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
                // Add other fields as needed
                list.add(account);
            }
        } catch (Exception ex) {
            Logger.getLogger(AdminDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    // Other existing methods...

    // Method to count the total number of accounts
    public int countAccounts() {
        int count = 0;
        String sql = "SELECT COUNT(*) AS total FROM account";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            // Log the exception (you may want to use a logging framework)
            System.err.println("SQL Exception: " + e.getMessage());
        }
        return count;
    }

    // Main method for testing
    public static void main(String[] args) {
        AdminDao adminDao = new AdminDao();

        // Test searchAccounts
        String keyword = "cu"; // Example keyword to search for
        List<Account> accounts = adminDao.searchAccounts(keyword);

        // Output the search results
        if (accounts.isEmpty()) {
            System.out.println("No accounts found for keyword: " + keyword);
        } else {
            System.out.println("Accounts found for keyword '" + keyword + "':");
            for (Account account : accounts) {
                System.out.println("UserId: " + account.getUserId() + ", UserName: " + account.getUserName() + ", Email: " + account.getEmail());
            }
        }

    }

}
