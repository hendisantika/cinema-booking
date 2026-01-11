package id.my.hendisantika.reservationservice.service;

import id.my.hendisantika.reservationservice.dto.Cinema;
import id.my.hendisantika.reservationservice.dto.response.CinemaResponseDTO;
import id.my.hendisantika.reservationservice.entity.CinemaEntity;
import id.my.hendisantika.reservationservice.repository.BranchRepository;
import id.my.hendisantika.reservationservice.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/01/26
 * Time: 15.08
 * To change this template use File | Settings | File Templates.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CinemaService {

    private final CinemaRepository cinemaRepository;
    private final ModelMapper modelMapper;
    private final BranchRepository branchRepository;

    public ResponseEntity<List<Cinema>> getAllCinemas() {
        try {
            List<CinemaEntity> cinemaEntities = cinemaRepository.findAll();
            if (cinemaEntities.isEmpty()) {
                log.warn("Cinema list is empty for the get all cinemas");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            List<Cinema> cinemas = cinemaEntities.stream()
                    .map(entity -> modelMapper.map(entity, Cinema.class))
                    .toList();
            log.info("Cinema list has been successfully retrieved");
            return ResponseEntity.status(HttpStatus.OK).body(cinemas);
        } catch (Exception ex) {
            log.error("Exception while finding Cinemas: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<CinemaResponseDTO> getCinemasById(Long id) {
        try {
            if (id == null) {
                log.warn("Cinema id is null for the get for the cinema");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if (cinemaRepository.existsById(id)) {
                Optional<CinemaEntity> cinemaEntity = cinemaRepository.findById(id);
                log.info("Cinema entity has been successfully retrieved");
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new CinemaResponseDTO("Cinema is id " + id, modelMapper.map(cinemaEntity, Cinema.class)));
            }
            log.warn("Cinema id {} not found for the get for the cinema", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            log.error("Exception while finding Cinema by ID: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<CinemaResponseDTO> deleteCinema(Long id) {
        try {
            if (id == null) {
                log.warn("Cinema id is null for the delete for the cinema");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if (cinemaRepository.existsById(id)) {
                CinemaEntity cinemaEntity = cinemaRepository.findById(id).orElse(null);
                if (cinemaEntity != null) {
                    cinemaRepository.delete(cinemaEntity);
                    log.info("Cinema {} entity has been successfully deleted", id);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new CinemaResponseDTO("Cinema " + id + "entity has been successfully deleted ", null));
                }

            }
            log.info("Cinema id {} not found for the delete for the cinema", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CinemaResponseDTO("Cinema id " + id + " not found for the delete for the cinema", null));
        } catch (Exception ex) {
            log.error("Exception while finding Cinema by ID: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
