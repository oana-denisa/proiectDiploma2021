package ro.pub.elth.itee.oana.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ro.pub.elth.itee.oana.web.rest.TestUtil;

class ConcluziiConsultatieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConcluziiConsultatie.class);
        ConcluziiConsultatie concluziiConsultatie1 = new ConcluziiConsultatie();
        concluziiConsultatie1.setId(1L);
        ConcluziiConsultatie concluziiConsultatie2 = new ConcluziiConsultatie();
        concluziiConsultatie2.setId(concluziiConsultatie1.getId());
        assertThat(concluziiConsultatie1).isEqualTo(concluziiConsultatie2);
        concluziiConsultatie2.setId(2L);
        assertThat(concluziiConsultatie1).isNotEqualTo(concluziiConsultatie2);
        concluziiConsultatie1.setId(null);
        assertThat(concluziiConsultatie1).isNotEqualTo(concluziiConsultatie2);
    }
}
