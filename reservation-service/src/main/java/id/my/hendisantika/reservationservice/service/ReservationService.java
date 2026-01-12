package id.my.hendisantika.reservationservice.service;

import id.my.hendisantika.reservationservice.event.ReservationCreatedEventProducer;
import id.my.hendisantika.reservationservice.event.ReservationSuccessEventProducer;
import id.my.hendisantika.reservationservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
