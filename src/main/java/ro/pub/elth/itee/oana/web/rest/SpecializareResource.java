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
import ro.pub.elth.itee.oana.domain.Specializare;
import ro.pub.elth.itee.oana.repository.SpecializareRepository;
import ro.pub.elth.itee.oana.security.AuthoritiesConstants;
import ro.pub.elth.itee.oana.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ro.pub.elth.itee.oana.domain.Specializare}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SpecializareResource {

    private final Logger log = LoggerFactory.getLogger(SpecializareResource.class);

    private static final String ENTITY_NAME = "specializare";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecializareRepository specializareRepository;

    public SpecializareResource(SpecializareRepository specializareRepository) {
        this.specializareRepository = specializareRepository;
    }

    /**
     * {@code POST  /specializares} : Create a new specializare.
     *
     * @param specializare the specializare to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specializare, or with status {@code 400 (Bad Request)} if the specializare has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/specializares")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Specializare> createSpecializare(@Valid @RequestBody Specializare specializare) throws URISyntaxException {
        log.debug("REST request to save Specializare : {}", specializare);
        if (specializare.getId() != null) {
            throw new BadRequestAlertException("A new specializare cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Specializare result = specializareRepository.save(specializare);
        return ResponseEntity
            .created(new URI("/api/specializares/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /specializares/:id} : Updates an existing specializare.
     *
     * @param id the id of the specializare to save.
     * @param specializare the specializare to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specializare,
     * or with status {@code 400 (Bad Request)} if the specializare is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specializare couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/specializares/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Specializare> updateSpecializare(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Specializare specializare
    ) throws URISyntaxException {
        log.debug("REST request to update Specializare : {}, {}", id, specializare);
        if (specializare.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specializare.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specializareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Specializare result = specializareRepository.save(specializare);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specializare.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /specializares/:id} : Partial updates given fields of an existing specializare, field will ignore if it is null
     *
     * @param id the id of the specializare to save.
     * @param specializare the specializare to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specializare,
     * or with status {@code 400 (Bad Request)} if the specializare is not valid,
     * or with status {@code 404 (Not Found)} if the specializare is not found,
     * or with status {@code 500 (Internal Server Error)} if the specializare couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/specializares/{id}", consumes = "application/merge-patch+json")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Specializare> partialUpdateSpecializare(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Specializare specializare
    ) throws URISyntaxException {
        log.debug("REST request to partial update Specializare partially : {}, {}", id, specializare);
        if (specializare.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, specializare.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!specializareRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Specializare> result = specializareRepository
            .findById(specializare.getId())
            .map(
                existingSpecializare -> {
                    if (specializare.getDenumire() != null) {
                        existingSpecializare.setDenumire(specializare.getDenumire());
                    }

                    return existingSpecializare;
                }
            )
            .map(specializareRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specializare.getId().toString())
        );
    }

    /**
     * {@code GET  /specializares} : get all the specializares.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specializares in body.
     */
    @GetMapping("/specializares")
    @PreAuthorize(
        "hasRole(\"" +
        AuthoritiesConstants.ADMIN +
        "\") || hasRole(\"" +
        AuthoritiesConstants.CLIENT +
        "\") || hasRole(\"" +
        AuthoritiesConstants.MEDIC +
        "\")"
    )
    public ResponseEntity<List<Specializare>> getAllSpecializares(Pageable pageable) {
        log.debug("REST request to get a page of Specializares");
        Page<Specializare> page = specializareRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /specializares/:id} : get the "id" specializare.
     *
     * @param id the id of the specializare to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specializare, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/specializares/{id}")
    @PreAuthorize(
        "hasRole(\"" +
        AuthoritiesConstants.ADMIN +
        "\") || hasRole(\"" +
        AuthoritiesConstants.CLIENT +
        "\") || hasRole(\"" +
        AuthoritiesConstants.MEDIC +
        "\")"
    )
    public ResponseEntity<Specializare> getSpecializare(@PathVariable Long id) {
        log.debug("REST request to get Specializare : {}", id);
        Optional<Specializare> specializare = specializareRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(specializare);
    }

    /**
     * {@code DELETE  /specializares/:id} : delete the "id" specializare.
     *
     * @param id the id of the specializare to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/specializares/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteSpecializare(@PathVariable Long id) {
        log.debug("REST request to delete Specializare : {}", id);
        specializareRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
