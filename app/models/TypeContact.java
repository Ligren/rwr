package models;

import java.util.List;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

@Entity
public class TypeContact extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Constraints.Required
    public String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="typeContact")
    public List<Contact> contacts;

    public TypeContact(String name) {
        this.name = name;
    }

    public TypeContact() { }

    public static Finder<Integer, TypeContact> find = new Finder (Integer.class, TypeContact.class);

    public static List<TypeContact> all() { return find.all(); }

}
