package local.epul4a.tpnotefotosharing.config;

import local.epul4a.tpnotefotosharing.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Authorization rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/register", "/users/login", "/css/**", "/js/**", "/images/**").permitAll() // Public routes
                        .anyRequest().authenticated() // Protect all other endpoints
                )
                // Configure form-based login
                .formLogin(form -> form
                        .loginPage("/users/login") // Custom login page
                        .defaultSuccessUrl("/", true) // Redirect after successful login
                        .permitAll() // Allow access to login page for everyone
                )
                // Configure logout
                .logout(logout -> logout
                        .logoutUrl("/users/logout") // Custom logout URL
                        .logoutSuccessUrl("/users/login") // Redirect to login page after logout
                        .invalidateHttpSession(true) // Invalidate session
                        .deleteCookies("JSESSIONID") // Clear cookies
                        .permitAll()
                )
                // Disable CSRF for simplicity (can be enabled in production)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
