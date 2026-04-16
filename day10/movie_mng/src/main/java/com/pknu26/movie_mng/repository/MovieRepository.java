package com.pknu26.movie_mng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pknu26.movie_mng.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

}
