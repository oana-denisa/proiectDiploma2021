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
import ro.pub.elth.itee.oana.domain.ConcluziiConsultatie;
import ro.pub.elth.itee.oana.domain.Consultatie;
import ro.pub.elth.itee.oana.domain.Medic;
import ro.pub.elth.itee.oana.repository.ConcluziiConsultatieRepository;
import ro.pub.elth.itee.oana.repository.ConsultatieRepository;
import ro.pub.elth.itee.oana.repository.MedicRepository;
import ro.pub.elth.itee.oana.security.AuthoritiesConstants;
import ro.pub.elth.itee.oana.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ro.pub.elth.itee.oana.domain.Medic}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MedicResource {

    private final Logger log = LoggerFactory.getLogger(MedicResource.class);

    private final Logger log2 = LoggerFactory.getLogger(ConsultatieResource.class);
    private final Logger log1 = LoggerFactory.getLogger(ConcluziiConsultatieResource.class);

    private static final String ENTITY_NAME = "medic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicRepository medicRepository;

    @Autowired
    private ConsultatieRepository consultatieRepository;

    @Autowired
    private ConcluziiConsultatieRepository concluziiConsultatieRepository;

    public MedicResource(MedicRepository medicRepository) {
        this.medicRepository = medicRepository;
    }

    /**
     * {@code POST  /medics} : Create a new medic.
     *
     * @param medic the medic to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new medic, or with status {@code 400 (Bad Request)} if the
     *         medic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medics")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Medic> createMedic(@Valid @RequestBody Medic medic) throws URISyntaxException {
        log.debug("REST request to save Medic : {}", medic);
        if (medic.getId() != null) {
            throw new BadRequestAlertException("A new medic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medic result = medicRepository.save(medic);
        return ResponseEntity
            .created(new URI("/api/medics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medics/:id} : Updates an existing medic.
     *
     * @param id    the id of the medic to save.
     * @param medic the medic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated medic, or with status {@code 400 (Bad Request)} if the
     *         medic is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the medic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medics/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Medic> updateMedic(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Medic medic)
        throws URISyntaxException {
        log.debug("REST request to update Medic : {}, {}", id, medic);
        if (medic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medic.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Medic result = medicRepository.save(medic);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medic.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /medics/:id} : Partial updates given fields of an existing
     * medic, field will ignore if it is null
     *
     * @param id    the id of the medic to save.
     * @param medic the medic to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated medic, or with status {@code 400 (Bad Request)} if the
     *         medic is not valid, or with status {@code 404 (Not Found)} if the
     *         medic is not found, or with status
     *         {@code 500 (Internal Server Error)} if the medic couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/medics/{id}", consumes = "application/merge-patch+json")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Medic> partialUpdateMedic(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Medic medic
    ) throws URISyntaxException {
        log.debug("REST request to partial update Medic partially : {}, {}", id, medic);
        if (medic.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medic.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Medic> result = medicRepository
            .findById(medic.getId())
            .map(
                existingMedic -> {
                    if (medic.getNumeSiPrenume() != null) {
                        existingMedic.setNumeSiPrenume(medic.getNumeSiPrenume());
                    }

                    return existingMedic;
                }
            )
            .map(medicRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medic.getId().toString())
        );
    }

    /**
     * {@code GET  /medics} : get all the medics.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of medics in body.
     */
    @GetMapping("/medics")
    @PreAuthorize(
        "hasRole(\"" +
        AuthoritiesConstants.ADMIN +
        "\") || hasRole(\"" +
        AuthoritiesConstants.CLIENT +
        "\") || hasRole(\"" +
        AuthoritiesConstants.MEDIC +
        "\")"
    )
    public ResponseEntity<List<Medic>> getAllMedics(Pageable pageable) {
        log.debug("REST request to get a page of Medics");
        Page<Medic> page = medicRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medics/:id} : get the "id" medic.
     *
     * @param id the id of the medic to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the medic, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medics/{id}")
    @PreAuthorize(
        "hasRole(\"" +
        AuthoritiesConstants.ADMIN +
        "\") || hasRole(\"" +
        AuthoritiesConstants.CLIENT +
        "\") || hasRole(\"" +
        AuthoritiesConstants.MEDIC +
        "\")"
    )
    public ResponseEntity<Medic> getMedic(@PathVariable Long id) {
        log.debug("REST request to get Medic : {}", id);
        Optional<Medic> medic = medicRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(medic);
    }

    /**
     * {@code DELETE  /medics/:id} : delete the "id" medic.
     *
     * @param id the id of the medic to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medics/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteMedic(@PathVariable Long id) {
        List<Consultatie> consultatiiCautate = consultatieRepository.findConsultatiiForCurrentMedicList(id);

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

        log.debug("REST request to delete Medic : {}", id);
        medicRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
