package br.com.murylomarques.auth.sistema_autenticacao_api.service;

import br.com.murylomarques.auth.sistema_autenticacao_api.model.User;
import br.com.murylomarques.auth.sistema_autenticacao_api.repository.UserRepository;
import br.com.murylomarques.auth.sistema_autenticacao_api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Busca o usuário no banco de dados pelo username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o username: " + username));

        // 2. Constrói um objeto UserDetails que o Spring Security entende
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }

    public void generatePasswordResetToken(String email) {
        // 1. Verifica se o usuário existe
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));

        // 2. Gera um token aleatório e seguro
        String token = UUID.randomUUID().toString();

        // 3. Define o token e a data de expiração (ex: 1 hora a partir de agora)
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));

        // 4. Salva as alterações no usuário
        userRepository.save(user);

        // 5. Envia o e-mail
        String resetLink = "http://localhost:3000/reset-password?token=" + token; // URL do seu futuro front-end
        String emailBody = "Olá,\n\nPara redefinir sua senha, clique no link abaixo:\n" + resetLink;

        emailService.sendSimpleMessage(user.getEmail(), "Redefinição de Senha", emailBody);
    }
    public void resetPassword(String token, String newPassword) {
        // 1. Busca o usuário pelo token
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("Token de redefinição de senha inválido."));

        // 2. Verifica se o token não expirou
        if (user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token de redefinição de senha expirado.");
        }

        // 3. Criptografa e define a nova senha
        user.setPassword(passwordEncoder.encode(newPassword));

        // 4. Limpa o token e a data de expiração para que não possa ser usado novamente
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);

        // 5. Salva o usuário com a nova senha
        userRepository.save(user);
    }

}