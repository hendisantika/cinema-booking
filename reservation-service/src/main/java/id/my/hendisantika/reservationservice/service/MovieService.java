package id.my.hendisantika.reservationservice.service;

import id.my.hendisantika.reservationservice.dto.Movie;
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
}
