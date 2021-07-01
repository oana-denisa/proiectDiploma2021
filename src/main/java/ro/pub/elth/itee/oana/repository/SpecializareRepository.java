package ro.pub.elth.itee.oana.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ro.pub.elth.itee.oana.domain.Specializare;

/**
 * Spring Data SQL repository for the Specializare entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecializareRepository extends JpaRepository<Specializare, Long> {}
