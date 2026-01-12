package id.my.hendisantika.reservationservice.service;

import id.my.hendisantika.reservationservice.dto.PaymentDetails;
import id.my.hendisantika.reservationservice.dto.Reservation;
import id.my.hendisantika.reservationservice.dto.SeatDetail;
import id.my.hendisantika.reservationservice.dto.response.ReservationResponseDTO;
import id.my.hendisantika.reservationservice.entity.*;
import id.my.hendisantika.reservationservice.enums.ReservationStatus;
import id.my.hendisantika.reservationservice.enums.SeatType;
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
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional
    public ResponseEntity<ReservationResponseDTO> setReservation(Reservation reservationRequestDTO) {
        try {
            if (reservationRequestDTO == null) {
                log.error("reservations is null for the add");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            CustomerEntity customerEntity = customerRepository.findById(reservationRequestDTO.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            CinemaEntity cinemaEntity = cinemaRepository.findById(reservationRequestDTO.getCinemaId())
                    .orElseThrow(() -> new RuntimeException("Cinema not found"));

            MovieEntity movieEntity = movieRepository.findById(reservationRequestDTO.getMovieId())
                    .orElseThrow(() -> new RuntimeException("Movie not found"));

            List<SeatEntity> seatEntities = seatRepository.findAllById(reservationRequestDTO.getSeatIds());

            if (seatEntities.size() != reservationRequestDTO.getSeatIds().size()) {
                log.error("Some seats not found");                        //check the all seat in the hall
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ReservationResponseDTO("Some seats not found", null));
            }
            for (SeatEntity seatEntity : seatEntities) {
                if (!seatEntity.isAvailable()) {
                    log.error("Seat {}{} not available", seatEntity.getRowLetter(), seatEntity.getNumber());
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ReservationResponseDTO("Seat " + seatEntity.getRowLetter() + seatEntity.getNumber() + " not available", null));
                }
            }
            ReservationEntity reservationEntity = new ReservationEntity();
            reservationEntity.setConNumber(reservationRequestDTO.getConNumber());
            reservationEntity.setDescription(reservationRequestDTO.getDescription());
            reservationEntity.setDate(reservationRequestDTO.getDate());
            reservationEntity.setTime(reservationRequestDTO.getTime());
            reservationEntity.setCustomer(customerEntity);
            reservationEntity.setCinema(cinemaEntity);
            reservationEntity.setStatus(ReservationStatus.PENDING);
            reservationEntity.setMovie(movieEntity);


            // Set<SeatEntity> seats=new HashSet<>();
            for (SeatEntity seatEntity : seatEntities) {
                seatEntity.setAvailable(false);
                seatEntity.getReservation().add(reservationEntity);
                reservationEntity.getSeats().add(seatEntity);
            }

            ReservationEntity savedReservation = reservationRepository.save(reservationEntity);
            log.info("Created reservation with id : {}", reservationEntity.getReservationId());


            Reservation reservation = new Reservation();
            reservation.setReservationId(savedReservation.getReservationId());
            reservation.setConNumber(savedReservation.getConNumber());
            reservation.setDescription(savedReservation.getDescription());
            reservation.setDate(savedReservation.getDate());
            reservation.setTime(savedReservation.getTime());
            reservation.setCinemaId(cinemaEntity.getId());
            reservation.setMovieId(movieEntity.getId());
            reservation.setCustomerId(customerEntity.getId());

            List<Long> seatIds = savedReservation.getSeats()
                    .stream().map(SeatEntity::getId).collect(Collectors.toList());
            reservation.setSeatIds(seatIds);

            sender.broadcast(new ReservationResponseDTO("Reservation made successfully", reservation));

            List<SeatDetail> seatDetails = new ArrayList<>();
            PaymentDetails paymentDetails = new PaymentDetails();
            paymentDetails.setReservationId(reservationEntity.getReservationId());
            paymentDetails.setCustomerName(reservationEntity.getCustomer().getName());
            paymentDetails.setEmail(reservationEntity.getCustomer().getEmail());
            paymentDetails.setCurrency("LKR");

            Map<SeatType, Integer> seatCountMap = new EnumMap<>(SeatType.class);
            Map<SeatType, Double> seatUnitPriceMap = new EnumMap<>(SeatType.class);

            for (SeatEntity seat : reservationEntity.getSeats()) {
                SeatType seatType = seat.getType();
                Double price = seat.getPrice();

                seatCountMap.put(seatType, seatCountMap.getOrDefault(seatType, 0) + 1);

                if (seatUnitPriceMap.containsKey(seatType)) {
                    if (!seatUnitPriceMap.get(seatType).equals(price)) {
                        throw new IllegalStateException("price mismatch for seat type: " + seatType);
                    }
                } else {
                    seatUnitPriceMap.put(seatType, price);
                }
            }
            for (SeatType type : seatCountMap.keySet()) {
                SeatDetail seatDetail = new SeatDetail();
                seatDetail.setType(type);
                seatDetail.setQuantity(seatCountMap.get(type));
                seatDetail.setPrice(seatUnitPriceMap.get(type));
                seatDetails.add(seatDetail);
            }
            paymentDetails.setSeats(seatDetails);

            reservationCreatedEventProducer.publishReservationCreated(paymentDetails);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ReservationResponseDTO("Reservation made successfully", reservation));

        } catch (ResourceAccessException ex) {
            log.error("Resource access exception: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            log.error("Exception while finding user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<ReservationResponseDTO> deleteReservation(UUID reservationId) {
        try {
            if (reservationId == null) {
                log.warn("reservations is null for the delete");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if (reservationRepository.existsByReservationId(reservationId)) {
                reservationRepository.deleteByReservationId(reservationId);
                log.info("Deleted reservation with id: {}", reservationId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ReservationResponseDTO("reservation " + reservationId + " delected Successfully ", null));
            }
            log.warn("No reservation with id: {} for the delete", reservationId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ReservationResponseDTO("reservation not found", null));
        } catch (Exception e) {
            log.error("Exception while finding user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<ReservationResponseDTO> updateReservation(Reservation reservation) {
        try {
            if (reservation == null) {
                log.error("reservations is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            ReservationEntity reservationEntity = reservationRepository.findByReservationId(reservation.getReservationId());
            if (reservationEntity != null) {
                reservationEntity.setDescription(reservation.getDescription());
                reservationEntity.setDate(reservation.getDate());
                reservationEntity.setTime(reservation.getTime());
                reservationEntity.setConNumber(reservation.getConNumber());


                ReservationEntity saved = reservationRepository.save(reservationEntity);
                Reservation reservationModel = modelMapper.map(reservationEntity, Reservation.class);
                reservationModel.setCinemaId(reservation.getCinemaId());
                reservationModel.setMovieId(reservation.getMovieId());
                reservationModel.setCustomerId(reservation.getCustomerId());
                log.info("Updated reservation with id: {}", saved.getReservationId());
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ReservationResponseDTO("reservation " + saved.getReservationId() + " updated Successfully ", reservationModel));
            }
            log.warn("No reservation with id: {}", reservation.getReservationId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ReservationResponseDTO("reservation not found", null));

        } catch (Exception e) {
            log.error("Exception while finding user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<Reservation>> getReservationByDate(LocalDate reservationDate) {
        try {
            if (reservationDate == null) {
                log.warn("reservationsDate is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<ReservationEntity> reservationEntities = reservationRepository.getReservationByDate(reservationDate);
            if (reservationEntities != null) {
                List<Reservation> reservations = reservationEntities.stream()
                        .map(entity -> modelMapper.map(entity, Reservation.class)).toList();
                log.info("Found reservations with date: {}", reservationDate);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .body(reservations);
            }
            log.warn("No reservations with date: {}", reservationDate);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            log.error("Exception while finding user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<Reservation>> getReservationByTime(LocalTime reservationTime) {
        try {
            if (reservationTime == null) {
                log.warn("reservationsTime is null for the get reservations");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<ReservationEntity> reservationEntities = reservationRepository.getReservationByTime(reservationTime);
            if (reservationEntities != null) {
                List<Reservation> reservations = reservationEntities.stream()
                        .map(entity -> modelMapper.map(entity, Reservation.class)).toList();
                log.info("Found reservations with time: {}", reservationTime);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .body(reservations);
            }
            log.warn("No reservations with time: {}", reservationTime);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            log.error("Exception while finding user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<List<Reservation>> getReservationByDateAndTime(LocalDate reservationDate, LocalTime reservationTime) {
        try {
            if (reservationDate == null || reservationTime == null) {
                log.warn("reservations Date or Time is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            List<ReservationEntity> reservationEntities = reservationRepository.findByDateAndTime(reservationDate, reservationTime);
            if (!reservationEntities.isEmpty()) {
                List<Reservation> reservations = reservationEntities.stream()
                        .map(entity -> modelMapper.map(entity, Reservation.class)).toList();
                log.info("Found reservations with date: {} and time: {}", reservationDate, reservationTime);
                return ResponseEntity.status(HttpStatus.FOUND)
                        .body(reservations);
            }
            log.warn("No reservations with date: {} and time: {}", reservationDate, reservationTime);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Exception while finding user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<ReservationResponseDTO> cancelReservation(UUID id) {
        try {
            ReservationEntity reservationEntity = reservationRepository.findByReservationId(id);
            for (SeatEntity seat : reservationEntity.getSeats()) {
                seat.setAvailable(true);
                seat.setReservation(null);
            }
            reservationRepository.delete(reservationEntity);
            log.info("Cancelled reservation with id: {}", id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ReservationResponseDTO("Reservation cancelled successfully", null));
        } catch (Exception e) {
            log.error("Exception while finding user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
