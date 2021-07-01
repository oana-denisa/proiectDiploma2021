package ro.pub.elth.itee.oana.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ro.pub.elth.itee.oana.IntegrationTest;
import ro.pub.elth.itee.oana.domain.ConcluziiConsultatie;
import ro.pub.elth.itee.oana.repository.ConcluziiConsultatieRepository;

/**
 * Integration tests for the {@link ConcluziiConsultatieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConcluziiConsultatieResourceIT {

    private static final String DEFAULT_DIAGNOSTIC = "AAAAAAAAAA";
    private static final String UPDATED_DIAGNOSTIC = "BBBBBBBBBB";

    private static final String DEFAULT_TRATAMENT = "AAAAAAAAAA";
    private static final String UPDATED_TRATAMENT = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATII = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATII = "BBBBBBBBBB";

    private static final String DEFAULT_CONTROL_URMATOR = "AAAAAAAAAA";
    private static final String UPDATED_CONTROL_URMATOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/concluzii-consultaties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConcluziiConsultatieRepository concluziiConsultatieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConcluziiConsultatieMockMvc;

    private ConcluziiConsultatie concluziiConsultatie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConcluziiConsultatie createEntity(EntityManager em) {
        ConcluziiConsultatie concluziiConsultatie = new ConcluziiConsultatie()
            .diagnostic(DEFAULT_DIAGNOSTIC)
            .tratament(DEFAULT_TRATAMENT)
            .observatii(DEFAULT_OBSERVATII)
            .controlUrmator(DEFAULT_CONTROL_URMATOR);
        return concluziiConsultatie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConcluziiConsultatie createUpdatedEntity(EntityManager em) {
        ConcluziiConsultatie concluziiConsultatie = new ConcluziiConsultatie()
            .diagnostic(UPDATED_DIAGNOSTIC)
            .tratament(UPDATED_TRATAMENT)
            .observatii(UPDATED_OBSERVATII)
            .controlUrmator(UPDATED_CONTROL_URMATOR);
        return concluziiConsultatie;
    }

    @BeforeEach
    public void initTest() {
        concluziiConsultatie = createEntity(em);
    }

    @Test
    @Transactional
    void createConcluziiConsultatie() throws Exception {
        int databaseSizeBeforeCreate = concluziiConsultatieRepository.findAll().size();
        // Create the ConcluziiConsultatie
        restConcluziiConsultatieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(concluziiConsultatie))
            )
            .andExpect(status().isCreated());

        // Validate the ConcluziiConsultatie in the database
        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeCreate + 1);
        ConcluziiConsultatie testConcluziiConsultatie = concluziiConsultatieList.get(concluziiConsultatieList.size() - 1);
        assertThat(testConcluziiConsultatie.getDiagnostic()).isEqualTo(DEFAULT_DIAGNOSTIC);
        assertThat(testConcluziiConsultatie.getTratament()).isEqualTo(DEFAULT_TRATAMENT);
        assertThat(testConcluziiConsultatie.getObservatii()).isEqualTo(DEFAULT_OBSERVATII);
        assertThat(testConcluziiConsultatie.getControlUrmator()).isEqualTo(DEFAULT_CONTROL_URMATOR);
    }

    @Test
    @Transactional
    void createConcluziiConsultatieWithExistingId() throws Exception {
        // Create the ConcluziiConsultatie with an existing ID
        concluziiConsultatie.setId(1L);

        int databaseSizeBeforeCreate = concluziiConsultatieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConcluziiConsultatieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(concluziiConsultatie))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConcluziiConsultatie in the database
        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDiagnosticIsRequired() throws Exception {
        int databaseSizeBeforeTest = concluziiConsultatieRepository.findAll().size();
        // set the field null
        concluziiConsultatie.setDiagnostic(null);

        // Create the ConcluziiConsultatie, which fails.

        restConcluziiConsultatieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(concluziiConsultatie))
            )
            .andExpect(status().isBadRequest());

        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTratamentIsRequired() throws Exception {
        int databaseSizeBeforeTest = concluziiConsultatieRepository.findAll().size();
        // set the field null
        concluziiConsultatie.setTratament(null);

        // Create the ConcluziiConsultatie, which fails.

        restConcluziiConsultatieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(concluziiConsultatie))
            )
            .andExpect(status().isBadRequest());

        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkObservatiiIsRequired() throws Exception {
        int databaseSizeBeforeTest = concluziiConsultatieRepository.findAll().size();
        // set the field null
        concluziiConsultatie.setObservatii(null);

        // Create the ConcluziiConsultatie, which fails.

        restConcluziiConsultatieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(concluziiConsultatie))
            )
            .andExpect(status().isBadRequest());

        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkControlUrmatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = concluziiConsultatieRepository.findAll().size();
        // set the field null
        concluziiConsultatie.setControlUrmator(null);

        // Create the ConcluziiConsultatie, which fails.

        restConcluziiConsultatieMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(concluziiConsultatie))
            )
            .andExpect(status().isBadRequest());

        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConcluziiConsultaties() throws Exception {
        // Initialize the database
        concluziiConsultatieRepository.saveAndFlush(concluziiConsultatie);

        // Get all the concluziiConsultatieList
        restConcluziiConsultatieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(concluziiConsultatie.getId().intValue())))
            .andExpect(jsonPath("$.[*].diagnostic").value(hasItem(DEFAULT_DIAGNOSTIC)))
            .andExpect(jsonPath("$.[*].tratament").value(hasItem(DEFAULT_TRATAMENT)))
            .andExpect(jsonPath("$.[*].observatii").value(hasItem(DEFAULT_OBSERVATII)))
            .andExpect(jsonPath("$.[*].controlUrmator").value(hasItem(DEFAULT_CONTROL_URMATOR)));
    }

    @Test
    @Transactional
    void getConcluziiConsultatie() throws Exception {
        // Initialize the database
        concluziiConsultatieRepository.saveAndFlush(concluziiConsultatie);

        // Get the concluziiConsultatie
        restConcluziiConsultatieMockMvc
            .perform(get(ENTITY_API_URL_ID, concluziiConsultatie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(concluziiConsultatie.getId().intValue()))
            .andExpect(jsonPath("$.diagnostic").value(DEFAULT_DIAGNOSTIC))
            .andExpect(jsonPath("$.tratament").value(DEFAULT_TRATAMENT))
            .andExpect(jsonPath("$.observatii").value(DEFAULT_OBSERVATII))
            .andExpect(jsonPath("$.controlUrmator").value(DEFAULT_CONTROL_URMATOR));
    }

    @Test
    @Transactional
    void getNonExistingConcluziiConsultatie() throws Exception {
        // Get the concluziiConsultatie
        restConcluziiConsultatieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConcluziiConsultatie() throws Exception {
        // Initialize the database
        concluziiConsultatieRepository.saveAndFlush(concluziiConsultatie);

        int databaseSizeBeforeUpdate = concluziiConsultatieRepository.findAll().size();

        // Update the concluziiConsultatie
        ConcluziiConsultatie updatedConcluziiConsultatie = concluziiConsultatieRepository.findById(concluziiConsultatie.getId()).get();
        // Disconnect from session so that the updates on updatedConcluziiConsultatie are not directly saved in db
        em.detach(updatedConcluziiConsultatie);
        updatedConcluziiConsultatie
            .diagnostic(UPDATED_DIAGNOSTIC)
            .tratament(UPDATED_TRATAMENT)
            .observatii(UPDATED_OBSERVATII)
            .controlUrmator(UPDATED_CONTROL_URMATOR);

        restConcluziiConsultatieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConcluziiConsultatie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConcluziiConsultatie))
            )
            .andExpect(status().isOk());

        // Validate the ConcluziiConsultatie in the database
        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeUpdate);
        ConcluziiConsultatie testConcluziiConsultatie = concluziiConsultatieList.get(concluziiConsultatieList.size() - 1);
        assertThat(testConcluziiConsultatie.getDiagnostic()).isEqualTo(UPDATED_DIAGNOSTIC);
        assertThat(testConcluziiConsultatie.getTratament()).isEqualTo(UPDATED_TRATAMENT);
        assertThat(testConcluziiConsultatie.getObservatii()).isEqualTo(UPDATED_OBSERVATII);
        assertThat(testConcluziiConsultatie.getControlUrmator()).isEqualTo(UPDATED_CONTROL_URMATOR);
    }

    @Test
    @Transactional
    void putNonExistingConcluziiConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = concluziiConsultatieRepository.findAll().size();
        concluziiConsultatie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConcluziiConsultatieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, concluziiConsultatie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(concluziiConsultatie))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConcluziiConsultatie in the database
        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConcluziiConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = concluziiConsultatieRepository.findAll().size();
        concluziiConsultatie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcluziiConsultatieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(concluziiConsultatie))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConcluziiConsultatie in the database
        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConcluziiConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = concluziiConsultatieRepository.findAll().size();
        concluziiConsultatie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcluziiConsultatieMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(concluziiConsultatie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConcluziiConsultatie in the database
        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConcluziiConsultatieWithPatch() throws Exception {
        // Initialize the database
        concluziiConsultatieRepository.saveAndFlush(concluziiConsultatie);

        int databaseSizeBeforeUpdate = concluziiConsultatieRepository.findAll().size();

        // Update the concluziiConsultatie using partial update
        ConcluziiConsultatie partialUpdatedConcluziiConsultatie = new ConcluziiConsultatie();
        partialUpdatedConcluziiConsultatie.setId(concluziiConsultatie.getId());

        restConcluziiConsultatieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConcluziiConsultatie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConcluziiConsultatie))
            )
            .andExpect(status().isOk());

        // Validate the ConcluziiConsultatie in the database
        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeUpdate);
        ConcluziiConsultatie testConcluziiConsultatie = concluziiConsultatieList.get(concluziiConsultatieList.size() - 1);
        assertThat(testConcluziiConsultatie.getDiagnostic()).isEqualTo(DEFAULT_DIAGNOSTIC);
        assertThat(testConcluziiConsultatie.getTratament()).isEqualTo(DEFAULT_TRATAMENT);
        assertThat(testConcluziiConsultatie.getObservatii()).isEqualTo(DEFAULT_OBSERVATII);
        assertThat(testConcluziiConsultatie.getControlUrmator()).isEqualTo(DEFAULT_CONTROL_URMATOR);
    }

    @Test
    @Transactional
    void fullUpdateConcluziiConsultatieWithPatch() throws Exception {
        // Initialize the database
        concluziiConsultatieRepository.saveAndFlush(concluziiConsultatie);

        int databaseSizeBeforeUpdate = concluziiConsultatieRepository.findAll().size();

        // Update the concluziiConsultatie using partial update
        ConcluziiConsultatie partialUpdatedConcluziiConsultatie = new ConcluziiConsultatie();
        partialUpdatedConcluziiConsultatie.setId(concluziiConsultatie.getId());

        partialUpdatedConcluziiConsultatie
            .diagnostic(UPDATED_DIAGNOSTIC)
            .tratament(UPDATED_TRATAMENT)
            .observatii(UPDATED_OBSERVATII)
            .controlUrmator(UPDATED_CONTROL_URMATOR);

        restConcluziiConsultatieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConcluziiConsultatie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConcluziiConsultatie))
            )
            .andExpect(status().isOk());

        // Validate the ConcluziiConsultatie in the database
        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeUpdate);
        ConcluziiConsultatie testConcluziiConsultatie = concluziiConsultatieList.get(concluziiConsultatieList.size() - 1);
        assertThat(testConcluziiConsultatie.getDiagnostic()).isEqualTo(UPDATED_DIAGNOSTIC);
        assertThat(testConcluziiConsultatie.getTratament()).isEqualTo(UPDATED_TRATAMENT);
        assertThat(testConcluziiConsultatie.getObservatii()).isEqualTo(UPDATED_OBSERVATII);
        assertThat(testConcluziiConsultatie.getControlUrmator()).isEqualTo(UPDATED_CONTROL_URMATOR);
    }

    @Test
    @Transactional
    void patchNonExistingConcluziiConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = concluziiConsultatieRepository.findAll().size();
        concluziiConsultatie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConcluziiConsultatieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, concluziiConsultatie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(concluziiConsultatie))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConcluziiConsultatie in the database
        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConcluziiConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = concluziiConsultatieRepository.findAll().size();
        concluziiConsultatie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcluziiConsultatieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(concluziiConsultatie))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConcluziiConsultatie in the database
        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConcluziiConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = concluziiConsultatieRepository.findAll().size();
        concluziiConsultatie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConcluziiConsultatieMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(concluziiConsultatie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConcluziiConsultatie in the database
        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConcluziiConsultatie() throws Exception {
        // Initialize the database
        concluziiConsultatieRepository.saveAndFlush(concluziiConsultatie);

        int databaseSizeBeforeDelete = concluziiConsultatieRepository.findAll().size();

        // Delete the concluziiConsultatie
        restConcluziiConsultatieMockMvc
            .perform(delete(ENTITY_API_URL_ID, concluziiConsultatie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConcluziiConsultatie> concluziiConsultatieList = concluziiConsultatieRepository.findAll();
        assertThat(concluziiConsultatieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
