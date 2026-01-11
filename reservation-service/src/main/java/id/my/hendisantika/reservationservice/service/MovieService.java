package id.my.hendisantika.reservationservice.service;

import id.my.hendisantika.reservationservice.repository.BranchRepository;
import id.my.hendisantika.reservationservice.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
}
