package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Skill extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy="skill")
    @Column(nullable = false, length = 50)
    @Constraints.Required
    public String name;

    public Skill() {}

    public Skill(String name) {
        this.name = name;
    }

    public static Finder<Integer, Skill> find = new Finder (Integer.class, Skill.class);

    public static List<Skill> all() { return find.all(); }

    @Override
    public String toString() {
        return "" + name;
    }
}
