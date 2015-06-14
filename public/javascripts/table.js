//http://shpargalkablog.ru/2013/08/appendchild-removechild-javascript.html
//http://javascript.ru/tutorial/dom/attributes
//http://www.wisdomweb.ru/AJAX/object.php
var skillNumber = 0;

/*
 $(document).ready(function() {
 $('#contsacts').dataTable( {
 processing: true,
 serverSide: true,
 ajax: "/searchApplicants"
 } );
 } );
 */

$(function() {
    $( ".datepicker" ).datepicker({ dateFormat: "dd-mm-yy" });
});

$(document).ready(function () {
    /*
     n — переменная представляет собой узел.
     o — переменная представляет объект.
     a — переменная представляет собой массив.
     s — переменная строка.
     b — логический тип.
     f — расположение.
     i — переменная содержит целое значение.
     fn — переменная представляет собой функцию.
     */

    $('#contacts').dataTable({
        processing: true,
        serverSide: true,
        //bProcessing: true,
        aLengthMenu: [[10, 20, 50, 75, -1], [10, 20, 50, 75, "All"]],
        iDisplayLength: 20,
        ajax: "/searchApplicants",
        order: [[2, "desc"]],
        aoColumns: [
            {
                "mData": "id",
                "visible": false
            },
            {
                "mData": "name",
                "sClass": "right"
            },
            {
                "mData": "dateInterview",
                "sClass": "center"
            },
            {
                "mData": "dateAddition",
                "sClass": "center"
            },
            {
                "sTitle": "Skill",
                "mData": "skill",
                "sClass": "right",
                "bSortable": false
            },
            {                 //Edit link
                "mData": "id",
                "sTitle": "Edit",
                "sClass": "center",
                "bSearchable": false,
                "bSortable": false,
                "sDefaultContent": "reference",
                "mRender": function (data, type, row) {
                    return "<a href='/applicants/edit/" + row.id + "'>Edit</a>"
                }
            },
            {                 //Delete link
                "mData": "id",
                "sTitle": "Delete",
                "sClass": "center",
                "bSearchable": false,
                "bSortable": false,
                "sDefaultContent": "reference",
                "mRender": function (data, type, row) {
                    return "<a href='/applicants/delete/" + row.id + "'>Delete</a>"
                }
            }
        ]
    })
});

jQuery.extend(jQuery.fn.dataTableExt.oSort, {
    "dateAddition-asc": function (a, b) {
        var dateAdditiona = $.trim(a).split('.');
        var dateAdditionb = $.trim(b).split('.');

        if (dateAdditiona[2] * 1 < dateAdditionb[2] * 1)
            return 1;
        if (dateAdditiona[2] * 1 > dateAdditionb[2] * 1)
            return -1;
        if (dateAdditiona[2] * 1 == dateAdditionb[2] * 1) {
            if (dateAdditiona[1] * 1 < dateAdditionb[1] * 1)
                return 1;
            if (dateAdditiona[1] * 1 > dateAdditionb[1] * 1)
                return -1;
            if (dateAdditiona[1] * 1 == dateAdditionb[1] * 1) {
                if (dateAdditiona[0] * 1 < dateAdditionb[0] * 1)
                    return 1;
                if (dateAdditiona[0] * 1 > dateAdditionb[0] * 1)
                    return -1;
            }
            else
                return 0;
        }
    },

    "dateAddition-desc": function (a, b) {
        var dateAdditiona = $.trim(a).split('.');
        var dateAdditionb = $.trim(b).split('.');

        if (dateAdditiona[2] * 1 < dateAdditionb[2] * 1)
            return -1;
        if (dateAdditiona[2] * 1 > dateAdditionb[2] * 1)
            return 1;
        if (dateAdditiona[2] * 1 == dateAdditionb[2] * 1) {
            if (dateAdditiona[1] * 1 < dateAdditionb[1] * 1)
                return -1;
            if (dateAdditiona[1] * 1 > dateAdditionb[1] * 1)
                return 1;
            if (dateAdditiona[1] * 1 == dateAdditionb[1] * 1) {
                if (dateAdditiona[0] * 1 < dateAdditionb[0] * 1)
                    return -1;
                if (dateAdditiona[0] * 1 > dateAdditionb[0] * 1)
                    return 1;
            }
            else
                return 0;
        }
    }
});

/*
 function namef (data1) {
 console.log("we are in the function");

 data = data1;
 console.log("message = " + data + "; typeof = " + (typeof data) //+
 //"; parse = " + JSON.parse(data) //+
 //"; stringify = " + JSON.stringify(""+data)
 );

 for (element in data1) {
 console.log("for in element = " + element + " data = " + data1[element]);
 }
 console.log("ratings");

 var categoryArray = data1["ratings"];

 var table = '<table>';
 for (var i = 0; i < categoryArray.length; i++) {
 table += '<tr><td>';
 table += "id = " + i;
 table += '</td></tr>';
 }

 table += '</table>';
 console.log("table = " + table);
 //document.getElementById('your-id').value = table2;
 document.getElementById('your-id').innerHTML = table;
 //console.log("id = " + i + ", value = " + categoryArray[i].skill + ", rating = " + categoryArray[i].rating);
 for (var i = 0; i < categoryArray.length; i++) {
 console.log("id = " + i);
 }

 }

function addSkill() {
    //console.log("add skill" + new Date());
    var skill = '<fieldset>';
    skill += '<select>';
    var skillsArray = [1, 2, 3];
    for (var i = 0; i < skillsArray.length; i++) {
        skill += '<option selected = "selected">' + skillsArray [i] + '</option>';
    }
    skill += '</select>';
    skill += '</fieldset>';

    var existing_skills = document.getElementById('skill').innerHTML;
    document.getElementById('skill').innerHTML = existing_skills + skill;
}
 */

