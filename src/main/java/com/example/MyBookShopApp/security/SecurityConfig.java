package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.security.jwt.JWTBlacklistRepository;
import com.example.MyBookShopApp.security.jwt.JWTRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final BookstoreUserDetailsService bookstoreUserDetailsService;
  private final JWTRequestFilter filter;
  private final JWTBlacklistRepository jwtBlacklistRepository;


  @Autowired
  public SecurityConfig(BookstoreUserDetailsService bookstoreUserDetailsService,
      JWTRequestFilter filter,
      JWTBlacklistRepository jwtBlacklistRepository) {
    this.bookstoreUserDetailsService = bookstoreUserDetailsService;
    this.filter = filter;
    this.jwtBlacklistRepository = jwtBlacklistRepository;
  }

  @Bean
  PasswordEncoder getPasswordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(bookstoreUserDetailsService)
        .passwordEncoder(getPasswordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/my","/profile", "/rateBook", "/bookReview", "/rateBookReview").authenticated()//hasRole("USER")
        .antMatchers("/**").permitAll()
        .and().formLogin()
        .loginPage("/signin").failureUrl("/signin")
        .and().logout().logoutUrl("/logout").addLogoutHandler(new MyLogoutHandler(jwtBlacklistRepository))
        .logoutSuccessUrl("/signin").deleteCookies("token")
        .and().oauth2Login()
        .and().oauth2Client();

    http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

  }
}
