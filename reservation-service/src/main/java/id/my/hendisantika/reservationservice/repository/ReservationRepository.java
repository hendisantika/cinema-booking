package id.my.hendisantika.reservationservice.repository;

import id.my.hendisantika.reservationservice.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * Project : cinema-booking
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 11/01/26
 * Time: 15.02
 * To change this template use File | Settings | File Templates.
 */
@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> getReservationByDate(LocalDate date);

    List<ReservationEntity> getReservationByTime(LocalTime time);

    List<ReservationEntity> findByDateAndTime(LocalDate date, LocalTime time);

    ReservationEntity findByReservationId(UUID uuid);

    boolean existsByReservationId(UUID reservationId);

    void deleteByReservationId(UUID reservationId);
}
