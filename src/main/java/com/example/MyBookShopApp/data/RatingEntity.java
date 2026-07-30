package com.example.MyBookShopApp.data;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rating")
public class RatingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "book_id")
  private int bookId;

  @Column(name = "one_star")
  private int oneStar;

  @Column(name = "two_star")
  private int twoStar;

  @Column(name = "three_star")
  private int threeStar;

  @Column(name = "four_star")
  private int fourStar;

  @Column(name = "five_star")
  private int fiveStar;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getBookId() {
    return bookId;
  }

  public void setBookId(int bookId) {
    this.bookId = bookId;
  }

  public int getOneStar() {
    return oneStar;
  }

  public void setOneStar(int oneStar) {
    this.oneStar = oneStar;
  }

  public int getTwoStar() {
    return twoStar;
  }

  public void setTwoStar(int twoStar) {
    this.twoStar = twoStar;
  }

  public int getThreeStar() {
    return threeStar;
  }

  public void setThreeStar(int threeStar) {
    this.threeStar = threeStar;
  }

  public int getFourStar() {
    return fourStar;
  }

  public void setFourStar(int fourStar) {
    this.fourStar = fourStar;
  }

  public int getFiveStar() {
    return fiveStar;
  }

  public void setFiveStar(int fiveStar) {
    this.fiveStar = fiveStar;
  }

  public RatingEntity(int oneStar, int twoStar, int threeStar, int fourStar, int fiveStar) {
    this.oneStar = oneStar;
    this.twoStar = twoStar;
    this.threeStar = threeStar;
    this.fourStar = fourStar;
    this.fiveStar = fiveStar;
  }

  public RatingEntity() {
  }
}
