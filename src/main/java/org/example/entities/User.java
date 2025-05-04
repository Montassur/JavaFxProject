package org.example.entities;

/**
 * Represents an application user with an associated Role.
 */
public class User {
    private int id;
    private String nom, prenom, email, numeroTelephone;
    private Role role;
    private String motDePasse, confirmerMotDePasse;
    private boolean active;

    public User() {}

    public User(int id,
                String nom,
                String prenom,
                String email,
                String numeroTelephone,
                Role role,
                String motDePasse,
                String confirmerMotDePasse,
                boolean active) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numeroTelephone = numeroTelephone;
        this.role = role;
        this.motDePasse = motDePasse;
        this.confirmerMotDePasse = confirmerMotDePasse;
        this.active = active;
    }

    public User(String nom,
                String prenom,
                String email,
                String numeroTelephone,
                Role role,
                String motDePasse,
                String confirmerMotDePasse,
                boolean active) {
        this(0, nom, prenom, email, numeroTelephone, role,
                motDePasse, confirmerMotDePasse, active);
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNumeroTelephone() { return numeroTelephone; }
    public void setNumeroTelephone(String numeroTelephone) { this.numeroTelephone = numeroTelephone; }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getConfirmerMotDePasse() { return confirmerMotDePasse; }
    public void setConfirmerMotDePasse(String confirmerMotDePasse) { this.confirmerMotDePasse = confirmerMotDePasse; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return id + " - " + nom + " " + prenom +
                " (" + email + ") role=" +
                (role != null ? role.getName() : "none") +
                " active=" + active;
    }
}
