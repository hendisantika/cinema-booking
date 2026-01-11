package id.my.hendisantika.reservationservice.service;

import id.my.hendisantika.reservationservice.dto.Movie;
import id.my.hendisantika.reservationservice.dto.response.MovieResponseDTO;
import id.my.hendisantika.reservationservice.entity.BranchEntity;
import id.my.hendisantika.reservationservice.entity.MovieEntity;
import id.my.hendisantika.reservationservice.repository.BranchRepository;
import id.my.hendisantika.reservationservice.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/01/26
 * Time: 15.12
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<List<Movie>> findAllMovies() {
        try {
            List<MovieEntity> movieEntities = movieRepository.findAll();
            if (movieEntities.isEmpty()) {
                log.warn("Movie list is empty for the get all movie");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            List<Movie> movieList = movieEntities.stream()
                    .map(entity -> {
                        Movie movie = modelMapper.map(entity, Movie.class);
                        movie.setBranchId(entity.getId());
                        return movie;
                    })
                    .toList();
            log.info("Movie list has been successfully retrieved");
            return ResponseEntity.status(HttpStatus.OK).body(movieList);

        } catch (Exception ex) {
            log.error("Exception while finding Branch list: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<MovieResponseDTO> findMovieById(Long movieId) {
        try {
            if (movieId == null) {
                log.warn("Movie id is null for the get movie by id");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if (movieRepository.existsById(movieId)) {
                log.info("Movie with id {} has been successfully retrieved", movieId);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .body(new MovieResponseDTO("found movie ", modelMapper.map(movieRepository.findById(movieId), Movie.class)));
            }
            log.warn("Movie with id {} not found for the get by id", movieId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            log.error("Exception while finding Branch list: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<MovieResponseDTO> addMovie(Movie movie) {
        try {
            if (movie == null) {
                log.warn("Movie is null for the add movie");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if (movieRepository.existsByName(movie.getName())) {
                log.warn("Movie with id {}  already  added", movie.getId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            BranchEntity branchEntity = modelMapper.map(branchRepository.findById(movie.getBranchId()), BranchEntity.class);
            MovieEntity movieEntity = modelMapper.map(movie, MovieEntity.class);
            movieEntity.getBranches().add(branchEntity);
            branchEntity.getMovies().add(movieEntity);

            MovieEntity savedMovieEntity = movieRepository.save(movieEntity);
            branchRepository.save(branchEntity);

            Movie movieMap = modelMapper.map(savedMovieEntity, Movie.class);
            movieMap.setBranchId(branchEntity.getId());

            log.info("Movie with id {} has been successfully added", movie.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MovieResponseDTO("movie Successfully added ", movieMap));
        } catch (Exception ex) {
            log.error("Exception while adding movie {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<MovieResponseDTO> updateMovie(Movie movie) {
        try {
            if (movie == null) {
                log.warn("Movie is null for the update movie");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if (movieRepository.existsById(movie.getId())) {
                movieRepository.save(modelMapper.map(movie, MovieEntity.class));
                log.info("Movie with id {} has been successfully updated", movie.getId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new MovieResponseDTO("Movie with id" + movie.getId() + "updated successfully", movie));
            }
            log.warn("Movie with id {} not found for the update movie", movie.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            log.error("Exception while updating movie {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
