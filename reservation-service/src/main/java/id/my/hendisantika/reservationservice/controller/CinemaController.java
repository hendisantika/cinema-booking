package id.my.hendisantika.reservationservice.controller;

import id.my.hendisantika.reservationservice.dto.Cinema;
import id.my.hendisantika.reservationservice.dto.response.CinemaResponseDTO;
import id.my.hendisantika.reservationservice.service.CinemaService;
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
 * Time: 07.02
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/api/cinema")
@RequiredArgsConstructor
public class CinemaController {
    private final CinemaService cinemaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CinemaResponseDTO> addCinema(@RequestBody Cinema cinema) {
        return cinemaService.addCinema(cinema);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Cinema>> getAllCinemas() {
        return cinemaService.getAllCinemas();
    }
}
