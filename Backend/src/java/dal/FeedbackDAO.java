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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Account;
import model.Feedback;

/**
 *
 * @author tuana
 */
public class FeedbackDAO extends DBContext {

    public List<Feedback> getAllFeedback() {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM feedback order by feedbackTime desc";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedbackId");
                int userId = rs.getInt("userId");
                String productId = rs.getString("productId");
                String feedbackInfo = rs.getString("feedbackInfo");
                String feedbackImg = rs.getString("feedbackImg");
                String feedbackTime = rs.getString("feedbackTime");
                int feedbackRate = rs.getInt("feedbackRate");
                int status = rs.getInt("status");

                if (feedbackImg == null) {
                    feedbackImg = "";
                }

                Feedback feedback = new Feedback(feedbackId, userId, productId, feedbackInfo, feedbackTime, feedbackImg, feedbackRate, status);
                list.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Feedback> getFeedbackByProductId(String productID) {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE status =1 and productId LIKE ?"
                + "order by feedbackTime desc";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            // Gán giá trị cho tham số productID
            st.setString(1, "%" + productID + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedbackId");
                int userId = rs.getInt("userId");
                String productId = rs.getString("productId");
                String feedbackInfo = rs.getString("feedbackInfo");
                String feedbackTime = rs.getString("feedbackTime");
                String feedbackImg = rs.getString("feedbackImg");
                int feedbackRate = rs.getInt("feedbackRate");
                int status = rs.getInt("status");

                if (feedbackImg == null) {
                    feedbackImg = "";
                }
                Feedback feedback = new Feedback(feedbackId, userId, productId, feedbackInfo, feedbackTime, feedbackImg, feedbackRate, status);
                list.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Account> getUserNameByProductId(String productId) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT a.* FROM feedback f JOIN account a ON f.userId = a.userId WHERE f.productId like ?;";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, productId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Account p = new Account(
                            rs.getInt("userId"),
                            rs.getString("userName"),
                            rs.getString("password"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getInt("gender"),
                            rs.getString("email"),
                            rs.getString("mobile"),
                            rs.getString("address"),
                            rs.getInt("roleId"),
                            rs.getString("avatar")
                    );
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Feedback> getFeedbackByRate(String productID, int rate) {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE status =1 and productId LIKE ? AND feedbackRate = ? ORDER BY feedbackTime DESC";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, productID);
            st.setInt(2, rate);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedbackId");
                int userId = rs.getInt("userId");
                String productId = rs.getString("productId");
                String feedbackInfo = rs.getString("feedbackInfo");
                String feedbackTime = rs.getString("feedbackTime");
                String feedbackImg = rs.getString("feedbackImg");
                int feedbackRate = rs.getInt("feedbackRate");
                int status = rs.getInt("status");

                if (feedbackImg == null) {
                    feedbackImg = "";
                }

                Feedback feedback = new Feedback(feedbackId, userId, productId, feedbackInfo, feedbackTime, feedbackImg, feedbackRate, status);
                list.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public int getRateProduct(String productId) {
        int rate = 0;
        String sql = "SELECT CEIL(AVG(feedbackRate)) AS avgRate "
                + "FROM feedback "
                + "WHERE productId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getObject("avgRate") != null) {  // Kiểm tra nếu có giá trị trả về
                    rate = rs.getInt("avgRate");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rate;
    }

    public void createFeedback(int userId, String productId, String feedbackInfo, String feedbackImg, int feedbackRate, int status) {
        String sql = "INSERT INTO feedback (userId, productId, feedbackInfo, feedbackTime, feedbackImg, feedbackRate, status)"
                + " VALUES (?, ?, ?, NOW(), ?, ?, ?)";

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            st.setString(2, productId);
            st.setString(3, feedbackInfo);
            st.setString(4, feedbackImg);
            st.setInt(5, feedbackRate);
            st.setInt(6, status);

            st.executeUpdate(); // Thực thi câu lệnh INSERT
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý ngoại lệ nếu có lỗi xảy ra
        }
    }

    public List<Feedback> getFeedbackListByPage(List<Feedback> feedbacks, int start, int end) {
        ArrayList<Feedback> arr = new ArrayList<>();
        for (int i = start; i < end; i++) {
            arr.add(feedbacks.get(i));
        }
        return arr;
    }

    public Map<String, Object> getFeedbackDetailsById(int feedbackId) {
        Map<String, Object> feedbackDetail = null;

        String sql = "SELECT "
                + "f.feedbackId, "
                + // Added feedbackId to the SELECT statement
                "CONCAT(a.firstName, ' ', a.lastName) AS fullName, "
                + "a.email, "
                + "a.mobile, "
                + "p.name AS productName, "
                + "f.feedbackRate AS ratedStar, "
                + "f.feedbackInfo AS feedback, "
                + "f.status AS feedbackStatus "
                + "FROM feedback f "
                + "JOIN account a ON f.userId = a.userId "
                + "JOIN product p ON f.productId = p.productId "
                + "WHERE f.feedbackId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, feedbackId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    feedbackDetail = new HashMap<>();
                    feedbackDetail.put("feedbackId", rs.getInt("feedbackId")); // Added feedbackId to the map
                    feedbackDetail.put("fullName", rs.getString("fullName"));
                    feedbackDetail.put("email", rs.getString("email"));
                    feedbackDetail.put("mobile", rs.getString("mobile"));
                    feedbackDetail.put("productName", rs.getString("productName"));
                    feedbackDetail.put("ratedStar", rs.getInt("ratedStar"));
                    feedbackDetail.put("feedback", rs.getString("feedback"));
                    feedbackDetail.put("feedbackStatus", rs.getInt("feedbackStatus"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feedbackDetail;
    }

    public String getFeedbackImageById(int feedbackId) {
        String feedbackImg = null;

        String sql = "SELECT feedbackImg FROM feedback WHERE feedbackId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, feedbackId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    feedbackImg = rs.getString("feedbackImg"); // Lấy thông tin feedbackImg
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feedbackImg; // Trả về link ảnh hoặc null nếu không tìm thấy
    }

    public void updateFeedbackStatus(int feedbackId, int status) {
        String sql = "UPDATE feedback SET status = ? WHERE feedbackId = ?";
        try (
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, status);
            stm.setInt(2, feedbackId);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getFeedbackCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM feedback";

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

    public List<Feedback> getFeedbackRateByBrand() {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT b.brandId, "
                + "       b.name AS brandName, "
                + "       COALESCE(ROUND(AVG(f.feedbackRate), 0), 0) AS averageFeedbackRate "
                + "FROM brand b "
                + "LEFT JOIN product p ON b.brandId = p.brandId "
                + "LEFT JOIN feedback f ON p.productId = f.productId "
                + "GROUP BY b.brandId, b.name;";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int averageFeedbackRate = rs.getInt("averageFeedbackRate");
                String brandName = rs.getString("brandName");
                Feedback feedback = new Feedback(averageFeedbackRate, brandName);
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return feedbackList;
    }

    public List<Feedback> getAllFeedbackAndPaging(int index) {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM carpipi.feedback order by feedbackTime desc limit 5 offset ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, ((index - 1) * 5));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedbackId");
                int userId = rs.getInt("userId");
                String productId = rs.getString("productId");
                String feedbackInfo = rs.getString("feedbackInfo");
                String feedbackImg = rs.getString("feedbackImg");
                String feedbackTime = rs.getString("feedbackTime");
                int feedbackRate = rs.getInt("feedbackRate");
                int status = rs.getInt("status");

                if (feedbackImg == null) {
                    feedbackImg = "";
                }

                Feedback feedback = new Feedback(feedbackId, userId, productId, feedbackInfo, feedbackTime, feedbackImg, feedbackRate, status);
                list.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Feedback> getOrderByTime(int index, String order) {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM carpipi.feedback order by feedbackTime" + order + "limit 5 offset ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, ((index - 1) * 5));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedbackId");
                int userId = rs.getInt("userId");
                String productId = rs.getString("productId");
                String feedbackInfo = rs.getString("feedbackInfo");
                String feedbackImg = rs.getString("feedbackImg");
                String feedbackTime = rs.getString("feedbackTime");
                int feedbackRate = rs.getInt("feedbackRate");
                int status = rs.getInt("status");

                if (feedbackImg == null) {
                    feedbackImg = "";
                }

                Feedback feedback = new Feedback(feedbackId, userId, productId, feedbackInfo, feedbackTime, feedbackImg, feedbackRate, status);
                list.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Feedback> getOrderByFeedbackId(int index, String order) {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM carpipi.feedback order by feedbackId " + order + " limit 5 offset ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, ((index - 1) * 5));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedbackId");
                int userId = rs.getInt("userId");
                String productId = rs.getString("productId");
                String feedbackInfo = rs.getString("feedbackInfo");
                String feedbackImg = rs.getString("feedbackImg");
                String feedbackTime = rs.getString("feedbackTime");
                int feedbackRate = rs.getInt("feedbackRate");
                int status = rs.getInt("status");

                if (feedbackImg == null) {
                    feedbackImg = "";
                }

                Feedback feedback = new Feedback(feedbackId, userId, productId, feedbackInfo, feedbackTime, feedbackImg, feedbackRate, status);
                list.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Feedback> getOrderByProductName(int index, String order) {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM carpipi.feedback order by productId " + order + " limit 5 offset ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, ((index - 1) * 5));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedbackId");
                int userId = rs.getInt("userId");
                String productId = rs.getString("productId");
                String feedbackInfo = rs.getString("feedbackInfo");
                String feedbackImg = rs.getString("feedbackImg");
                String feedbackTime = rs.getString("feedbackTime");
                int feedbackRate = rs.getInt("feedbackRate");
                int status = rs.getInt("status");

                if (feedbackImg == null) {
                    feedbackImg = "";
                }

                Feedback feedback = new Feedback(feedbackId, userId, productId, feedbackInfo, feedbackTime, feedbackImg, feedbackRate, status);
                list.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Feedback> getFeedbackBySearchAndPaging(int index, String search, String status) {
        List<Feedback> list = new ArrayList<>();
        String sql = "";

        // Trường hợp 1: Chỉ có search, không có status
        if ((status == null || status.isEmpty()) && (search != null && !search.isEmpty())) {
            sql = "SELECT f.*, a.firstName, a.lastName, p.name \n"
                    + "FROM feedback f JOIN account a \n"
                    + "ON f.userId = a.userId \n"
                    + "JOIN product p \n"
                    + "ON f.productId = p.productId\n"
                    + "WHERE p.name Like ? OR f.feedbackInfo LIKE ? \n"
                    + "order by feedbackTime desc \n"
                    + "limit 5 offset ?";
            System.out.println("case1");
            System.out.println("---------");
        } // Trường hợp 2: Chỉ có status, không có search
        else if ((status != null && !status.isEmpty()) && (search == null || search.isEmpty())) {
            sql = "SELECT f.*, a.firstName, a.lastName, p.name \n"
                    + "FROM carpipi.feedback f JOIN carpipi.account a \n"
                    + "ON f.userId = a.userId \n"
                    + "JOIN carpipi.product p \n"
                    + "ON f.productId = p.productId\n"
                    + "WHERE f.status = ?\n"
                    + "order by feedbackTime desc \n"
                    + "limit 5 offset ?";
            System.out.println("case2");
            System.out.println("---------");
        } // Trường hợp 3: Có cả search và status
        else if ((status != null && !status.isEmpty()) && (search != null && !search.isEmpty())) {
            sql = "SELECT f.*, a.firstName, a.lastName, p.name \n"
                    + "FROM carpipi.feedback f JOIN carpipi.account a \n"
                    + "ON f.userId = a.userId \n"
                    + "JOIN carpipi.product p \n"
                    + "ON f.productId = p.productId\n"
                    + "WHERE (p.name Like ? OR f.feedbackInfo LIKE ?) And f.status = ?\n"
                    + "order by feedbackTime desc \n"
                    + "limit 5 offset ?";
            System.out.println("case3");
            System.out.println("---------");
        } // Trường hợp 4: Status là "10" và có search

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            // Thiết lập tham số cho từng trường hợp
            if ((status == null || status.isEmpty()) && (search != null && !search.isEmpty())) { // case 1
                st.setString(1, "%" + search + "%");
                st.setString(2, "%" + search + "%");
                st.setInt(3, ((index - 1) * 5));
            } else if ((status != null && !status.isEmpty()) && (search == null || search.isEmpty())) { // case 2
                st.setString(1, status);
                st.setInt(2, ((index - 1) * 5));
            } else if ((status != null && !status.isEmpty()) && (search != null && !search.isEmpty())) { // case 3
                st.setString(1, "%" + search + "%");
                st.setString(2, "%" + search + "%");
                st.setString(3, status);
                st.setInt(4, ((index - 1) * 5));
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedbackId");
                int userId = rs.getInt("userId");
                String productId = rs.getString("productId");
                String feedbackInfo = rs.getString("feedbackInfo");
                String feedbackImg = rs.getString("feedbackImg");
                String feedbackTime = rs.getString("feedbackTime");
                int feedbackRate = rs.getInt("feedbackRate");
                int statuss = rs.getInt("status");

                if (feedbackImg == null) {
                    feedbackImg = "";
                }

                Feedback feedback = new Feedback(feedbackId, userId, productId, feedbackInfo, feedbackTime, feedbackImg, feedbackRate, statuss);
                list.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Feedback> getFeedbackBySearchOrderByTime(int index, String search, String status, String order) {
        List<Feedback> list = new ArrayList<>();
        String sql = "";

        // Trường hợp 1: Chỉ có search, không có status
        if ((status == null || status.isEmpty()) && (search != null && !search.isEmpty())) {
            sql = "SELECT f.*, a.firstName, a.lastName, p.name \n"
                    + "FROM feedback f JOIN account a \n"
                    + "ON f.userId = a.userId \n"
                    + "JOIN product p \n"
                    + "ON f.productId = p.productId\n"
                    + "WHERE p.name Like ? OR f.feedbackInfo LIKE ? \n"
                    + "order by feedbackTime desc \n"
                    + "limit 5 offset ?";
            System.out.println("case1");
            System.out.println("---------");
        } // Trường hợp 2: Chỉ có status, không có search
        else if ((status != null && !status.isEmpty()) && (search == null || search.isEmpty())) {
            sql = "SELECT f.*, a.firstName, a.lastName, p.name \n"
                    + "FROM carpipi.feedback f JOIN carpipi.account a \n"
                    + "ON f.userId = a.userId \n"
                    + "JOIN carpipi.product p \n"
                    + "ON f.productId = p.productId\n"
                    + "WHERE f.status = ?\n"
                    + "order by feedbackTime desc \n"
                    + "limit 5 offset ?";
            System.out.println("case2");
            System.out.println("---------");
        } // Trường hợp 3: Có cả search và status
        else if ((status != null && !status.isEmpty()) && (search != null && !search.isEmpty())) {
            sql = "SELECT f.*, a.firstName, a.lastName, p.name \n"
                    + "FROM carpipi.feedback f JOIN carpipi.account a \n"
                    + "ON f.userId = a.userId \n"
                    + "JOIN carpipi.product p \n"
                    + "ON f.productId = p.productId\n"
                    + "WHERE (p.name Like ? OR f.feedbackInfo LIKE ?) And f.status = ?\n"
                    + "order by feedbackTime desc \n"
                    + "limit 5 offset ?";
            System.out.println("case3");
            System.out.println("---------");
        } // Trường hợp 4: Status là "10" và có search

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            // Thiết lập tham số cho từng trường hợp
            if ((status == null || status.isEmpty()) && (search != null && !search.isEmpty())) { // case 1
                st.setString(1, "%" + search + "%");
                st.setString(2, "%" + search + "%");
                st.setInt(3, ((index - 1) * 5));
            } else if ((status != null && !status.isEmpty()) && (search == null || search.isEmpty())) { // case 2
                st.setString(1, status);
                st.setInt(2, ((index - 1) * 5));
            } else if ((status != null && !status.isEmpty()) && (search != null && !search.isEmpty())) { // case 3
                st.setString(1, "%" + search + "%");
                st.setString(2, "%" + search + "%");
                st.setString(3, status);
                st.setInt(4, ((index - 1) * 5));
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedbackId");
                int userId = rs.getInt("userId");
                String productId = rs.getString("productId");
                String feedbackInfo = rs.getString("feedbackInfo");
                String feedbackImg = rs.getString("feedbackImg");
                String feedbackTime = rs.getString("feedbackTime");
                int feedbackRate = rs.getInt("feedbackRate");
                int statuss = rs.getInt("status");

                if (feedbackImg == null) {
                    feedbackImg = "";
                }

                Feedback feedback = new Feedback(feedbackId, userId, productId, feedbackInfo, feedbackTime, feedbackImg, feedbackRate, statuss);
                list.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Feedback> getFeedbackBySearchOrderByName(int index, String search, String status, String order) {
        List<Feedback> list = new ArrayList<>();
        String sql = "";

        // Trường hợp 1: Chỉ có search, không có status
        if ((status == null || status.isEmpty()) && (search != null && !search.isEmpty())) {
            sql = "SELECT f.*, a.firstName, a.lastName, p.name \n"
                    + "FROM feedback f JOIN account a \n"
                    + "ON f.userId = a.userId \n"
                    + "JOIN product p \n"
                    + "ON f.productId = p.productId\n"
                    + "WHERE p.name Like ? OR f.feedbackInfo LIKE ? \n"
                    + "order by f.productId " + order + " \n"
                    + "limit 5 offset ?";
            System.out.println("case1");
            System.out.println("---------");
        } // Trường hợp 2: Chỉ có status, không có search
        else if ((status != null && !status.isEmpty()) && (search == null || search.isEmpty())) {
            sql = "SELECT f.*, a.firstName, a.lastName, p.name \n"
                    + "FROM carpipi.feedback f JOIN carpipi.account a \n"
                    + "ON f.userId = a.userId \n"
                    + "JOIN carpipi.product p \n"
                    + "ON f.productId = p.productId\n"
                    + "WHERE f.status = ?\n"
                    + "order by f.productId " + order + " \n"
                    + "limit 5 offset ?";
            System.out.println("case2");
            System.out.println("---------");
        } // Trường hợp 3: Có cả search và status
        else if ((status != null && !status.isEmpty()) && (search != null && !search.isEmpty())) {
            sql = "SELECT f.*, a.firstName, a.lastName, p.name \n"
                    + "FROM carpipi.feedback f JOIN carpipi.account a \n"
                    + "ON f.userId = a.userId \n"
                    + "JOIN carpipi.product p \n"
                    + "ON f.productId = p.productId\n"
                    + "WHERE (p.name Like ? OR f.feedbackInfo LIKE ?) And f.status = ?\n"
                    + "order by f.productId " + order + " \n"
                    + "limit 5 offset ?";
            System.out.println("case3");
            System.out.println("---------");
        } // Trường hợp 4: Status là "10" và có search

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            // Thiết lập tham số cho từng trường hợp
            if ((status == null || status.isEmpty()) && (search != null && !search.isEmpty())) { // case 1
                st.setString(1, "%" + search + "%");
                st.setString(2, "%" + search + "%");
                st.setInt(3, ((index - 1) * 5));
            } else if ((status != null && !status.isEmpty()) && (search == null || search.isEmpty())) { // case 2
                st.setString(1, status);
                st.setInt(2, ((index - 1) * 5));
            } else if ((status != null && !status.isEmpty()) && (search != null && !search.isEmpty())) { // case 3
                st.setString(1, "%" + search + "%");
                st.setString(2, "%" + search + "%");
                st.setString(3, status);
                st.setInt(4, ((index - 1) * 5));
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedbackId");
                int userId = rs.getInt("userId");
                String productId = rs.getString("productId");
                String feedbackInfo = rs.getString("feedbackInfo");
                String feedbackImg = rs.getString("feedbackImg");
                String feedbackTime = rs.getString("feedbackTime");
                int feedbackRate = rs.getInt("feedbackRate");
                int statuss = rs.getInt("status");

                if (feedbackImg == null) {
                    feedbackImg = "";
                }

                Feedback feedback = new Feedback(feedbackId, userId, productId, feedbackInfo, feedbackTime, feedbackImg, feedbackRate, statuss);
                list.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Feedback> getFeedbackBySearchOrderById(int index, String search, String status, String order) {
        List<Feedback> list = new ArrayList<>();
        String sql = "";

        // Trường hợp 1: Chỉ có search, không có status
        if ((status == null || status.isEmpty()) && (search != null && !search.isEmpty())) {
            sql = "SELECT f.*, a.firstName, a.lastName, p.name \n"
                    + "FROM feedback f JOIN account a \n"
                    + "ON f.userId = a.userId \n"
                    + "JOIN product p \n"
                    + "ON f.productId = p.productId\n"
                    + "WHERE p.name Like ? OR f.feedbackInfo LIKE ? \n"
                    + "order by f.feedbackId " + order + " \n"
                    + "limit 5 offset ?";
            System.out.println("case1");
            System.out.println("---------");
        } // Trường hợp 2: Chỉ có status, không có search
        else if ((status != null && !status.isEmpty()) && (search == null || search.isEmpty())) {
            sql = "SELECT f.*, a.firstName, a.lastName, p.name \n"
                    + "FROM carpipi.feedback f JOIN carpipi.account a \n"
                    + "ON f.userId = a.userId \n"
                    + "JOIN carpipi.product p \n"
                    + "ON f.productId = p.productId\n"
                    + "WHERE f.status = ?\n"
                    + "order by f.feedbackId " + order + " \n"
                    + "limit 5 offset ?";
            System.out.println("case2");
            System.out.println("---------");
        } // Trường hợp 3: Có cả search và status
        else if ((status != null && !status.isEmpty()) && (search != null && !search.isEmpty())) {
            sql = "SELECT f.*, a.firstName, a.lastName, p.name \n"
                    + "FROM carpipi.feedback f JOIN carpipi.account a \n"
                    + "ON f.userId = a.userId \n"
                    + "JOIN carpipi.product p \n"
                    + "ON f.productId = p.productId\n"
                    + "WHERE (p.name Like ? OR f.feedbackInfo LIKE ?) And f.status = ?\n"
                    + "order by f.feedbackId " + order + " \n"
                    + "limit 5 offset ?";
            System.out.println("case3");
            System.out.println("---------");
        } // Trường hợp 4: Status là "10" và có search

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            // Thiết lập tham số cho từng trường hợp
            if ((status == null || status.isEmpty()) && (search != null && !search.isEmpty())) { // case 1
                st.setString(1, "%" + search + "%");
                st.setString(2, "%" + search + "%");
                st.setInt(3, ((index - 1) * 5));
            } else if ((status != null && !status.isEmpty()) && (search == null || search.isEmpty())) { // case 2
                st.setString(1, status);
                st.setInt(2, ((index - 1) * 5));
            } else if ((status != null && !status.isEmpty()) && (search != null && !search.isEmpty())) { // case 3
                st.setString(1, "%" + search + "%");
                st.setString(2, "%" + search + "%");
                st.setString(3, status);
                st.setInt(4, ((index - 1) * 5));
            }

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int feedbackId = rs.getInt("feedbackId");
                int userId = rs.getInt("userId");
                String productId = rs.getString("productId");
                String feedbackInfo = rs.getString("feedbackInfo");
                String feedbackImg = rs.getString("feedbackImg");
                String feedbackTime = rs.getString("feedbackTime");
                int feedbackRate = rs.getInt("feedbackRate");
                int statuss = rs.getInt("status");

                if (feedbackImg == null) {
                    feedbackImg = "";
                }

                Feedback feedback = new Feedback(feedbackId, userId, productId, feedbackInfo, feedbackTime, feedbackImg, feedbackRate, statuss);
                list.add(feedback);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public static void main(String[] args) {
        FeedbackDAO f = new FeedbackDAO();
//        List<Feedback> l = f.getFeedbackBySearchAndPaging(1, "me", "");
//
//        for (Feedback feedback : l) {
//            System.out.println(feedback.getFeedbackRate());
//        }
String feedbackImage = f.getFeedbackImageById(1); // Truy vấn ảnh của feedback với feedbackId = 1
System.out.println("Feedback Image: " + feedbackImage);
    }
}
