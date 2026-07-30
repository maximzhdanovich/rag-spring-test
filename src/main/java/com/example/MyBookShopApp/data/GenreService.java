package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.genre.GenreEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreService {

  private final GenreRepository genreRepository;

  @Autowired
  public GenreService(GenreRepository genreRepository) {
    this.genreRepository = genreRepository;
  }

  public List<GenreEntity> getGenres() {
    return genreRepository.findAll();
  }

  public GenreEntity getGenreById(Integer genreId){
    return genreRepository.findGenreEntityById(genreId);
  }
}
