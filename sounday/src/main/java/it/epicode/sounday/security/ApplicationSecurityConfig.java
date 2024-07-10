package it.epicode.sounday.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Properties;

@Configuration
//QUESTA ANNOTAZIONE SERVE A COMUNICARE A SPRING CHE QUESTA  CLASSE Ã¨ UTILIZZATA PER CONFIGURARE LA SECURITY
@EnableWebSecurity()
@EnableMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {

    @Bean
    PasswordEncoder stdPasswordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    AuthTokenFilter authenticationJwtToken() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder,
                                                       UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()) // Utilizza la configurazione CORS
                .authorizeHttpRequests(authorize ->
                                authorize //CONFIGURAZIONE DELLA PROTEZIONE DEI VARI ENDPOINT
                                        .requestMatchers("/api/users/login").permitAll()
                                        .requestMatchers("/api/users/registerArtist").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/events").hasAuthority("ARTIST")
                                        .requestMatchers(HttpMethod.PATCH, "/api/users/{id}").authenticated()
                                        .requestMatchers(HttpMethod.PUT, "/**").hasAuthority("ARTIST")
                                        .requestMatchers(HttpMethod.DELETE, "/**").hasAuthority("ARTIST")
                        //.requestMatchers("/**").authenticated() //TUTTO CIO CHE PUO ESSERE SFUGGITO RICHIEDE L'AUTENTICAZIONE (SERVE A GESTIRE EVENTUALI DIMENTICANZE)
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //COMUNICA ALLA FILTERCHAIN QUALE FILTRO UTILIZZARE, SENZA QUESTA RIGA DI CODICE IL FILTRO NON VIENE RICHIAMATO
                .addFilterBefore(authenticationJwtToken(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}

