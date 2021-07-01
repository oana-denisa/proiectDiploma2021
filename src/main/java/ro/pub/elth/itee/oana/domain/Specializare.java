package ro.pub.elth.itee.oana.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Specializare.
 */
@Entity
@Table(name = "specializare")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Specializare implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "denumire", length = 80, nullable = false)
    private String denumire;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Specializare id(Long id) {
        this.id = id;
        return this;
    }

    public String getDenumire() {
        return this.denumire;
    }

    public Specializare denumire(String denumire) {
        this.denumire = denumire;
        return this;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Specializare)) {
            return false;
        }
        return id != null && id.equals(((Specializare) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Specializare{" +
            "id=" + getId() +
            ", denumire='" + getDenumire() + "'" +
            "}";
    }
}
