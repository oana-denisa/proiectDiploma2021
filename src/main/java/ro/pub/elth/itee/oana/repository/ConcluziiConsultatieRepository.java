package ro.pub.elth.itee.oana.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.pub.elth.itee.oana.domain.ConcluziiConsultatie;

/**
 * Spring Data SQL repository for the ConcluziiConsultatie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConcluziiConsultatieRepository extends JpaRepository<ConcluziiConsultatie, Long> {
    @Query(
        "select concluziiConsultatie from ConcluziiConsultatie concluziiConsultatie where concluziiConsultatie.consultatie.client.user.login = ?#{principal.username}"
    )
    Page<ConcluziiConsultatie> findByClientIsCurrentClient(Pageable pageable);

    @Query(
        "select concluziiConsultatie from ConcluziiConsultatie concluziiConsultatie where concluziiConsultatie.id = :id and concluziiConsultatie.consultatie.client.user.login = ?#{principal.username}"
    )
    Optional<ConcluziiConsultatie> findByClientIsCurrentClientById(@Param("id") Long id);

    @Query(
        "select concluziiConsultatie from ConcluziiConsultatie concluziiConsultatie where concluziiConsultatie.consultatie.medic.user.login = ?#{principal.username}"
    )
    Page<ConcluziiConsultatie> findByMedicIsCurrentMedic(Pageable pageable);

    @Query(
        "select concluziiConsultatie from ConcluziiConsultatie concluziiConsultatie where concluziiConsultatie.id = :id and concluziiConsultatie.consultatie.medic.user.login = ?#{principal.username}"
    )
    Optional<ConcluziiConsultatie> findByMedicIsCurrentMedicById(@Param("id") Long id);

    @Query("select concluziiConsultatie from ConcluziiConsultatie concluziiConsultatie where concluziiConsultatie.consultatie.id = :id")
    Optional<ConcluziiConsultatie> findConcluziiForCurrentConsultatie(@Param("id") Long id);
}
