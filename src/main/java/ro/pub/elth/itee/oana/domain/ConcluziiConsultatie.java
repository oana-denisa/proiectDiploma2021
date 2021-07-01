package ro.pub.elth.itee.oana.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ConcluziiConsultatie.
 */
@Entity
@Table(name = "concluzii_consultatie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConcluziiConsultatie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "diagnostic", length = 255, nullable = false)
    private String diagnostic;

    @NotNull
    @Size(max = 255)
    @Column(name = "tratament", length = 255, nullable = false)
    private String tratament;

    @NotNull
    @Size(max = 255)
    @Column(name = "observatii", length = 255, nullable = false)
    private String observatii;

    @NotNull
    @Size(max = 30)
    @Column(name = "control_urmator", length = 30, nullable = false)
    private String controlUrmator;

    @JsonIgnoreProperties(value = { "medic", "client" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Consultatie consultatie;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConcluziiConsultatie id(Long id) {
        this.id = id;
        return this;
    }

    public String getDiagnostic() {
        return this.diagnostic;
    }

    public ConcluziiConsultatie diagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
        return this;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getTratament() {
        return this.tratament;
    }

    public ConcluziiConsultatie tratament(String tratament) {
        this.tratament = tratament;
        return this;
    }

    public void setTratament(String tratament) {
        this.tratament = tratament;
    }

    public String getObservatii() {
        return this.observatii;
    }

    public ConcluziiConsultatie observatii(String observatii) {
        this.observatii = observatii;
        return this;
    }

    public void setObservatii(String observatii) {
        this.observatii = observatii;
    }

    public String getControlUrmator() {
        return this.controlUrmator;
    }

    public ConcluziiConsultatie controlUrmator(String controlUrmator) {
        this.controlUrmator = controlUrmator;
        return this;
    }

    public void setControlUrmator(String controlUrmator) {
        this.controlUrmator = controlUrmator;
    }

    public Consultatie getConsultatie() {
        return this.consultatie;
    }

    public ConcluziiConsultatie consultatie(Consultatie consultatie) {
        this.setConsultatie(consultatie);
        return this;
    }

    public void setConsultatie(Consultatie consultatie) {
        this.consultatie = consultatie;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConcluziiConsultatie)) {
            return false;
        }
        return id != null && id.equals(((ConcluziiConsultatie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConcluziiConsultatie{" +
            "id=" + getId() +
            ", diagnostic='" + getDiagnostic() + "'" +
            ", tratament='" + getTratament() + "'" +
            ", observatii='" + getObservatii() + "'" +
            ", controlUrmator='" + getControlUrmator() + "'" +
            "}";
    }
}
