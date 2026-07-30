package com.example.MyBookShopApp.data;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

  private final TagRepository tagRepository;

  @Autowired
  public TagService(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  public List<TagEntity> getTags() {
    return tagRepository.findAll();
  }

  public TagEntity getTagById(Integer tagId){
    return  tagRepository.findTagEntityById(tagId);
  }
}
