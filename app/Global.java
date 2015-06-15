import com.avaje.ebean.Ebean;
import models.*;
import play.Application;
import play.GlobalSettings;
import play.libs.Yaml;

import java.util.List;
import java.util.Map;

public class Global extends GlobalSettings {

  @Override
  public void onStart(Application app) {

    /**
     * Here we load the initial data into the database
     */

    if(Ebean.find(Applicant.class).findRowCount() == 0) {
      Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data-applicants.yml");
      Ebean.save(all.get("applicants"));
    }

    if(Ebean.find(Skill.class).findRowCount() == 0) {
      Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data-applicants.yml");
      Ebean.save(all.get("skills"));
    }

    if(Ebean.find(Rating.class).findRowCount() == 0) {
      Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data-applicants.yml");
      Ebean.save(all.get("ratings"));
    }

    if(Ebean.find(TypeContact.class).findRowCount() == 0) {
      Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data-applicants.yml");
      Ebean.save(all.get("typeContacts"));
    }
/*
    if(Ebean.find(Contact.class).findRowCount() == 0) {
      Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data-applicants.yml");
      Ebean.save(all.get("contacts"));
    }
*/
  }

}