function getSkills() {
    var req = getXmlHttp();

    // span рядом с кнопкой
    // в нем будем отображать ход выполнения
    //назначает обработчик ответа сервера onreadystatechange
    var statusElem = document.getElementById('vote_status')

    req.onreadystatechange = function () {
        // onreadystatechange активируется при получении ответа сервера

        if (req.readyState == 4) {
            // если запрос закончил выполняться

            statusElem.innerHTML = req.statusText // показать статус (Not Found, ОК..)

            if (req.status == 200) {

                var fieldset = document.createElement("fieldset");
                fieldset.setAttribute("id", "skillFieldsetId-" + (++skillNumber));

                var myDiv = document.getElementById("skill");
                myDiv.appendChild(fieldset);

                var selectList = document.createElement("select");
                selectList.id = skillNumber;
                selectList.name = "skillNameId-" + skillNumber;
                selectList.setAttribute("onchange", "newSkill(this)");

                //var myDiv = document.getElementById("formApplicant");
                //myDiv.appendChild(selectList);

                var option = document.createElement("option");
                option.value = 0;
                option.text = 'select skill';
                selectList.appendChild(option);

                var option = document.createElement("option");
                option.value = -1;
                option.text = 'new skill';
                selectList.appendChild(option);

                var skillsArray = JSON.parse(req.responseText).skills;
                for (var i = 0; i < skillsArray.length; i++) {
                    var option = document.createElement("option");
                    option.value = skillsArray[i].id;
                    option.text = skillsArray[i].name + ', id = ' + skillsArray[i].id;
                    //option.text = skillsArray[i].name + '<a href="/deleteSkill">delete</a>';
                    selectList.appendChild(option);
                }

                fieldset.appendChild(selectList);
            }
            // тут можно добавить else с обработкой ошибок запроса
        }
    }

    //задать адрес подключения
    //открывает соединение open
    req.open('GET', '/getSkills', true);

    // объект запроса подготовлен: указан адрес и создана функция onreadystatechange
    // для обработки ответа сервера
    //отправляет запрос вызовом send (ответ сервера принимается срабатывающей в асинхронном режиме функцией onreadystatechange)
    req.send(null);  // отослать запрос

    //показывает индикатор состояния процесса
    statusElem.innerHTML = 'Ожидаю ответа сервера...'
}

/*
function newSkillw(data) {
    //console.log("here is my skill ============= " + data.value);
    if (data.value == "new skill") {
        var newSkill = prompt('Enter new skill', '');
    }
}
*/

// javascript-код голосования из примера
//function newSkill(data, skillNumber) {
function newSkill(data) {
    if (data.value == "-1") {
        var newSkill = prompt('Enter new skill', '');
        var req = getXmlHttp();
        var statusElem = document.getElementById('vote_status')

        req.onreadystatechange = function () {
            if (req.readyState == 4) {
                statusElem.innerHTML = req.statusText // показать статус (Not Found, ОК..)
                if (req.status == 200) {
                    if (JSON.parse(req.responseText).createNewSkill === "true") {
                        alert("Навык: " + newSkill + " Успешно добавлен !");
                        deleteSkill(data.id);
                    } else {
                        alert("Навык: " + newSkill + " Уже существует !");
                    }
                }
            }

        }
        req.open('GET', '/addSkill?newSkill=' + newSkill, true);
        req.send(null);
        statusElem.innerHTML = 'Ожидаю ответа сервера...';
    } else {
        if (data.value > 0) {
            console.log(document.getElementById('skillValueId-' + skillNumber));
            if (!document.getElementById('skillValueId-' + skillNumber)) {
                var skillValue = document.createElement("input");
                skillValue.type = 'number';
                skillValue.value = 5;
                skillValue.name = 'skillValueName-' + skillNumber;
                skillValue.id = 'skillValueId-' + skillNumber;
                skillValue.min = 0;
                skillValue.max = 10;
                skillValue.step = 1;
                var selectList = document.getElementById("skillFieldsetId-" + skillNumber);
                selectList.appendChild(skillValue);

                var buttonDelete = document.createElement("input");
                buttonDelete.type = "button";
                buttonDelete.name = skillNumber;
                buttonDelete.value = "Delete skill";
                buttonDelete.setAttribute("onclick", "deleteSkill(" + skillNumber + ")");
                selectList.appendChild(buttonDelete);
            }
        }
    }
}

function deleteSkill(numberDeleteRow) {
    var deleteRow = document.getElementById('skillFieldsetId-' + numberDeleteRow);
    deleteRow.parentNode.removeChild(deleteRow);
}

function getXmlHttp() {
    var xmlhttp;
    try {
        /* Если же объект XMLHttpRequest не существует значит мы имеем дело с IE6 и нам придется
         воспользоваться специальным синтаксисом */
        xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
        try {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (E) {
            xmlhttp = false;
        }
    }
    /* Если объект XMLHttpRequest существует, значит мы имеем дело с современным браузером
     Chrome, Firefox, Safari, Opera или IE7 и выше. */
    if (!xmlhttp && typeof XMLHttpRequest != 'undefined') {
        xmlhttp = new XMLHttpRequest();
    }
    return xmlhttp;
}