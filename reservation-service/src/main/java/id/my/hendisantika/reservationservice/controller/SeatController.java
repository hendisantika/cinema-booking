package id.my.hendisantika.reservationservice.controller;

import id.my.hendisantika.reservationservice.dto.Seat;
import id.my.hendisantika.reservationservice.dto.response.SeatResponseDTO;
import id.my.hendisantika.reservationservice.service.SeatService;
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
 * Time: 07.55
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/api/seat")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeatResponseDTO> addSeat(@RequestBody Seat seat) {
        return seatService.addSeat(seat);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeatResponseDTO> updateSeat(@RequestBody Seat seat) {
        return seatService.updateSeat(seat);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeatResponseDTO> deleteSeat(@PathVariable Long id) {
        return seatService.deleteSeat(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Seat>> seats() {
        return seatService.getSeats();
    }
}
