package ro.pub.elth.itee.oana.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import ro.pub.elth.itee.oana.domain.Grad;

/**
 * Spring Data SQL repository for the Grad entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GradRepository extends JpaRepository<Grad, Long> {}
