package ro.pub.elth.itee.oana.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.pub.elth.itee.oana.domain.Client;
import ro.pub.elth.itee.oana.domain.Consultatie;

/**
 * Spring Data SQL repository for the Client entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("select client from Client client where client.user.login = ?#{principal.username}")
    Page<Client> findByUserIsCurrentUser(Pageable pageable);

    @Query("select client from Client client where client.id = :id and client.user.login = ?#{principal.username}")
    Optional<Client> findByUserIsCurrentUserById(@Param("id") Long id);

    @Query("select client from Client client where client.user.login = :login")
    Optional<Client> findClientForCurrentLogin(@Param("login") String login);
}
