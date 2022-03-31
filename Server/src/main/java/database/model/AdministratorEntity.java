package database.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "administrator", schema = "public", catalog = "competition")
public class AdministratorEntity {
    private long id;
    private long personId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "person_id", nullable = false)
    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdministratorEntity that = (AdministratorEntity) o;
        return id == that.id && personId == that.personId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personId);
    }
}
