package ro.pub.elth.itee.oana.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Medic.
 */
@Entity
@Table(name = "medic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Medic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nume_si_prenume", length = 100, nullable = false)
    private String numeSiPrenume;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToOne
    private Grad grad;

    @ManyToOne
    private Specializare specializare;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medic id(Long id) {
        this.id = id;
        return this;
    }

    public String getNumeSiPrenume() {
        return this.numeSiPrenume;
    }

    public Medic numeSiPrenume(String numeSiPrenume) {
        this.numeSiPrenume = numeSiPrenume;
        return this;
    }

    public void setNumeSiPrenume(String numeSiPrenume) {
        this.numeSiPrenume = numeSiPrenume;
    }

    public User getUser() {
        return this.user;
    }

    public Medic user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Grad getGrad() {
        return this.grad;
    }

    public Medic grad(Grad grad) {
        this.setGrad(grad);
        return this;
    }

    public void setGrad(Grad grad) {
        this.grad = grad;
    }

    public Specializare getSpecializare() {
        return this.specializare;
    }

    public Medic specializare(Specializare specializare) {
        this.setSpecializare(specializare);
        return this;
    }

    public void setSpecializare(Specializare specializare) {
        this.specializare = specializare;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medic)) {
            return false;
        }
        return id != null && id.equals(((Medic) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medic{" +
            "id=" + getId() +
            ", numeSiPrenume='" + getNumeSiPrenume() + "'" +
            "}";
    }
}
