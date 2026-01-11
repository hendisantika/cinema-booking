package id.my.hendisantika.reservationservice.repository;

import id.my.hendisantika.reservationservice.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    boolean existsByName(String name);
}
