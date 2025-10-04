package br.com.murylomarques.auth.sistema_autenticacao_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {

    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(min = 3, max = 20, message = "O nome de usuário deve ter entre 3 e 20 caracteres")
    private String username;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String password;

    // Opcional: O cliente pode pedir papéis específicos (admin, etc.)
    private Set<String> roles;
}