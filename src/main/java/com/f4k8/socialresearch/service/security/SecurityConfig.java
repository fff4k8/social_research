package com.f4k8.socialresearch.service.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private  SecurityService securityService;

  String[] adminUrl = {"/admin/**"};
  String[] userUrl = {"/quizzes/**", "/get_quiz/**", "/submit_answers/**"};
  String[] anonUrl = {"/registration/**"};

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
   http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/person_styles.css").permitAll()
        .antMatchers(adminUrl).hasRole("ADMIN")
        .antMatchers(userUrl).hasRole("USER")
        .antMatchers(anonUrl).anonymous()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .usernameParameter("email")
        .passwordParameter("personDetails.password")
        .permitAll()
        .defaultSuccessUrl("/");

    return http.
        authenticationProvider(customAuthProvider()).build();
  }

  @Bean
  protected CustomAuthProvider customAuthProvider() {
    CustomAuthProvider customAuthProvider = new CustomAuthProvider();
    customAuthProvider.setPasswordEncoder(passwordEncoder());
    customAuthProvider.setUserDetailsService(securityService);
    return customAuthProvider;
  }

  @Bean
  protected PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder(12);
  }
}
