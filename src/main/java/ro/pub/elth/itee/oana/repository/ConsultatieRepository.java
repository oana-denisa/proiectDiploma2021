package ro.pub.elth.itee.oana.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.pub.elth.itee.oana.domain.Consultatie;

/**
 * Spring Data SQL repository for the Consultatie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsultatieRepository extends JpaRepository<Consultatie, Long> {
    @Query("select consultatie from Consultatie consultatie where consultatie.client.user.login = ?#{principal.username}")
    Page<Consultatie> findByClientIsCurrentClient(Pageable pageable);

    @Query(
        "select consultatie from Consultatie consultatie where consultatie.id = :id and consultatie.client.user.login = ?#{principal.username}"
    )
    Optional<Consultatie> findByClientIsCurrentClientById(@Param("id") Long id);

    @Query("select consultatie from Consultatie consultatie where consultatie.medic.user.login = ?#{principal.username}")
    Page<Consultatie> findByMedicIsCurrentMedic(Pageable pageable);

    @Query(
        "select consultatie from Consultatie consultatie where consultatie.id = :id and consultatie.medic.user.login = ?#{principal.username}"
    )
    Optional<Consultatie> findByMedicIsCurrentMedicById(@Param("id") Long id);

    @Query("select consultatie from Consultatie consultatie where consultatie.medic.id = :id")
    List<Consultatie> findConsultatiiForCurrentMedicList(@Param("id") Long id);

    @Query("select consultatie from Consultatie consultatie where consultatie.client.id = :id")
    List<Consultatie> findConsultatiiForCurrentClientList(@Param("id") Long id);
}
