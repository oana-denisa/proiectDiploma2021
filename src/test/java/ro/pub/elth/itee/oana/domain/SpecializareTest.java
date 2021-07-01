package ro.pub.elth.itee.oana.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ro.pub.elth.itee.oana.web.rest.TestUtil;

class SpecializareTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Specializare.class);
        Specializare specializare1 = new Specializare();
        specializare1.setId(1L);
        Specializare specializare2 = new Specializare();
        specializare2.setId(specializare1.getId());
        assertThat(specializare1).isEqualTo(specializare2);
        specializare2.setId(2L);
        assertThat(specializare1).isNotEqualTo(specializare2);
        specializare1.setId(null);
        assertThat(specializare1).isNotEqualTo(specializare2);
    }
}
