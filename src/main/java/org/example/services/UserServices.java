package org.example.services;

import org.example.entities.User;
import org.example.entities.Role;
import org.example.utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServices implements ICrud<User> {

    private final Connection con;

    public UserServices() {
        con = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(User user) throws SQLException {
        String sql = """
            INSERT INTO users
              (nom, prenom, email, numero_telephone, role_id, mot_de_passe, active, created_at)
            VALUES
              (?, ?, ?, ?, ?, ?, ?, NOW())
            """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getNumeroTelephone());
            ps.setInt   (5, user.getRole().getId());      // use Role.id
            ps.setString(6, user.getMotDePasse());
            ps.setBoolean(7, user.isActive());
            ps.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void modifier(User user) throws SQLException {
        String sql = """
            UPDATE users SET
              nom = ?, prenom = ?, email = ?, numero_telephone = ?,
              role_id = ?, mot_de_passe = ?, active = ?
            WHERE id = ?
            """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getNumeroTelephone());
            ps.setInt   (5, user.getRole().getId());
            ps.setString(6, user.getMotDePasse());
            ps.setBoolean(7, user.isActive());
            ps.setInt   (8, user.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = """
            SELECT
              u.id, u.nom, u.prenom, u.email, u.numero_telephone,
              u.mot_de_passe, u.active,
              r.id   AS role_id,
              r.name AS role_name
            FROM users u
            JOIN roles r ON u.role_id = r.id
            ORDER BY u.id
            """;
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setEmail(rs.getString("email"));
                u.setNumeroTelephone(rs.getString("numero_telephone"));
                u.setMotDePasse(rs.getString("mot_de_passe"));
                u.setActive(rs.getBoolean("active"));

                Role role = new Role(
                        rs.getInt("role_id"),
                        rs.getString("role_name")
                );
                u.setRole(role);

                users.add(u);
            }
        }
        return users;
    }
}
