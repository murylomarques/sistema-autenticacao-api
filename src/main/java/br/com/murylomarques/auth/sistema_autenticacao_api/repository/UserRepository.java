package br.com.murylomarques.auth.sistema_autenticacao_api.repository;

import br.com.murylomarques.auth.sistema_autenticacao_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importe esta classe

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // --- ADICIONE ESTA LINHA ---
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}