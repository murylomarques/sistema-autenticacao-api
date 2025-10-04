package br.com.murylomarques.auth.sistema_autenticacao_api.repository;

import br.com.murylomarques.auth.sistema_autenticacao_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Métodos de busca personalizados virão aqui depois
}