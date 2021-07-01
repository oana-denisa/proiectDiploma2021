package ro.pub.elth.itee.oana.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Consultatie.
 */
@Entity
@Table(name = "consultatie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Consultatie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "data_ora", nullable = false)
    private Instant dataOra;

    @NotNull
    @Size(max = 30)
    @Column(name = "descriere", length = 30, nullable = false)
    private String descriere;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "grad", "specializare" }, allowSetters = true)
    private Medic medic;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consultatie id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getDataOra() {
        return this.dataOra;
    }

    public Consultatie dataOra(Instant dataOra) {
        this.dataOra = dataOra;
        return this;
    }

    public void setDataOra(Instant dataOra) {
        this.dataOra = dataOra;
    }

    public String getDescriere() {
        return this.descriere;
    }

    public Consultatie descriere(String descriere) {
        this.descriere = descriere;
        return this;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Medic getMedic() {
        return this.medic;
    }

    public Consultatie medic(Medic medic) {
        this.setMedic(medic);
        return this;
    }

    public void setMedic(Medic medic) {
        this.medic = medic;
    }

    public Client getClient() {
        return this.client;
    }

    public Consultatie client(Client client) {
        this.setClient(client);
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consultatie)) {
            return false;
        }
        return id != null && id.equals(((Consultatie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Consultatie{" +
            "id=" + getId() +
            ", dataOra='" + getDataOra() + "'" +
            ", descriere='" + getDescriere() + "'" +
            "}";
    }
}
