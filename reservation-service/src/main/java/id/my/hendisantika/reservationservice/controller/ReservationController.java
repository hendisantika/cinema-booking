package id.my.hendisantika.reservationservice.controller;

import id.my.hendisantika.reservationservice.dto.Reservation;
import id.my.hendisantika.reservationservice.dto.response.ReservationResponseDTO;
import id.my.hendisantika.reservationservice.service.ReservationService;
import id.my.hendisantika.reservationservice.service.UserSyncService;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
 * Time: 07.52
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final UserSyncService userSyncService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ReservationResponseDTO> addReservation(@RequestBody Reservation reservationRequestDTO, @AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ReservationResponseDTO("not found jwt", null));
        }
        userSyncService.syncUser(jwt);
        return reservationService.setReservation(reservationRequestDTO);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return reservationService.getAllReservation();
    }
}
