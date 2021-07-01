package ro.pub.elth.itee.oana.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.pub.elth.itee.oana.domain.Client;
import ro.pub.elth.itee.oana.domain.ConcluziiConsultatie;
import ro.pub.elth.itee.oana.domain.Consultatie;
import ro.pub.elth.itee.oana.repository.ClientRepository;
import ro.pub.elth.itee.oana.repository.ConcluziiConsultatieRepository;
import ro.pub.elth.itee.oana.repository.ConsultatieRepository;
import ro.pub.elth.itee.oana.security.AuthoritiesConstants;
import ro.pub.elth.itee.oana.security.SecurityUtils;
import ro.pub.elth.itee.oana.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ro.pub.elth.itee.oana.domain.Client}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private final Logger log2 = LoggerFactory.getLogger(ConsultatieResource.class);
    private final Logger log1 = LoggerFactory.getLogger(ConcluziiConsultatieResource.class);

    private static final String ENTITY_NAME = "client";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientRepository clientRepository;

    @Autowired
    private ConsultatieRepository consultatieRepository;

    @Autowired
    private ConcluziiConsultatieRepository concluziiConsultatieRepository;

    public ClientResource(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * {@code POST  /clients} : Create a new client.
     *
     * @param client the client to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new client, or with status {@code 400 (Bad Request)} if the
     *         client has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clients")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) throws URISyntaxException {
        log.debug("REST request to save Client : {}", client);
        if (client.getId() != null) {
            throw new BadRequestAlertException("A new client cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Client result = clientRepository.save(client);
        return ResponseEntity
            .created(new URI("/api/clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clients/:id} : Updates an existing client.
     *
     * @param id     the id of the client to save.
     * @param client the client to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated client, or with status {@code 400 (Bad Request)} if the
     *         client is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the client couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clients/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Client> updateClient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Client client
    ) throws URISyntaxException {
        log.debug("REST request to update Client : {}, {}", id, client);
        if (client.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, client.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Client result = clientRepository.save(client);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, client.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clients/:id} : Partial updates given fields of an existing
     * client, field will ignore if it is null
     *
     * @param id     the id of the client to save.
     * @param client the client to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated client, or with status {@code 400 (Bad Request)} if the
     *         client is not valid, or with status {@code 404 (Not Found)} if the
     *         client is not found, or with status
     *         {@code 500 (Internal Server Error)} if the client couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clients/{id}", consumes = "application/merge-patch+json")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Client> partialUpdateClient(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Client client
    ) throws URISyntaxException {
        log.debug("REST request to partial update Client partially : {}, {}", id, client);
        if (client.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, client.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Client> result = clientRepository
            .findById(client.getId())
            .map(
                existingClient -> {
                    if (client.getNumeSiPrenume() != null) {
                        existingClient.setNumeSiPrenume(client.getNumeSiPrenume());
                    }
                    if (client.getDataNastere() != null) {
                        existingClient.setDataNastere(client.getDataNastere());
                    }
                    if (client.getAdresa() != null) {
                        existingClient.setAdresa(client.getAdresa());
                    }
                    if (client.getTelefon() != null) {
                        existingClient.setTelefon(client.getTelefon());
                    }
                    if (client.getEmail() != null) {
                        existingClient.setEmail(client.getEmail());
                    }

                    return existingClient;
                }
            )
            .map(clientRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, client.getId().toString())
        );
    }

    /**
     * {@code GET  /clients} : get all the clients.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of clients in body.
     */
    @GetMapping("/clients")
    @PreAuthorize(
        "hasRole(\"" +
        AuthoritiesConstants.ADMIN +
        "\") || hasRole(\"" +
        AuthoritiesConstants.CLIENT +
        "\") || hasRole(\"" +
        AuthoritiesConstants.MEDIC +
        "\")"
    )
    public ResponseEntity<List<Client>> getAllClients(Pageable pageable) {
        log.debug("REST request to get a page of Clients");
        if (
            SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN) ||
            SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.MEDIC)
        ) {
            Page<Client> page = clientRepository.findAll(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } else {
            Page<Client> page = clientRepository.findByUserIsCurrentUser(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
    }

    /**
     * {@code GET  /clients/:id} : get the "id" client.
     *
     * @param id the id of the client to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the client, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clients/{id}")
    @PreAuthorize(
        "hasRole(\"" +
        AuthoritiesConstants.ADMIN +
        "\") || hasRole(\"" +
        AuthoritiesConstants.CLIENT +
        "\") || hasRole(\"" +
        AuthoritiesConstants.MEDIC +
        "\")"
    )
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        log.debug("REST request to get Client : {}", id);
        if (
            SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN) ||
            SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.MEDIC)
        ) {
            Optional<Client> client = clientRepository.findById(id);
            return ResponseUtil.wrapOrNotFound(client);
        } else {
            Optional<Client> client = clientRepository.findByUserIsCurrentUserById(id);
            return ResponseUtil.wrapOrNotFound(client);
        }
    }

    /**
     * {@code DELETE  /clients/:id} : delete the "id" client.
     *
     * @param id the id of the client to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clients/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        List<Consultatie> consultatiiCautate = consultatieRepository.findConsultatiiForCurrentClientList(id);

        if (!consultatiiCautate.isEmpty()) {
            for (Consultatie consultatieGasita : consultatiiCautate) {
                Optional<ConcluziiConsultatie> concluzieCautata = concluziiConsultatieRepository.findConcluziiForCurrentConsultatie(
                    consultatieGasita.getId()
                );

                if (concluzieCautata.isPresent()) {
                    log1.debug("REST request to delete ConcluziiConsultatie : {}", concluzieCautata.get().getId());
                    concluziiConsultatieRepository.deleteById(concluzieCautata.get().getId());
                }

                log2.debug("REST request to delete Consultatie : {}", consultatieGasita.getId());
                consultatieRepository.deleteById(consultatieGasita.getId());
            }
        }

        log.debug("REST request to delete Client : {}", id);
        clientRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
