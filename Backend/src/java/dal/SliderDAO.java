/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import model.Slider;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 *
 * @author ADMIN
 */
public class SliderDAO extends DBContext {

    public List<Slider> getFilteredSliders(String search, int status, int offset, int limit) {
        List<Slider> sliders = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM slider");

        boolean hasCondition = false;

        if (status != -1) {
            sql.append(" WHERE status = ?");
            hasCondition = true; // Đánh dấu có điều kiện
        }

        // Thêm điều kiện tìm kiếm nếu có
        if (search != null && !search.isEmpty()) {
            if (hasCondition) {
                sql.append(" AND (title LIKE ? OR backlink LIKE ?)");
            } else {
                sql.append(" WHERE (title LIKE ? OR backlink LIKE ?)");
            }
        }

        sql.append(" LIMIT ? OFFSET ?"); // Thêm LIMIT và OFFSET

        try (PreparedStatement stm = connection.prepareStatement(sql.toString())) {
            int index = 1;

            // Nếu có điều kiện status, thiết lập tham số
            if (hasCondition) {
                stm.setInt(index++, status);
            }

            // Thiết lập tham số tìm kiếm
            if (search != null && !search.isEmpty()) {
                String searchParam = "%" + search + "%";
                stm.setString(index++, searchParam);
                stm.setString(index++, searchParam);
            }

            // Thiết lập LIMIT và OFFSET
            stm.setInt(index++, limit);
            stm.setInt(index, offset);

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Slider slider = new Slider();
                slider.setSliderId(rs.getInt("sliderId"));
                slider.setProductId(rs.getString("productId"));
                slider.setTitle(rs.getString("title"));
                slider.setDescription(rs.getString("description"));
                slider.setImageUrl(rs.getString("imageUrl"));
                slider.setBacklink(rs.getString("backlink"));
                slider.setStatus(rs.getInt("status"));
                sliders.add(slider);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra lỗi để kiểm tra
        }

        return sliders;
    }

    public List<Slider> getAllActiveSlider() {
        List<Slider> activeSliders = new ArrayList<>();
        String sql = "SELECT * FROM slider WHERE status = 1"; // Giả sử trạng thái 1 là hoạt động

        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Slider slider = new Slider();
                slider.setSliderId(resultSet.getInt("sliderId"));
                slider.setProductId(resultSet.getString("productId"));
                slider.setTitle(resultSet.getString("title"));
                slider.setDescription(resultSet.getString("description"));
                slider.setImageUrl(resultSet.getString("imageUrl"));
                slider.setBacklink(resultSet.getString("backlink"));
                slider.setStatus(resultSet.getInt("status"));

                activeSliders.add(slider);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý lỗi
        }

        return activeSliders;
    }

    // Phương thức cập nhật trạng thái slider
    public void updateSliderStatus(int sliderId, int status) {
        String sql = "UPDATE slider SET status = ? WHERE sliderId = ?";
        try (
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, status);
            stm.setInt(2, sliderId);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ManhHuy ---
    public Slider getSliderById(int sliderId) {
        String sql = "SELECT * FROM Slider WHERE sliderId = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {

            stm.setInt(1, sliderId);
            try (ResultSet resultSet = stm.executeQuery()) {
                if (resultSet.next()) {
                    Slider slider = new Slider();
                    slider.setSliderId(resultSet.getInt("sliderId"));
                    slider.setProductId(resultSet.getString("productId"));
                    slider.setTitle(resultSet.getString("title"));
                    slider.setDescription(resultSet.getString("description"));
                    slider.setImageUrl(resultSet.getString("imageUrl"));
                    slider.setBacklink(resultSet.getString("backlink"));
                    slider.setStatus(resultSet.getInt("status"));
                    return slider;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ManhHuy ---
    public boolean updateSlider(int sliderId, String productId, String title, String backlink, int status, String description, String imagePath) {
        String sql = "UPDATE slider SET title = ?, backlink = ?, status = ?, description = ?, imageUrl = ?, productId = ? WHERE sliderId = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, title);
            stm.setString(2, backlink);
            stm.setInt(3, status);
            stm.setString(4, description);

            if (imagePath != null) {
                stm.setString(5, imagePath);
            } else {
                Slider currentSlider = getSliderById(sliderId); // Lấy slider hiện tại
                stm.setString(5, currentSlider.getImageUrl()); // Giữ nguyên ảnh cũ nếu không cập nhật
            }

            stm.setString(6, productId);
            stm.setInt(7, sliderId);

            int rowsUpdated = stm.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public int getSliderCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM slider";

        try (
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1); // Lấy giá trị của cột đầu tiên
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
    
    public int getActiveSliderCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM slider where status=1";

        try (
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1); // Lấy giá trị của cột đầu tiên
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
    
    
    public static void main(String[] args) {
        SliderDAO dao = new SliderDAO();
        Slider slider = dao.getSliderById(1);
        dao.updateSlider(1, "Au01", "test", "test", 1, "test", null);
        System.out.println(slider.getTitle() + slider.getDescription());
        
        int sliderCount = dao.getSliderCount();
        System.out.println("tong so slider"+sliderCount);
    }

}
