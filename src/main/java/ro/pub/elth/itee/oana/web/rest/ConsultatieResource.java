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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.pub.elth.itee.oana.domain.ConcluziiConsultatie;
import ro.pub.elth.itee.oana.domain.Consultatie;
import ro.pub.elth.itee.oana.repository.ConcluziiConsultatieRepository;
import ro.pub.elth.itee.oana.repository.ConsultatieRepository;
import ro.pub.elth.itee.oana.security.AuthoritiesConstants;
import ro.pub.elth.itee.oana.security.SecurityUtils;
import ro.pub.elth.itee.oana.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link ro.pub.elth.itee.oana.domain.Consultatie}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ConsultatieResource {

    private final Logger log = LoggerFactory.getLogger(ConsultatieResource.class);
    private final Logger log1 = LoggerFactory.getLogger(ConcluziiConsultatieResource.class);

    private static final String ENTITY_NAME = "consultatie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsultatieRepository consultatieRepository;

    @Autowired
    private ConcluziiConsultatieRepository concluziiConsultatieRepository;

    public ConsultatieResource(ConsultatieRepository consultatieRepository) {
        this.consultatieRepository = consultatieRepository;
    }

    /**
     * {@code POST  /consultaties} : Create a new consultatie.
     *
     * @param consultatie the consultatie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new consultatie, or with status {@code 400 (Bad Request)} if
     *         the consultatie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/consultaties")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Consultatie> createConsultatie(@Valid @RequestBody Consultatie consultatie) throws URISyntaxException {
        log.debug("REST request to save Consultatie : {}", consultatie);
        if (consultatie.getId() != null) {
            throw new BadRequestAlertException("A new consultatie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Consultatie result = consultatieRepository.save(consultatie);
        return ResponseEntity
            .created(new URI("/api/consultaties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /consultaties/:id} : Updates an existing consultatie.
     *
     * @param id          the id of the consultatie to save.
     * @param consultatie the consultatie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated consultatie, or with status {@code 400 (Bad Request)} if
     *         the consultatie is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the consultatie couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/consultaties/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Consultatie> updateConsultatie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Consultatie consultatie
    ) throws URISyntaxException {
        log.debug("REST request to update Consultatie : {}, {}", id, consultatie);
        if (consultatie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultatie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultatieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Consultatie result = consultatieRepository.save(consultatie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultatie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /consultaties/:id} : Partial updates given fields of an
     * existing consultatie, field will ignore if it is null
     *
     * @param id          the id of the consultatie to save.
     * @param consultatie the consultatie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated consultatie, or with status {@code 400 (Bad Request)} if
     *         the consultatie is not valid, or with status {@code 404 (Not Found)}
     *         if the consultatie is not found, or with status
     *         {@code 500 (Internal Server Error)} if the consultatie couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/consultaties/{id}", consumes = "application/merge-patch+json")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Consultatie> partialUpdateConsultatie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Consultatie consultatie
    ) throws URISyntaxException {
        log.debug("REST request to partial update Consultatie partially : {}, {}", id, consultatie);
        if (consultatie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultatie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultatieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Consultatie> result = consultatieRepository
            .findById(consultatie.getId())
            .map(
                existingConsultatie -> {
                    if (consultatie.getDataOra() != null) {
                        existingConsultatie.setDataOra(consultatie.getDataOra());
                    }
                    if (consultatie.getDescriere() != null) {
                        existingConsultatie.setDescriere(consultatie.getDescriere());
                    }

                    return existingConsultatie;
                }
            )
            .map(consultatieRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultatie.getId().toString())
        );
    }

    /**
     * {@code GET  /consultaties} : get all the consultaties.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of consultaties in body.
     */
    @GetMapping("/consultaties")
    @PreAuthorize(
        "hasRole(\"" +
        AuthoritiesConstants.ADMIN +
        "\") || hasRole(\"" +
        AuthoritiesConstants.CLIENT +
        "\") || hasRole(\"" +
        AuthoritiesConstants.MEDIC +
        "\")"
    )
    public ResponseEntity<List<Consultatie>> getAllConsultaties(Pageable pageable) {
        log.debug("REST request to get a page of Consultaties");
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            Page<Consultatie> page = consultatieRepository.findAll(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } else if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.MEDIC)) {
            Page<Consultatie> page = consultatieRepository.findByMedicIsCurrentMedic(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } else {
            Page<Consultatie> page = consultatieRepository.findByClientIsCurrentClient(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
    }

    //
    @GetMapping("/getListaConsultatii")
    public @ResponseBody ResponseEntity<List<Consultatie>> getListaConsultatii() {
        log.debug("REST request to get Rezervare : {}");
        List<Consultatie> consultatii = consultatieRepository.findAll();
        try {
            return new ResponseEntity<List<Consultatie>>(consultatii, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return null;
        }
    }

    /**
     * {@code GET  /consultaties/:id} : get the "id" consultatie.
     *
     * @param id the id of the consultatie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the consultatie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/consultaties/{id}")
    @PreAuthorize( "hasRole(\"" + AuthoritiesConstants.ADMIN + "\") || hasRole(\"" 
    + AuthoritiesConstants.CLIENT + "\") || hasRole(\"" + AuthoritiesConstants.MEDIC + "\")")
    public ResponseEntity<Consultatie> getConsultatie(@PathVariable Long id) {
        log.debug("REST request to get Consultatie : {}", id);
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            Optional<Consultatie> consultatie = consultatieRepository.findById(id);
            return ResponseUtil.wrapOrNotFound(consultatie);
        } else if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.MEDIC)) {
            Optional<Consultatie> consultatie = consultatieRepository.findByMedicIsCurrentMedicById(id);
            return ResponseUtil.wrapOrNotFound(consultatie);
        } else {
            Optional<Consultatie> consultatie = consultatieRepository.findByClientIsCurrentClientById(id);
            return ResponseUtil.wrapOrNotFound(consultatie);
        }
    }

    /**
     * {@code DELETE  /consultaties/:id} : delete the "id" consultatie.
     *
     * @param id the id of the consultatie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */

    @DeleteMapping("/consultaties/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteConsultatie(@PathVariable Long id) {
        Optional<ConcluziiConsultatie> concluziiCautate = concluziiConsultatieRepository.findConcluziiForCurrentConsultatie(id);

        if (concluziiCautate.isPresent()) {
            log1.debug("REST request to delete ConcluziiConsultatie : {}", concluziiCautate.get().getId());
            concluziiConsultatieRepository.deleteById(concluziiCautate.get().getId());
        }
        log.debug("REST request to delete Consultatie : {}", id);
        consultatieRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
