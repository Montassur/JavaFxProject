package org.example.dao;

import org.example.entities.User;
import org.example.entities.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {
    private final Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Maps the current row of the ResultSet to a User, including its Role.
     */
    private User mapRowToUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));
        u.setNumeroTelephone(rs.getString("numero_telephone"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        u.setActive(rs.getBoolean("active"));

        int roleId = rs.getInt("role_id");
        if (!rs.wasNull()) {
            Role r = new Role(roleId, rs.getString("role_name"));
            u.setRole(r);
        }

        return u;
    }

    /**
     * Finds a single user by email, joining in their Role.
     */
    public Optional<User> findByEmail(String email) throws SQLException {
        String sql =
                "SELECT u.*, r.id AS role_id, r.name AS role_name " +
                        "FROM users u " +
                        "LEFT JOIN roles r ON u.role_id = r.id " +
                        "WHERE u.email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUser(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Inserts a new user into the database. The generated ID is set back on the User.
     */
    public void save(User user) throws SQLException {
        String sql =
                "INSERT INTO users " +
                        "  (nom, prenom, email, numero_telephone, role_id, mot_de_passe, active, created_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getNumeroTelephone());
            if (user.getRole() != null) {
                ps.setInt(5, user.getRole().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setString(6, user.getMotDePasse());
            ps.setBoolean(7, user.isActive());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }
            }
        }
    }

    /**
     * Returns all users with the given role name.
     */
    public List<User> findAllByRoleName(String roleName) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql =
                "SELECT u.*, r.id AS role_id, r.name AS role_name " +
                        "FROM users u " +
                        "JOIN roles r ON u.role_id = r.id " +
                        "WHERE r.name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roleName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(mapRowToUser(rs));
                }
            }
        }
        return users;
    }

    /**
     * Returns all users, including their roles.
     */
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql =
                "SELECT u.*, r.id AS role_id, r.name AS role_name " +
                        "FROM users u " +
                        "LEFT JOIN roles r ON u.role_id = r.id " +
                        "ORDER BY u.id";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        }
        return users;
    }

    /**
     * Updates an existing user.
     */
    public void update(User user) throws SQLException {
        String sql =
                "UPDATE users SET " +
                        "  nom = ?, prenom = ?, email = ?, numero_telephone = ?, " +
                        "  role_id = ?, mot_de_passe = ?, active = ? " +
                        "WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getNumeroTelephone());
            ps.setInt(5, user.getRole().getId());
            ps.setString(6, user.getMotDePasse());
            ps.setBoolean(7, user.isActive());
            ps.setInt(8, user.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a user by ID.
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
