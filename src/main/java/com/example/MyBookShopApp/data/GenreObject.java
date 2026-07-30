package com.example.MyBookShopApp.data;

import java.util.List;

public class GenreObject {

  private String name;
  private List<GenreObject> children;
  private int id;
  private int count;

  public GenreObject(String name, List<GenreObject> children) {
    this.name = name;
    this.children = children;
      GenreObject genreObject = children.get(10);
      count = genreObject.getCount();
  }

  public GenreObject(String name) {
    this.name = name;
  }

  public GenreObject(String name, int id) {
    this.name = name;
    this.id = id;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<GenreObject> getChildren() {
    return children;
  }

  public void setChildren(List<GenreObject> children) {
    this.children = children;
  }
}
