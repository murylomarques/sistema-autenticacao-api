package br.com.murylomarques.auth.sistema_autenticacao_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // --- INÍCIO DA MODIFICAÇÃO ---

    @ManyToMany(fetch = FetchType.EAGER) // Carrega os papéis junto com o usuário
    @JoinTable(
            name = "user_roles", // Nome da tabela de junção
            joinColumns = @JoinColumn(name = "user_id"), // Chave estrangeira para User
            inverseJoinColumns = @JoinColumn(name = "role_id") // Chave estrangeira para Role
    )
    private Set<Role> roles = new HashSet<>();

    // --- FIM DA MODIFICAÇÃO ---
}