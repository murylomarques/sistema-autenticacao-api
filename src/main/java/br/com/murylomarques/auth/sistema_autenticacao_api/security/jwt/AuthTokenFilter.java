package br.com.murylomarques.auth.sistema_autenticacao_api.security.jwt;

import br.com.murylomarques.auth.sistema_autenticacao_api.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// A classe NÃO deve ser anotada com @Component, pois a criamos como um @Bean em SecurityConfig.
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. Extrai o token JWT do cabeçalho da requisição
            String jwt = parseJwt(request);

            // 2. Verifica se o token não é nulo e se é válido
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // 3. Extrai o username do token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 4. Carrega os detalhes do usuário a partir do banco de dados
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Cria o objeto de autenticação
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null, // A senha não é necessária aqui
                                userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Define o usuário como autenticado no contexto de segurança do Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Não foi possível autenticar o usuário: {}", e.getMessage());
        }

        // 7. Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o token do cabeçalho "Authorization".
     * Ex: "Bearer eyJhbGciOiJIUzUxMiJ9..." -> "eyJhbGciOiJIUzUxMiJ9..."
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}