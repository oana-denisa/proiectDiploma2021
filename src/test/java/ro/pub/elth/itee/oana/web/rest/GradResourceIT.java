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
import ro.pub.elth.itee.oana.domain.Grad;
import ro.pub.elth.itee.oana.repository.GradRepository;

/**
 * Integration tests for the {@link GradResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GradResourceIT {

    private static final String DEFAULT_DENUMIRE = "AAAAAAAAAA";
    private static final String UPDATED_DENUMIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/grads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GradRepository gradRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGradMockMvc;

    private Grad grad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grad createEntity(EntityManager em) {
        Grad grad = new Grad().denumire(DEFAULT_DENUMIRE);
        return grad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Grad createUpdatedEntity(EntityManager em) {
        Grad grad = new Grad().denumire(UPDATED_DENUMIRE);
        return grad;
    }

    @BeforeEach
    public void initTest() {
        grad = createEntity(em);
    }

    @Test
    @Transactional
    void createGrad() throws Exception {
        int databaseSizeBeforeCreate = gradRepository.findAll().size();
        // Create the Grad
        restGradMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grad)))
            .andExpect(status().isCreated());

        // Validate the Grad in the database
        List<Grad> gradList = gradRepository.findAll();
        assertThat(gradList).hasSize(databaseSizeBeforeCreate + 1);
        Grad testGrad = gradList.get(gradList.size() - 1);
        assertThat(testGrad.getDenumire()).isEqualTo(DEFAULT_DENUMIRE);
    }

    @Test
    @Transactional
    void createGradWithExistingId() throws Exception {
        // Create the Grad with an existing ID
        grad.setId(1L);

        int databaseSizeBeforeCreate = gradRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGradMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grad)))
            .andExpect(status().isBadRequest());

        // Validate the Grad in the database
        List<Grad> gradList = gradRepository.findAll();
        assertThat(gradList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDenumireIsRequired() throws Exception {
        int databaseSizeBeforeTest = gradRepository.findAll().size();
        // set the field null
        grad.setDenumire(null);

        // Create the Grad, which fails.

        restGradMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grad)))
            .andExpect(status().isBadRequest());

        List<Grad> gradList = gradRepository.findAll();
        assertThat(gradList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGrads() throws Exception {
        // Initialize the database
        gradRepository.saveAndFlush(grad);

        // Get all the gradList
        restGradMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(grad.getId().intValue())))
            .andExpect(jsonPath("$.[*].denumire").value(hasItem(DEFAULT_DENUMIRE)));
    }

    @Test
    @Transactional
    void getGrad() throws Exception {
        // Initialize the database
        gradRepository.saveAndFlush(grad);

        // Get the grad
        restGradMockMvc
            .perform(get(ENTITY_API_URL_ID, grad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(grad.getId().intValue()))
            .andExpect(jsonPath("$.denumire").value(DEFAULT_DENUMIRE));
    }

    @Test
    @Transactional
    void getNonExistingGrad() throws Exception {
        // Get the grad
        restGradMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGrad() throws Exception {
        // Initialize the database
        gradRepository.saveAndFlush(grad);

        int databaseSizeBeforeUpdate = gradRepository.findAll().size();

        // Update the grad
        Grad updatedGrad = gradRepository.findById(grad.getId()).get();
        // Disconnect from session so that the updates on updatedGrad are not directly saved in db
        em.detach(updatedGrad);
        updatedGrad.denumire(UPDATED_DENUMIRE);

        restGradMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGrad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGrad))
            )
            .andExpect(status().isOk());

        // Validate the Grad in the database
        List<Grad> gradList = gradRepository.findAll();
        assertThat(gradList).hasSize(databaseSizeBeforeUpdate);
        Grad testGrad = gradList.get(gradList.size() - 1);
        assertThat(testGrad.getDenumire()).isEqualTo(UPDATED_DENUMIRE);
    }

    @Test
    @Transactional
    void putNonExistingGrad() throws Exception {
        int databaseSizeBeforeUpdate = gradRepository.findAll().size();
        grad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGradMockMvc
            .perform(
                put(ENTITY_API_URL_ID, grad.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(grad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grad in the database
        List<Grad> gradList = gradRepository.findAll();
        assertThat(gradList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGrad() throws Exception {
        int databaseSizeBeforeUpdate = gradRepository.findAll().size();
        grad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(grad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grad in the database
        List<Grad> gradList = gradRepository.findAll();
        assertThat(gradList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGrad() throws Exception {
        int databaseSizeBeforeUpdate = gradRepository.findAll().size();
        grad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(grad)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Grad in the database
        List<Grad> gradList = gradRepository.findAll();
        assertThat(gradList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGradWithPatch() throws Exception {
        // Initialize the database
        gradRepository.saveAndFlush(grad);

        int databaseSizeBeforeUpdate = gradRepository.findAll().size();

        // Update the grad using partial update
        Grad partialUpdatedGrad = new Grad();
        partialUpdatedGrad.setId(grad.getId());

        partialUpdatedGrad.denumire(UPDATED_DENUMIRE);

        restGradMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGrad))
            )
            .andExpect(status().isOk());

        // Validate the Grad in the database
        List<Grad> gradList = gradRepository.findAll();
        assertThat(gradList).hasSize(databaseSizeBeforeUpdate);
        Grad testGrad = gradList.get(gradList.size() - 1);
        assertThat(testGrad.getDenumire()).isEqualTo(UPDATED_DENUMIRE);
    }

    @Test
    @Transactional
    void fullUpdateGradWithPatch() throws Exception {
        // Initialize the database
        gradRepository.saveAndFlush(grad);

        int databaseSizeBeforeUpdate = gradRepository.findAll().size();

        // Update the grad using partial update
        Grad partialUpdatedGrad = new Grad();
        partialUpdatedGrad.setId(grad.getId());

        partialUpdatedGrad.denumire(UPDATED_DENUMIRE);

        restGradMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGrad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGrad))
            )
            .andExpect(status().isOk());

        // Validate the Grad in the database
        List<Grad> gradList = gradRepository.findAll();
        assertThat(gradList).hasSize(databaseSizeBeforeUpdate);
        Grad testGrad = gradList.get(gradList.size() - 1);
        assertThat(testGrad.getDenumire()).isEqualTo(UPDATED_DENUMIRE);
    }

    @Test
    @Transactional
    void patchNonExistingGrad() throws Exception {
        int databaseSizeBeforeUpdate = gradRepository.findAll().size();
        grad.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGradMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, grad.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(grad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grad in the database
        List<Grad> gradList = gradRepository.findAll();
        assertThat(gradList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGrad() throws Exception {
        int databaseSizeBeforeUpdate = gradRepository.findAll().size();
        grad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(grad))
            )
            .andExpect(status().isBadRequest());

        // Validate the Grad in the database
        List<Grad> gradList = gradRepository.findAll();
        assertThat(gradList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGrad() throws Exception {
        int databaseSizeBeforeUpdate = gradRepository.findAll().size();
        grad.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGradMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(grad)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Grad in the database
        List<Grad> gradList = gradRepository.findAll();
        assertThat(gradList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGrad() throws Exception {
        // Initialize the database
        gradRepository.saveAndFlush(grad);

        int databaseSizeBeforeDelete = gradRepository.findAll().size();

        // Delete the grad
        restGradMockMvc
            .perform(delete(ENTITY_API_URL_ID, grad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Grad> gradList = gradRepository.findAll();
        assertThat(gradList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
