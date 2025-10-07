package br.com.murylomarques.auth.sistema_autenticacao_api.service;

import br.com.murylomarques.auth.sistema_autenticacao_api.model.User;
import br.com.murylomarques.auth.sistema_autenticacao_api.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Habilita a integração do Mockito com o JUnit 5
class CustomUserDetailsServiceTest {

    @Mock // 1. Cria uma versão FAKE (mock) do UserRepository
    private UserRepository userRepository;

    @Mock // 2. Cria uma versão FAKE (mock) do EmailService
    private EmailService emailService;

    @InjectMocks // 3. Cria uma instância REAL do nosso serviço e injeta os mocks acima nela
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("Deve gerar token de redefinição quando o e-mail for válido")
    void generatePasswordResetToken_WhenEmailExists_ShouldSaveUserWithToken() {
        // --- 1. ARRANGE (PREPARAR) ---

        // Dados de entrada do nosso teste
        String userEmail = "test@example.com";
        User fakeUser = new User(); // Um usuário falso para o repositório retornar
        fakeUser.setEmail(userEmail);

        // Ensinando o mock a se comportar:
        // "QUANDO o método findByEmail for chamado com 'userEmail', ENTÃO retorne nosso usuário falso"
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(fakeUser));

        // "QUANDO o método sendSimpleMessage for chamado com quaisquer argumentos, ENTÃO não faça nada"
        doNothing().when(emailService).sendSimpleMessage(anyString(), anyString(), anyString());


        // --- 2. ACT (AGIR) ---

        // Executamos o método que queremos testar
        customUserDetailsService.generatePasswordResetToken(userEmail);


        // --- 3. ASSERT (VERIFICAR) ---

        // Verificamos se o método save do repositório foi chamado exatamente 1 vez.
        // Isso prova que a lógica para salvar o token foi atingida.
        verify(userRepository, times(1)).save(fakeUser);

        // Verificações extras para garantir que o token foi realmente gerado no objeto
        assertNotNull(fakeUser.getResetPasswordToken());
        assertNotNull(fakeUser.getResetPasswordTokenExpiry());
    }
}