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
import model.Role;

/**
 *
 * @author tuana
 */
public class RoleDAO extends DBContext {

    public List<Role> getAllRole() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM Role;";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Role p = new Role(rs.getInt(1),
                        rs.getString(2));
                list.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public String getRoleNameById(int roleId) {
        String sql = "SELECT roleName FROM Role WHERE roleId = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, roleId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("roleName");
            }
        } catch (SQLException e) {
        }
        return "Unknown";
    }

    public List<Role> getRoleList() {
        List<Role> roleList = new ArrayList<>();

        String sql = "SELECT \n"
                + "    r.roleId,\n"
                + "    r.roleName,\n"
                + "    COUNT(a.roleId) AS roleCount,\n"
                + "    r.status\n"
                + "FROM \n"
                + "    role r\n"
                + "LEFT JOIN \n"
                + "    account a ON r.roleId = a.roleId\n"
                + "GROUP BY \n"
                + "    r.roleId, r.roleName, r.status;";

        try (PreparedStatement st = connection.prepareStatement(sql); ResultSet rs = st.executeQuery()) {

            // Iterate over the result set
            while (rs.next()) {
                int roleId = rs.getInt("roleId");
                String roleName = rs.getString("roleName");
                int roleCount = rs.getInt("roleCount");
                int status = rs.getInt("status");

                // Create BlogTopicStats object and add to the list
                Role stats = new Role(roleId, roleName, roleCount, status);
                roleList.add(stats);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roleList;
    }
    public static void main(String[] args) {
                RoleDAO roleDAO = new RoleDAO();

        
        // Get the list of BlogTopic objects
        List<Role> roleList = roleDAO.getRoleList();
        
        // Display the results
        if (roleList != null && !roleList.isEmpty()) {
            System.out.println("Blog Topic Statistics:");
            for (Role role : roleList) {
                System.out.println("role ID: " + role.getRoleId());
                System.out.println("role Name: " + role.getRoleName());
                System.out.println("role Count: " + role.getRoleCount());
                System.out.println("Status: " + role.getStatus());
                System.out.println("-------------------------");
            }
        } else {
            System.out.println("No role found.");
        }
    }
}
