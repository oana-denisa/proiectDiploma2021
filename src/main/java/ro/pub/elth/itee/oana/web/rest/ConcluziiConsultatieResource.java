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
import ro.pub.elth.itee.oana.domain.ConcluziiConsultatie;
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
 * {@link ro.pub.elth.itee.oana.domain.ConcluziiConsultatie}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ConcluziiConsultatieResource {

    private final Logger log = LoggerFactory.getLogger(ConcluziiConsultatieResource.class);

    private static final String ENTITY_NAME = "concluziiConsultatie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConcluziiConsultatieRepository concluziiConsultatieRepository;

    public ConcluziiConsultatieResource(ConcluziiConsultatieRepository concluziiConsultatieRepository) {
        this.concluziiConsultatieRepository = concluziiConsultatieRepository;
    }

    /**
     * {@code POST  /concluzii-consultaties} : Create a new concluziiConsultatie.
     *
     * @param concluziiConsultatie the concluziiConsultatie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new concluziiConsultatie, or with status
     *         {@code 400 (Bad Request)} if the concluziiConsultatie has already an
     *         ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/concluzii-consultaties")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ConcluziiConsultatie> createConcluziiConsultatie(@Valid @RequestBody ConcluziiConsultatie concluziiConsultatie)
        throws URISyntaxException {
        log.debug("REST request to save ConcluziiConsultatie : {}", concluziiConsultatie);
        if (concluziiConsultatie.getId() != null) {
            throw new BadRequestAlertException("A new concluziiConsultatie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConcluziiConsultatie result = concluziiConsultatieRepository.save(concluziiConsultatie);
        return ResponseEntity
            .created(new URI("/api/concluzii-consultaties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /concluzii-consultaties/:id} : Updates an existing
     * concluziiConsultatie.
     *
     * @param id                   the id of the concluziiConsultatie to save.
     * @param concluziiConsultatie the concluziiConsultatie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated concluziiConsultatie, or with status
     *         {@code 400 (Bad Request)} if the concluziiConsultatie is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         concluziiConsultatie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/concluzii-consultaties/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ConcluziiConsultatie> updateConcluziiConsultatie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConcluziiConsultatie concluziiConsultatie
    ) throws URISyntaxException {
        log.debug("REST request to update ConcluziiConsultatie : {}, {}", id, concluziiConsultatie);
        if (concluziiConsultatie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, concluziiConsultatie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!concluziiConsultatieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConcluziiConsultatie result = concluziiConsultatieRepository.save(concluziiConsultatie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, concluziiConsultatie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /concluzii-consultaties/:id} : Partial updates given fields of
     * an existing concluziiConsultatie, field will ignore if it is null
     *
     * @param id                   the id of the concluziiConsultatie to save.
     * @param concluziiConsultatie the concluziiConsultatie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated concluziiConsultatie, or with status
     *         {@code 400 (Bad Request)} if the concluziiConsultatie is not valid,
     *         or with status {@code 404 (Not Found)} if the concluziiConsultatie is
     *         not found, or with status {@code 500 (Internal Server Error)} if the
     *         concluziiConsultatie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/concluzii-consultaties/{id}", consumes = "application/merge-patch+json")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ConcluziiConsultatie> partialUpdateConcluziiConsultatie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConcluziiConsultatie concluziiConsultatie
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConcluziiConsultatie partially : {}, {}", id, concluziiConsultatie);
        if (concluziiConsultatie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, concluziiConsultatie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!concluziiConsultatieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConcluziiConsultatie> result = concluziiConsultatieRepository
            .findById(concluziiConsultatie.getId())
            .map(
                existingConcluziiConsultatie -> {
                    if (concluziiConsultatie.getDiagnostic() != null) {
                        existingConcluziiConsultatie.setDiagnostic(concluziiConsultatie.getDiagnostic());
                    }
                    if (concluziiConsultatie.getTratament() != null) {
                        existingConcluziiConsultatie.setTratament(concluziiConsultatie.getTratament());
                    }
                    if (concluziiConsultatie.getObservatii() != null) {
                        existingConcluziiConsultatie.setObservatii(concluziiConsultatie.getObservatii());
                    }
                    if (concluziiConsultatie.getControlUrmator() != null) {
                        existingConcluziiConsultatie.setControlUrmator(concluziiConsultatie.getControlUrmator());
                    }

                    return existingConcluziiConsultatie;
                }
            )
            .map(concluziiConsultatieRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, concluziiConsultatie.getId().toString())
        );
    }

    /**
     * {@code GET  /concluzii-consultaties} : get all the concluziiConsultaties.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of concluziiConsultaties in body.
     */
    @GetMapping("/concluzii-consultaties")
    @PreAuthorize(
        "hasRole(\"" +
        AuthoritiesConstants.ADMIN +
        "\") || hasRole(\"" +
        AuthoritiesConstants.CLIENT +
        "\") || hasRole(\"" +
        AuthoritiesConstants.MEDIC +
        "\")"
    )
    public ResponseEntity<List<ConcluziiConsultatie>> getAllConcluziiConsultaties(Pageable pageable) {
        log.debug("REST request to get a page of ConcluziiConsultaties");
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            Page<ConcluziiConsultatie> page = concluziiConsultatieRepository.findAll(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } else if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.MEDIC)) {
            Page<ConcluziiConsultatie> page = concluziiConsultatieRepository.findByMedicIsCurrentMedic(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } else {
            Page<ConcluziiConsultatie> page = concluziiConsultatieRepository.findByUserIsCurrentUser(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
    }

    /**
     * {@code GET  /concluzii-consultaties/:id} : get the "id" concluziiConsultatie.
     *
     * @param id the id of the concluziiConsultatie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the concluziiConsultatie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/concluzii-consultaties/{id}")
    @PreAuthorize(
        "hasRole(\"" +
        AuthoritiesConstants.ADMIN +
        "\") || hasRole(\"" +
        AuthoritiesConstants.CLIENT +
        "\") || hasRole(\"" +
        AuthoritiesConstants.MEDIC +
        "\")"
    )
    public ResponseEntity<ConcluziiConsultatie> getConcluziiConsultatie(@PathVariable Long id) {
        log.debug("REST request to get ConcluziiConsultatie : {}", id);
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            Optional<ConcluziiConsultatie> concluziiConsultatie = concluziiConsultatieRepository.findById(id);
            return ResponseUtil.wrapOrNotFound(concluziiConsultatie);
        } else if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.MEDIC)) {
            Optional<ConcluziiConsultatie> concluziiConsultatie = concluziiConsultatieRepository.findByMedicIsCurrentMedicById(id);
            return ResponseUtil.wrapOrNotFound(concluziiConsultatie);
        } else {
            Optional<ConcluziiConsultatie> concluziiConsultatie = concluziiConsultatieRepository.findByUserIsCurrentUserById(id);
            return ResponseUtil.wrapOrNotFound(concluziiConsultatie);
        }
    }

    /**
     * {@code DELETE  /concluzii-consultaties/:id} : delete the "id"
     * concluziiConsultatie.
     *
     * @param id the id of the concluziiConsultatie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/concluzii-consultaties/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteConcluziiConsultatie(@PathVariable Long id) {
        log.debug("REST request to delete ConcluziiConsultatie : {}", id);
        concluziiConsultatieRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
