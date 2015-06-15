package models;

import play.db.ebean.Model;

import javax.persistence.*;

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
