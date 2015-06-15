package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

import play.data.format.*;
import play.data.validation.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * http://stackoverflow.com/questions/5127129/mapping-many-to-many-association-table-with-extra-columns
 http://en.wikibooks.org/wiki/Java_Persistence/ManyToMany
 https://giannigar.wordpress.com/2009/09/04/mapping-a-many-to-many-join-table-with-extra-column-using-jpa/
 */
@Entity
@Table(name = "APPLICANTS")
public class Applicant extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Constraints.Required
    public String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Formats.DateTime(pattern="dd/MM/yyyy")
    @CreatedTimestamp
    @Column(updatable=false)
    public Date dateAddition;

    @Constraints.Required
    @Formats.DateTime(pattern="dd/MM/yyyy")
    public Date dateInterview;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="owner")
    public List<Contact> contacts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="owner")
    public List<Rating> ratings;

    public static Finder<Integer, Applicant> find = new Finder (Integer.class, Applicant.class);

    public static List<Applicant> all() { return find.all(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Applicant applicant = (Applicant) o;

        return !(id != null ? !id.equals(applicant.id) : applicant.id != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateAddition=" + dateAddition +
                ", dateInterview=" + dateInterview +
                ", contacts=" + contacts +
                ", ratings=" + ratings +
                '}';
    }
}