package it.epicode.sounday.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwt;

    @Autowired
    ApplicationUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Verifica del token
            log.info("Processing AuthTokenFilter");

            var header = request.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                // Il token esiste ed è nella forma Bearer xxxxxxxxx
                var token = header.substring(7); // Questo è il token estratto a partire dal settimo carattere
                log.info("Token: {}", token);

                // Si effettua la validazione del token
                if (!jwt.isTokenValid(token))
                    throw new JwtException("Token non valido");

                // Si estraggono dal token le informazioni relative allo username
                var username = jwt.getUsernameFromToken(token);
                log.info("Username: {}", username);
                // Si estrae l'utente dal suo username
                var details = userDetailsService.loadUserByUsername(username);
                log.info("UserDetails: {}", details);

                // Si genera il contesto contenente i dati dell'utente recuperati dal token
                var auth = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                // Si completa l'oggetto auth
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Si passa al contesto l'oggetto auth per renderlo sempre disponibile
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.warn("No Token or Invalid Token format");
            }

        } catch (Exception e) {
            log.error("Exception in auth filter", e);
        }
        // Chiama a cascata tutti i filtri creati
        filterChain.doFilter(request, response);
    }
}
