package com.example.MyBookShopApp.data;

import java.util.List;

public class BooksPageDto {

  private Integer count;
  private List<BookEntity> books;

  public BooksPageDto(List<BookEntity> books) {
    this.books = books;
    this.count = books.size();
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public List<BookEntity> getBooks() {
    return books;
  }

  public void setBooks(List<BookEntity> books) {
    this.books = books;
  }
}
