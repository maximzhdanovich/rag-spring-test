package com.example.MyBookShopApp.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class BookstoreUserRepositoryTests {

  private final BookstoreUserRepository bookstoreUserRepository;

  @Autowired
  public BookstoreUserRepositoryTests(
      BookstoreUserRepository bookstoreUserRepository) {
    this.bookstoreUserRepository = bookstoreUserRepository;
  }

  @Test
  public void testAddNewUser(){
    BookstoreUser user = new BookstoreUser();
    user.setPassword("1234567890");
    user.setPhone("79031230101");
    user.setName("TestUser");
    user.setEmail("test@gmail.com");

    assertNotNull(bookstoreUserRepository.save(user));
  }
}