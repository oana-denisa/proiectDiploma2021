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
import ro.pub.elth.itee.oana.domain.Specializare;
import ro.pub.elth.itee.oana.repository.SpecializareRepository;

/**
 * Integration tests for the {@link SpecializareResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecializareResourceIT {

    private static final String DEFAULT_DENUMIRE = "AAAAAAAAAA";
    private static final String UPDATED_DENUMIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/specializares";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpecializareRepository specializareRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecializareMockMvc;

    private Specializare specializare;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specializare createEntity(EntityManager em) {
        Specializare specializare = new Specializare().denumire(DEFAULT_DENUMIRE);
        return specializare;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specializare createUpdatedEntity(EntityManager em) {
        Specializare specializare = new Specializare().denumire(UPDATED_DENUMIRE);
        return specializare;
    }

    @BeforeEach
    public void initTest() {
        specializare = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecializare() throws Exception {
        int databaseSizeBeforeCreate = specializareRepository.findAll().size();
        // Create the Specializare
        restSpecializareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specializare)))
            .andExpect(status().isCreated());

        // Validate the Specializare in the database
        List<Specializare> specializareList = specializareRepository.findAll();
        assertThat(specializareList).hasSize(databaseSizeBeforeCreate + 1);
        Specializare testSpecializare = specializareList.get(specializareList.size() - 1);
        assertThat(testSpecializare.getDenumire()).isEqualTo(DEFAULT_DENUMIRE);
    }

    @Test
    @Transactional
    void createSpecializareWithExistingId() throws Exception {
        // Create the Specializare with an existing ID
        specializare.setId(1L);

        int databaseSizeBeforeCreate = specializareRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecializareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specializare)))
            .andExpect(status().isBadRequest());

        // Validate the Specializare in the database
        List<Specializare> specializareList = specializareRepository.findAll();
        assertThat(specializareList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDenumireIsRequired() throws Exception {
        int databaseSizeBeforeTest = specializareRepository.findAll().size();
        // set the field null
        specializare.setDenumire(null);

        // Create the Specializare, which fails.

        restSpecializareMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specializare)))
            .andExpect(status().isBadRequest());

        List<Specializare> specializareList = specializareRepository.findAll();
        assertThat(specializareList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecializares() throws Exception {
        // Initialize the database
        specializareRepository.saveAndFlush(specializare);

        // Get all the specializareList
        restSpecializareMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specializare.getId().intValue())))
            .andExpect(jsonPath("$.[*].denumire").value(hasItem(DEFAULT_DENUMIRE)));
    }

    @Test
    @Transactional
    void getSpecializare() throws Exception {
        // Initialize the database
        specializareRepository.saveAndFlush(specializare);

        // Get the specializare
        restSpecializareMockMvc
            .perform(get(ENTITY_API_URL_ID, specializare.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specializare.getId().intValue()))
            .andExpect(jsonPath("$.denumire").value(DEFAULT_DENUMIRE));
    }

    @Test
    @Transactional
    void getNonExistingSpecializare() throws Exception {
        // Get the specializare
        restSpecializareMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSpecializare() throws Exception {
        // Initialize the database
        specializareRepository.saveAndFlush(specializare);

        int databaseSizeBeforeUpdate = specializareRepository.findAll().size();

        // Update the specializare
        Specializare updatedSpecializare = specializareRepository.findById(specializare.getId()).get();
        // Disconnect from session so that the updates on updatedSpecializare are not directly saved in db
        em.detach(updatedSpecializare);
        updatedSpecializare.denumire(UPDATED_DENUMIRE);

        restSpecializareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpecializare.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSpecializare))
            )
            .andExpect(status().isOk());

        // Validate the Specializare in the database
        List<Specializare> specializareList = specializareRepository.findAll();
        assertThat(specializareList).hasSize(databaseSizeBeforeUpdate);
        Specializare testSpecializare = specializareList.get(specializareList.size() - 1);
        assertThat(testSpecializare.getDenumire()).isEqualTo(UPDATED_DENUMIRE);
    }

    @Test
    @Transactional
    void putNonExistingSpecializare() throws Exception {
        int databaseSizeBeforeUpdate = specializareRepository.findAll().size();
        specializare.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecializareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specializare.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specializare))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specializare in the database
        List<Specializare> specializareList = specializareRepository.findAll();
        assertThat(specializareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecializare() throws Exception {
        int databaseSizeBeforeUpdate = specializareRepository.findAll().size();
        specializare.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecializareMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(specializare))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specializare in the database
        List<Specializare> specializareList = specializareRepository.findAll();
        assertThat(specializareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecializare() throws Exception {
        int databaseSizeBeforeUpdate = specializareRepository.findAll().size();
        specializare.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecializareMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(specializare)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specializare in the database
        List<Specializare> specializareList = specializareRepository.findAll();
        assertThat(specializareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecializareWithPatch() throws Exception {
        // Initialize the database
        specializareRepository.saveAndFlush(specializare);

        int databaseSizeBeforeUpdate = specializareRepository.findAll().size();

        // Update the specializare using partial update
        Specializare partialUpdatedSpecializare = new Specializare();
        partialUpdatedSpecializare.setId(specializare.getId());

        partialUpdatedSpecializare.denumire(UPDATED_DENUMIRE);

        restSpecializareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecializare.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecializare))
            )
            .andExpect(status().isOk());

        // Validate the Specializare in the database
        List<Specializare> specializareList = specializareRepository.findAll();
        assertThat(specializareList).hasSize(databaseSizeBeforeUpdate);
        Specializare testSpecializare = specializareList.get(specializareList.size() - 1);
        assertThat(testSpecializare.getDenumire()).isEqualTo(UPDATED_DENUMIRE);
    }

    @Test
    @Transactional
    void fullUpdateSpecializareWithPatch() throws Exception {
        // Initialize the database
        specializareRepository.saveAndFlush(specializare);

        int databaseSizeBeforeUpdate = specializareRepository.findAll().size();

        // Update the specializare using partial update
        Specializare partialUpdatedSpecializare = new Specializare();
        partialUpdatedSpecializare.setId(specializare.getId());

        partialUpdatedSpecializare.denumire(UPDATED_DENUMIRE);

        restSpecializareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecializare.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecializare))
            )
            .andExpect(status().isOk());

        // Validate the Specializare in the database
        List<Specializare> specializareList = specializareRepository.findAll();
        assertThat(specializareList).hasSize(databaseSizeBeforeUpdate);
        Specializare testSpecializare = specializareList.get(specializareList.size() - 1);
        assertThat(testSpecializare.getDenumire()).isEqualTo(UPDATED_DENUMIRE);
    }

    @Test
    @Transactional
    void patchNonExistingSpecializare() throws Exception {
        int databaseSizeBeforeUpdate = specializareRepository.findAll().size();
        specializare.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecializareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specializare.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specializare))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specializare in the database
        List<Specializare> specializareList = specializareRepository.findAll();
        assertThat(specializareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecializare() throws Exception {
        int databaseSizeBeforeUpdate = specializareRepository.findAll().size();
        specializare.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecializareMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(specializare))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specializare in the database
        List<Specializare> specializareList = specializareRepository.findAll();
        assertThat(specializareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecializare() throws Exception {
        int databaseSizeBeforeUpdate = specializareRepository.findAll().size();
        specializare.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecializareMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(specializare))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specializare in the database
        List<Specializare> specializareList = specializareRepository.findAll();
        assertThat(specializareList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecializare() throws Exception {
        // Initialize the database
        specializareRepository.saveAndFlush(specializare);

        int databaseSizeBeforeDelete = specializareRepository.findAll().size();

        // Delete the specializare
        restSpecializareMockMvc
            .perform(delete(ENTITY_API_URL_ID, specializare.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Specializare> specializareList = specializareRepository.findAll();
        assertThat(specializareList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
