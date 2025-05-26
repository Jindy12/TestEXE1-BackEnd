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
import model.Order;
import model.OrderDetail;

/**
 *
 * @author tuana
 */
public class OrderDetailDAO extends DBContext {

//    public List<OrderDetail> getAllOrderDetail(int userId) {
//        List<OrderDetail> orderDetails = new ArrayList<>();
//        String sql = "SELECT \n"
//                + "    od.quantity,\n"
//                + "    o.shippingAddress,\n"
//                + "    p.name AS productName,\n"
//                + "    os.description,\n"
//                + "    o.createDate,\n"
//                + "    b.name AS brandName,\n"
//                + "    c.colorName,\n"
//                + "    p.price,\n"
//                + "    IFNULL(d.discount, 0) AS discount,\n"
//                + "    pi.imageUrl\n"
//                + "FROM \n"
//                + "    orderdetail od\n"
//                + "JOIN \n"
//                + "    `order` o ON od.orderId = o.orderId\n"
//                + "JOIN \n"
//                + "    OrderStatus os ON o.orderStatus = os.statusId\n"
//                + "JOIN \n"
//                + "    product p ON od.productId = p.productId\n"
//                + "JOIN \n"
//                + "    brand b ON p.brandId = b.brandId\n"
//                + "JOIN \n"
//                + "    colorofcar coc ON od.productId = coc.productId AND od.colorId = coc.colorId\n"
//                + "JOIN \n"
//                + "    color c ON coc.colorId = c.colorId\n"
//                + "LEFT JOIN \n"
//                + "    discount d ON od.discountId = d.discountId AND od.productId = d.productId\n"
//                + "LEFT JOIN \n"
//                + "    (\n"
//                + "        SELECT productId, imageUrl\n"
//                + "        FROM productimage\n"
//                + "        WHERE (productId, imageId) IN (\n"
//                + "            SELECT productId, MIN(imageId)\n"
//                + "            FROM productimage\n"
//                + "            GROUP BY productId\n"
//                + "        )\n"
//                + "    ) pi ON p.productId = pi.productId\n"
//                + "WHERE \n"
//                + "    o.userId = ?\n"
//                + "LIMIT 0, 1000;";
//
//        try (PreparedStatement st = connection.prepareStatement(sql)) {
//            st.setInt(1, userId); // Thiết lập userId
//            try (ResultSet rs = st.executeQuery()) { // Đưa ResultSet vào try-with-resources
//                while (rs.next()) {
//                    OrderDetail o = new OrderDetail(
//                            rs.getInt("quantity"),
//                            rs.getString("shippingAddress"),
//                            rs.getString("productName"),
//                            rs.getString("description"),
//                            rs.getString("createDate"),
//                            rs.getString("brandName"),
//                            rs.getString("colorName"),
//                            rs.getDouble("price"),
//                            rs.getDouble("discount"),
//                            rs.getString("imageUrl")
//                    );
//                    orderDetails.add(o);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // In lỗi ra console
//        }
//        return orderDetails; // Trả về danh sách OrderDetail
//    }
    ////Sơn
    public List<OrderDetail> getAllOrderList() {
        List<OrderDetail> list = new ArrayList<>();

        String sql = "SELECT acc.firstName, \n"
                + "       acc.lastName, \n"
                + "       orr.orderId, \n"
                + "       orr.createDate, \n"
                + "       orr.totalPrice, \n"
                + "       orr.orderStatus,\n"
                + "       SUBSTRING_INDEX(GROUP_CONCAT(p.name ORDER BY od.orderId SEPARATOR ', '), ', ', 1) AS firstProductName,\n"
                + "       COUNT(od.productId) - 1 AS additionalProductCount\n"
                + "FROM carpipi.order orr\n"
                + "JOIN carpipi.account acc ON orr.userId = acc.userId\n"
                + "JOIN carpipi.orderdetail od ON orr.orderId = od.orderId\n"
                + "JOIN carpipi.product p ON od.productId = p.productId\n"
                + "GROUP BY orr.orderId";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                OrderDetail o = new OrderDetail();
                o.setOrderId(rs.getInt("orderId"));
                o.setUserId(rs.getInt("userId"));
                o.setCreateDate(rs.getDate("createDate"));
                o.setFirstName(rs.getString("firstName"));
                o.setLastName(rs.getString("lastName"));
                o.setTotalPrice(rs.getDouble("totalPrice"));
                o.setOrderStatus(rs.getInt("orderStatus"));
                o.setProductId(rs.getString("productId"));
                o.setProductName(rs.getString("firstProductName"));
                o.setQuantity(rs.getInt("additionalProductCount"));

                list.add(o);

            }

        } catch (SQLException e) {
        }

        return list;

    }
    /// Tuan-----------------------------------------------------------------------------------------------------//

    public List<OrderDetail> getOrderDetail(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT \n"
                + "    od.orderDetailId, \n"
                + "    od.orderId, \n"
                + "    od.productId, \n"
                + "    od.quantity, \n"
                + "    od.colorId, \n"
                + "    od.isfeedback, \n"
                + "    p.name AS productName, \n"
                + "    os.description, \n"
                + "    o.createDate, \n"
                + "    b.name AS brandName, \n"
                + "    c.colorName, \n"
                + "    p.price, \n"
                + "    pi.imageUrl \n"
                + "FROM \n"
                + "    orderdetail od \n"
                + "JOIN \n"
                + "    `order` o ON od.orderId = o.orderId \n"
                + "JOIN \n"
                + "    OrderStatus os ON o.orderStatus = os.statusId \n"
                + "JOIN \n"
                + "    product p ON od.productId = p.productId \n"
                + "JOIN \n"
                + "    brand b ON p.brandId = b.brandId \n"
                + "JOIN \n"
                + "    colorofcar coc ON od.productId = coc.productId AND od.colorId = coc.colorId \n"
                + "JOIN \n"
                + "    color c ON coc.colorId = c.colorId \n"
                + "LEFT JOIN \n"
                + "    ( \n"
                + "        SELECT productId, imageUrl \n"
                + "        FROM productimage \n"
                + "        WHERE (productId, imageId) IN ( \n"
                + "            SELECT productId, MIN(imageId) \n"
                + "            FROM productimage \n"
                + "            GROUP BY productId \n"
                + "        ) \n"
                + "    ) pi ON p.productId = pi.productId \n"
                + "WHERE \n"
                + "    o.orderId = ?\n"
                + "ORDER BY o.createDate DESC ";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, orderId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    OrderDetail orderDetail = new OrderDetail(
                            rs.getInt("orderId"),
                            rs.getDate("createDate"), // Change to getDate
                            rs.getString("productId"),
                            rs.getString("productName"),
                            rs.getInt("quantity"),
                            rs.getString("brandName"),
                            rs.getString("colorName"),
                            rs.getString("imageUrl"),
                            rs.getInt("colorId"),
                            rs.getInt("isfeedback"),
                            rs.getInt("orderDetailId"),
                            rs.getDouble("price"),
                            rs.getString("description")
                    );
                    orderDetails.add(orderDetail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }

    public List<Order> getOrderByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * "
                + "FROM `order` "
                + "WHERE userId = ? "
                + "ORDER BY createDate desc, orderStatus ASC ";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = new Order(
                            resultSet.getInt("orderId"),
                            resultSet.getString("orderDeliverCode"),
                            resultSet.getInt("userId"),
                            resultSet.getString("orderName"),
                            resultSet.getString("orderEmail"), // Corrected typo
                            resultSet.getString("orderPhone"),
                            resultSet.getBigDecimal("totalPrice"), // Use getBigDecimal
                            resultSet.getString("note"),
                            resultSet.getInt("saleId"),
                            resultSet.getInt("shipperId"),
                            resultSet.getDate("createDate"), // Use getDate
                            resultSet.getString("shippingAddress"), // Ensure shippingAddress is retrieved
                            resultSet.getInt("orderStatus"),
                            resultSet.getInt("payMethod")
                    );
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<Order> getOrderByStatus(int userId, int status) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT *"
                + "FROM `order` "
                + "WHERE userId = ? and orderStatus = ? "
                + "ORDER BY createDate desc";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, status);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = new Order(
                            resultSet.getInt("orderId"),
                            resultSet.getString("orderDeliverCode"),
                            resultSet.getInt("userId"),
                            resultSet.getString("orderName"),
                            resultSet.getString("orderEmail"), // Corrected typo
                            resultSet.getString("orderPhone"),
                            resultSet.getBigDecimal("totalPrice"), // Use getBigDecimal
                            resultSet.getString("note"),
                            resultSet.getInt("saleId"),
                            resultSet.getInt("shipperId"),
                            resultSet.getDate("createDate"), // Use getDate
                            resultSet.getString("shippingAddress"), // Ensure shippingAddress is retrieved
                            resultSet.getInt("orderStatus"),
                            resultSet.getInt("payMethod")
                    );
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<OrderDetail> getOrderDetailByStatus(int userId, int statusId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT \n"
                + "    od.orderDetailId, \n"
                + "    od.orderId, \n"
                + "    od.productId, \n"
                + "    od.quantity, \n"
                + "    od.colorId, \n"
                + "    od.isfeedback, \n"
                + "    p.name AS productName, \n"
                + "    os.description, \n"
                + "    o.createDate, \n"
                + "    b.name AS brandName, \n"
                + "    c.colorName, \n"
                + "    p.price, \n"
                + "    pi.imageUrl \n"
                + "FROM \n"
                + "    orderdetail od \n"
                + "JOIN \n"
                + "    `order` o ON od.orderId = o.orderId \n"
                + "JOIN \n"
                + "    OrderStatus os ON o.orderStatus = os.statusId \n"
                + "JOIN \n"
                + "    product p ON od.productId = p.productId \n"
                + "JOIN \n"
                + "    brand b ON p.brandId = b.brandId \n"
                + "JOIN \n"
                + "    colorofcar coc ON od.productId = coc.productId AND od.colorId = coc.colorId \n"
                + "JOIN \n"
                + "    color c ON coc.colorId = c.colorId \n"
                + "LEFT JOIN \n"
                + "    ( \n"
                + "        SELECT productId, imageUrl \n"
                + "        FROM productimage \n"
                + "        WHERE (productId, imageId) IN ( \n"
                + "            SELECT productId, MIN(imageId) \n"
                + "            FROM productimage \n"
                + "            GROUP BY productId \n"
                + "        ) \n"
                + "    ) pi ON p.productId = pi.productId \n"
                + "WHERE o.orderId = ? and od.orderStatus= ?\n"
                + "ORDER BY o.createDate DESC ";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            st.setInt(2, statusId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    OrderDetail orderDetail = new OrderDetail(
                            rs.getInt("orderId"),
                            rs.getDate("createDate"), // Change to getDate
                            rs.getString("productId"),
                            rs.getString("productName"),
                            rs.getInt("quantity"),
                            rs.getString("brandName"),
                            rs.getString("colorName"),
                            rs.getString("imageUrl"),
                            rs.getInt("colorId"),
                            rs.getInt("isfeedback"),
                            rs.getInt("orderDetailId"),
                            rs.getDouble("price"), // Assuming this is productPrice
                            rs.getString("description") // Assuming this is orderStatusDescription
                    );
                    orderDetails.add(orderDetail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }

    public List<Order> getShipOrder() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * "
                + "FROM `order` "
                + "WHERE orderStatus IN (2,3,4) "
                + "ORDER BY createDate desc";

        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {  // Chỉ cần gọi executeQuery() mà không cần tham số query
            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("orderId"),
                        resultSet.getString("orderDeliverCode"),
                        resultSet.getInt("userId"),
                        resultSet.getString("orderName"),
                        resultSet.getString("orderEmail"),
                        resultSet.getString("orderPhone"),
                        resultSet.getBigDecimal("totalPrice"),
                        resultSet.getString("note"),
                        resultSet.getInt("saleId"),
                        resultSet.getInt("shipperId"),
                        resultSet.getTimestamp("createDate"), // Dùng getTimestamp() để lấy cả ngày và giờ
                        resultSet.getString("shippingAddress"),
                        resultSet.getInt("orderStatus"),
                        resultSet.getInt("payMethod")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving ship orders: " + e.getMessage());
        }
        return orders;
    }

    public List<Order> getShipOrderByStatus(int statusId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * "
                + "FROM `order` "
                + "WHERE orderStatus = ? "
                + "ORDER BY createDate DESC";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, statusId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = new Order(
                            resultSet.getInt("orderId"),
                            resultSet.getString("orderDeliverCode"),
                            resultSet.getInt("userId"),
                            resultSet.getString("orderName"),
                            resultSet.getString("orderEmail"), // Corrected typo
                            resultSet.getString("orderPhone"),
                            resultSet.getBigDecimal("totalPrice"), // Use getBigDecimal
                            resultSet.getString("note"),
                            resultSet.getInt("saleId"),
                            resultSet.getInt("shipperId"),
                            resultSet.getDate("createDate"), // Use getDate
                            resultSet.getString("shippingAddress"), // Ensure shippingAddress is retrieved
                            resultSet.getInt("orderStatus"),
                            resultSet.getInt("payMethod")
                    );
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void updateFeedbackOrder(int id) {
        String sql = "UPDATE orderdetail SET isfeedback = 1 WHERE orderDetailId = ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);  // Thiết lập giá trị orderDetailId
            st.executeUpdate();  // Thực thi truy vấn
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOrderStatus(int orderId, int orderStatus) {
        String query = "UPDATE `order` SET orderStatus = ? WHERE orderId = ?;";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, orderStatus);
            st.setInt(2, orderId);
            st.executeUpdate(); // Gọi phương thức này để thực hiện cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getSaleCheckOrder() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM `order` ORDER BY createDate DESC, orderStatus ASC ";

        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("orderId"),
                        resultSet.getString("orderDeliverCode"),
                        resultSet.getInt("userId"),
                        resultSet.getString("orderName"),
                        resultSet.getString("orderEmail"), // Corrected typo
                        resultSet.getString("orderPhone"),
                        resultSet.getBigDecimal("totalPrice"), // Use getBigDecimal
                        resultSet.getString("note"),
                        resultSet.getInt("saleId"),
                        resultSet.getInt("shipperId"),
                        resultSet.getDate("createDate"), // Use getDate
                        resultSet.getString("shippingAddress"), // Ensure shippingAddress is retrieved
                        resultSet.getInt("orderStatus"),
                        resultSet.getInt("payMethod")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving ship orders: " + e.getMessage()); // Consider using logging
        }
        return orders;
    }

    public List<Order> getSaleOrderByStatus(int statusId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * "
                + "FROM `order` "
                + "WHERE orderStatus = ? "
                + "ORDER BY createDate DESC";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, statusId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = new Order(
                            resultSet.getInt("orderId"),
                            resultSet.getString("orderDeliverCode"),
                            resultSet.getInt("userId"),
                            resultSet.getString("orderName"),
                            resultSet.getString("orderEmail"), // Corrected typo
                            resultSet.getString("orderPhone"),
                            resultSet.getBigDecimal("totalPrice"), // Use getBigDecimal
                            resultSet.getString("note"),
                            resultSet.getInt("saleId"),
                            resultSet.getInt("shipperId"),
                            resultSet.getDate("createDate"), // Use getDate
                            resultSet.getString("shippingAddress"), // Ensure shippingAddress is retrieved
                            resultSet.getInt("orderStatus"),
                            resultSet.getInt("payMethod")
                    );
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<Order> getMyOrderListByPage(List<Order> order, int start, int end) {
        ArrayList<Order> arr = new ArrayList<>();
        for (int i = start; i < end; i++) {
            arr.add(order.get(i));
        }
        return arr;
    }
/// Tuan-----------------------------------------------------------------------------------------------------//

    public static void main(String[] args) {
        OrderDetailDAO oddao = new OrderDetailDAO();

        // Assuming getAllOrderDetail is defined correctly and returns List<OrderDetail>
        List<Order> orderDetails = oddao.getOrderByStatus(6, 4);
        //List<Order> orderDetails2 = oddao.getShipOrderByStatus(4);
//        for (Order order : orderDetails2) {
//            System.out.println("Order Detail ID: " + order.getOrderId());
//            System.out.println("Quantity: " + order.getOrderName());
//        }
        List<OrderDetail> orderDetails3 = oddao.getOrderDetail(1);

        for (OrderDetail orderDetail : orderDetails3) {
            System.out.println(orderDetail.getProductId());
        }
        // Check if the list is not null or empty
        if (orderDetails != null && !orderDetails.isEmpty()) {
            for (Order orderDetail : orderDetails) {
                // Adjust the method call to match the actual methods in OrderDetail
                System.out.println("Order Detail ID: " + orderDetail.getOrderId());
                System.out.println("Quantity: " + orderDetail.getOrderName());
                // Add any additional fields you want to print
            }
        } else {
            System.out.println("No order details found.");
        }
    }

}
