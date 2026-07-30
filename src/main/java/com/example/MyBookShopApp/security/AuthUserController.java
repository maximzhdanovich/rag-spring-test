package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.SmsCode;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthUserController {

  private final BookStoreUserRegister userRegister;
  private final SmsService smsService;
  private final JavaMailSender javaMailSender;

  @Autowired
  public AuthUserController(BookStoreUserRegister userRegister,
      SmsService smsService, JavaMailSender javaMailSender) {
    this.userRegister = userRegister;
    this.smsService = smsService;
    this.javaMailSender = javaMailSender;
  }

  @GetMapping("/signin")
  public String handleSignIn(){
    return "signin";
  }

  @GetMapping("/signup")
  public String handleSignUp(Model model){
    model.addAttribute("regForm", new RegistrationForm());
    return "signup";
  }

  @PostMapping("/requestContactConfirmation")
  @ResponseBody
  public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody
      ContactConfirmationPayload payload) {
    ContactConfirmationResponse response = new ContactConfirmationResponse();
    response.setResult("true");

    if (payload.getContact().contains("@")){
      return response;  //for email
    } else {
      String smsCodeString = smsService.sendSecretCodeSms(payload.getContact());
      smsService.saveNewCode(new SmsCode(smsCodeString, 60));  //expires in 1 min
      return response;
    }
  }

  @PostMapping("/requestEmailConfirmation")
  @ResponseBody
  public ContactConfirmationResponse handleRequestEmailConfirmation(@RequestBody
      ContactConfirmationPayload payload) {
    ContactConfirmationResponse response = new ContactConfirmationResponse();

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("nicebookshophelp@gmail.com");
    message.setTo(payload.getContact());
    SmsCode smsCode = new SmsCode(smsService.generateCode(), 300); //5 minutes
    smsService.saveNewCode(smsCode);
    message.setSubject("Bookstore email verification!");
    message.setText("Verification code is: " + smsCode.getCode());
    javaMailSender.send(message);
    response.setResult("true");
    return response;
  }


  @PostMapping("/approveContact")
  @ResponseBody
  public ContactConfirmationResponse handleApproveContact(@RequestBody
      ContactConfirmationPayload payload) {
    ContactConfirmationResponse response = new ContactConfirmationResponse();

    if (smsService.verifyCode(payload.getCode())){
      response.setResult("true");
    }
    return response;

  }

  @PostMapping("/reg")
  public String handleUserregistration(RegistrationForm registrationForm,
      Model model){
    userRegister.registerNewUser(registrationForm);
    model.addAttribute("regOk", true);
    return "signin";
  }


  @PostMapping("/login")
  @ResponseBody
  public ContactConfirmationResponse handleLogin(@RequestBody
      ContactConfirmationPayload payload, HttpServletResponse httpServletResponse) {
//    return userRegister.login(payload);

    ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
    Cookie cookie = new Cookie("token", loginResponse.getResult());
    httpServletResponse.addCookie(cookie);
    return loginResponse;
  }

  @PostMapping("/login-by-phone-number")
  @ResponseBody
  public ContactConfirmationResponse handleLoginByPhoneNumber(@RequestBody
      ContactConfirmationPayload payload, HttpServletResponse httpServletResponse) {

    if (smsService.verifyCode(payload.getCode())) {

      ContactConfirmationResponse loginResponse = userRegister.jwtLoginByPhoneNumber(payload);
      Cookie cookie = new Cookie("token", loginResponse.getResult());
      httpServletResponse.addCookie(cookie);
      return loginResponse;
    } else {
      return null;
    }
  }


  @GetMapping("/my")
  public String handleMy(){
    return "my";
  }

  @GetMapping("/profile")
  public String handleProfile(Model model){
    model.addAttribute("curUsr", userRegister.getCurrentUser());
    return "profile";
  }

}
