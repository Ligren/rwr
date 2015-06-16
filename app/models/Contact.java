package models;

import java.util.*;
import javax.persistence.*;

import play.api.libs.Crypto;
import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import play.Logger;

import com.avaje.ebean.*;

@Entity
public class Contact extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@Constraints.Required
	public String value;

	@ManyToOne
	public TypeContact typeContact;

	@ManyToOne
	public Applicant owner;

	public Contact() { }

	public Contact(TypeContact typeContact, String value, Applicant owner) {
		this.typeContact = typeContact;
		this.value = value;
		this.owner = owner;
	}

	public static Model.Finder<Long,Contact> find = new Model.Finder(Long.class, Contact.class);

	public static List<Contact> findAll() {
		return find.all();
	}

}