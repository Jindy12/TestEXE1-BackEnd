/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Blog;
import model.ImageBlog;

/**
 *
 * @author tuana
 */
public class BlogDAO extends DBContext {

    public List<Blog> getAllBlog() {
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM Blog";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int blogId = rs.getInt("blogId");
                int userId = rs.getInt("userId");
                int blogTopicId = rs.getInt("blogTopicId");
                String blogTitle = rs.getString("blogTitle");
                String blogTime = rs.getString("blogTime");
                String openBlog = rs.getString("openBlog");
                String bodyMain1 = rs.getString("bodyMain1");
                String bodySp1 = rs.getString("bodySp1");
                String bodyMain2 = rs.getString("bodyMain2");
                String bodySp2 = rs.getString("bodySp2");
                String bodyMain3 = rs.getString("bodyMain3");
                String bodySp3 = rs.getString("bodySp3");
                String endBlog = rs.getString("endBlog");
                int status = rs.getInt("status");
                List<ImageBlog> images = getImagesByProductId(blogId);

                Blog blog = new Blog(blogId, userId, blogTopicId, blogTitle, blogTime, openBlog, bodyMain1, bodySp1, bodyMain2, bodySp2, bodyMain3, bodySp3, endBlog, status, images);
                list.add(blog);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Blog> getAllBlogCommon() {
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM Blog where status = 1";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int blogId = rs.getInt("blogId");
                int userId = rs.getInt("userId");
                int blogTopicId = rs.getInt("blogTopicId");
                String blogTitle = rs.getString("blogTitle");
                String blogTime = rs.getString("blogTime");
                String openBlog = rs.getString("openBlog");
                String bodyMain1 = rs.getString("bodyMain1");
                String bodySp1 = rs.getString("bodySp1");
                String bodyMain2 = rs.getString("bodyMain2");
                String bodySp2 = rs.getString("bodySp2");
                String bodyMain3 = rs.getString("bodyMain3");
                String bodySp3 = rs.getString("bodySp3");
                String endBlog = rs.getString("endBlog");
                int status = rs.getInt("status");
                List<ImageBlog> images = getImagesByProductId(blogId);

                Blog blog = new Blog(blogId, userId, blogTopicId, blogTitle, blogTime, openBlog, bodyMain1, bodySp1, bodyMain2, bodySp2, bodyMain3, bodySp3, endBlog, status, images);
                list.add(blog);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public Blog getBlogById(int blogId) {
        String sql = "SELECT * FROM Blog WHERE blogId=?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, blogId);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("userId");
                int blogTopicId = rs.getInt("blogTopicId");
                String blogTitle = rs.getString("blogTitle");
                String blogTime = rs.getString("blogTime");
                String openBlog = rs.getString("openBlog");
                String bodyMain1 = rs.getString("bodyMain1");
                String bodySp1 = rs.getString("bodySp1");
                String bodyMain2 = rs.getString("bodyMain2");
                String bodySp2 = rs.getString("bodySp2");
                String bodyMain3 = rs.getString("bodyMain3");
                String bodySp3 = rs.getString("bodySp3");
                String endBlog = rs.getString("endBlog");
                int status = rs.getInt("status");
                List<ImageBlog> images = getImagesByProductId(blogId);
                return new Blog(blogId, userId, blogTopicId, blogTitle, blogTime, openBlog, bodyMain1, bodySp1, bodyMain2, bodySp2, bodyMain3, bodySp3, endBlog, status, images);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public List<ImageBlog> getImagesByProductId(int blogId) {
        List<ImageBlog> images = new ArrayList<>();
        String query = "SELECT * FROM imageblog WHERE blogId = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, blogId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ImageBlog image = new ImageBlog();
                image.setImageBlogId(resultSet.getInt("imageBlogId"));
                image.setImageUrl(resultSet.getString("imageUrl"));
                image.setImageText(resultSet.getString("imageText"));
                images.add(image);
            }
        } catch (Exception e) {
        }
        return images;
    }

    public String getUserFullNameById(int userId) {
        String sql = "SELECT a.firstName, a.lastName FROM account a "
                + "JOIN blog b ON a.userId = b.userId "
                + "WHERE b.userId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                return firstName + " " + lastName; // Trả về full name
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Blog> getTop5NewBlog() {
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM Blog ORDER BY blogTime DESC LIMIT 5;";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int blogId = rs.getInt("blogId");
                int userId = rs.getInt("userId");
                int blogTopicId = rs.getInt("blogTopicId");
                String blogTitle = rs.getString("blogTitle");
                String blogTime = rs.getString("blogTime");
                String openBlog = rs.getString("openBlog");
                String bodyMain1 = rs.getString("bodyMain1");
                String bodySp1 = rs.getString("bodySp1");
                String bodyMain2 = rs.getString("bodyMain2");
                String bodySp2 = rs.getString("bodySp2");
                String bodyMain3 = rs.getString("bodyMain3");
                String bodySp3 = rs.getString("bodySp3");
                String endBlog = rs.getString("endBlog");
                int status = rs.getInt("status");
                List<ImageBlog> images = getImagesByProductId(blogId);

                Blog blog = new Blog(blogId, userId, blogTopicId, blogTitle, blogTime, openBlog, bodyMain1, bodySp1, bodyMain2, bodySp2, bodyMain3, bodySp3, endBlog, status, images);
                list.add(blog);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<Blog> getBlogBySearch(String search) {
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM Blog WHERE blogTitle LIKE ? ORDER BY blogTime DESC;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, "%" + search + "%");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int blogId = rs.getInt("blogId");
                int userId = rs.getInt("userId");
                int blogTopicId = rs.getInt("blogTopicId");
                String blogTitle = rs.getString("blogTitle");
                String blogTime = rs.getString("blogTime");
                String openBlog = rs.getString("openBlog");
                String bodyMain1 = rs.getString("bodyMain1");
                String bodySp1 = rs.getString("bodySp1");
                String bodyMain2 = rs.getString("bodyMain2");
                String bodySp2 = rs.getString("bodySp2");
                String bodyMain3 = rs.getString("bodyMain3");
                String bodySp3 = rs.getString("bodySp3");
                String endBlog = rs.getString("endBlog");
                int status = rs.getInt("status");
                List<ImageBlog> images = getImagesByProductId(blogId);

                Blog blog = new Blog(blogId, userId, blogTopicId, blogTitle, blogTime, openBlog, bodyMain1, bodySp1, bodyMain2, bodySp2, bodyMain3, bodySp3, endBlog, status, images);
                list.add(blog);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra thông báo lỗi chi tiết
        }
        return list;
    }

    public List<Blog> getBlogByStatus(int statusBlog) {
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM Blog WHERE status = ? ORDER BY blogTime DESC;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, statusBlog);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int blogId = rs.getInt("blogId");
                int userId = rs.getInt("userId");
                int blogTopicId = rs.getInt("blogTopicId");
                String blogTitle = rs.getString("blogTitle");
                String blogTime = rs.getString("blogTime");
                String openBlog = rs.getString("openBlog");
                String bodyMain1 = rs.getString("bodyMain1");
                String bodySp1 = rs.getString("bodySp1");
                String bodyMain2 = rs.getString("bodyMain2");
                String bodySp2 = rs.getString("bodySp2");
                String bodyMain3 = rs.getString("bodyMain3");
                String bodySp3 = rs.getString("bodySp3");
                String endBlog = rs.getString("endBlog");
                int status = rs.getInt("status");
                List<ImageBlog> images = getImagesByProductId(blogId);

                Blog blog = new Blog(blogId, userId, blogTopicId, blogTitle, blogTime, openBlog, bodyMain1, bodySp1, bodyMain2, bodySp2, bodyMain3, bodySp3, endBlog, status, images);
                list.add(blog);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra thông báo lỗi chi tiết
        }
        return list;
    }

    public List<Blog> getBlogByTopic(int topic) {
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM Blog WHERE blogTopicId = ? ORDER BY blogTime DESC;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, topic);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int blogId = rs.getInt("blogId");
                int userId = rs.getInt("userId");
                int blogTopicId = rs.getInt("blogTopicId");
                String blogTitle = rs.getString("blogTitle");
                String blogTime = rs.getString("blogTime");
                String openBlog = rs.getString("openBlog");
                String bodyMain1 = rs.getString("bodyMain1");
                String bodySp1 = rs.getString("bodySp1");
                String bodyMain2 = rs.getString("bodyMain2");
                String bodySp2 = rs.getString("bodySp2");
                String bodyMain3 = rs.getString("bodyMain3");
                String bodySp3 = rs.getString("bodySp3");
                String endBlog = rs.getString("endBlog");
                int status = rs.getInt("status");
                List<ImageBlog> images = getImagesByProductId(blogId);

                Blog blog = new Blog(blogId, userId, blogTopicId, blogTitle, blogTime, openBlog, bodyMain1, bodySp1, bodyMain2, bodySp2, bodyMain3, bodySp3, endBlog, status, images);
                list.add(blog);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra thông báo lỗi chi tiết
        }
        return list;
    }

    public List<Blog> getBlogByAuthor(int userID) {
        List<Blog> list = new ArrayList<>();
        String sql = "SELECT * FROM Blog WHERE userId = ? ORDER BY blogTime DESC;";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userID);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int blogId = rs.getInt("blogId");
                int userId = rs.getInt("userId");
                int blogTopicId = rs.getInt("blogTopicId");
                String blogTitle = rs.getString("blogTitle");
                String blogTime = rs.getString("blogTime");
                String openBlog = rs.getString("openBlog");
                String bodyMain1 = rs.getString("bodyMain1");
                String bodySp1 = rs.getString("bodySp1");
                String bodyMain2 = rs.getString("bodyMain2");
                String bodySp2 = rs.getString("bodySp2");
                String bodyMain3 = rs.getString("bodyMain3");
                String bodySp3 = rs.getString("bodySp3");
                String endBlog = rs.getString("endBlog");
                int status = rs.getInt("status");
                List<ImageBlog> images = getImagesByProductId(blogId);

                Blog blog = new Blog(blogId, userId, blogTopicId, blogTitle, blogTime, openBlog, bodyMain1, bodySp1, bodyMain2, bodySp2, bodyMain3, bodySp3, endBlog, status, images);
                list.add(blog);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra thông báo lỗi chi tiết
        }
        return list;
    }

    public void updateBlogStatus(int blogId, int status) {
        String sql = "UPDATE blog SET status = ? WHERE blogId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, blogId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Blog> getBlogListByPage(List<Blog> blogs, int start, int end) {
        ArrayList<Blog> arr = new ArrayList<>();
        for (int i = start; i < end; i++) {
            arr.add(blogs.get(i));
        }
        return arr;
    }

    public void updateBlog(int blogId, int blogTopicId, String title, String open,
            String main1, String sp1, String main2, String sp2,
            String main3, String sp3, String end) {

        String sql = "UPDATE `carpipi`.`blog`\n"
                + "SET\n"
                + "`blogTopicId` = ?,\n"
                + "`blogTitle` = ?,\n"
                + "`openBlog` = ?,\n"
                + "`bodyMain1` = ?,\n"
                + "`bodySp1` = ?,\n"
                + "`bodyMain2` = ?,\n"
                + "`bodySp2` = ?,\n"
                + "`bodyMain3` = ?,\n"
                + "`bodySp3` = ?,\n"
                + "`endBlog` = ?\n"
                + "WHERE `blogId` = ?";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, blogTopicId);
            st.setString(2, title);
            st.setString(3, open);
            st.setString(4, main1);
            st.setString(5, sp1);
            st.setString(6, main2);
            st.setString(7, sp2);
            st.setString(8, main3);
            st.setString(9, sp3);
            st.setString(10, end);
            st.setInt(11, blogId);

            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();  // Thêm để hiển thị lỗi chi tiết nếu có

        }

    }

    public boolean insertTopic(String userId, String blogTopicId, String title, Timestamp blogTime, String openBlog,
            String main1, String sp1, String main2, String sp2,
            String main3, String sp3, String endBlog) {

        String sql = "INSERT INTO `carpipi`.`blog`\n"
                + "(`userId`, `blogTopicId`, `blogTitle`, `blogTime`, `openBlog`, `bodyMain1`, \n"
                + "`bodySp1`, `bodyMain2`, `bodySp2`, `bodyMain3`, `bodySp3`, `endBlog`, `status`)\n"
                + "VALUES\n"
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, userId);
            st.setString(2, blogTopicId);
            st.setString(3, title);
            st.setTimestamp(4, blogTime);
            st.setString(5, openBlog);
            st.setString(6, main1);
            st.setString(7, sp1);
            st.setString(8, main2);
            st.setString(9, sp2);
            st.setString(10, main3);
            st.setString(11, sp3);
            st.setString(12, endBlog);
            st.setString(13, "1");

            st.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println(e);  // Thêm để hiển thị lỗi chi tiết nếu có

        }
        return false;
    }

    public int getBlogCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM blog";

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

    public int getActiveBlogCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM blog where status=1";

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

    public static void main(String[] args) {
        BlogDAO blogDAO = new BlogDAO();
        int blogCount = blogDAO.getBlogCount();
        System.out.println("Số lượng blog: " + blogCount);
    }
}
