package controllers;
//http://stackoverflow.com/questions/20457420/how-do-inner-joins-work-in-ebean

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Applicant;
import models.Rating;
import models.Skill;
import play.*;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.*;
import play.data.*;

import models.Contact;
import views.html.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static play.libs.Json.stringify;
import static play.libs.Json.toJson;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;


public class Application extends Controller {

//    public static Result index() {
//        return ok(index.render("Your new application is ready."));
//    }

//    public static Result list() {
//        /**
//         * Get needed params
//         * Получаем требуемые парметры
//         */
//        Map<String, String[]> params = request().queryString();
//        System.out.println("params (map) = " + params);
//
//        Integer iTotalRecords = Contact.find.findRowCount();
//        System.out.println("iTotalRecords (int) = " + iTotalRecords);
//        String filter = params.get("search[value]")[0];
//        System.out.println("filter (string) = " + filter); //возвращает тот запрос, который введел пользователь
//
//
//        Integer pageSize = Integer.valueOf(params.get("length")[0]);
//        System.out.println("page size (int) = " + pageSize); //сколько записей (строк) должно быть выведено на экнаре на одной page
//        Integer page = Integer.valueOf(params.get("start")[0]) / pageSize; //номер страницы для вывода
//        System.out.println("page (int) = " + page);
//
//        /**
//         * Get sorting order and column
//         */
//        String sortBy = "name";
//        System.out.println("sort by (string) = " + sortBy);
//        String order = params.get("order[0][dir]")[0];
//        System.out.println("order (string) = " + order); //asc - порядок возврастания, desc - в порядке убывания
//
//
//        switch (Integer.valueOf(params.get("order[0][column]")[0])) {
//            case 0:
//                sortBy = "name";
//                break;
//            case 1:
//                sortBy = "title";
//                break;
//            case 2:
//                sortBy = "email";
//                break;
//        }
//        System.out.println("sort by (string) after case = " + sortBy);
//
//        /**
//         * Get page to show from database
//         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
//         * Получаем страницу к показу из базы данных
//         * Важно указать setFetchAhead (набор получать вперед) = false, в противном случае он не будет преимуществом неизменного состояния
//         */
//        List<Contact> resultList = new ArrayList<Contact>();
//        String[] r = divide(filter);
//        for (int i = 0; i < r.length; i++) {
//            System.out.println("в цикле = " + r[i] + '+');
//            filter = r[i];
//
//            //создаем переменную с названием contactsPage ссылочного типа Page (типа Contact) и присваиваем ей значение, которое возвращает метод find вызванный у Contact
//            Page<Contact> contactsPage = Contact.find.where(
//
//                    Expr.or(
//                            Expr.ilike("name", "%" + filter + "%"), //ищем в name
//                            Expr.or( //или ищем в
//                                    Expr.ilike("title", "%" + filter + "%"), //title
//                                    Expr.ilike("email", "%" + filter + "%") //email
////                                    Expr.ilike("contact.typeContact", "%" + filter + "%") //email
//                            )
//                    )
//
//            )
//                    .orderBy(sortBy + " " + order + ", id " + order) //сортируем
//                    .findPagingList(pageSize) //разбиваем результаты поиска по page
//                    .setFetchAhead(false) //тут указываем setFetchAhead = false
//                    .getPage(page); //получаем страницу по номеру page
//
//            //получаем количество значений возвращаемое на странице поиска
////            Integer iTotalDisplayRecords = contactsPage.getTotalRowCount();
////            System.out.println("iTotalDisplayRecords (int) кол-во найденных значений = " + iTotalDisplayRecords);
//
////        List<Contact> resultList = contactsPage.getList();
//            if (resultList.isEmpty()) {
//                resultList = contactsPage.getList();
//            } else {
//                for (Iterator<Contact> it = resultList.iterator(); it.hasNext(); ) {
//                    if (!contactsPage.getList().contains(it.next())) {
//                        it.remove();
//                    }
//                }
//            }
//        }
//        Integer iTotalDisplayRecords = resultList.size();
//        System.out.println("iTotalDisplayRecords (int) кол-во найденных значений = " + iTotalDisplayRecords);
//
//
//        /**
//         * Construct the JSON to return
//         * Строим возвращаемый JSON
//         */
//        // создаем JSON с именем result
//        ObjectNode result = Json.newObject();
//        System.out.println("result (ObjectNode) Json = " + result);
//
//        result.put("draw", Integer.valueOf(params.get("draw")[0])); //ложим в JSON параметр draw = значение, которое пришло (request().queryString()) от клиента, с ключем draw
//        System.out.println("draw (int) request().queryString() = " + Integer.valueOf(params.get("draw")[0]));
//        result.put("recordsTotal", iTotalRecords); //ложим в JSON параметр recordsTotal = всего записей
//        System.out.println("records total (int) = " + iTotalRecords);
//        result.put("recordsFilter", iTotalDisplayRecords); //ложим в JSON параметр recordsFilter = всего записей после поиска
//        System.out.println("records filter (int) = " + iTotalDisplayRecords);
//
//
//        //создали ArrayNode с именем an и положили в JSON массив с именем data
//        ArrayNode an = result.putArray("data");
//
////        for (Contact c : contactsPage.getList()) { //получили лист контактов, которые должны выводится на странице, и перебираем его результаты
//        for (Contact c : resultList) { //получили лист контактов, которые должны выводится на странице, и перебираем его результаты
//            ObjectNode row = Json.newObject(); //создаем новый JSON с именем row и ложим в него результаты нашего поиска
//            row.put("0", c.name);
//            row.put("1", c.title);
//            row.put("2", c.email);
//            row.put("3", "21-02-1982");
//            an.add(row); //добавляем row в ArrayNode с именем an
//        }
//
//        //сервер возвращает код 200 и JSON с результатами поиска
//        return ok(result);
//    }

