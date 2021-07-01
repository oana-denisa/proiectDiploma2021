package ro.pub.elth.itee.oana.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ro.pub.elth.itee.oana.web.rest.TestUtil;

class GradTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Grad.class);
        Grad grad1 = new Grad();
        grad1.setId(1L);
        Grad grad2 = new Grad();
        grad2.setId(grad1.getId());
        assertThat(grad1).isEqualTo(grad2);
        grad2.setId(2L);
        assertThat(grad1).isNotEqualTo(grad2);
        grad1.setId(null);
        assertThat(grad1).isNotEqualTo(grad2);
    }
}
