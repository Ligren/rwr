package controllers;
//http://stackoverflow.com/questions/20457420/how-do-inner-joins-work-in-ebean

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.*;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.*;
import play.data.*;

import views.html.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;


public class Application extends Controller {

    public static Result indexApplicants() {
        return ok(indexApp.render("Your new application is ready."));
    }

    public static Result listApplicants() {
        /**
         * Get needed params
         * Получаем требуемые парметры
         */
        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = Contact.find.findRowCount();
        String filter = params.get("search[value]")[0];
        Integer pageSize = Integer.valueOf(params.get("length")[0]);
        Integer page = Integer.valueOf(params.get("start")[0]) / pageSize; //номер страницы для вывода

        /**
         * Get sorting order and column
         */
        String sortBy = "dateAddition";
        String order = params.get("order[0][dir]")[0];

        switch (Integer.valueOf(params.get("order[0][column]")[0])) {
            case 0:
                sortBy = "name";
                break;
            case 1:
                sortBy = "dateAddition";
                break;
            case 2:
                sortBy = "dateInterview";
                break;
        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         * Получаем страницу к показу из базы данных
         * Важно указать setFetchAhead (набор получать вперед) = false, в противном случае он не будет преимуществом неизменного состояния
         */
        List<Applicant> resultList = new ArrayList<Applicant>();
        String[] r = divide(filter);
        for (int i = 0; i < r.length; i++) {
            filter = r[i];

            //создаем переменную с названием contactsPage ссылочного типа Page (типа Contact) и присваиваем ей значение, которое возвращает метод find вызванный у Contact
            Page<Applicant> contactsPage = Applicant.find.where(

                    Expr.or(
                            Expr.ilike("name", "%" + filter + "%"), //ищем в name
                            Expr.or(
                                    Expr.ilike("ratings.skill.name", "%" + filter + "%"),
                                    Expr.ilike("contacts.value", "%" + filter + "%")
                            )
                    )
            )
                    .orderBy(sortBy + " " + order + ", id " + order) //сортируем
                    .findPagingList(pageSize) //разбиваем результаты поиска по page
                    .setFetchAhead(false) //тут указываем setFetchAhead = false
                    .getPage(page); //получаем страницу по номеру page

            //получаем количество значений возвращаемое на странице поиска
            if (resultList.isEmpty()) {
                resultList = contactsPage.getList();
            } else {
                for (Iterator<Applicant> it = resultList.iterator(); it.hasNext(); ) {
                    if (!contactsPage.getList().contains(it.next())) {
                        it.remove();
                    }
                }
            }
        }
        Integer iTotalDisplayRecords = resultList.size();

        /**
         * Construct the JSON to return
         * Строим возвращаемый JSON
         */
        // создаем JSON с именем result
        ObjectNode result = Json.newObject();

        result.put("draw", Integer.valueOf(params.get("draw")[0])); //ложим в JSON параметр draw = значение, которое пришло (request().queryString()) от клиента, с ключем draw
        result.put("recordsTotal", iTotalRecords); //ложим в JSON параметр recordsTotal = всего записей
        result.put("recordsFilter", iTotalDisplayRecords); //ложим в JSON параметр recordsFilter = всего записей после поиска

        ArrayNode an = result.putArray("data");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        HashMap<String, Integer> ratings = new HashMap<String, Integer>();
        ValueComparator bvc = new ValueComparator(ratings);
        TreeMap<String, Integer> sortedRatings = new TreeMap<String, Integer>(bvc);

        String skillName;
        Integer skillRating;
        for (Applicant c : resultList) { //получили лист контактов, которые должны выводится на странице, и перебираем его результаты
            ObjectNode row = Json.newObject(); //создаем новый JSON с именем row и ложим в него результаты нашего поиска
            row.put("name", c.name);
            row.put("dateInterview", dateFormat.format(c.dateInterview));
            row.put("dateAddition", dateFormat.format(c.dateAddition));
            row.put("id", c.id);


            for (Rating s : c.ratings) ratings.put("" + s.skill, s.value);

            if (!ratings.isEmpty()) {
                sortedRatings.putAll(ratings);

                skillName = (String) sortedRatings.keySet().toArray()[0];
                skillRating = ratings.get(skillName);
                String skillToTable = skillName + " (" + skillRating + "); ";
                if (ratings.size() > 1) {
                    skillName = (String) sortedRatings.keySet().toArray()[1];
                    skillRating = ratings.get(skillName);
                    skillToTable = skillToTable + skillName + " (" + skillRating + "); ";
                    if (ratings.size() > 2) {
                        skillName = (String) sortedRatings.keySet().toArray()[2];
                        skillRating = ratings.get(skillName);
                        skillToTable = skillToTable + skillName + " (" + skillRating + ");";
                    }
                }
                row.put("skill", skillToTable);

            } else {
                row.put("skill", "Неудачник, ничегоне умеет!");
            }
            ratings.clear();
            sortedRatings.clear();
            an.add(row);
        }
        return ok(result);
    }

    public static Result editApplicant(int id) {
        Applicant applicant = Applicant.find.byId(id);
        ObjectNode result = Json.newObject();

        result.put("name", applicant.name);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        result.put("dateInterview", dateFormat.format(applicant.dateInterview));
        ArrayNode contacts = result.putArray("contacts");
        ArrayNode ratings = result.putArray("ratings");

        for (Contact c : applicant.contacts) {
            ObjectNode row = Json.newObject();
            row.put("value", "" + c.value);
            row.put("id", "" + c.typeContact.id);
            contacts.add(row);
        }
        for (Rating r : applicant.ratings) {
            ObjectNode row = Json.newObject();
            row.put("rating", "" + r.value);
            row.put("id", "" + r.skill.id);
            ratings.add(row);
        }


        return ok(edit.render(result.toString()));
    }

    public static Result deleteApplicant(int id) {
            Applicant applicant = Applicant.find.byId(id);
            List<Rating> ratingsList = Ebean.find(Rating.class)
                    .where()
                    .eq("owner", applicant)
                    .findList();
            for (Rating r : ratingsList) r.delete();
            applicant.delete();
        return ok();
    }

    public static Result newApplicant() {
        ObjectNode result = Json.newObject();
        return ok(edit.render(result.toString()));
    }

    public static Result addSkill() {
        String newSkillName = Form.form().bindFromRequest().get("newSkill");
        List<Skill> skills = Skill.find.where().ilike("name", newSkillName).findList();
        ObjectNode result = Json.newObject();
        if (!skills.isEmpty()) {
            result.put("createNewSkill", "false");
        } else {
            Skill skill = new Skill(newSkillName);
            skill.save();
            result.put("createNewSkill", "true");
        }
        return ok(result);
    }

    public static Result addTypeContact() {
        String newTypeContactName = Form.form().bindFromRequest().get("newTypeContact");
        List<TypeContact> typeContacts = TypeContact.find.where().ilike("name", newTypeContactName).findList();
        ObjectNode result = Json.newObject();
        if (!typeContacts.isEmpty()) {
            result.put("createNewTypeContact", "false");
        } else {
            TypeContact typeContact = new TypeContact(newTypeContactName);
            typeContact.save();
            result.put("createNewTypeContact", "true");
        }
        return ok(result);
    }

    @Transactional
    public static Result createApplicant() {

        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        List<Applicant> applicantsList = Applicant.find.where().ilike("name", values.get("name")[0]).findList();
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        Applicant applicant;

        if (applicantsList.isEmpty()) {
            applicant = new Applicant();
            applicant.name = values.get("name")[0];
        } else {
            applicant = applicantsList.get(0);
            List<Rating> ratingsList = Ebean.find(Rating.class)
                    .where()
                    .eq("owner", applicant)
                    .findList();
            for (Rating r : ratingsList) r.delete();
            List<Contact> contactsList = Ebean.find(Contact.class)
                    .where()
                    .eq("owner", applicant)
                    .findList();
            for (Contact c : contactsList) c.delete();
        }

        try {
            applicant.dateInterview = ft.parse(values.get("dateInterview")[0]);
        } catch (ParseException e) {
        }
        applicant.save();
        for (Map.Entry<String, String[]> entry : values.entrySet()) {
            if (entry.getKey().startsWith("contactNameId")) {
                int start = entry.getKey().indexOf('-') + 1;
                Contact contact = new Contact(
                        TypeContact.find.byId(Integer.parseInt(entry.getValue()[0])),
                        values.get("contactValueName-" + entry.getKey().substring(start))[0],
                        applicant
                );
                contact.save();
            }
            if (entry.getKey().startsWith("skillNameId")) {
                int start = entry.getKey().indexOf('-') + 1;
                Rating rating = new Rating(
                        Skill.find.byId(Integer.parseInt(entry.getValue()[0])),
                        Integer.parseInt(values.get("skillValueName-" + entry.getKey().substring(start))[0]),
                        applicant
                );
                rating.save();
            }
        }
        return indexApplicants();
    }

    public static Result getSkills() {
        ObjectNode result = Json.newObject();
        ArrayNode skillArray = result.putArray("skills");

        List<Skill> skills = Skill.find.all();

        for (Skill s : skills) {
            ObjectNode row = Json.newObject();
            row.put("id", "" + s.id);
            row.put("name", "" + s.name);
            skillArray.add(row);
        }
        return ok(result);
    }

    public static Result getTypeContacts() {
        ObjectNode result = Json.newObject();
        ArrayNode typeContactsArray = result.putArray("typeContacts");

        List<TypeContact> typeContacts = TypeContact.find.all();

        for (TypeContact s : typeContacts) {
            ObjectNode row = Json.newObject();
            row.put("id", "" + s.id);
            row.put("name", "" + s.name);
            typeContactsArray.add(row);
        }
        return ok(result);
    }

    public static String[] divide(String s) {
        ArrayList<String> tmp = new ArrayList<String>();
        int i = 0;
        boolean f = false;

        for (int j = 0; j < s.length(); j++) {
            if (s.charAt(j) == ' ') {
                if (j > i) {
                    tmp.add(s.substring(i, j));
                }
                i = j + 1;
            }
        }
        if (i < s.length()) {
            tmp.add(s.substring(i));
        }
        if (tmp.size() == 0) tmp.add("");
        return tmp.toArray(new String[tmp.size()]);
    }
}

class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;

    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}