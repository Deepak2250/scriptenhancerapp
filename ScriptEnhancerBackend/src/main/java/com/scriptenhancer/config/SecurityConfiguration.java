package com.scriptenhancer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.scriptenhancer.JwtConfig.JwtFilter;
import com.scriptenhancer.service.UserDetailsServicImp;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CustomAuthenticationFailureHandler customFailureHandler;

    @Autowired
    private EncoderConfig config;

    @Autowired

    private UserDetailsServicImp userDetailsServicImp;

    // @Bean
    // SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    // httpSecurity.csrf(csrf -> csrf.disable())
    // .sessionManagement(session ->
    // session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) //
    // Stateless
    // // session
    // .authorizeHttpRequests(auth -> auth
    // .requestMatchers("/api/**").permitAll()
    // .anyRequest().authenticated())
    // .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) ///
    // The jwt filter will
    // /// intercepts before every http
    // /// request and ensures that
    // /// does this request demands
    // /// the login or not and if the
    // /// jwt token is valid and not
    // /// expired and the user already
    // /// logged in then it will no
    // /// need to log in again and
    // /// again
    // .formLogin(formLogin -> formLogin
    // .loginPage("/login") // Custom login page
    // .defaultSuccessUrl("/hi", true) // Redirect after successful login
    // .failureHandler(customFailureHandler) // Set custom failure handler
    // // .failureUrl("/login?error=true") // Redirect after failed login
    // .permitAll()) // Allow all to access login endpoints
    // .logout(logout -> logout
    // .logoutUrl("/logout") // Process logout requests
    // .logoutSuccessUrl("/login?logout=true") // Redirect after logout
    // .invalidateHttpSession(true) // Invalidate session on logout
    // .clearAuthentication(true)
    // .permitAll());
    // return httpSecurity.build();
    // }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> corsFilter())
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/**", "/api/scriptenhancer/**").permitAll()
                        .anyRequest().authenticated() // Protect all other endpoints
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                ).exceptionHandling(e -> {
                    e.authenticationEntryPoint((req, res, ex) -> {
                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                    });
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Autowired
    protected void configureAuthentication(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsServicImp).passwordEncoder(config.passwordEncoder());
    }

    @Bean
    CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedHeader("*");
        config.addExposedHeader("Auth");
        config.addExposedHeader("abc");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    /*
     * @Bean
     * AuthenticationManager authenticationManager(AuthenticationConfiguration
     * authenticationConfiguration)
     * throws Exception {
     * return authenticationConfiguration.getAuthenticationManager();
     * }
     */

    /*
     * @Bean
     * public InMemoryUserDetailsManager userDetailsService() {
     * UserDetails user = User.withUsername("admin")
     * .password(passwordEncoder.passwordEncoder().encode("adminpass"))
     * .roles("ADMIN")
     * .build();
     * 
     * UserDetails user2 = User.withUsername("user")
     * .password(passwordEncoder.passwordEncoder().encode("userpass"))
     * .roles("USER")
     * .build();
     * 
     * return new InMemoryUserDetailsManager(user, user2);
     * }
     */

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for password hashing
    }
}