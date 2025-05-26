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
import model.OrderStatus;

/**
 *
 * @author tuana
 */
public class OrderDAO extends DBContext {

    public List<OrderStatus> getListOrderStatus() {

        List<OrderStatus> listStatus = new ArrayList<>();

        String sql = "SELECT * FROM carpipi.orderstatus";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                OrderStatus os = new OrderStatus();

                os.setStatusId(rs.getInt("statusId"));
                os.setStatusName(rs.getString("statusName"));
                os.setDescription(rs.getString("description"));

                listStatus.add(os);

            }
        } catch (Exception e) {
        }

        return listStatus;
    }

    public Order getOrderById(int orderId) {
        Order o = new Order();

        String sql = "SELECT * FROM carpipi.order WHERE orderId = ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, orderId); // Thay đổi từ setString sang setInt cho orderId
            ResultSet rs = st.executeQuery();

            if (rs.next()) { // Sử dụng if thay vì while vì chỉ có một kết quả
                o.setOrderId(rs.getInt("orderId"));
                o.setOrderDeliverCode(rs.getString("orderDeliverCode"));
                o.setUserId(rs.getObject("userId") != null ? rs.getInt("userId") : null); // Sử dụng rs.getObject() để kiểm tra null
                o.setOrderName(rs.getString("orderName"));
                o.setOrderEmail(rs.getString("orderEmail"));
                o.setOrderPhone(rs.getString("orderPhone"));
                o.setTotalPrice(rs.getBigDecimal("totalPrice")); // Sử dụng BigDecimal cho totalPrice
                o.setNote(rs.getString("note"));
                o.setSaleId(rs.getInt("saleId"));
                o.setShipperId(rs.getObject("shipperId") != null ? rs.getInt("shipperId") : null); // Tương tự cho shipperId
                o.setCreateDate(rs.getDate("createDate")); // Sử dụng rs.getDate() cho createDate
                o.setShippingAddress(rs.getString("shippingAddress"));
                o.setOrderStatus(rs.getInt("orderStatus"));
            }
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi để kiểm tra dễ hơn
        }

        return o;
    }

    public int getOrderCount() {
        int orderCount = 0;
        String query = "SELECT COUNT(*) AS order_count FROM `order`"; // Enclose 'order' in backticks

        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                orderCount = resultSet.getInt("order_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderCount;
    }
    /// Manhhuy-----------------------------------------------------------------------------------------------------//

    public List<OrderDetail> getOrderInforById(String orderId) {
        List<OrderDetail> list = new ArrayList<>();

        String sql = "SELECT acc.firstName, acc.lastName, acc.mobile, acc.email, acc.gender, "
                + "orr.orderId, orr.orderDeliverCode, orr.userId, orr.orderName, orr.orderEmail, "
                + "orr.orderPhone, orr.totalPrice, orr.note, orr.saleId, orr.shipperId, "
                + "orr.createDate, orr.shippingAddress, orr.orderStatus, orr.payMethod,"
                + "os.description AS orderStatusDescription, "
                + "od.orderDetailId, od.productId, od.quantity, od.colorId, od.isfeedback, "
                + "p.name AS productName, p.price AS productPrice, MIN(pri.imageUrl) AS imageUrl, "
                + "color.colorName AS colorName " // Thêm colorName vào SELECT
                + "FROM carpipi.order orr "
                + "JOIN carpipi.account acc ON orr.userId = acc.userId "
                + "JOIN carpipi.orderdetail od ON orr.orderId = od.orderId "
                + "JOIN carpipi.product p ON od.productId = p.productId "
                + "JOIN carpipi.productImage pri ON pri.productId = p.productId "
                + "JOIN carpipi.orderStatus os ON orr.orderStatus = os.statusId "
                + "JOIN carpipi.color color ON od.colorId = color.colorId " // JOIN với bảng color
                + "WHERE od.orderId = ? "
                + "GROUP BY acc.firstName, acc.lastName, acc.mobile, acc.email, acc.gender, "
                + "orr.orderId, orr.orderDeliverCode, orr.userId, orr.orderName, orr.orderEmail, "
                + "orr.orderPhone, orr.totalPrice, orr.note, orr.saleId, orr.shipperId, "
                + "orr.createDate, orr.shippingAddress, orr.orderStatus, os.description, "
                + "od.orderDetailId, od.productId, od.quantity, od.colorId, od.isfeedback, "
                + "p.name, p.price, color.colorName"; // Thêm colorName vào GROUP BY

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, orderId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                OrderDetail o = new OrderDetail();
                o.setUserId(rs.getInt("userId"));
                o.setFirstName(rs.getString("firstName"));
                o.setLastName(rs.getString("lastName"));
                o.setMobile(rs.getString("mobile"));
                o.setEmail(rs.getString("email"));
                o.setGender(rs.getInt("gender"));
                o.setOrderId(rs.getInt("orderId"));
                o.setOrderDeliverCode(rs.getString("orderDeliverCode"));
                o.setOrderName(rs.getString("orderName"));
                o.setOrderEmail(rs.getString("orderEmail"));
                o.setOrderPhone(rs.getString("orderPhone"));
                o.setTotalPrice(rs.getDouble("totalPrice"));
                o.setNote(rs.getString("note"));
                o.setSaleId(rs.getInt("saleId"));
                o.setShipperId(rs.getInt("shipperId"));
                o.setCreateDate(rs.getDate("createDate"));
                o.setShippingAddress(rs.getString("shippingAddress"));
                o.setOrderStatus(rs.getInt("orderStatus"));
                o.setOrderStatusDescription(rs.getString("orderStatusDescription")); // Set thêm trường mới
                o.setOrderDetailId(rs.getInt("orderDetailId"));
                o.setProductId(rs.getString("productId"));
                o.setQuantity(rs.getInt("quantity"));
                o.setColorId(rs.getInt("colorId"));
                o.setIsFeedback(rs.getInt("isfeedback"));
                o.setProductName(rs.getString("productName"));
                o.setProductPrice(rs.getDouble("productPrice"));
                o.setImageUrl(rs.getString("imageUrl"));
                o.setColorName(rs.getString("colorName"));
                o.setPayMethod(rs.getInt("payMethod"));
                list.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Ghi lại lỗi
        }

        return list;
    }
    /// Manhuy-end------------------------------------------------------------------------------------------------///
    
    //son--------------//
    
