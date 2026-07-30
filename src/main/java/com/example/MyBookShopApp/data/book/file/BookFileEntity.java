package com.example.MyBookShopApp.data.book.file;

import com.example.MyBookShopApp.data.BookEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "book_file")
public class BookFileEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;


  private String hash;

  @Column(name = "type_id")
  private int typeId;

  private String path;

  @ManyToOne
  @JoinColumn(name = "book_id", referencedColumnName = "id")
  private BookEntity book;

  public String getBookFileExtensionString(){
    return BookFileType.getExtensionStringByTypeId(typeId);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public int getTypeId() {
    return typeId;
  }

  public void setTypeId(int typeId) {
    this.typeId = typeId;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
