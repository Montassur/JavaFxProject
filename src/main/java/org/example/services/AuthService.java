package org.example.services;

import org.example.dao.UserDAO;
import org.example.dao.RoleDAO;
import org.example.entities.User;
import org.example.entities.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import jakarta.mail.MessagingException;

public class AuthService {
    private final UserDAO   userDAO;
    private final RoleDAO   roleDAO;
    private final MailService mailService;

    public AuthService(Connection conn, MailService mailService) {
        this.userDAO     = new UserDAO(conn);
        this.roleDAO     = new RoleDAO(conn);
        this.mailService = mailService;
    }

    /**
     * Looks up the user by email, logs what we find, then checks the password.
     */
    public Optional<User> login(String email, String password) throws SQLException {
        Optional<User> opt = userDAO.findByEmail(email);
        if (opt.isEmpty()) {
            System.out.println("[AuthService] No user found for email: " + email);
            return Optional.empty();
        }

        User u = opt.get();
        System.out.println("[AuthService] Found user: " + u.getEmail() +
                " (active=" + u.isActive() + "), storedHash=" + u.getMotDePasse());

        if (!BCrypt.checkpw(password, u.getMotDePasse())) {
            System.out.println("[AuthService] Password mismatch for: " + email);
            return Optional.empty();
        }

        return Optional.of(u);
    }

    /**
     * Signs up a new user with role 'pending' and inactive, then notifies
     * the user and all admins.
     */
    public void signup(User user) throws SQLException {
        if (!user.getMotDePasse().equals(user.getConfirmerMotDePasse())) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
        }
        if (userDAO.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        // hash + deactivate
        String hashed = BCrypt.hashpw(user.getMotDePasse(), BCrypt.gensalt());
        user.setMotDePasse(hashed);
        user.setActive(false);

        // assign pending role
        Role pending = roleDAO.findByName("pending");
        if (pending == null) {
            throw new IllegalStateException("Rôle 'pending' introuvable");
        }
        user.setRole(pending);

        // persist
        userDAO.save(user);

        // notify user
        try {
            mailService.sendSimple(
                    user.getEmail(),
                    "Inscription reçue",
                    "Bonjour " + user.getNom() + ",\n\n"
                            + "Votre inscription a bien été reçue. Un administrateur activera votre compte.\n\n"
                            + "Cordialement."
            );
        } catch (MessagingException me) {
            me.printStackTrace();
        }

        // notify admins
        List<User> admins = userDAO.findAllByRoleName("admin");
        for (User admin : admins) {
            try {
                mailService.sendSimple(
                        admin.getEmail(),
                        "Nouvelle inscription",
                        "Un nouvel utilisateur (" + user.getEmail() + ") vient de s'inscrire.\n"
                                + "Veuillez valider son compte."
                );
            } catch (MessagingException me) {
                me.printStackTrace();
            }
        }
    }
}
