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
import model.Product;
import model.Style;

/**
 *
 * @author Sonvu
 */
public class StyleDAO extends DBContext {

    public int getStyleIdByName(String name) {
        String sql = "SELECT styleId FROM carpipi.style where styleName = ?";

        String styleId = null; // Biến để lưu kết quả trả về

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1, name);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                styleId = rs.getString("styleId");
            }

        } catch (SQLException e) {
        }

        int sId = Integer.parseInt(styleId);

        return sId;
    }

    public List<Style> getAllStyleCar() {

        List<Style> listStyle = new ArrayList<>();

        String sql = "SELECT * FROM carpipi.style where status = 1";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Style style = new Style();

                style.setStyleId(rs.getString("styleId"));
                style.setStyleName(rs.getString("styleName"));
                listStyle.add(style);

            }

        } catch (SQLException e) {
        }

        return listStyle;
    }

    public String getStyleNameByStyleId(int styleId) {
        String styleName = null;
        String sql = "SELECT styleName FROM style WHERE styleId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, styleId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                styleName = rs.getString("styleName");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return styleName;
    }

    // Lấy danh sách các Style với số lượng sản phẩm và trạng thái
    public List<Style> getStyleListWithProductCount() {
        List<Style> styleList = new ArrayList<>();
        String sql = "SELECT s.styleId, s.styleName, s.status, COUNT(p.productId) AS styleProductCount "
                + "FROM style s LEFT JOIN product p ON s.styleId = p.styleId "
                + "GROUP BY s.styleId, s.styleName, s.status";

        try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Style segment = new Style();
                segment.setStyleId(rs.getString("styleId"));
                segment.setStyleName(rs.getString("styleName"));
                segment.setStatus(rs.getInt("status"));
                segment.setStyleProductCount(rs.getInt("styleProductCount"));
                styleList.add(segment);

                // Print each brand's details
                System.out.println("style ID: " + segment.getStyleId()
                        + ",style Name: " + segment.getStyleName()
                        + ", Status: " + segment.getStatus()
                        + ",style Product Count: " + segment.getStyleProductCount());
            }

            System.out.println("Total Segments fetched: " + styleList.size()); // Debugging
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return styleList;
    }

    public List<Style> getStyleListWithPagination(String search, int status, int offset, int limit) {
        List<Style> styleList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT s.styleId, s.styleName, s.status, COUNT(p.productId) AS styleProductCount "
                + "FROM style s LEFT JOIN product p ON s.styleId = p.styleId");

        boolean hasCondition = false;

        // Thêm điều kiện lọc theo status
        if (status != -1) {
            sql.append(" WHERE s.status = ?");
            hasCondition = true;
        }

        // Thêm điều kiện tìm kiếm theo tên style nếu có
        if (search != null && !search.isEmpty()) {
            if (hasCondition) {
                sql.append(" AND s.styleName LIKE ?");
            } else {
                sql.append(" WHERE s.styleName LIKE ?");
                hasCondition = true;
            }
        }

        // Nhóm theo styleId và styleName
        sql.append(" GROUP BY s.styleId, s.styleName, s.status");

        // Thêm điều kiện LIMIT và OFFSET để phân trang
        sql.append(" LIMIT ? OFFSET ?");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int index = 1;

            // Thiết lập giá trị cho status
            if (status != -1) {
                ps.setInt(index++, status);
            }

            // Thiết lập giá trị cho điều kiện tìm kiếm
            if (search != null && !search.isEmpty()) {
                ps.setString(index++, "%" + search + "%");
            }

            // Thiết lập giá trị cho LIMIT và OFFSET
            ps.setInt(index++, limit);
            ps.setInt(index, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Style style = new Style();
                    style.setStyleId(rs.getString("styleId"));
                    style.setStyleName(rs.getString("styleName"));
                    style.setStatus(rs.getInt("status"));
                    style.setStyleProductCount(rs.getInt("styleProductCount"));
                    styleList.add(style);

                    // Debugging: Print each style's details
                    System.out.println("Style ID: " + style.getStyleId()
                            + ", Style Name: " + style.getStyleName()
                            + ", Status: " + style.getStatus()
                            + ", Style Product Count: " + style.getStyleProductCount());
                }
            }

            // Debugging: Print total number of styles fetched
            System.out.println("Total Styles fetched: " + styleList.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return styleList;
    }

    // Cập nhật trạng thái của brand
    public boolean updateStyleStatus(int styleId, int status) {
        String sql = "UPDATE style SET status = ? WHERE styleId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, styleId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Style> getStyleFilter(String[] ids) {

        List<Style> list = new ArrayList<>();

        int count = 0;
        if (ids != null) {
            count = ids.length;
        } else {
            count = 0;
        }

        StringBuilder bonusSql = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                // Thêm điều kiện đầu tiên với styleId
                bonusSql.append("styleId = ").append(ids[i]);
            } else {
                // Các điều kiện tiếp theo sử dụng OR
                bonusSql.append(" OR styleId = ").append(ids[i]);
            }
        }

        System.out.println(bonusSql);

        String sql = "SELECT * FROM carpipi.style WHERE (" + bonusSql.toString() + ") AND status = 1";

        System.out.println(sql);

        try {
            PreparedStatement st = connection.prepareStatement(sql);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Style style = new Style();

                style.setStyleId(rs.getString("styleId"));
                style.setStyleName(rs.getString("styleName"));
                style.setStatus(rs.getInt("status"));
                list.add(style);

            }

        } catch (SQLException e) {
        }

        return list;
    }

    public int countForFilter(String[] countSome) {
        int count = 0;
        if (countSome != null) {
            count = countSome.length;
        } else {
            count = 0;
        }

        return count;

    }

    public String checkFilter(String brandId, String[] styleId, String[] segmentId, String[] supplyId) {

        StringBuilder bonusSql = new StringBuilder();
        String sql = "";
        String string = "";

        if (brandId != null && !brandId.isEmpty()) {
            sql = " brandId = " + brandId + " and";

        } else {
            System.out.println("No brand");
        }

        if (styleId == null && segmentId != null && supplyId != null) {

            int countSegmentId = countForFilter(segmentId);
            int countSupplyId = countForFilter(supplyId);

            for (int i = 0; i < countSegmentId; i++) {
                if (i == 0) {
                    // Thêm điều kiện đầu tiên với styleId
                    bonusSql.append(" ( pro.segmentId = ").append(segmentId[i]);
                } else if (i < 1) {
                    bonusSql.append(")");
                } else {
                    // Các điều kiện tiếp theo sử dụng OR
                    bonusSql.append(" OR pro.segmentId = ").append(segmentId[i]);

                }
            }
            if (countSegmentId >= 1) {
                bonusSql.append(")");
            }

            bonusSql.append(" and ");

            for (int i = 0; i < countSupplyId; i++) {
                if (i == 0) {
                    // Thêm điều kiện đầu tiên với styleId
                    bonusSql.append(" ( pro.supplyId = ").append(supplyId[i]);
                } else if (i < 1) {
                    bonusSql.append(")");
                } else {
                    // Các điều kiện tiếp theo sử dụng OR
                    bonusSql.append(" OR pro.supplyId = ").append(supplyId[i]);
                    bonusSql.append(")");
                }
            }
            if (countSupplyId >= 1) {
                bonusSql.append(")");
            }

            sql += bonusSql;

        } else if (segmentId == null && styleId != null && supplyId != null) {

            int countStyleIds = countForFilter(styleId);
            int countSupplyId = countForFilter(supplyId);

            for (int i = 0; i < countStyleIds; i++) {
                if (i == 0) {
                    // Thêm điều kiện đầu tiên với styleId
                    bonusSql.append(" ( pro.styleId = ").append(styleId[i]);
                } else if (i < 1) {
                    bonusSql.append(")");
                } else {
                    // Các điều kiện tiếp theo sử dụng OR
                    bonusSql.append(" OR pro.styleId = ").append(styleId[i]);

                }
            }
            if (countStyleIds >= 1) {
                bonusSql.append(")");
            }

            bonusSql.append(" and ");

            for (int i = 0; i < countSupplyId; i++) {
                if (i == 0) {
                    // Thêm điều kiện đầu tiên với styleId
                    bonusSql.append(" ( pro.supplyId = ").append(supplyId[i]);
                } else if (i < 1) {
                    bonusSql.append(")");
                } else {
                    // Các điều kiện tiếp theo sử dụng OR
                    bonusSql.append(" OR pro.supplyId = ").append(supplyId[i]);

                }
            }
            if (countSupplyId >= 1) {
                bonusSql.append(")");
            }
            sql += bonusSql;
            System.out.println("bbbbbbbbb");

        } else if (supplyId == null && segmentId != null && styleId != null) {

            int countStyleIds = countForFilter(styleId);
            int countSegmentId = countForFilter(segmentId);

            for (int i = 0; i < countStyleIds; i++) {
                if (i == 0) {
                    // Thêm điều kiện đầu tiên với styleId
                    bonusSql.append(" ( pro.styleId = ").append(styleId[i]);
                } else if (i < 1) {
                    bonusSql.append(")");
                } else {
                    // Các điều kiện tiếp theo sử dụng OR
                    bonusSql.append(" OR pro.styleId = ").append(styleId[i]);

                }
            }
            if (countStyleIds >= 1) {
                bonusSql.append(")");
            }

            bonusSql.append(" and ");

            for (int i = 0; i < countSegmentId; i++) {
                if (i == 0) {
                    // Thêm điều kiện đầu tiên với styleId
                    bonusSql.append(" ( pro.segmentId = ").append(segmentId[i]);
                } else if (i < 1) {
                    bonusSql.append(")");
                } else {
                    // Các điều kiện tiếp theo sử dụng OR
                    bonusSql.append(" OR pro.segmentId = ").append(segmentId[i]);

                }
            }
            if (countSegmentId >= 1) {
                bonusSql.append(")");
            }

            sql += bonusSql;

        } else if ((styleId == null && segmentId == null) && supplyId != null) {

            int countSupplyId = countForFilter(supplyId);
            for (int i = 0; i < countSupplyId; i++) {
                if (i == 0) {
                    // Thêm điều kiện đầu tiên với styleId
                    bonusSql.append("( pro.supplyId = ").append(supplyId[i]);
                } else if (i < 1) {
                    bonusSql.append(")");
                } else {
                    // Các điều kiện tiếp theo sử dụng OR
                    bonusSql.append(" OR pro.supplyId = ").append(supplyId[i]);

                }
            }

            if (countSupplyId >= 1) {
                bonusSql.append(")");
            }

            sql += bonusSql;

        } else if ((styleId == null && supplyId == null) && segmentId != null) {

            int countSegmentId = countForFilter(segmentId);
            for (int i = 0; i < countSegmentId; i++) {
                if (i == 0) {
                    // Thêm điều kiện đầu tiên với styleId
                    bonusSql.append("( pro.segmentId = ").append(segmentId[i]);
                } else if (i < 1) {
                    bonusSql.append(")");
                } else {
                    // Các điều kiện tiếp theo sử dụng OR
                    bonusSql.append(" OR pro.segmentId = ").append(segmentId[i]);

                }
            }

            if (countSegmentId >= 1) {
                bonusSql.append(")");
            }
            sql += bonusSql;

        } else if ((segmentId == null && supplyId == null) && styleId != null) {
            int countStyleId = countForFilter(styleId);
            for (int i = 0; i < countStyleId; i++) {
                if (i == 0) {
                    // Thêm điều kiện đầu tiên với styleId
                    bonusSql.append("( pro.styleId = ").append(styleId[i]);
                } else if (i < 1) {
                    bonusSql.append(")");
                } else {
                    // Các điều kiện tiếp theo sử dụng OR
                    bonusSql.append(" OR pro.styleId = ").append(styleId[i]);

                }
            }
            if (countStyleId >= 1) {
                bonusSql.append(")");
            }

            sql += bonusSql;
        } else if (styleId == null && segmentId == null && supplyId == null) {
            sql = "sup.status = 1";
            System.out.println("aaaaaaaaaaa");
        } else if (styleId != null && segmentId != null && supplyId != null) {

            int countStyleId = countForFilter(styleId);
            int countSupplyId = countForFilter(supplyId);
            int countSegmentId = countForFilter(segmentId);

            for (int i = 0; i < countStyleId; i++) {
                if (i == 0) {
                    // Thêm điều kiện đầu tiên với styleId
                    bonusSql.append("( pro.styleId = ").append(styleId[i]);
                } else if (i < 1) {
                    bonusSql.append(") AND ");
                } else {
                    // Các điều kiện tiếp theo sử dụng OR
                    bonusSql.append(" OR pro.styleId = ").append(styleId[i]);

                }
            }

            if (countStyleId >= 1) {
                bonusSql.append(") AND ");
            }

            for (int i = 0; i < countSegmentId; i++) {
                if (i == 0) {
                    // Thêm điều kiện đầu tiên với segmentId
                    bonusSql.append("( pro.segmentId = ").append(segmentId[i]);
                } else if (i < 1) {
                    bonusSql.append(") AND ");
                } else {
                    // Các điều kiện tiếp theo sử dụng OR
                    bonusSql.append(" OR pro.segmentId = ").append(segmentId[i]);

                }
            }

            if (countSegmentId >= 1) {
                bonusSql.append(") AND ");
            }

            for (int i = 0; i < countSupplyId; i++) {
                if (i == 0) {
                    // Thêm điều kiện đầu tiên với supplyId
                    bonusSql.append("( pro.supplyId = ").append(supplyId[i]);
                } else if (i < 1) {
                    bonusSql.append(")");
                } else {
                    // Các điều kiện tiếp theo sử dụng OR
                    bonusSql.append(" OR pro.supplyId = ").append(supplyId[i]);

                }
            }

            if (countSupplyId >= 1) {
                bonusSql.append(")");
            }
            sql += bonusSql;
        }

        return sql;
    }

    public List<Product> getListFilter(String brandId, String[] styleId, String[] segmentId, String[] supplyId) {

        List<Product> list = new ArrayList<>();

        String string = checkFilter(brandId, styleId, segmentId, supplyId);

        System.out.println("string: " + string);

        String sql = "SELECT pro.* FROM \n"
                + "carpipi.product pro Join carpipi.style sty\n"
                + "On pro.styleId = sty.styleId\n"
                + "Join carpipi.segment seg \n"
                + "On seg.segmentId = pro.segmentId\n"
                + "Join carpipi.supply sup \n"
                + "On sup.supplyId = pro.supplyId\n"
                + "Where " + string + "\n"
                + "And sty.status = 1\n"
                + "AND seg.status = 1 \n"
                + "And sup.status = 1\n"
                + "ORDER BY pro.productId ASC ";

        System.out.println("sql: " + sql);

        try {

            PreparedStatement st = connection.prepareStatement(sql);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getString("productId"));
                product.setName(rs.getString("name"));
                product.setSeatNumber(rs.getInt("seatNumber"));
                product.setPrice(rs.getDouble("price"));
                product.setFuel(rs.getString("fuel"));
                product.setStock(rs.getInt("stock"));
                product.setDescription(rs.getString("description"));
                product.setVAT(rs.getDouble("VAT"));
                product.setSupplyId(rs.getInt("supplyId"));
                product.setBrandId(rs.getInt("brandId"));
                product.setSegmentId(rs.getInt("segmentId"));
                product.setStyleId(rs.getInt("styleId"));
                product.setStatus(rs.getInt("status"));

                list.add(product);

            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return list;
    }

    public List<Product> getListFilterOrderByName(String brandId, String[] styleId, String[] segmentId, String[] supplyId, String order) {

        List<Product> list = new ArrayList<>();

        String string = checkFilter(brandId, styleId, segmentId, supplyId);

        System.out.println("string: " + string);

        String sql = "SELECT pro.* FROM \n"
                + "carpipi.product pro Join carpipi.style sty\n"
                + "On pro.styleId = sty.styleId\n"
                + "Join carpipi.segment seg \n"
                + "On seg.segmentId = pro.segmentId\n"
                + "Join carpipi.supply sup \n"
                + "On sup.supplyId = pro.supplyId\n"
                + "Where " + string + "\n"
                + "And sty.status = 1\n"
                + "AND seg.status = 1 \n"
                + "And sup.status = 1\n"
                + "ORDER BY pro.name " + order + " ";

        System.out.println("sql: " + sql);

        try {

            PreparedStatement st = connection.prepareStatement(sql);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getString("productId"));
                product.setName(rs.getString("name"));
                product.setSeatNumber(rs.getInt("seatNumber"));
                product.setPrice(rs.getDouble("price"));
                product.setFuel(rs.getString("fuel"));
                product.setStock(rs.getInt("stock"));
                product.setDescription(rs.getString("description"));
                product.setVAT(rs.getDouble("VAT"));
                product.setSupplyId(rs.getInt("supplyId"));
                product.setBrandId(rs.getInt("brandId"));
                product.setSegmentId(rs.getInt("segmentId"));
                product.setStyleId(rs.getInt("styleId"));
                product.setStatus(rs.getInt("status"));

                list.add(product);

            }
        } catch (Exception e) {
        }

        return list;
    }

    public List<Product> getListFilterOrderByPrice(String brandId, String[] styleId, String[] segmentId, String[] supplyId, String order) {

        List<Product> list = new ArrayList<>();

        String string = checkFilter(brandId, styleId, segmentId, supplyId);

        System.out.println("string: " + string);

        String sql = "SELECT pro.* FROM \n"
                + "carpipi.product pro Join carpipi.style sty\n"
                + "On pro.styleId = sty.styleId\n"
                + "Join carpipi.segment seg \n"
                + "On seg.segmentId = pro.segmentId\n"
                + "Join carpipi.supply sup \n"
                + "On sup.supplyId = pro.supplyId\n"
                + "Where " + string + "\n"
                + "And sty.status = 1\n"
                + "AND seg.status = 1 \n"
                + "And sup.status = 1\n"
                + "ORDER BY pro.price " + order + " ";

        System.out.println("sql: " + sql);

        try {

            PreparedStatement st = connection.prepareStatement(sql);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getString("productId"));
                product.setName(rs.getString("name"));
                product.setSeatNumber(rs.getInt("seatNumber"));
                product.setPrice(rs.getDouble("price"));
                product.setFuel(rs.getString("fuel"));
                product.setStock(rs.getInt("stock"));
                product.setDescription(rs.getString("description"));
                product.setVAT(rs.getDouble("VAT"));
                product.setSupplyId(rs.getInt("supplyId"));
                product.setBrandId(rs.getInt("brandId"));
                product.setSegmentId(rs.getInt("segmentId"));
                product.setStyleId(rs.getInt("styleId"));
                product.setStatus(rs.getInt("status"));

                list.add(product);

            }
        } catch (Exception e) {
        }

        return list;
    }

    public List<Product> getProductListByPage(List<Product> product, int start, int end) {
        ArrayList<Product> arr = new ArrayList<>();
        for (int i = start; i < end; i++) {
            arr.add(product.get(i));
        }
        return arr;
    }

    public void updateProductStatus(String productId, int status) {
        String sql = "UPDATE carpipi.product SET status = ? WHERE productId = ?";
        try (
                PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, status);
            stm.setString(2, productId);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStyle(String styleId, String styleName, int styleStatus) throws SQLException {
        String query = "UPDATE style SET styleName = ?, status = ? WHERE styleId = ?";
        try (
                PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, styleName);
            stmt.setInt(2, styleStatus);
            stmt.setString(3, styleId);
            stmt.executeUpdate();
        }
    }

    public Style getStyleById(String styleId) {
        Style style = null;
        String sql = "SELECT * FROM style WHERE styleId = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, styleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    style = new Style();
                    style.setStyleId(rs.getString("styleId"));
                    style.setStyleName(rs.getString("styleName"));
                    //  brand.setProductCount(rs.getInt("productCount"));
                    style.setStatus(rs.getInt("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return style;
    }

    public static void main(String[] args) {
        StyleDAO s = new StyleDAO();
//        List<Style> styletList = s.getStyleListWithProductCount();
//        System.out.println(s.getAllStyleCar());
// 
//    Style style = s.getStyleById("1");
//
//        if (style != null) {
//            System.out.println("Style ID: " + style.getStyleId());
//            System.out.println("Style Name: " + style.getStyleName());
//         //   System.out.println("Product Count: " + brand.getProductCount());
//            System.out.println("Status: " + style.getStatus());
//        } else {
//            System.out.println("No style found with the given ID.");
//        }

        try {
            s.updateStyle("1", "Sedan", 1);
        } catch (SQLException e) {
            e.printStackTrace();  // Handle the exception, maybe log it or notify the user
        }

    }
}
