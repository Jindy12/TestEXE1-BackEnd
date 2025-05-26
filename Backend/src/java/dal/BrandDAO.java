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
import model.Brand;

/**
 *
 * @author tuana
 */
public class BrandDAO extends DBContext {

    public int getBrandIdByName(String name) {
        String sql = "SELECT brandId FROM carpipi.brand where name = ?";

        String brandId = ""; // Biến để lưu kết quả trả về

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, name);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                brandId = rs.getString("brandId");
            }

        } catch (SQLException e) {
        }

        int bId = Integer.parseInt(brandId);

        return bId;
    }

    public List<Brand> getAllBrand() {
    List<Brand> list = new ArrayList<>();
    String sql = "SELECT * FROM brand where status =1";
    
    try (PreparedStatement st = connection.prepareStatement(sql);
         ResultSet rs = st.executeQuery()) {
        
        while (rs.next()) {
            Brand brand = new Brand(rs.getInt("brandId"),
                                    rs.getString("name"),
                                    rs.getString("image"));
            list.add(brand);
        }
        
    } catch (SQLException e) {
        System.out.println("Error in getAllBrand: " + e.getMessage());
    }
    
    return list;
}

    public String getBrandById(int brandId) {
        String sql = "select * from Brand where brandId=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, brandId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return rs.getString(2);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    
    // Method to get brand details by brandId
    public Brand getBrandById1(int brandId) {
        Brand brand = null;
        String sql = "SELECT * FROM brand WHERE brandId = ?";
        
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, brandId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    brand = new Brand();
                    brand.setBrandId(rs.getInt("brandId"));
                    brand.setName(rs.getString("name"));
                    brand.setStatus(rs.getInt("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return brand;
    }
    
    // Lấy danh sách các brand với số lượng sản phẩm và trạng thái
    public List<Brand> getBrandListWithProductCount() {
    List<Brand> brandList = new ArrayList<>();
    String sql = "SELECT b.brandId, b.name, b.status, COUNT(p.productId) AS productCount " +
                 "FROM brand b LEFT JOIN product p ON b.brandId = p.brandId " +
                 "GROUP BY b.brandId, b.name, b.status";

    try (PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        
        while (rs.next()) {
            Brand brand = new Brand();
            brand.setBrandId(rs.getInt("brandId"));
            brand.setName(rs.getString("name"));
            brand.setStatus(rs.getInt("status"));
            brand.setProductCount(rs.getInt("productCount"));
            brandList.add(brand);
            
            // Print each brand's details
            System.out.println("Brand ID: " + brand.getBrandId() + 
                               ", Name: " + brand.getName() + 
                               ", Status: " + brand.getStatus() + 
                               ", Product Count: " + brand.getProductCount());
        }
        
        System.out.println("Total brands fetched: " + brandList.size()); // Debugging
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return brandList;
}
    public List<Brand> getBrandListWithPagination(String search, int status, int offset, int limit) {
    List<Brand> brandList = new ArrayList<>();
    StringBuilder sql = new StringBuilder("SELECT b.brandId, b.name, b.status, COUNT(p.productId) AS productCount " +
                                          "FROM brand b LEFT JOIN product p ON b.brandId = p.brandId ");
    
    boolean hasCondition = false;

    // Điều kiện lọc theo status
    if (status != -1) {
        sql.append("WHERE b.status = ? ");
        hasCondition = true;
    }

    // Điều kiện tìm kiếm
    if (search != null && !search.isEmpty()) {
        if (hasCondition) {
            sql.append("AND b.name LIKE ? ");
        } else {
            sql.append("WHERE b.name LIKE ? ");
            hasCondition = true;
        }
    }

    sql.append("GROUP BY b.brandId, b.name, b.status ");
    sql.append("LIMIT ? OFFSET ?");

    try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
        int paramIndex = 1;

        // Thiết lập tham số cho status
        if (status != -1) {
            ps.setInt(paramIndex++, status);
        }

        // Thiết lập tham số tìm kiếm
        if (search != null && !search.isEmpty()) {
            ps.setString(paramIndex++, "%" + search + "%");
        }

        // Thiết lập tham số LIMIT và OFFSET
        ps.setInt(paramIndex++, limit);
        ps.setInt(paramIndex, offset);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Brand brand = new Brand();
                brand.setBrandId(rs.getInt("brandId"));
                brand.setName(rs.getString("name"));
                brand.setStatus(rs.getInt("status"));
                brand.setProductCount(rs.getInt("productCount"));
                brandList.add(brand);
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return brandList;
}

    // Cập nhật trạng thái của brand
    public boolean updateBrandStatus(int brandId, int status) {
        String sql = "UPDATE brand SET status = ? WHERE brandId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, brandId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void updateBrand(int brandId, String brandName, int brandStatus) {
        String query = "UPDATE Brand SET name = ?, status = ? WHERE brandId = ?";
        
        try (
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, brandName);
            ps.setInt(2, brandStatus); // Cập nhật status với giá trị mới
            ps.setInt(3, brandId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
public List<Brand> getTotalRevenueByBrand() {
        List<Brand> revenues = new ArrayList<>();
        String query = "SELECT b.brandId, b.name AS brandName, "
                + "SUM(od.quantity * p.price * 2.2) AS totalRevenue "
                + "FROM `order` AS o "
                + "JOIN orderdetail AS od ON o.orderId = od.orderId "
                + "JOIN product AS p ON od.productId = p.productId "
                + "JOIN brand AS b ON p.brandId = b.brandId "
                + "WHERE o.orderStatus = 4 "
                + "GROUP BY b.brandId, b.name;";

        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int brandId = rs.getInt("brandId");
                String brandName = rs.getString("brandName");
                double totalRevenue = rs.getDouble("totalRevenue");

                Brand brand = new Brand();
                brand.setBrandId(brandId);
                brand.setName(brandName);
                brand.setTotalRevenue(totalRevenue);

                revenues.add(brand);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        return revenues;
    }
    
    public static void main(String[] args) {
        //System.out.println(b.getBrandIdByName("Audi"));
//        BrandDAO b = new BrandDAO();
//    //List<Brand> brandList = b.getBrandListWithProductCount();
//    List<Brand> brandList = b.getTotalRevenueByBrand();
//
//if (brandList.isEmpty()) {
//    System.out.println("No brands found with revenue.");
//} else {
//    System.out.println("Brand Revenue:");
//    for (Brand brand : brandList) {
//        System.out.printf("%s\t%.6f%n", brand.getName(), brand.getTotalRevenue());
//    }
//}
BrandDAO brandDAO = new BrandDAO();
brandDAO.updateBrand(1, "New Brand Name", 1);
        
    // Check if brandList is empty
    
}
}
    
