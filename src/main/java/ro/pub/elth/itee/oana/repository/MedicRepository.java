package ro.pub.elth.itee.oana.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.pub.elth.itee.oana.domain.Medic;

/**
 * Spring Data SQL repository for the Medic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicRepository extends JpaRepository<Medic, Long> {
    @Query("select medic from Medic medic where medic.user.login = :login")
    Optional<Medic> findMedicForCurrentLogin(@Param("login") String login);
}
