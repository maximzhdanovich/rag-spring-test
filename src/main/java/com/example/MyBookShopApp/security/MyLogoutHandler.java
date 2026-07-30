package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.security.jwt.JWTBlackListEntity;
import com.example.MyBookShopApp.security.jwt.JWTBlacklistRepository;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class MyLogoutHandler implements LogoutHandler {

  private final JWTBlacklistRepository jwtBlacklistRepository;

  @Autowired
  public MyLogoutHandler(
      JWTBlacklistRepository jwtBlacklistRepository) {
    this.jwtBlacklistRepository = jwtBlacklistRepository;
  }

  @Override
  public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      Authentication authentication) {
    Cookie[] cookies = httpServletRequest.getCookies();

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("token")) {
          String token = cookie.getValue();
          JWTBlackListEntity blackListEntity = new JWTBlackListEntity();
          blackListEntity.setToken(token);
          jwtBlacklistRepository.save(blackListEntity);

          Logger.getLogger(this.getClass().getSimpleName()).warning("blacklist " + token);

        }
      }
    }
    HttpSession session = httpServletRequest.getSession();
    SecurityContextHolder.clearContext();
    if (session != null) {
      session.invalidate();
    }
    authentication.setAuthenticated(false);
    authentication.setAuthenticated(true);

  }
}