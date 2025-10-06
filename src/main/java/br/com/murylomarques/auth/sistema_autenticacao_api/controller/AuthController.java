package br.com.murylomarques.auth.sistema_autenticacao_api.controller;

// Imports para DTOs (Data Transfer Objects)
import br.com.murylomarques.auth.sistema_autenticacao_api.dto.JwtResponse;
import br.com.murylomarques.auth.sistema_autenticacao_api.dto.LoginRequest;
import br.com.murylomarques.auth.sistema_autenticacao_api.dto.MessageResponse;
import br.com.murylomarques.auth.sistema_autenticacao_api.dto.SignupRequest;

// Imports para o Modelo (Entidades)
import br.com.murylomarques.auth.sistema_autenticacao_api.model.Role;
import br.com.murylomarques.auth.sistema_autenticacao_api.model.User;

// Imports para Repositórios
import br.com.murylomarques.auth.sistema_autenticacao_api.repository.RoleRepository;
import br.com.murylomarques.auth.sistema_autenticacao_api.repository.UserRepository;

// Imports para Segurança e JWT
import br.com.murylomarques.auth.sistema_autenticacao_api.security.jwt.JwtUtils;

// Imports do Spring Framework
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// Imports do Java Util
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // --- DEPENDÊNCIAS INJETADAS ---
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    // --- ENDPOINT DE LOGIN (AUTENTICAÇÃO) ---
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // Autentica o usuário usando o AuthenticationManager do Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // Armazena a autenticação no contexto de segurança para a sessão atual
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Gera o token JWT para o usuário autenticado
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Obtém os detalhes do usuário (UserDetails) a partir do objeto de autenticação
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Busca o usuário completo no banco para obter mais detalhes (como email e id)
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Erro: Usuário não encontrado no banco de dados após autenticação."));

        // Extrai os papéis (roles) do usuário
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Retorna a resposta com o token JWT e os detalhes do usuário
        return ResponseEntity.ok(new JwtResponse(jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles));
    }

    // --- ENDPOINT DE REGISTRO DE NOVO USUÁRIO ---
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        // Verifica se o username já está em uso
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erro: Username já está em uso!"));
        }

        // Verifica se o e-mail já está em uso
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erro: E-mail já está em uso!"));
        }

        // Cria uma nova instância do usuário
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()) // Criptografa a senha
        );

        // Define os papéis (roles) do novo usuário. Por padrão, será ROLE_USER.
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Erro: Papel (Role) 'ROLE_USER' não encontrado."));
        roles.add(userRole);

        user.setRoles(roles);

        // Salva o novo usuário no banco de dados
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuário registrado com sucesso!"));
    }

    /*
     * NOTA: O construtor new User(username, email, password) usado no método registerUser
     * assume que você adicionou um construtor correspondente na sua entidade User.java.
     * Se você estiver usando Lombok, pode adicionar um construtor manualmente ou ajustar
     * as anotações para que ele seja gerado.
     *
     * Exemplo de construtor na classe User:
     * public User(String username, String email, String password) {
     *     this.username = username;
     *     this.email = email;
     *     this.password = password;
     * }
     */
}