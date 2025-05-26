/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.AddressUser;

/**
 *
 * @author hiule
 */
public class CheckOutDAO extends DBContext{
     public List<AddressUser> getAddressByUserId(int userId) {
        List<AddressUser> addressList = new ArrayList<>();
        String sql = "SELECT * FROM addressUser WHERE userId = ?";

        try (
            PreparedStatement st = connection.prepareStatement(sql)) {
            
            st.setInt(1, userId);
              ResultSet rs = st.executeQuery();

            while (rs.next()) {
                AddressUser addressUser = new AddressUser();
                addressUser.setAddressId(rs.getInt("addressId"));
                addressUser.setUserId(rs.getInt("userId"));
                addressUser.setAddress(rs.getString("address"));
                addressUser.setName(rs.getString("name"));
                addressUser.setEmail(rs.getString("email"));
                addressUser.setPhone(rs.getString("phone"));
                
                addressList.add(addressUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addressList;
    }
       public AddressUser getAddressById(int addressId) {
        AddressUser addressUser = null;
        String sql = "SELECT * FROM addressUser WHERE addressId = ?";

        try (
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, addressId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    addressUser = new AddressUser();
                    addressUser.setUserId(rs.getInt("userId"));
                    addressUser.setAddressId(rs.getInt("addressId"));
                    addressUser.setAddress(rs.getString("address"));
                    addressUser.setName(rs.getString("name"));
                    addressUser.setEmail(rs.getString("email"));
                    addressUser.setPhone(rs.getString("phone"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // You can log the exception or rethrow it based on your application's needs
        }

        return addressUser;
    }
       
       public boolean insertAddress(int userId, String address, String name, String email, String phone) {
    String sql = "INSERT INTO addressUser (userId, address, name, email, phone) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, userId);
        ps.setString(2, address);
        ps.setString(3, name);
        ps.setString(4, email);
        ps.setString(5, phone);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0; // Return true if at least one row was inserted
    } catch (SQLException e) {
        e.printStackTrace(); // Consider logging the exception instead
        return false;
    }
}
 public boolean updateAddress(int addressId, String name, String email, String phone, String address) {
        String sql = "UPDATE addressUser SET name = ?, email = ?, phone = ?, address = ? WHERE addressId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setInt(5, addressId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Returns true if at least one row was updated
        } catch (SQLException e) {
            e.printStackTrace(); // Consider logging the exception instead
            return false;
        }
    }
  public static void main(String[] args) {
        CheckOutDAO checkOutDAO = new CheckOutDAO();
        
        int userId = 1; // Replace with a valid user ID
        double amountToAdd = 100.00; // Amount to add to the user's balance

        // Call addMoneyToBalance and print the result
        boolean isUpdated = checkOutDAO.addMoneyToBalance(userId, amountToAdd);
        
        if (isUpdated) {
            System.out.println("Balance updated successfully for user ID: " + userId);
            System.out.println("New Balance: " + checkOutDAO.getMoneyByUserId(userId));
        } else {
            System.out.println("Failed to update balance for user ID: " + userId);
        }
    }

  public double getMoneyByUserId(int userId) {
    String sql = "SELECT money FROM account WHERE userId = ?";
    double money = -1.0; // Default to -1.0 if no money is found

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, userId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                money = rs.getDouble("money");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // You could also log the exception instead of printing it
    }

    return money;
}

public boolean updateMoneyAfterPurchase(int userId, double totalPrice) {
    String sql = "UPDATE account SET money = money - ? WHERE userId = ? AND money >= ?";
    boolean success = false;

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setDouble(1, totalPrice);
        ps.setInt(2, userId);
        ps.setDouble(3, totalPrice); // Ensures money does not go negative

        int affectedRows = ps.executeUpdate();
        success = affectedRows > 0; // If rows were affected, the update was successful
    } catch (SQLException e) {
        e.printStackTrace(); // You could also log the exception instead of printing it
    }

    return success;
}
public boolean addMoneyToBalance(int userId, double amountToAdd) {
    String selectSql = "SELECT money FROM account WHERE userId = ?";
    String updateSql = "UPDATE account SET money = ? WHERE userId = ?";

    try (
        PreparedStatement selectPs = connection.prepareStatement(selectSql);
        PreparedStatement updatePs = connection.prepareStatement(updateSql)
    ) {
        // Step 1: Retrieve current balance
        selectPs.setInt(1, userId);
        double currentBalance = 0.0;

        try (ResultSet rs = selectPs.executeQuery()) {
            if (rs.next()) {
                currentBalance = rs.getDouble("money");
            } else {
                System.out.println("User not found with ID: " + userId);
                return false;
            }
        }

        // Step 2: Calculate new balance
        double newBalance = currentBalance + amountToAdd;

        // Step 3: Update balance in the database
        updatePs.setDouble(1, newBalance);
        updatePs.setInt(2, userId);

        int rowsUpdated = updatePs.executeUpdate();
        return rowsUpdated > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


}
