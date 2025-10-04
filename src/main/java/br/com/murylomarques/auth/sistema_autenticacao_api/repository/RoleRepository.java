package br.com.murylomarques.auth.sistema_autenticacao_api.repository;

import br.com.murylomarques.auth.sistema_autenticacao_api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}