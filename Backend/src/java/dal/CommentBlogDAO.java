/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import context.DBContext;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Account;
import model.CommentBlog;

/**
 *
 * @author tuana
 */
public class CommentBlogDAO extends DBContext {

    public List<CommentBlog> getCommentBlogByBlogId(int blogId) {
        List<CommentBlog> list = new ArrayList<>();
        String sql = "SELECT * FROM comment WHERE blogId=? ORDER BY commentTime DESC";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, blogId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                CommentBlog p = new CommentBlog(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public void creatComment(int userId, int blogId, String commentInfor, Timestamp commentDate, int commentRating) {
        String sql = "INSERT INTO comment (userId, blogId, commentInfor, commentTime, commentRating) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            st.setInt(2, blogId);
            st.setString(3, commentInfor);
            st.setTimestamp(4, commentDate);
            st.setInt(5, commentRating);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while creating comment: " + e.getMessage());
        }
    }

    public List<Account> getUserNameByBlogId(int blogId) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT a.* "
                + "FROM comment c "
                + "JOIN account a ON c.userId = a.userId "
                + "WHERE c.blogId = ?;";  // Sử dụng tham số

        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, blogId); // Sử dụng biến blogId
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

    public List<CommentBlog> getCommentByRating(int blogId, int commentRating) {
        List<CommentBlog> list = new ArrayList<>();
        String sql = "SELECT * FROM comment WHERE blogId=? and commentRating =?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, blogId);
            st.setInt(2, commentRating);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                CommentBlog p = new CommentBlog(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<CommentBlog> getCommentListByPage(List<CommentBlog> comments, int start, int end) {
        ArrayList<CommentBlog> arr = new ArrayList<>();
        for (int i = start; i < end; i++) {
            arr.add(comments.get(i));
        }
        return arr;
    }
}
