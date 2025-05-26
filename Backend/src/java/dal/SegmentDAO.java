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
import model.Segment;

/**
 *
 * @author tuana
 */
public class SegmentDAO extends DBContext{
    
    public String getSegmentNameBySegmentId(int segmentId) {
        String segmentName = null;
        String sql = "SELECT segmentName FROM Segment WHERE segmentId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, segmentId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                segmentName = rs.getString("segmentName");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return segmentName;
    }
    
    public int getSegmentIdByName(String name) {
        String sql = "SELECT segmentId FROM carpipi.segment where segmentName = ?";

        String segmentId = null; // Biến để lưu kết quả trả về

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            
            while (rs.next()) {                
                segmentId = rs.getString("segmentId");
            }

        } catch (SQLException e) {
        }
        
        int sId = Integer.parseInt(segmentId);
        
        return sId;
    }
    
    public List<Segment> getAllSegment(){
        List<Segment> list = new ArrayList<>();
        String sql = "select * from Segment";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Segment p = new Segment(rs.getInt(1),
                        rs.getString(2));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
    
    // Lấy danh sách các segment với số lượng sản phẩm và trạng thái
    public List<Segment> getSegmentListWithProductCount() {
    List<Segment> segmentList = new ArrayList<>();
    String sql = "SELECT se.segmentId, se.segmentName, se.status, COUNT(p.productId) AS segmentProductCount " +
                 "FROM segment se LEFT JOIN product p ON se.segmentId = p.segmentId " +
                 "GROUP BY se.segmentId, se.segmentName, se.status";

    try (PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        
        while (rs.next()) {
            Segment segment = new Segment();
            segment.setSegmentId(rs.getInt("segmentId"));
            segment.setSegmentName(rs.getString("segmentName"));
            segment.setStatus(rs.getInt("status"));
            segment.setSegmentProductCount(rs.getInt("segmentProductCount"));
            segmentList.add(segment);
            
            // Print each brand's details
            System.out.println("Segment ID: " + segment.getSegmentId() + 
                               ",Segment Name: " + segment.getSegmentName() + 
                               ", Status: " + segment.getStatus() + 
                               ",Segment Product Count: " + segment.getSegmentProductCount());
        }
        
        System.out.println("Total Segments fetched: " + segmentList.size()); // Debugging
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return segmentList;
}
    
    public List<Segment> getSegmentListWithPagination(String search, int status, int offset, int limit) {
    List<Segment> segmentList = new ArrayList<>();
    String sql = "SELECT se.segmentId, se.segmentName, se.status, COUNT(p.productId) AS segmentProductCount " +
                 "FROM segment se LEFT JOIN product p ON se.segmentId = p.segmentId " +
                 "WHERE (? IS NULL OR se.segmentName LIKE ?) " +
                 "AND (? = -1 OR se.status = ?) " +
                 "GROUP BY se.segmentId, se.segmentName, se.status " +
                 "LIMIT ? OFFSET ?";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        // Set parameters
        ps.setString(1, search);
        ps.setString(2, search != null ? "%" + search + "%" : null);
        ps.setInt(3, status);
        ps.setInt(4, status);
        ps.setInt(5, limit);
        ps.setInt(6, offset);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Segment segment = new Segment();
                segment.setSegmentId(rs.getInt("segmentId"));
                segment.setSegmentName(rs.getString("segmentName"));
                segment.setStatus(rs.getInt("status"));
                segment.setSegmentProductCount(rs.getInt("segmentProductCount"));
                segmentList.add(segment);
                
                // In ra thông tin cho từng phân khúc
                System.out.println("Segment ID: " + segment.getSegmentId() + 
                                   ", Segment Name: " + segment.getSegmentName() + 
                                   ", Status: " + segment.getStatus() + 
                                   ", Segment Product Count: " + segment.getSegmentProductCount());
            }
            
            System.out.println("Total segments fetched: " + segmentList.size()); // Debugging
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return segmentList;
}

    
    // Cập nhật trạng thái của segment
    public boolean updateSegmentStatus(int segmentId, int status) {
        String sql = "UPDATE segment SET status = ? WHERE segmentId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, segmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Segment getSegmentById(int segmentId) {
        Segment segment = null;
        String sql = "SELECT * FROM segment WHERE segmentId = ?";
        
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, segmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    segment = new Segment();
                    segment.setSegmentId(rs.getInt("segmentId"));
                    segment.setSegmentName(rs.getString("segmentName"));
                    segment.setStatus(rs.getInt("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return segment;
    }
    
    public void updateSegment(int segmentId, String segmentName, int segmentStatus) {
        String query = "UPDATE segment SET segmentName = ?, status = ? WHERE segmentId = ?";
        
        try (
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, segmentName);
            ps.setInt(2, segmentStatus); // Cập nhật status với giá trị mới
            ps.setInt(3, segmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        SegmentDAO s = new SegmentDAO();
        List<Segment> segmentList = s.getSegmentListWithProductCount();
        //System.out.println(s.getAllSupplyCar());
    }
}
