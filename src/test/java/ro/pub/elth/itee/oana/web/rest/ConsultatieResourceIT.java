package ro.pub.elth.itee.oana.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import ro.pub.elth.itee.oana.domain.Consultatie;
import ro.pub.elth.itee.oana.repository.ConsultatieRepository;

/**
 * Integration tests for the {@link ConsultatieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsultatieResourceIT {

    private static final Instant DEFAULT_DATA_ORA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_ORA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIERE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIERE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/consultaties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsultatieRepository consultatieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsultatieMockMvc;

    private Consultatie consultatie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultatie createEntity(EntityManager em) {
        Consultatie consultatie = new Consultatie().dataOra(DEFAULT_DATA_ORA).descriere(DEFAULT_DESCRIERE);
        return consultatie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consultatie createUpdatedEntity(EntityManager em) {
        Consultatie consultatie = new Consultatie().dataOra(UPDATED_DATA_ORA).descriere(UPDATED_DESCRIERE);
        return consultatie;
    }

    @BeforeEach
    public void initTest() {
        consultatie = createEntity(em);
    }

    @Test
    @Transactional
    void createConsultatie() throws Exception {
        int databaseSizeBeforeCreate = consultatieRepository.findAll().size();
        // Create the Consultatie
        restConsultatieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultatie)))
            .andExpect(status().isCreated());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeCreate + 1);
        Consultatie testConsultatie = consultatieList.get(consultatieList.size() - 1);
        assertThat(testConsultatie.getDataOra()).isEqualTo(DEFAULT_DATA_ORA);
        assertThat(testConsultatie.getDescriere()).isEqualTo(DEFAULT_DESCRIERE);
    }

    @Test
    @Transactional
    void createConsultatieWithExistingId() throws Exception {
        // Create the Consultatie with an existing ID
        consultatie.setId(1L);

        int databaseSizeBeforeCreate = consultatieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsultatieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultatie)))
            .andExpect(status().isBadRequest());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataOraIsRequired() throws Exception {
        int databaseSizeBeforeTest = consultatieRepository.findAll().size();
        // set the field null
        consultatie.setDataOra(null);

        // Create the Consultatie, which fails.

        restConsultatieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultatie)))
            .andExpect(status().isBadRequest());

        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriereIsRequired() throws Exception {
        int databaseSizeBeforeTest = consultatieRepository.findAll().size();
        // set the field null
        consultatie.setDescriere(null);

        // Create the Consultatie, which fails.

        restConsultatieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultatie)))
            .andExpect(status().isBadRequest());

        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConsultaties() throws Exception {
        // Initialize the database
        consultatieRepository.saveAndFlush(consultatie);

        // Get all the consultatieList
        restConsultatieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultatie.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataOra").value(hasItem(DEFAULT_DATA_ORA.toString())))
            .andExpect(jsonPath("$.[*].descriere").value(hasItem(DEFAULT_DESCRIERE)));
    }

    @Test
    @Transactional
    void getConsultatie() throws Exception {
        // Initialize the database
        consultatieRepository.saveAndFlush(consultatie);

        // Get the consultatie
        restConsultatieMockMvc
            .perform(get(ENTITY_API_URL_ID, consultatie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consultatie.getId().intValue()))
            .andExpect(jsonPath("$.dataOra").value(DEFAULT_DATA_ORA.toString()))
            .andExpect(jsonPath("$.descriere").value(DEFAULT_DESCRIERE));
    }

    @Test
    @Transactional
    void getNonExistingConsultatie() throws Exception {
        // Get the consultatie
        restConsultatieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConsultatie() throws Exception {
        // Initialize the database
        consultatieRepository.saveAndFlush(consultatie);

        int databaseSizeBeforeUpdate = consultatieRepository.findAll().size();

        // Update the consultatie
        Consultatie updatedConsultatie = consultatieRepository.findById(consultatie.getId()).get();
        // Disconnect from session so that the updates on updatedConsultatie are not directly saved in db
        em.detach(updatedConsultatie);
        updatedConsultatie.dataOra(UPDATED_DATA_ORA).descriere(UPDATED_DESCRIERE);

        restConsultatieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConsultatie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConsultatie))
            )
            .andExpect(status().isOk());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeUpdate);
        Consultatie testConsultatie = consultatieList.get(consultatieList.size() - 1);
        assertThat(testConsultatie.getDataOra()).isEqualTo(UPDATED_DATA_ORA);
        assertThat(testConsultatie.getDescriere()).isEqualTo(UPDATED_DESCRIERE);
    }

    @Test
    @Transactional
    void putNonExistingConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = consultatieRepository.findAll().size();
        consultatie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultatieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultatie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultatie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = consultatieRepository.findAll().size();
        consultatie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultatieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consultatie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = consultatieRepository.findAll().size();
        consultatie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultatieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consultatie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsultatieWithPatch() throws Exception {
        // Initialize the database
        consultatieRepository.saveAndFlush(consultatie);

        int databaseSizeBeforeUpdate = consultatieRepository.findAll().size();

        // Update the consultatie using partial update
        Consultatie partialUpdatedConsultatie = new Consultatie();
        partialUpdatedConsultatie.setId(consultatie.getId());

        partialUpdatedConsultatie.descriere(UPDATED_DESCRIERE);

        restConsultatieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultatie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultatie))
            )
            .andExpect(status().isOk());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeUpdate);
        Consultatie testConsultatie = consultatieList.get(consultatieList.size() - 1);
        assertThat(testConsultatie.getDataOra()).isEqualTo(DEFAULT_DATA_ORA);
        assertThat(testConsultatie.getDescriere()).isEqualTo(UPDATED_DESCRIERE);
    }

    @Test
    @Transactional
    void fullUpdateConsultatieWithPatch() throws Exception {
        // Initialize the database
        consultatieRepository.saveAndFlush(consultatie);

        int databaseSizeBeforeUpdate = consultatieRepository.findAll().size();

        // Update the consultatie using partial update
        Consultatie partialUpdatedConsultatie = new Consultatie();
        partialUpdatedConsultatie.setId(consultatie.getId());

        partialUpdatedConsultatie.dataOra(UPDATED_DATA_ORA).descriere(UPDATED_DESCRIERE);

        restConsultatieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultatie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsultatie))
            )
            .andExpect(status().isOk());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeUpdate);
        Consultatie testConsultatie = consultatieList.get(consultatieList.size() - 1);
        assertThat(testConsultatie.getDataOra()).isEqualTo(UPDATED_DATA_ORA);
        assertThat(testConsultatie.getDescriere()).isEqualTo(UPDATED_DESCRIERE);
    }

    @Test
    @Transactional
    void patchNonExistingConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = consultatieRepository.findAll().size();
        consultatie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultatieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consultatie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultatie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = consultatieRepository.findAll().size();
        consultatie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultatieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consultatie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsultatie() throws Exception {
        int databaseSizeBeforeUpdate = consultatieRepository.findAll().size();
        consultatie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultatieMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(consultatie))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consultatie in the database
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsultatie() throws Exception {
        // Initialize the database
        consultatieRepository.saveAndFlush(consultatie);

        int databaseSizeBeforeDelete = consultatieRepository.findAll().size();

        // Delete the consultatie
        restConsultatieMockMvc
            .perform(delete(ENTITY_API_URL_ID, consultatie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Consultatie> consultatieList = consultatieRepository.findAll();
        assertThat(consultatieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
