package id.my.hendisantika.reservationservice.controller;

import id.my.hendisantika.reservationservice.dto.Movie;
import id.my.hendisantika.reservationservice.dto.response.MovieResponseDTO;
import id.my.hendisantika.reservationservice.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 13/01/26
 * Time: 07.11
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<MovieResponseDTO> getMovieById(@PathVariable Long id) {
        return movieService.findMovieById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return movieService.findAllMovies();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponseDTO> addMovie(@RequestBody Movie movie) {
        return movieService.addMovie(movie);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponseDTO> updateMovie(@RequestBody Movie movie) {
        return movieService.updateMovie(movie);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponseDTO> deleteMovie(@PathVariable Long id) {
        return movieService.deleteMovie(id);
    }
}
