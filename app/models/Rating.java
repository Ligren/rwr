package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Rating extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @OneToOne
    public Skill skill;

    @ManyToOne
    public Applicant owner;

    @Column(nullable = false, length = 3)
    public Integer value;

    public Rating(Skill skill, Integer value, Applicant applicant) {
        this.skill = skill;
        this.value = value;
        this.owner = applicant;
    }

    public Rating() { }

    public static Finder<Integer, Applicant> find = new Finder (Integer.class, Applicant.class);

    public static List<Applicant> all() { return find.all(); }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", skill=" + skill +
                ", id owner=" + owner.id +
                ", value=" + value +
                '}';
    }
}
