package br.com.murylomarques.auth.sistema_autenticacao_api.controller;

// Imports para DTOs (Data Transfer Objects)
import br.com.murylomarques.auth.sistema_autenticacao_api.dto.ForgotPasswordRequest;
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
import br.com.murylomarques.auth.sistema_autenticacao_api.service.CustomUserDetailsService;


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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import br.com.murylomarques.auth.sistema_autenticacao_api.dto.ResetPasswordRequest;

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

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    // --- ENDPOINT DE LOGIN (AUTENTICAÇÃO) ---
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Erro: Usuário não encontrado no banco de dados após autenticação."));

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles));
    }

    // --- ENDPOINT DE REGISTRO DE NOVO USUÁRIO ---
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erro: Username já está em uso!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Erro: E-mail já está em uso!"));
        }

        // --- INÍCIO DA CORREÇÃO ---
        // Cria uma nova instância do usuário usando o construtor vazio
        User user = new User();
        // Define os atributos usando os setters
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword())); // Criptografa a senha
        // --- FIM DA CORREÇÃO ---

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Erro: Papel (Role) 'ROLE_USER' não encontrado."));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuário registrado com sucesso!"));
    }

    // --- ENDPOINT PARA SOLICITAR REDEFINIÇÃO DE SENHA ---
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        try {
            customUserDetailsService.generatePasswordResetToken(forgotPasswordRequest.getEmail());
            // Retorna 200 OK em ambos os casos para não revelar se um e-mail existe no sistema
            return ResponseEntity.ok(new MessageResponse("Se o e-mail estiver cadastrado, um link de redefinição de senha foi enviado."));
        } catch (UsernameNotFoundException e) {
            // Log do erro para monitoramento interno, se desejado
            // logger.info("Tentativa de recuperação de senha para e-mail não cadastrado: {}", forgotPasswordRequest.getEmail());
            return ResponseEntity.ok(new MessageResponse("Se o e-mail estiver cadastrado, um link de redefinição de senha foi enviado."));
        } catch (Exception e) {
            // logger.error("Erro inesperado no processo de forgot-password: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("Erro ao processar a solicitação."));
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            customUserDetailsService.resetPassword(
                    resetPasswordRequest.getToken(),
                    resetPasswordRequest.getNewPassword()
            );
            return ResponseEntity.ok(new MessageResponse("Sua senha foi redefinida com sucesso!"));
        } catch (RuntimeException e) {
            // Captura as exceções lançadas pelo serviço (token inválido/expirado)
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }


}