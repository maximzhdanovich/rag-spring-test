package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.security.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
public class BookStoreUserRegister {

  private final BookstoreUserRepository bookstoreUserRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final BookstoreUserDetailsService bookstoreUserDetailsService;
  private final JWTUtil jwtUtil;

  public BookStoreUserRegister(
      BookstoreUserRepository bookstoreUserRepository,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager,
      BookstoreUserDetailsService bookstoreUserDetailsService,
      JWTUtil jwtUtil) {
    this.bookstoreUserRepository = bookstoreUserRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.bookstoreUserDetailsService = bookstoreUserDetailsService;
    this.jwtUtil = jwtUtil;
  }

  public BookstoreUser registerNewUser(RegistrationForm registrationForm) {
    BookstoreUser userByEmail = bookstoreUserRepository.findBookstoreUserByEmail(
        registrationForm.getEmail());
    BookstoreUser userByPhone = bookstoreUserRepository.findBookstoreUserByPhone(
        registrationForm.getPhone());

    if(userByEmail == null && userByPhone == null){
      BookstoreUser user = new BookstoreUser();
      user.setName(registrationForm.getName());
      user.setEmail(registrationForm.getEmail());
      user.setPhone(registrationForm.getPhone());
      user.setPassword(passwordEncoder.encode(registrationForm.getPass()));
      bookstoreUserRepository.save(user);
      return user;
    } else {
      return userByPhone;
    }
//    return null;
  }

  public ContactConfirmationResponse login(ContactConfirmationPayload payload) {
    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    ContactConfirmationResponse response = new ContactConfirmationResponse();
    response.setResult("true");
    return response;
  }

  public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) {
    authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
    BookstoreUserDetails userDetails =
        (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(payload.getContact());
    String jwtToken = jwtUtil.generateToken(userDetails);
    ContactConfirmationResponse response = new ContactConfirmationResponse();
    response.setResult(jwtToken);
    return response;
  }

  public ContactConfirmationResponse jwtLoginByPhoneNumber(ContactConfirmationPayload payload) {
    RegistrationForm registrationForm = new RegistrationForm();
    registrationForm.setPhone(payload.getContact());
    registrationForm.setPass(payload.getCode());
    registerNewUser(registrationForm);
    UserDetails userDetails = bookstoreUserDetailsService.loadUserByUsername(payload.getContact());
    String jwtToken = jwtUtil.generateToken(userDetails);
    ContactConfirmationResponse response = new ContactConfirmationResponse();
    response.setResult(jwtToken);
    return response;
  }

  public Object getCurrentUser() {
    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass() == DefaultOAuth2User.class){
      DefaultOAuth2User user = (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      BookstoreUser bookstoreUser = new BookstoreUser();

      bookstoreUser.setName(user.getAttribute("name"));
      bookstoreUser.setEmail(user.getAttribute("email"));
      return bookstoreUser;
    }

    BookstoreUserDetails userDetails =
        (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userDetails.getBookstoreUser();
  }


}
