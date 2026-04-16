package com.pknu26.movie_mng.service;


import com.pknu26.movie_mng.entity.Movie;
import com.pknu26.movie_mng.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    // 전체 영화 목록
    public List<Movie> findMovies() {
        return movieRepository.findAll();
    }

    // 특정 영화 한 건 조회
    public Movie findOne(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    // 영화 등록
    @Transactional
    public void saveMovie(Movie movie) {
        movie.setCreatedAt(LocalDateTime.now()); // 직접 생성일 설정
        movieRepository.save(movie);
    }

    // 영화 정보 수정
    @Transactional
    public void updateMovie(Long id, Movie updateParam) {
        // 1. 기존 엔티티 조회
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 영화가 존재하지 않습니다. id=" + id));
        
        // 2. 데이터 수정
        movie.setTitle(updateParam.getTitle());
        movie.setOriginalTitle(updateParam.getOriginalTitle());
        movie.setDirector(updateParam.getDirector());
        movie.setActors(updateParam.getActors());
        movie.setGenre(updateParam.getGenre());
        movie.setReleaseDate(updateParam.getReleaseDate());
        movie.setRunningTime(updateParam.getRunningTime());
        movie.setRating(updateParam.getRating());
        movie.setDescription(updateParam.getDescription());
        
        // 3. 수정일 직접 설정
        movie.setUpdatedAt(LocalDateTime.now());
    }

    // 영화 삭제
    @Transactional
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}