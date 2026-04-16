package com.pknu26.movie_mng.controller;

import com.pknu26.movie_mng.entity.Movie;
import com.pknu26.movie_mng.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    // 영화 목록 조회
    @GetMapping
    public String list(Model model) {
        List<Movie> movies = movieService.findMovies();
        model.addAttribute("movies", movies);
        return "list";
    }

    // 영화 등록 폼 이동
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "createForm";
    }

    // 영화 등록 실행
    @PostMapping("/new")
    public String create(@ModelAttribute Movie movie) {
        movieService.saveMovie(movie);
        return "redirect:/movies";
    }

    // 영화 상세 조회
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Movie movie = movieService.findOne(id);
        model.addAttribute("movie", movie);
        return "detail";
    }

    // 영화 수정 폼 이동
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Movie movie = movieService.findOne(id);
        model.addAttribute("movie", movie);
        return "editForm";
    }

    // 영화 수정
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, @ModelAttribute Movie movie) {
        movieService.updateMovie(id, movie);
        return "redirect:/movies/{id}";
    }

    // 영화 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }
}