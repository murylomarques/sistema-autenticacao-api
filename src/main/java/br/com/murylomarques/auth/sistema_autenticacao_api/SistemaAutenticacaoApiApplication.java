package br.com.murylomarques.auth.sistema_autenticacao_api;

import br.com.murylomarques.auth.sistema_autenticacao_api.model.Role;
import br.com.murylomarques.auth.sistema_autenticacao_api.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SistemaAutenticacaoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaAutenticacaoApiApplication.class, args);
    }

    // --- CÓDIGO ADICIONADO PARA POPULAR AS ROLES ---
    @Bean
    CommandLineRunner init(RoleRepository roleRepository) {
        return args -> {
            // Verifica se a role já existe para não duplicar
            if (roleRepository.findByName("ROLE_USER").isEmpty()) {
                roleRepository.save(new Role(null, "ROLE_USER"));
            }
            if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                roleRepository.save(new Role(null, "ROLE_ADMIN"));
            }
        };
    }
    // --- FIM DO CÓDIGO ADICIONADO ---
}