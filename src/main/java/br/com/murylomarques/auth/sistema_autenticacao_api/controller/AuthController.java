package br.com.murylomarques.auth.sistema_autenticacao_api.controller;

import br.com.murylomarques.auth.sistema_autenticacao_api.dto.MessageResponse;
import br.com.murylomarques.auth.sistema_autenticacao_api.dto.SignupRequest;
import br.com.murylomarques.auth.sistema_autenticacao_api.model.Role;
import br.com.murylomarques.auth.sistema_autenticacao_api.model.User;
import br.com.murylomarques.auth.sistema_autenticacao_api.repository.RoleRepository;
import br.com.murylomarques.auth.sistema_autenticacao_api.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // 1. Verifica se username ou email já existem
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erro: Username já está em uso!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erro: E-mail já está em uso!"));
        }

        // 2. Cria o novo usuário (criptografando a senha!)
        // --- LINHA CORRIGIDA ---
        // Agora usamos o novo construtor de 3 argumentos que criamos na classe User.
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword())
        );
        // --- FIM DA CORREÇÃO ---

        // 3. Define os papéis (Roles) do usuário
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Erro: Role não encontrada."));
        roles.add(userRole);

        user.setRoles(roles);

        // 4. Salva o usuário no banco
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuário registrado com sucesso!"));
    }
}