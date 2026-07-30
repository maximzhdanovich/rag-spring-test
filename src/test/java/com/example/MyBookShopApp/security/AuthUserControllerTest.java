package com.example.MyBookShopApp.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class AuthUserControllerTest {

  private final MockMvc mockMvc;
  private final BookstoreUserRepository userRepository;

  @Autowired
  public AuthUserControllerTest(MockMvc mockMvc,
      BookstoreUserRepository userRepository) {
    this.mockMvc = mockMvc;
    this.userRepository = userRepository;
  }

  @Test
  public void regNewUserTest() throws Exception{

    BookstoreUser user = userRepository.findBookstoreUserByEmail("test-test@gmail.com");
    assertNull(user);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("name", "Test Usr");
    params.add("phone", "+79031004456");
    params.add("phoneCode", "776 767");
    params.add("email", "test-test@gmail.com");
    params.add("mailCode", "583 807");
    params.add("pass", "123123");

    mockMvc.perform(post("/reg")
        .params(params))
        .andDo(print())
        .andExpect(status().isOk());

    user = userRepository.findBookstoreUserByEmail("test-test@gmail.com");
    assertNotNull(user);

    userRepository.delete(user);
  }

  @Test
  public void loginByPhoneTest() throws Exception{
    mockMvc.perform(formLogin("/signin").user("+7 (123) 123-12-31").password("123123"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
  }

  @Test
  public void loginByPhoneFailTest() throws Exception{
    mockMvc.perform(formLogin("/signin").user("fake_user").password("123123"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/signin"));
  }

  @Test
  public void addBookToCart() throws Exception{
    mockMvc.perform(post("/books/changeBookStatus/book-zbl-563"))
        .andDo(print())
        .andExpect(cookie().exists("cartContents"))
        .andExpect(cookie().value("cartContents", "book-zbl-563"))
        .andExpect(redirectedUrl("/books/book-zbl-563"));
  }

  @Test
  public void removeBookFromCart() throws Exception{
    mockMvc.perform(post("/books/changeBookStatus/cart/remove/book-zbl-563")
        .cookie(new Cookie("cartContents", "book-zbl-563")))
        .andDo(print())
        .andExpect(cookie().exists("cartContents"))
        .andExpect(cookie().value("cartContents", ""))
        .andExpect(redirectedUrl("/books/cart"));
  }

  @Test
  @WithMockUser(username = "aleksandr.baranov@skillbox.ru", password = "123123")
  public void logoutSuccess() throws Exception{
    mockMvc.perform(post("/logout"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/signin"));
  }


}