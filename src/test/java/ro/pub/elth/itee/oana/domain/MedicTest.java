package ro.pub.elth.itee.oana.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ro.pub.elth.itee.oana.web.rest.TestUtil;

class MedicTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medic.class);
        Medic medic1 = new Medic();
        medic1.setId(1L);
        Medic medic2 = new Medic();
        medic2.setId(medic1.getId());
        assertThat(medic1).isEqualTo(medic2);
        medic2.setId(2L);
        assertThat(medic1).isNotEqualTo(medic2);
        medic1.setId(null);
        assertThat(medic1).isNotEqualTo(medic2);
    }
}