    public static Result indexApplicants() {
        return ok(indexApp.render("Your new application is ready."));
    }

    public static Result listApplicants() {
        /**
         * Get needed params
         * Получаем требуемые парметры
         */
        Map<String, String[]> params = request().queryString();
//        for (Map.Entry<String, String[]> entry : params.entrySet()) {
//            System.out.println("==============ID =  " + entry.getKey() + " value = " + entry.getValue()[0]);
//        }

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
            case 0: sortBy = "name"; break;
            case 1: sortBy = "dateAddition"; break;
            case 2: sortBy = "dateInterview"; break;
        }
        System.out.println("sort by (string) after case = " + sortBy);

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         * Получаем страницу к показу из базы данных
         * Важно указать setFetchAhead (набор получать вперед) = false, в противном случае он не будет преимуществом неизменного состояния
         */
        List<Applicant> resultList = new ArrayList<Applicant>();
        String[] r = divide(filter);
        for (int i = 0; i < r.length; i++) {
            System.out.println("в цикле = " + r[i] + '+');
            filter = r[i];

            //создаем переменную с названием contactsPage ссылочного типа Page (типа Contact) и присваиваем ей значение, которое возвращает метод find вызванный у Contact
            Page<Applicant> contactsPage = Applicant.find.where(

                    Expr.or(
                            Expr.ilike("name", "%" + filter + "%"), //ищем в name
                            Expr.ilike("ratings.skill.name", "%" + filter + "%")
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
        System.out.println("iTotalDisplayRecords (int) кол-во найденных значений = " + iTotalDisplayRecords);

        /////////////////////////////////////////////////
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
        System.out.println("results: " + sortedRatings);
        for (Applicant c : resultList) { //получили лист контактов, которые должны выводится на странице, и перебираем его результаты
            ObjectNode row = Json.newObject(); //создаем новый JSON с именем row и ложим в него результаты нашего поиска
            row.put("name", c.name);
            row.put("dateInterview", dateFormat.format(c.dateInterview));
            row.put("dateAddition", dateFormat.format(c.dateAddition));
            row.put("id", c.id);

            System.out.println("name = " + c.name + ", id = " + c.id + ", skills = " + c.ratings);

            for (Rating s : c.ratings) ratings.put("" + s.skill, s.value);
            System.out.println("ratings = " + ratings);

            if (!ratings.isEmpty()) {
                sortedRatings.putAll(ratings);
                System.out.println("ratings = " + sortedRatings);

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

    public static Result edit(int id) {
        System.out.println("id in controller = " + id);
        Applicant applicant = Applicant.find.byId(id);
        ObjectNode result = Json.newObject();

        result.put("name", applicant.name);
        ArrayNode contacts = result.putArray("contacts");
        ArrayNode ratings = result.putArray("ratings");

        for (Contact c : applicant.contacts) {
            ObjectNode row = Json.newObject();
            row.put("skill", "" + c.name);
            row.put("rating", "" + c.value);
            contacts.add(row);
        }
        for (Rating r : applicant.ratings) {
            ObjectNode row = Json.newObject();
            row.put("skill", "" + r.skill);
            row.put("rating", "" + r.value);
            ratings.add(row);
        }

        System.out.println("our JSON = " + result.toString());

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

    @Transactional
    public static Result newApplicant() {

        final Map<String, String[]> values = request().body().asFormUrlEncoded();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Applicant applicant = new Applicant();
        applicant.name = values.get("name")[0];
        SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy");
        try {
            applicant.dateInterview = ft.parse(values.get("dateInterview")[0]);
        } catch (ParseException e) {
            System.out.println("Unparseable using " + ft);
        }
        System.out.println("date interview = " + applicant.dateInterview);
        List<Rating> ratings = new ArrayList<Rating>();
        applicant.ratings = ratings;
        applicant.save();
        for (Map.Entry<String, String[]> entry : values.entrySet()) {
            if (entry.getKey().startsWith("skillNameId")){
            int start = entry.getKey().indexOf('-') + 1;
                Rating rating = new Rating(
                        Skill.find.byId(Integer.parseInt(entry.getValue()[0])),
                        Integer.parseInt(values.get("skillValueName-" + entry.getKey().substring(start))[0]),
                        applicant
                );
                ratings.add(rating);
                rating.save();
            }
        }
        applicant.save();
        return indexApplicants();
    }

    public static Result getSkills() {
        DynamicForm dynamicForm = Form.form().bindFromRequest();

        final Set<Map.Entry<String,String[]>> entries = request().queryString().entrySet();
        for (Map.Entry<String,String[]> entry : entries) {
            final String key = entry.getKey();
            final String value = Arrays.toString(entry.getValue());
            Logger.debug(key + " " + value);
        }
        Integer iTotalRecords = Contact.find.findRowCount();
        System.out.println("iTotalRecords (int) = " + iTotalRecords);
        ObjectNode result = Json.newObject();
        List<Skill> skills = Skill.find.all();
        ArrayNode skillArray = result.putArray("skills");

        for (Skill s:skills) {
            ObjectNode row = Json.newObject();
            row.put("id", ""+s.id);
            row.put("name", ""+s.name);
            skillArray.add(row);
        }

        System.out.println("our JSON = " + result.toString());
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