package br.com.murylomarques.auth.sistema_autenticacao_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor // Construtor sem argumentos (obrigatório pelo JPA)
@AllArgsConstructor // Construtor com todos os argumentos
public class User {

    private LocalDateTime resetPasswordTokenExpiry;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "reset_password_token_expiry")

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // --- CONSTRUTOR CORRIGIDO ADICIONADO AQUI ---
    // Este construtor será usado especificamente para o registro de novos usuários.
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}