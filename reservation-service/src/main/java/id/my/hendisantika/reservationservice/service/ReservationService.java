package id.my.hendisantika.reservationservice.service;

import id.my.hendisantika.reservationservice.dto.Reservation;
import id.my.hendisantika.reservationservice.dto.response.ReservationResponseDTO;
import id.my.hendisantika.reservationservice.entity.ReservationEntity;
import id.my.hendisantika.reservationservice.entity.SeatEntity;
import id.my.hendisantika.reservationservice.event.ReservationCreatedEventProducer;
import id.my.hendisantika.reservationservice.event.ReservationSuccessEventProducer;
import id.my.hendisantika.reservationservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 13/01/26
 * Time: 06.14
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;
    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;
    private final ReservationSender sender;
    private final ReservationCreatedEventProducer reservationCreatedEventProducer;
    private final ReservationSuccessEventProducer reservationSuccessEventProducer;

    public ResponseEntity<List<Reservation>> getAllReservation() {
        try {
            List<ReservationEntity> reservationEntities = reservationRepository.findAll();
            if (reservationEntities.isEmpty()) {
                log.info("No reservations found");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            List<Reservation> reservations = reservationEntities.stream()
                    .map(entity -> {
                        Reservation reservation = modelMapper.map(entity, Reservation.class);
                        List<Long> seatIds = entity.getSeats().stream().map(SeatEntity::getId).toList();
                        reservation.setSeatIds(seatIds);
                        return reservation;
                    })
                    .toList();

            log.info("Fetched all reservations successfully, count: {}", reservations.size());
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            log.error("Exception while finding user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<ReservationResponseDTO> getReservationById(UUID reservationId) {
        try {
            if (reservationId == null) {
                log.warn("No reservations found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if (reservationRepository.existsByReservationId(reservationId)) {
                log.info("Found reservation with id: {}", reservationId);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .body(new ReservationResponseDTO("reservation ", modelMapper
                                .map(reservationRepository.findByReservationId(reservationId), Reservation.class)));
            }
            log.info("No reservation with id: {}", reservationId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ReservationResponseDTO("reservation with " + reservationId + " not exist ", null));
        } catch (Exception e) {
            log.error("Exception while finding user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
