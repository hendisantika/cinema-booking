package id.my.hendisantika.reservationservice.service;

import id.my.hendisantika.reservationservice.dto.Seat;
import id.my.hendisantika.reservationservice.dto.response.SeatResponseDTO;
import id.my.hendisantika.reservationservice.entity.SeatEntity;
import id.my.hendisantika.reservationservice.repository.CinemaRepository;
import id.my.hendisantika.reservationservice.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 13/01/26
 * Time: 06.24
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final ModelMapper modelMapper;
    private final CinemaRepository cinemaRepository;

    public ResponseEntity<SeatResponseDTO> addSeat(Seat seatRequest) {
        try {
            if (seatRequest == null) {
                log.warn("seat is null for add seat");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if (seatRepository.existsByNumber(seatRequest.getNumber()) &&
                    seatRepository.existsByRowLetter(seatRequest.getRowLetter())) {
                log.warn("seat is already exist for add seat");
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            SeatEntity seatEntity = modelMapper.map(seatRequest, SeatEntity.class);
            seatEntity.setCinema(cinemaRepository.findById(seatRequest.getCinemaId()).orElse(null));

            SeatEntity saved = seatRepository.save(seatEntity);
            Seat seat = modelMapper.map(saved, Seat.class);
            seat.setCinemaId(saved.getId());
            log.info("seat added successfully for add seat");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SeatResponseDTO("Seat successfully added", seat));

        } catch (Exception ex) {
            log.error("Exception while adding Seat {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<SeatResponseDTO> updateSeat(Seat seatRequest) {
        try {
            if (seatRequest == null) {
                log.warn("seatRequest is null for update seatRequest");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if (seatRepository.existsById(seatRequest.getId())) {
                SeatEntity seatEntity = seatRepository.save(modelMapper.map(seatRequest, SeatEntity.class));
                Seat seat = modelMapper.map(seatEntity, Seat.class);
                seat.setCinemaId(seat.getCinemaId());
                log.info("seatRequest id {} updated successfully for update seatRequest", seatRequest.getId());
                return ResponseEntity.status(HttpStatus.FOUND)
                        .body(new SeatResponseDTO("Seat id " + seatRequest.getId() + " successfully updated", seat));

            }
            log.warn("seatRequest id {} not found for update seatRequest", seatRequest.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            log.error("Exception while updating Seat {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
