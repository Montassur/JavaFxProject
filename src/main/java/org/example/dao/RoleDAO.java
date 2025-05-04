// src/main/java/org/example/dao/RoleDAO.java
package org.example.dao;

import org.example.entities.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    private final Connection conn;
    public RoleDAO(Connection conn) { this.conn = conn; }

    public List<Role> findAll() throws SQLException {
        List<Role> roles = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id,name FROM roles")) {
            while (rs.next()) {
                roles.add(new Role(rs.getInt("id"), rs.getString("name")));
            }
        }
        return roles;
    }

    /** Finds exactly one Role by its name */
    public Role findByName(String name) throws SQLException {
        String sql = "SELECT id, name FROM roles WHERE name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Role(rs.getInt("id"), rs.getString("name"));
                }
            }
        }
        return null;
    }
}
