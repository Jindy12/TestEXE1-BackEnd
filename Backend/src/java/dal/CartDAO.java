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
import model.Cart;
import model.Product;
import dal.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import model.Account;
import model.Color;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author hiule
 */
public class CartDAO extends DBContext {

    public List<Integer> getColorIdsOfCar(String productId) {
        List<Integer> colorIds = new ArrayList<>();
        String sql = "SELECT c.colorId "
                + "FROM color c "
                + "JOIN colorofcar coc ON c.colorId = coc.colorId "
                + "WHERE coc.productId = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, productId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int colorId = rs.getInt("colorId");
                colorIds.add(colorId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return colorIds;
    }

    public Color getColorById(int colorId) {
        String sql = "SELECT colorId, colorName FROM color WHERE colorId = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, colorId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new Color(rs.getInt("colorId"), rs.getString("colorName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getQuantityByUserIdAndProductId(int userId, String productId) {
        String sql = "SELECT quantity FROM cart WHERE userId = ? AND productId = ? AND isDeleted = 0";
        int quantity = -1; // Default to -1 if no cart item is found

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    quantity = rs.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // You could also log the exception instead of printing it
        }

        return quantity;
    }

    public int getCartIdByUserIdAndProductId(int userId, String productId) throws SQLException {
        String sql = "SELECT cartId FROM cart WHERE userId = ? AND productId = ? AND isDeleted = 0";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("cartId");
            }
            return -1; // Không tìm thấy cart
        }
    }

    public boolean updateQuantityByCartId(int cartId, int quantity) throws SQLException {
        String sql = "UPDATE cart SET quantity = quantity + ? WHERE cartId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, quantity); // Cộng thêm số lượng vào cart
            ps.setInt(2, cartId);
            return ps.executeUpdate() > 0; // Trả về true nếu cập nhật thành công
        }
    }

    public boolean addToCart(int userId, String productId, int quantity, int colorId) throws SQLException {

        // Nếu cart không tồn tại, thực hiện insert mới
        String sql = "INSERT INTO cart (userId, productId, quantity,colorId) VALUES (?, ?, ?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, productId);
            ps.setInt(3, quantity);
            ps.setInt(4, colorId);
            return ps.executeUpdate() > 0; // Trả về true nếu insert thành công

        }
    }

    public Integer getCartIdByUserId(int userId) {
        String sql = "SELECT cartId FROM cart WHERE userId = ? LIMIT 1"; // Adjust based on your SQL dialect

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("cartId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // or throw an exception if no cart is found
    }

    public boolean deleteCartItem(int cartId) throws SQLException {
        String sql = "UPDATE cart SET isDeleted = 1 WHERE cartId = ? ";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cartId);

            return ps.executeUpdate() > 0; // Trả về true nếu cập nhật thành công
        }
    }

    public boolean deleteCar(int cartId) {
        String sql = "UPDATE cart SET isDeleted = 1 WHERE cartId = ? and isSelect = 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0; // Return true if update is successful
        } catch (SQLException e) {
            System.err.println("Error deleting cart with ID " + cartId + ": " + e.getMessage());
            return false;
        }
    }

    // Lấy danh sách sản phẩm từ giỏ hàng của người dùng
    public List<Cart> getCartsByUserId(int userId) {
        List<Cart> cartList = new ArrayList<>();
        String sql = "SELECT * FROM cart WHERE userId = ? AND isDeleted = 0 ";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Assuming your Cart class has a constructor that takes these parameters
                int cartId = rs.getInt("cartId");
                String productId = rs.getString("productId");
                int quantity = rs.getInt("quantity");
                int isSelect = rs.getInt("isSelect");
                int colorId = rs.getInt("colorId");
                // You may need to fetch product details based on productId
                Product product = new ProductDAO().getProductById(productId);  // Implement this method as needed

                Cart cart = new Cart(cartId, userId, product, quantity, isSelect, colorId);
                cartList.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        return cartList;
    }

    public List<Cart> getCartsSelectByUserId(int userId) {
        List<Cart> cartList = new ArrayList<>();
        String sql = "SELECT * FROM cart WHERE userId = ? AND isDeleted = 0 and isSelect =1 ";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Assuming your Cart class has a constructor that takes these parameters
                int cartId = rs.getInt("cartId");
                String productId = rs.getString("productId");
                int quantity = rs.getInt("quantity");
                int isSelect = rs.getInt("isSelect");
                int colorId = rs.getInt("colorId");
                // You may need to fetch product details based on productId
                Product product = new ProductDAO().getProductById(productId);  // Implement this method as needed

                Cart cart = new Cart(cartId, userId, product, quantity, isSelect, colorId);
                cartList.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        return cartList;
    }

    public Integer getStockByCartId(int cartId) {
        String sql = "SELECT quantity FROM cart WHERE cartId = ? AND isDeleted = 0";
        Integer quantity = null; // Use Integer to allow null if no record is found

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Retrieve the stock quantity
                quantity = rs.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        return quantity; // Will be null if no stock is found
    }

    public void updateCart1(int cartId, int userId, String productId, int quantity) throws SQLException {
        String sql = "UPDATE cart SET quantity = quantity + ? WHERE cartId = ? AND userId = ? AND productId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, quantity); // Increment the existing quantity
            ps.setInt(2, cartId);   // Include cartId in the update condition
            ps.setInt(3, userId);
            ps.setString(4, productId);
            ps.executeUpdate();
        }
    }

    // Phương thức cập nhật số lượng sản phẩm trong giỏ hàng
    public void updateCart2(int cartId, int userId, String productId, int quantity, int isSelect, int colorId) {
        String sql = "UPDATE cart SET quantity = ?, isSelect = ?, colorId = ? WHERE cartId = ? AND userId = ? AND productId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, quantity);   // Set the quantity directly
            ps.setInt(2, isSelect);   // Set isSelect in the SET clause
            ps.setInt(3, colorId);    // Set colorId
            ps.setInt(4, cartId);     // Include cartId in the update condition
            ps.setInt(5, userId);
            ps.setString(6, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateCartColor(int cartId, int userId, String productId, int colorId) throws SQLException {
        String sql = "UPDATE cart SET colorId = ? WHERE cartId = ? AND userId = ? AND productId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, colorId);
            ps.setInt(2, cartId);
            ps.setInt(3, userId);
            ps.setString(4, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating color: " + e.getMessage());
            throw e; // Rethrow to handle in the servlet if needed
        }
    }
// Phương thức xóa sản phẩm khỏi giỏ hàng

    public void removeFromCart(int userId, String productId) {
        String sql = "DELETE FROM cart WHERE userId = ? AND productId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error removing product from cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Cart> searchCartItems(int userId, String keyword) throws SQLException {
        List<Cart> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM cart WHERE userId = ? and isDeleted = 0 AND productId   IN (SELECT productId FROM product WHERE name LIKE ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, "%" + keyword + "%"); // Tìm kiếm với từ khóa
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int cartId = rs.getInt("cartId");
                String productId = rs.getString("productId");
                int quantity = rs.getInt("quantity");
                int isSelect = rs.getInt("isSelect");
                // Tạo Cart object và thêm vào danh sách
                Cart cartItem = new Cart(cartId, userId, new ProductDAO().getProductById(productId), quantity, isSelect);
                cartItems.add(cartItem);
            }
        }
        return cartItems;
    }

    public List<Cart> getCartsBySupplyId(int supplyId, int userId) throws SQLException {
        List<Cart> cartList = new ArrayList<>();
        String sql = "SELECT c.cartId, c.userId, c.productId, c.quantity, c.isSelect, p.name AS productName, s.supplyName "
                + "FROM carpipi.cart c "
                + "JOIN product p ON c.productId = p.productId "
                + "JOIN supply s ON p.supplyId = s.supplyId "
                + "WHERE s.supplyId = ? AND c.userId = ? AND c.isDeleted = 0";  // Added userId condition

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, supplyId); // Assign value for supplyId
            ps.setInt(2, userId);   // Assign value for userId
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int cartId = rs.getInt("cartId");
                String productId = rs.getString("productId");
                int quantity = rs.getInt("quantity");
                int isSelect = rs.getInt("isSelect");
                // Fetch product details based on productId
                Product product = new ProductDAO().getProductById(productId);  // Ensure this method is implemented

                // Create Cart object with new isSelect parameter
                Cart cart = new Cart(cartId, userId, product, quantity, isSelect);
                cartList.add(cart);
            }
        }
        return cartList;
    }

    public int countCartsByUserId(int userId) {
        int totalCarts = 0;  // Variable to hold the count
        String sql = "SELECT COUNT(*) AS totalCarts "
                + "FROM carpipi.cart "
                + "WHERE userId = ? AND isDeleted = 0";  // Ensure only active carts are counted

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);  // Set the userId parameter
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalCarts = rs.getInt("totalCarts");  // Retrieve the count
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Print the stack trace to help debug any SQL errors
        }

        return totalCarts;  // Return the total count
    }

    public List<Cart> getSelectedCarts(int userId) {
        List<Cart> selectedCarts = new ArrayList<>();
        String sql = "SELECT c.cartId, c.userId, c.productId, c.quantity, p.name AS productName, s.supplyName "
                + "FROM cart c "
                + "JOIN product p ON c.productId = p.productId "
                + "JOIN supply s ON p.supplyId = s.supplyId "
                + "WHERE c.isSelect = 1 AND c.userId = ? AND c.isDeleted = 0";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId); // Set userId parameter in the query
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int cartId = rs.getInt("cartId");
                String productId = rs.getString("productId");
                int quantity = rs.getInt("quantity");

                // Fetch product details based on productId
                Product product = new ProductDAO().getProductById(productId);
                // Create a Cart object and add it to the list
                Cart cart = new Cart(cartId, userId, product, quantity, 1);
                selectedCarts.add(cart);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving selected carts for userId: " + userId);
            e.printStackTrace(); // Log or handle exceptions as necessary
        }

        return selectedCarts;
    }

    public void updateCartSelection(int userId, String productId, int isSelect) throws SQLException {
        String sql = "UPDATE cart c "
                + "JOIN account a ON c.userId = a.userId "
                + "JOIN product p ON c.productId = p.productId "
                + "SET c.isSelect = ? "
                + "WHERE a.userId = ? AND p.productId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, isSelect);
            ps.setInt(2, userId);
            ps.setString(3, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Handle the exception (e.g., log it or rethrow)
            throw new SQLException("Error updating cart selection for userId: " + userId + " and productId: " + productId, e);
        }
    }

    public boolean updateStockByCartId(int cartId) {
        String getProductSql = "SELECT productId, quantity FROM cart WHERE cartId = ? AND isSelect = 1";
        String updateStockSql = "UPDATE product SET stock = stock - ? WHERE productId = ? AND stock >= ?"; // Ensure stock does not go negative

        try (PreparedStatement getProductStmt = connection.prepareStatement(getProductSql)) {
            getProductStmt.setInt(1, cartId);
            ResultSet rs = getProductStmt.executeQuery();

            if (rs.next()) {
                String productId = rs.getString("productId");
                int quantity = rs.getInt("quantity"); // Get the quantity from the cart

                try (PreparedStatement updateStockStmt = connection.prepareStatement(updateStockSql)) {
                    updateStockStmt.setInt(1, quantity);      // Decrement by quantity
                    updateStockStmt.setString(2, productId);
                    updateStockStmt.setInt(3, quantity);      // Ensure stock is sufficient

                    int rowsAffected = updateStockStmt.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Stock for product ID " + productId + " has been decremented by " + quantity + ".");
                        return true;
                    } else {
                        System.out.println("Failed to update stock. Product might be out of stock or does not exist.");
                    }
                }
            } else {
                System.out.println("No product found for the given cart ID.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating stock for cart ID " + cartId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public int addOrder(int userId, String orderName, String email, String phone, double totalPrice, String address, int orderStatus, int payMethod) {
        int orderId = -1; // ID of the inserted order
        LocalDateTime currentDate = java.time.LocalDateTime.now();
        String date = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String sql = "INSERT INTO `order` (`orderDeliverCode`, `userId`, `orderName`, `orderEmail`, `orderPhone`, `totalPrice`, `shippingAddress`, `orderStatus`, `createDate`, `payMethod`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            String deliverCode = "DEL" + (System.currentTimeMillis() % 1000000); // Short delivery code

            st.setString(1, deliverCode);
            st.setInt(2, userId);
            st.setString(3, orderName);
            st.setString(4, email);
            st.setString(5, phone);
            st.setDouble(6, totalPrice);
            st.setString(7, address);
            st.setInt(8, orderStatus);
            st.setString(9, date);
            st.setInt(10, payMethod); // New parameter for payment method

            st.executeUpdate();

            // Retrieve the generated order ID
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log error details
        }

        return orderId;
    }

    public void addOrderDetail(int orderId, String productId, int quantity, int colorId, int isFeedback) {
    String sql = "INSERT INTO orderdetail (orderId, productId, quantity, colorId, isfeedback) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

        preparedStatement.setInt(1, orderId);
        preparedStatement.setString(2, productId);
        preparedStatement.setInt(3, quantity);
        preparedStatement.setInt(4, colorId);
        preparedStatement.setInt(5, isFeedback);

        int rowsInserted = preparedStatement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new order detail was inserted successfully!");
        } else {
            System.out.println("Failed to insert order detail.");
        }
    } catch (SQLException e) {
        System.err.println("An error occurred while inserting order detail: " + e.getMessage());
    }
}

 public static void main(String[] args) {
        // Assuming DbContext is handling the connection and OrderDetailDAO is initialized with it
       
        CartDAO orderDetailDAO = new CartDAO();

        // Sample data to insert
        int orderId = 1;            // Assume this is a valid order ID
        String productId = "AU03";   // Assume this is a valid product ID
        int quantity = 2;
                // Assume this is a valid discount ID
        int colorId = 1;            // Assume this is a valid color ID
        int isFeedback = 0;         // Feedback status (0 = no feedback, 1 = feedback given)

        // Insert order detail
        orderDetailDAO.addOrderDetail(orderId, productId, quantity, colorId, isFeedback);
    }
 
}
