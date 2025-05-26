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
import model.BlogTopic;

/**
 *
 * @author tuana
 */
public class BlogTopicDAO extends DBContext {

    public List<BlogTopic> getAllTopic() {
        List<BlogTopic> list = new ArrayList<>();
        String sql = "SELECT * FROM blogtopic";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int blogTopicId = rs.getInt("blogTopicId");
                String toppicName = rs.getString("toppicName");
                BlogTopic btlist = new BlogTopic(blogTopicId, toppicName);
                list.add(btlist);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public String getTopicById(int blogTopicId) {
        String topicName = null;
        String sql = "SELECT toppicName FROM blogtopic WHERE blogTopicId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, blogTopicId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                topicName = rs.getString("toppicName");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return topicName;
    }

    public List<BlogTopic> getBlogByTopicAndStatus() {
        List<BlogTopic> blogTopicStatsList = new ArrayList<>();

        String sql = "SELECT bt.blogTopicId, bt.toppicName, COUNT(b.blogId) AS blogTopicCount, bt.status "
                + "FROM blogtopic bt "
                + "LEFT JOIN blog b ON bt.blogTopicId = b.blogTopicId "
                + "GROUP BY bt.blogTopicId, bt.toppicName, bt.status";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            // Iterate over the result set
            while (rs.next()) {
                int blogTopicId = rs.getInt("blogTopicId");
                String toppicName = rs.getString("toppicName");
                int blogTopicCount = rs.getInt("blogTopicCount");
                int status = rs.getInt("status");

                // Create BlogTopicStats object and add to the list
                BlogTopic stats = new BlogTopic(blogTopicId, toppicName, blogTopicCount, status);
                blogTopicStatsList.add(stats);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return blogTopicStatsList;
    }
    
    public static void main(String[] args) {
                BlogTopicDAO blogTopicDAO = new BlogTopicDAO();

        
        // Get the list of BlogTopic objects
        List<BlogTopic> statsList = blogTopicDAO.getBlogByTopicAndStatus();
        
        // Display the results
        if (statsList != null && !statsList.isEmpty()) {
            System.out.println("Blog Topic Statistics:");
            for (BlogTopic stats : statsList) {
                System.out.println("Topic ID: " + stats.getBlogTopicId());
                System.out.println("Topic Name: " + stats.getToppicName());
                System.out.println("Blog Count: " + stats.getBlogTopicCount());
                System.out.println("Status: " + stats.getStatus());
                System.out.println("-------------------------");
            }
        } else {
            System.out.println("No blog topic statistics found.");
        }
    }
}