public List<OrderDetail> getListOrderdetailById(String orderId) {
        List<OrderDetail> list = new ArrayList<>();

        String sql = "SELECT acc.firstName, acc.lastName, acc.mobile, acc.email, acc.gender,\n"
                + "       orr.*, od.productId, od.quantity, od.discountId, od.colorId,\n"
                + "       p.name, p.price, MIN(pri.imageUrl) AS imageUrl\n"
                + "FROM carpipi.order orr\n"
                + "JOIN carpipi.account acc\n"
                + "ON orr.userId = acc.userId\n"
                + "JOIN carpipi.orderdetail od\n"
                + "ON orr.orderId = od.orderId\n"
                + "JOIN carpipi.product p\n"
                + "ON od.productId = p.productId\n"
                + "JOIN carpipi.productImage pri\n"
                + "ON pri.productId = p.productId\n"
                + "WHERE od.orderId = ?\n"
                + "GROUP BY acc.firstName, acc.lastName, acc.mobile, acc.email, acc.gender,\n"
                + "         orr.orderId, od.productId, od.quantity, od.discountId, od.colorId,\n"
                + "         p.name, p.price";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, orderId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                OrderDetail o = new OrderDetail();
                o.setOrderId(rs.getInt("orderId"));
                o.setUserId(rs.getInt("userId")); // Đảm bảo userId có trong truy vấn SQL nếu cần
                o.setCreateDate(rs.getDate("createDate")); // Thay đổi kiểu Date
                o.setOrderName(rs.getString("orderName"));
                o.setOrderEmail(rs.getString("orderEmail"));
                o.setOrderPhone(rs.getString("orderPhone"));
                o.setTotalPrice(rs.getDouble("totalPrice"));
                o.setOrderStatus(rs.getInt("orderStatus"));
                o.setFirstName(rs.getString("firstName"));
                o.setLastName(rs.getString("lastName"));
                o.setMobile(rs.getString("acc.mobile"));
                o.setEmail(rs.getString("email"));
                o.setSaleId(rs.getInt("saleId"));
                o.setProductName(rs.getString("p.name"));
                o.setQuantity(rs.getInt("od.quantity"));
                o.setImageUrl(rs.getString("imageUrl"));
                o.setProductPrice(rs.getDouble("p.price"));
                o.setProductId(rs.getString("productId"));
                o.setColorId(rs.getInt("od.colorId"));
                list.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Ghi lại lỗi
        }

        return list;
    }

    public OrderDetail getOrderdetailById(String orderId) {

        String sql = "SELECT acc.firstName, acc.lastName, acc.mobile, acc.email, acc.gender,\n"
                + "       orr.*, od.productId, od.quantity, od.discountId, od.colorId,\n"
                + "       p.name, p.price, MIN(pri.imageUrl) AS imageUrl\n"
                + "FROM carpipi.order orr\n"
                + "JOIN carpipi.account acc\n"
                + "ON orr.userId = acc.userId\n"
                + "JOIN carpipi.orderdetail od\n"
                + "ON orr.orderId = od.orderId\n"
                + "JOIN carpipi.product p\n"
                + "ON od.productId = p.productId\n"
                + "JOIN carpipi.productImage pri\n"
                + "ON pri.productId = p.productId\n"
                + "WHERE od.orderId = ?\n"
                + "GROUP BY acc.firstName, acc.lastName, acc.mobile, acc.email, acc.gender,\n"
                + "         orr.orderId, od.productId, od.quantity, od.discountId, od.colorId,\n"
                + "         p.name, p.price";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, orderId);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                OrderDetail o = new OrderDetail();
                o.setOrderId(rs.getInt("orderId"));
                o.setUserId(rs.getInt("userId")); // Đảm bảo userId có trong truy vấn SQL nếu cần
                o.setCreateDate(rs.getDate("createDate")); // Thay đổi kiểu Date
                o.setOrderName(rs.getString("orderName"));
                o.setOrderEmail(rs.getString("orderEmail"));
                o.setOrderPhone(rs.getString("orderPhone"));
                o.setShippingAddress(rs.getString("shippingAddress"));
                o.setTotalPrice(rs.getDouble("totalPrice"));
                o.setOrderStatus(rs.getInt("orderStatus"));
                o.setFirstName(rs.getString("firstName"));
                o.setLastName(rs.getString("lastName"));
                o.setNote(rs.getString("note"));
                o.setMobile(rs.getString("acc.mobile"));
                o.setEmail(rs.getString("email"));
                o.setSaleId(rs.getInt("saleId"));
                o.setProductName(rs.getString("p.name"));
                o.setQuantity(rs.getInt("od.quantity"));
                o.setImageUrl(rs.getString("imageUrl"));
                o.setProductPrice(rs.getDouble("p.price"));
                o.setProductId(rs.getString("productId"));
                o.setColorId(rs.getInt("od.colorId"));
                return o;
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Ghi lại lỗi
        }

        return null;
    }




    public List<OrderDetail> getAllOrderList(int index) {
        List<OrderDetail> list = new ArrayList<>();

        String sql = "SELECT acc.userId, acc.firstName, acc.lastName, orr.orderId, orr.createDate, orr.totalPrice, orr.saleId, orr.orderName,"
                + "orr.orderStatus, "
                + "SUBSTRING_INDEX(GROUP_CONCAT(p.name ORDER BY od.orderId SEPARATOR ', '), ', ', 1) AS firstProductName, "
                + "COUNT(od.productId) - 1 AS additionalProductCount "
                + "FROM carpipi.order orr "
                + "JOIN carpipi.account acc ON orr.userId = acc.userId "
                + "JOIN carpipi.orderdetail od ON orr.orderId = od.orderId "
                + "JOIN carpipi.product p ON od.productId = p.productId "
                + "GROUP BY orr.orderId  limit 5 offset ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, ((index - 1) * 5));
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                System.out.println("Record found: " + rs.getInt("orderId")); // Kiểm tra xem có bản ghi nào trả về

                OrderDetail o = new OrderDetail();
                o.setOrderId(rs.getInt("orderId"));
                o.setUserId(rs.getInt("userId")); // Đảm bảo userId có trong truy vấn SQL nếu cần
                o.setCreateDate(rs.getDate("createDate")); // Thay đổi kiểu Date
                o.setOrderName(rs.getString("orderName"));
                o.setTotalPrice(rs.getDouble("totalPrice"));
                o.setOrderStatus(rs.getInt("orderStatus"));
                o.setFirstName(rs.getString("firstName"));
                o.setLastName(rs.getString("lastName"));
                o.setSaleId(rs.getInt("saleId"));
                o.setProductName(rs.getString("firstProductName"));
                o.setQuantity(rs.getInt("additionalProductCount"));

                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi lại lỗi để dễ dàng gỡ lỗi
        }

        if (list.isEmpty()) {
            System.out.println("No records found.");
        }

        return list;
    }

    public List<OrderDetail> getListOrderWithStatus(String status, int index) {
        List<OrderDetail> list = new ArrayList();

        String sql = "SELECT acc.userId, acc.firstName, acc.lastName, orr.orderId, orr.createDate, orr.totalPrice, orr.saleId, orr.orderName,\n"
                + "orr.orderStatus, SUBSTRING_INDEX(GROUP_CONCAT(p.name ORDER BY od.orderId SEPARATOR ', '), ', ', 1) AS firstProductName, \n"
                + "COUNT(od.productId) - 1 AS additionalProductCount \n"
                + "FROM carpipi.order orr \n"
                + "JOIN carpipi.account acc ON orr.userId = acc.userId \n"
                + "JOIN carpipi.orderdetail od ON orr.orderId = od.orderId \n"
                + "JOIN carpipi.product p ON od.productId = p.productId \n"
                + "Where orr.orderStatus = ?\n"
                + "GROUP BY orr.orderId limit 5 offset ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, status);
            st.setInt(2, ((index - 1) * 5));
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                System.out.println("Record found: " + rs.getInt("orderStatus")); // Kiểm tra xem có bản ghi nào trả về

                OrderDetail o = new OrderDetail();
                o.setOrderId(rs.getInt("orderId"));
                o.setUserId(rs.getInt("userId")); // Đảm bảo userId có trong truy vấn SQL nếu cần
                o.setSaleId(rs.getInt("saleId"));
                o.setCreateDate(rs.getDate("createDate"));
                o.setFirstName(rs.getString("firstName"));
                o.setLastName(rs.getString("lastName"));
                o.setOrderName(rs.getString("orderName"));
                o.setTotalPrice(rs.getDouble("totalPrice"));
                o.setOrderStatus(rs.getInt("orderStatus"));
                o.setProductName(rs.getString("firstProductName"));
                o.setQuantity(rs.getInt("additionalProductCount"));

                list.add(o);
            }

        } catch (Exception e) {
        }

        return list;
    }

    public List<OrderDetail> getListOrderWithSearch(String search, int index) {
        List<OrderDetail> list = new ArrayList();
        String sql = "SELECT acc.userId, acc.firstName, acc.lastName, orr.orderId, orr.createDate, orr.totalPrice, orr.saleId, orr.orderName,\n"
                + "       orr.orderStatus, SUBSTRING_INDEX(GROUP_CONCAT(p.name ORDER BY od.orderId SEPARATOR ', '), ', ', 1) AS firstProductName, \n"
                + "       COUNT(od.productId) - 1 AS additionalProductCount \n"
                + "FROM carpipi.`order` orr \n"
                + "JOIN carpipi.account acc ON orr.userId = acc.userId \n"
                + "JOIN carpipi.orderdetail od ON orr.orderId = od.orderId \n"
                + "JOIN carpipi.product p ON od.productId = p.productId \n"
                + "GROUP BY orr.orderId\n"
                + "HAVING firstProductName LIKE ? OR acc.firstName LIKE ? OR acc.lastName LIKE ?  limit 5 offset ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, "%" + search + "%");  // Order status
            st.setString(2, "%" + search + "%");  // First name
            st.setString(3, "%" + search + "%");  // Last name
            st.setInt(4, ((index - 1) * 5));

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                System.out.println("Record found: " + rs.getString("firstName")); // Kiểm tra xem có bản ghi nào trả về

                OrderDetail o = new OrderDetail();
                o.setOrderId(rs.getInt("orderId"));
                o.setUserId(rs.getInt("userId")); // Đảm bảo userId có trong truy vấn SQL nếu cần
                o.setSaleId(rs.getInt("saleId"));
                o.setCreateDate(rs.getDate("createDate"));
                o.setFirstName(rs.getString("firstName"));
                o.setLastName(rs.getString("lastName"));
                o.setOrderName(rs.getString("orderName"));
                o.setTotalPrice(rs.getDouble("totalPrice"));
                o.setOrderStatus(rs.getInt("orderStatus"));
                o.setProductName(rs.getString("firstProductName"));
                o.setQuantity(rs.getInt("additionalProductCount"));

                list.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // In ra lỗi khi có vấn đề xảy ra

        }

        return list;
    }

    public List<OrderDetail> getListOrderWithSearchAndStatus(String search, String status, int index) {
        List<OrderDetail> list = new ArrayList();
        String sql = "SELECT acc.userId, acc.firstName, acc.lastName, orr.orderId, orr.createDate, orr.totalPrice, orr.saleId, orr.orderName,\n"
                + "       orr.orderStatus, SUBSTRING_INDEX(GROUP_CONCAT(p.name ORDER BY od.orderId SEPARATOR ', '), ', ', 1) AS firstProductName, \n"
                + "       COUNT(od.productId) - 1 AS additionalProductCount \n"
                + "FROM carpipi.`order` orr \n"
                + "JOIN carpipi.account acc ON orr.userId = acc.userId \n"
                + "JOIN carpipi.orderdetail od ON orr.orderId = od.orderId \n"
                + "JOIN carpipi.product p ON od.productId = p.productId \n"
                + "GROUP BY orr.orderId\n"
                + "HAVING (firstProductName LIKE ? OR acc.firstName LIKE ? OR acc.lastName LIKE ?) And orr.orderstatus = ? limit 5 offset ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, "%" + search + "%");
            st.setString(2, "%" + search + "%");
            st.setString(3, "%" + search + "%");
            st.setString(4, status);
            st.setInt(5, ((index - 1) * 5));

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                System.out.println("Record found: " + rs.getString("firstName")); // Kiểm tra xem có bản ghi nào trả về

                OrderDetail o = new OrderDetail();
                o.setOrderId(rs.getInt("orderId"));
                o.setUserId(rs.getInt("userId")); // Đảm bảo userId có trong truy vấn SQL nếu cần
                o.setSaleId(rs.getInt("saleId"));
                o.setCreateDate(rs.getDate("createDate"));
                o.setFirstName(rs.getString("firstName"));
                o.setLastName(rs.getString("lastName"));
                o.setOrderName(rs.getString("orderName"));
                o.setTotalPrice(rs.getDouble("totalPrice"));
                o.setOrderStatus(rs.getInt("orderStatus"));
                o.setProductName(rs.getString("firstProductName"));
                o.setQuantity(rs.getInt("additionalProductCount"));

                list.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // In ra lỗi khi có vấn đề xảy ra

        }

        return list;
    }

    public List<OrderDetail> getListOrderWithSearchAndSort(String search, String sortBy, String order, int index) {
        List<OrderDetail> list = new ArrayList();
        String sql = "SELECT acc.userId, acc.firstName, acc.lastName, orr.orderId, orr.createDate, orr.totalPrice, orr.saleId, orr.orderName,\n"
                + "       orr.orderStatus, SUBSTRING_INDEX(GROUP_CONCAT(p.name ORDER BY od.orderId SEPARATOR ', '), ', ', 1) AS firstProductName, \n"
                + "       COUNT(od.productId) - 1 AS additionalProductCount \n"
                + "FROM carpipi.`order` orr \n"
                + "JOIN carpipi.account acc ON orr.userId = acc.userId \n"
                + "JOIN carpipi.orderdetail od ON orr.orderId = od.orderId \n"
                + "JOIN carpipi.product p ON od.productId = p.productId \n"
                + "GROUP BY orr.orderId\n"
                + "HAVING firstProductName LIKE ? OR acc.firstName LIKE ? OR acc.lastName LIKE ? order by " + sortBy + order + " limit 5 offset ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, "%" + search + "%");  // Order status
            st.setString(2, "%" + search + "%");  // First name
            st.setString(3, "%" + search + "%");  // Last name
            st.setInt(4, ((index - 1) * 5));

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                System.out.println("Record found: " + rs.getString("firstName")); // Kiểm tra xem có bản ghi nào trả về

                OrderDetail o = new OrderDetail();
                o.setOrderId(rs.getInt("orderId"));
                o.setUserId(rs.getInt("userId")); // Đảm bảo userId có trong truy vấn SQL nếu cần
                o.setSaleId(rs.getInt("saleId"));
                o.setCreateDate(rs.getDate("createDate"));
                o.setFirstName(rs.getString("firstName"));
                o.setLastName(rs.getString("lastName"));
                o.setOrderName(rs.getString("orderName"));
                o.setTotalPrice(rs.getDouble("totalPrice"));
                o.setOrderStatus(rs.getInt("orderStatus"));
                o.setProductName(rs.getString("firstProductName"));
                o.setQuantity(rs.getInt("additionalProductCount"));

                list.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // In ra lỗi khi có vấn đề xảy ra

        }

        return list;
    }

    public List<OrderDetail> getListOrderWithSearchAndStatusAndSort(String search, String status, String sortBy, String order, int index) {
        List<OrderDetail> list = new ArrayList();
        String sql = "SELECT acc.userId, acc.firstName, acc.lastName, orr.orderId, orr.createDate, orr.totalPrice, orr.saleId, orr.orderName,\n"
                + "       orr.orderStatus, SUBSTRING_INDEX(GROUP_CONCAT(p.name ORDER BY od.orderId SEPARATOR ', '), ', ', 1) AS firstProductName, \n"
                + "       COUNT(od.productId) - 1 AS additionalProductCount \n"
                + "FROM carpipi.`order` orr \n"
                + "JOIN carpipi.account acc ON orr.userId = acc.userId \n"
                + "JOIN carpipi.orderdetail od ON orr.orderId = od.orderId \n"
                + "JOIN carpipi.product p ON od.productId = p.productId \n"
                + "GROUP BY orr.orderId\n"
                + "HAVING (firstProductName LIKE ? OR acc.firstName LIKE ? OR acc.lastName LIKE ?) And orr.orderstatus = ? order by " + sortBy + order + " limit 5 offset ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, "%" + search + "%");
            st.setString(2, "%" + search + "%");
            st.setString(3, "%" + search + "%");
            st.setString(4, status);
            st.setInt(5, ((index - 1) * 5));

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                System.out.println("Record found: " + rs.getString("firstName")); // Kiểm tra xem có bản ghi nào trả về

                OrderDetail o = new OrderDetail();
                o.setOrderId(rs.getInt("orderId"));
                o.setUserId(rs.getInt("userId")); // Đảm bảo userId có trong truy vấn SQL nếu cần
                o.setSaleId(rs.getInt("saleId"));
                o.setCreateDate(rs.getDate("createDate"));
                o.setFirstName(rs.getString("firstName"));
                o.setLastName(rs.getString("lastName"));
                o.setOrderName(rs.getString("orderName"));
                o.setTotalPrice(rs.getDouble("totalPrice"));
                o.setOrderStatus(rs.getInt("orderStatus"));
                o.setProductName(rs.getString("firstProductName"));
                o.setQuantity(rs.getInt("additionalProductCount"));

                list.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // In ra lỗi khi có vấn đề xảy ra

        }

        return list;
    }

    public List<OrderDetail> getAllOrderListAndSort(String sortBy, String order, int index) {
        List<OrderDetail> list = new ArrayList<>();

        String sql = "SELECT acc.userId, acc.firstName, acc.lastName, orr.orderId, orr.createDate, orr.totalPrice, orr.saleId, orr.orderName,"
                + "orr.orderStatus, "
                + "SUBSTRING_INDEX(GROUP_CONCAT(p.name ORDER BY od.orderId SEPARATOR ', '), ', ', 1) AS firstProductName, "
                + "COUNT(od.productId) - 1 AS additionalProductCount "
                + "FROM carpipi.order orr "
                + "JOIN carpipi.account acc ON orr.userId = acc.userId "
                + "JOIN carpipi.orderdetail od ON orr.orderId = od.orderId "
                + "JOIN carpipi.product p ON od.productId = p.productId "
                + "GROUP BY orr.orderId order by " + sortBy + order + " limit 5 offset ?";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, ((index - 1) * 5));
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                System.out.println("Record found: " + rs.getInt("orderId")); // Kiểm tra xem có bản ghi nào trả về

                OrderDetail o = new OrderDetail();
                o.setOrderId(rs.getInt("orderId"));
                o.setUserId(rs.getInt("userId")); // Đảm bảo userId có trong truy vấn SQL nếu cần
                o.setCreateDate(rs.getDate("createDate")); // Thay đổi kiểu Date
                o.setOrderName(rs.getString("orderName"));
                o.setTotalPrice(rs.getDouble("totalPrice"));
                o.setOrderStatus(rs.getInt("orderStatus"));
                o.setFirstName(rs.getString("firstName"));
                o.setLastName(rs.getString("lastName"));
                o.setSaleId(rs.getInt("saleId"));
                o.setProductName(rs.getString("firstProductName"));
                o.setQuantity(rs.getInt("additionalProductCount"));

                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi lại lỗi để dễ dàng gỡ lỗi
        }

        if (list.isEmpty()) {
            System.out.println("No records found.");
        }

        return list;
    }

    public List<OrderDetail> getListOrderWithStatusAndSort(String status, String sortBy, String order, int index) {
        List<OrderDetail> list = new ArrayList();

        String sql = "SELECT acc.userId, acc.firstName, acc.lastName, orr.orderId, orr.createDate, orr.totalPrice, orr.saleId, orr.orderName,\n"
                + "orr.orderStatus, SUBSTRING_INDEX(GROUP_CONCAT(p.name ORDER BY od.orderId SEPARATOR ', '), ', ', 1) AS firstProductName, \n"
                + "COUNT(od.productId) - 1 AS additionalProductCount \n"
                + "FROM carpipi.order orr \n"
                + "JOIN carpipi.account acc ON orr.userId = acc.userId \n"
                + "JOIN carpipi.orderdetail od ON orr.orderId = od.orderId \n"
                + "JOIN carpipi.product p ON od.productId = p.productId \n"
                + "Where orr.orderStatus = ?\n"
                + "GROUP BY " + sortBy + order + " limit 5 offset ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, status);
            st.setInt(2, ((index - 1) * 5));
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                System.out.println("Record found: " + rs.getInt("orderStatus")); // Kiểm tra xem có bản ghi nào trả về

                OrderDetail o = new OrderDetail();
                o.setOrderId(rs.getInt("orderId"));
                o.setUserId(rs.getInt("userId")); // Đảm bảo userId có trong truy vấn SQL nếu cần
                o.setSaleId(rs.getInt("saleId"));
                o.setCreateDate(rs.getDate("createDate"));
                o.setFirstName(rs.getString("firstName"));
                o.setLastName(rs.getString("lastName"));
                o.setOrderName(rs.getString("orderName"));
                o.setTotalPrice(rs.getDouble("totalPrice"));
                o.setOrderStatus(rs.getInt("orderStatus"));
                o.setProductName(rs.getString("firstProductName"));
                o.setQuantity(rs.getInt("additionalProductCount"));

                list.add(o);
            }

        } catch (Exception e) {
        }

        return list;
    }

    public int getTotalOrderList() {

        int total = 0;

        String sql = "SELECT count(*) as total FROM carpipi.order";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi lại lỗi để dễ dàng gỡ lỗi
        }

        return total;
    }

    public void updateStatusOfOrder(String orderId, String status) {

        String sql = "UPDATE `carpipi`.`order`\n"
                + "SET `orderStatus` = ?\n"
                + "WHERE `orderId` = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, status);
            st.setString(2, orderId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public double getTotalPrice() {
        double total = 0;
        String query = "SELECT SUM(totalPrice) AS total_order_price \n"
                + "FROM `order` where orderStatus= 4";
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                total = resultSet.getDouble("total_order_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public double getTotalCarSale() {
        double total = 0;
        String query = "SELECT SUM(od.quantity) as quantity\n"
                + "FROM `order` o join carpipi.orderDetail od\n"
                + "On o.orderId = od.orderId\n"
                + "where orderStatus= 4";
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                total = resultSet.getDouble("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public int getTotalSaler() {
        int total = 0;
        String query = "select Count(*) as quantity from Account where roleId = 3";
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                total = resultSet.getInt("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public List<Order> getTop5SalerByOrder() {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT a.userName AS saleName, COUNT(o.orderId) AS total_orders\n"
                + "FROM `order` o\n"
                + "JOIN `account` a ON o.saleId = a.userId\n"
                + "WHERE o.orderStatus = 4\n"
                + "GROUP BY a.userName\n"
                + "ORDER BY total_orders DESC\n"
                + "LIMIT 5";

        try (
                PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                // Lấy dữ liệu từ ResultSet
                String saleName = resultSet.getString("saleName");
                int totalOrders = resultSet.getInt("total_orders");

                // Tạo đối tượng Order và thiết lập các giá trị
                Order order = new Order();
                order.setSaleName(saleName); // Giả sử lớp Order có phương thức setSaleName
                order.setTotalOrders(totalOrders); // Giả sử lớp Order có phương thức setTotalOrders

                // Thêm vào danh sách
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ghi log ngoại lệ
        }

        return orderList;
    }

//son--------------//  

    
//tuan----------------------------------------------------------------------//
    public void cancelOrder(int orderId) {
        String sql = "UPDATE `order` SET orderStatus = 5 WHERE orderId = ?;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, orderId);
            st.executeUpdate(); // Gọi phương thức này để thực hiện cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //tuan----------------------------------------------------------------------//    
    public static void main(String[] args) {
        OrderDAO o = new OrderDAO();

       

    }
}
