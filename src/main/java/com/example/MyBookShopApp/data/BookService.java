package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.google.api.books.Item;
import com.example.MyBookShopApp.data.google.api.books.Root;
import com.example.MyBookShopApp.errs.BookstoreApiWrongParameterException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookService {

  //private JdbcTemplate jdbcTemplate;

  private final BookRepository bookRepository;
  private final RestTemplate restTemplate;

  @Autowired
  public BookService(BookRepository bookRepository,
      RestTemplate restTemplate) {
    this.bookRepository = bookRepository;
    this.restTemplate = restTemplate;
  }

  //  @Autowired
//  public BookService(JdbcTemplate jdbcTemplate) {
//    this.jdbcTemplate = jdbcTemplate;
//  }

  public List<BookEntity> getBooksData() {
    return bookRepository.findAll();
  }

    // NEW BOOK SERVICE METHODS

  public List<BookEntity> getBooksByAuthor(String authorName) {
    return bookRepository.findBookEntitiesByAuthorNameContaining(authorName);
  }

  public List<BookEntity> getBooksByTitle(String title) throws BookstoreApiWrongParameterException {
    if (title.equals("") || title.length() <=1){
      throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
    } else {
      List<BookEntity> data = bookRepository.findBookEntitiesByTitleContaining(title);
      if (data.size() > 0){
        return data;
      } else {
        throw new BookstoreApiWrongParameterException("No data found with specified parameters...");
      }
    }
  }

  public List<BookEntity> getBooksWithPriceBetween(Integer min, Integer max){
    return bookRepository.findBookEntitiesByPriceOldBetween(min, max);
  }

  public List<BookEntity> getBooksWithPrice(Integer price){
    return bookRepository.findBookEntitiesByPriceOldIs(price);
  }

  public List<BookEntity> getBooksWithMaxDiscount(){
    return bookRepository.getBooksWithMaxDiscount();
  }

  public List<BookEntity> getBestsellers(){
    return bookRepository.getBestsellers();
  }

  public Page<BookEntity> getPageOfRecommendedBooks(Integer offset, Integer limit){
    Pageable nextPage = PageRequest.of(offset, limit);
    return bookRepository.findAll(nextPage);
  }

  public Page<BookEntity> getPageOfNewBooks(Integer offset, Integer limit){
    Pageable nextPage = PageRequest.of(offset, limit);
    return bookRepository.findAllByOrderByPubDateDesc(nextPage);
  }

  public Page<BookEntity> getPageOfNewBooksFromTo(String from, String to, Integer offset, Integer limit){
    Pageable nextPage = PageRequest.of(offset, limit);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    LocalDateTime fromDate = LocalDate.parse(from, formatter).atStartOfDay();
    LocalDateTime toDate = LocalDate.parse(to, formatter).atStartOfDay();
    return bookRepository.findBookEntitiesByPubDateBetweenOrderByPubDateDesc(fromDate, toDate, nextPage);
  }



  public Page<BookEntity> getPageOfPopularBooks(Integer offset, Integer limit){
    Pageable nextPage = PageRequest.of(offset, limit);
    return bookRepository.findAll(nextPage);
  }

  public Page<BookEntity> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit){
    Pageable nextPage = PageRequest.of(offset, limit);
    return bookRepository.findBookEntityByTitleContaining(searchWord, nextPage);
  }

  public Page<BookEntity> getPageOfBooksByTagId(Integer tagId, Integer offset, Integer limit){
    Pageable nextPage = PageRequest.of(offset, limit);
    return bookRepository.findBookEntityByTagIdOrderByPubDateDesc(tagId, nextPage);
  }

  public Page<BookEntity> getPageOfBooksByAuthorId(Integer tagId, Integer offset, Integer limit){
    Pageable nextPage = PageRequest.of(offset, limit);
    return bookRepository.findBookEntitiesByAuthorIdOrderByPubDateDesc(tagId, nextPage);
  }

  public Page<BookEntity> getPageOfBooksByGenreId(Integer genreId, Integer offset, Integer limit){
    Pageable nextPage = PageRequest.of(offset, limit);
    return bookRepository.findBookEntityByGenreIdOrderByPubDateDesc(genreId, nextPage);
  }

  public List<BookEntity> getBooksByTag(Integer tagId){
    return bookRepository.findBookEntitiesByTagId(tagId);
  }

  @Value("${google.books.api.key}")
  private String apiKey;

  public List<BookEntity> getPageOfGoogleBooksApiSearchResult(String searchWord, Integer offset, Integer limit){
    String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes"
        + "?q=" + searchWord
        + "&key=" + apiKey
        + "&filter=paid-ebooks"
        + "&startIndex=" + offset
        + "&maxResult=" + limit;
    Root root = restTemplate.getForEntity(REQUEST_URL, Root.class).getBody();
    ArrayList<BookEntity> list = new ArrayList<>();
    if (root != null){
      for (Item item : root.items){
        BookEntity book = new BookEntity();
        if (item.volumeInfo != null){
          book.setAuthor(new AuthorEntity(item.volumeInfo.authors));
          book.setTitle(item.volumeInfo.title);
          book.setImage(item.volumeInfo.imageLinks.thumbnail);
        }
        if (item.saleInfo != null){
          book.setPrice(item.saleInfo.retailPrice.amount);
          Double oldPrice = book.getPrice();
          book.setPriceOld(oldPrice.intValue());
        }
        list.add(book);
      }
    }
    return list;
  }

}

