/*
http://shpargalkablog.ru/2013/08/appendchild-removechild-javascript.html
http://javascript.ru/tutorial/dom/attributes
http://www.wisdomweb.ru/AJAX/object.php
http://editor.datatables.net/examples/styling/envelopeInTable.html
http://www.datatables.net/forums/discussion/9966/want-to-have-a-edit-and-delete-button-added-to-datatable
http://www.pixelcom.crimea.ua/rabota-s-jquery-datatables.html
*/
var skillNumber = 0;
var contactNumber = 0;

$(function() {
    $( ".datepicker" ).datepicker({ dateFormat: "dd/mm/yy" });
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
                    return "<a onclick='deleteApplicant(" + row.id + ")' href='javascript:void(0);'>Delete</a>";
                    //return "<a href='/applicants/delete/" + row.id + "'>Delete</a>"
                }
            }
        ]
    })
});

//For right sort date column
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


$.fn.dataTableExt.oApi.fnStandingRedraw = function(oSettings) {
    //redraw to account for filtering and sorting
    // concept here is that (for client side) there is a row got inserted at the end (for an add)
    // or when a record was modified it could be in the middle of the table
    // that is probably not supposed to be there - due to filtering / sorting
    // so we need to re process filtering and sorting
    // BUT - if it is server side - then this should be handled by the server - so skip this step

    //if(oSettings.oFeatures.bServerSide === false){
        var before = oSettings._iDisplayStart;
        oSettings.oApi._fnReDraw(oSettings);
        //iDisplayStart has been reset to zero - so lets change it back
        oSettings._iDisplayStart = before;
        oSettings.oApi._fnCalculateEnd(oSettings);
    //}

    //draw the 'current' page
    oSettings.oApi._fnDraw(oSettings);
};

function deleteApplicant(idApplicant) {
    var r = confirm("Are you shure!");
    if (r == true) {
        var req = getXmlHttp();
        console.log('ok1');
        req.onreadystatechange = function () {};
        console.log('ok2');
        req.open('GET', '/applicants/delete/' + idApplicant, true);
        console.log('ok3');
        req.send(null);
        var oTable1 = $('#contacts').dataTable();
        oTable1.fnStandingRedraw();
    }
}

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
                selectList.id = 'skill-' + skillNumber;
                selectList.name = "skillNameId-" + skillNumber;
                selectList.setAttribute("onchange", "newSkill(this)");

                var option = document.createElement("option");
                //option.value = 0;
                option.text = 'select skill';
                selectList.appendChild(option);

                var option = document.createElement("option");
                option.value = -1;
                option.text = 'new skill';
                selectList.appendChild(option);
                console.log("in add = " + req.responseText);
                var skillsArray = JSON.parse(req.responseText).skills;
                for (var i = 0; i < skillsArray.length; i++) {
                    var option = document.createElement("option");
                    option.value = skillsArray[i].id;
                    option.text = skillsArray[i].name;
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

function getContacts() {
    var req = getXmlHttp();
    req.onreadystatechange = function () {
        if (req.readyState == 4) {
            if (req.status == 200) {

                var fieldset = document.createElement("fieldset");
                fieldset.setAttribute("id", "contactFieldsetId-" + (++contactNumber));

                var myDiv = document.getElementById("contact");
                myDiv.appendChild(fieldset);

                var selectList = document.createElement("select");
                selectList.id = contactNumber;
                selectList.name = "contactNameId-" + contactNumber;
                selectList.setAttribute("onchange", "newContact(this)");

                var option = document.createElement("option");
                option.value = 0;
                option.text = 'select contact';
                selectList.appendChild(option);

                var option = document.createElement("option");
                option.value = -1;
                option.text = 'new contact';
                selectList.appendChild(option);
                //console.log("in add = " + req.responseText);
                var typeContactsArray = JSON.parse(req.responseText).typeContacts;
                for (var i = 0; i < typeContactsArray .length; i++) {
                    var option = document.createElement("option");
                    option.value = typeContactsArray [i].id;
                    option.text = typeContactsArray [i].name;
                    selectList.appendChild(option);
                }

                fieldset.appendChild(selectList);
            }
        }
    }
    req.open('GET', '/getTypeContacts', true);
    req.send(null);  // отослать запрос
}

function newSkill(data) {
    if (data.value == "-1") {
        var newSkill = prompt('Enter new skill', '');
        var req = getXmlHttp();
        req.onreadystatechange = function () {
            if (req.readyState == 4) {
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

function newContact(data) {
    if (data.value == "-1") {
        var newContact = prompt('Enter new type contact', '');
        var req = getXmlHttp();

        req.onreadystatechange = function () {
            if (req.readyState == 4) {
                if (req.status == 200) {
                    if (JSON.parse(req.responseText).createNewTypeContact == "true") {
                        alert("Тип контакта: " + newContact + " Успешно добавлен !");
                        deleteContact(data.id);
                    } else {
                        alert("Тип контакта: " + newContact + " Уже существует !");
                    }
                }
            }
        }
        req.open('GET', '/addTypeContact?newTypeContact=' + newContact, true);
        req.send(null);
    } else {
        if (data.value > 0) {
            if (!document.getElementById('contactValueId-' + contactNumber)) {
                var typeContactValue = document.createElement("input");
                typeContactValue.type = 'text';
                typeContactValue.name = 'contactValueName-' + contactNumber;
                typeContactValue.id = 'contactValueId-' + contactNumber;
                var selectList = document.getElementById("contactFieldsetId-" + contactNumber);
                selectList.appendChild(typeContactValue);

                var buttonDelete = document.createElement("input");
                buttonDelete.type = "button";
                buttonDelete.name = contactNumber;
                buttonDelete.value = "Delete contact";
                buttonDelete.setAttribute("onclick", "deleteContact(" + contactNumber + ")");
                selectList.appendChild(buttonDelete);
            }
        }
    }
}

function deleteSkill(numberDeleteRow) {
    var deleteRow = document.getElementById('skillFieldsetId-' + numberDeleteRow);
    deleteRow.parentNode.removeChild(deleteRow);
}

function deleteContact(numberDeleteRow) {
    var deleteRow = document.getElementById('contactFieldsetId-' + numberDeleteRow);
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

function loadApplicant(message) {
    if (message.name != undefined) {
        var selectInput = document.getElementById("nameInput");
        selectInput.setAttribute('value', message.name);
        selectInput = document.getElementById("dateInput");
        selectInput.setAttribute('value', message.dateInterview);
        var req = getXmlHttp();
        req.onreadystatechange = function () {
            if (req.readyState == 4) {
                if (req.status == 200) {

                    var ownSkillsArray = message.ratings;
                    for (var i = 0; i < ownSkillsArray.length; i++) {
                        var option = document.createElement("option");
                        //console.log('skill = ' + ownSkillsArray[i].skill + ', rating ' + ownSkillsArray[i].rating + ', id skill = ' + ownSkillsArray[i].id);

                        var fieldset = document.createElement("fieldset");
                        fieldset.setAttribute("id", "skillFieldsetId-" + (++skillNumber));

                        var myDiv = document.getElementById("skill");
                        myDiv.appendChild(fieldset);

                        var selectList = document.createElement("select");
                        selectList.id = 'skill-' + skillNumber;
                        selectList.name = "skillNameId-" + skillNumber;
                        selectList.setAttribute("onchange", "newSkill(this)");

                        var option = document.createElement("option");
                        option.value = 0;
                        option.text = 'select skill';
                        selectList.appendChild(option);

                        var option = document.createElement("option");
                        option.value = -1;
                        option.text = 'new skill';
                        selectList.appendChild(option);

                        var skillsArray = JSON.parse(req.responseText).skills;
                        //console.log('ddd = '+req.responseText);
                        for (var a = 0; a < skillsArray.length; a++) {
                            var option = document.createElement("option");
                            option.value = skillsArray[a].id;
                            option.text = skillsArray[a].name;
                            selectList.appendChild(option);
                        }
                        fieldset.appendChild(selectList);

                        document.getElementById('skill-' + skillNumber).value = ownSkillsArray[i].id;

                        var skillValue = document.createElement("input");
                        skillValue.type = 'number';
                        skillValue.value = 5;
                        skillValue.name = 'skillValueName-' + skillNumber;
                        skillValue.id = 'skillValueId-' + skillNumber;
                        skillValue.min = 0;
                        skillValue.max = 10;
                        skillValue.step = 1;
                        skillValue.value = ownSkillsArray[i].rating;
                        var selectList = document.getElementById("skillFieldsetId-" + skillNumber);
                        selectList.appendChild(skillValue);

                        //document.getElementById('skillValueId-' + skillNumber).value = ownSkillsArray[i].rating;

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
        req.open('GET', '/getSkills', true);
        req.send(null);
    }
    var req2 = getXmlHttp();
        req2.onreadystatechange = function () {
            if (req2.readyState == 4) {
                if (req2.status == 200) {

                    var ownContactsArray = message.contacts;
                    for (var i = 0; i < ownContactsArray.length; i++) {
                        var option = document.createElement("option");
                        //console.log('skill = ' + ownSkillsArray[i].skill + ', rating ' + ownSkillsArray[i].rating + ', id skill = ' + ownSkillsArray[i].id);

                        var fieldset = document.createElement("fieldset");
                        fieldset.setAttribute("id", "contactFieldsetId-" + (++contactNumber));

                        var myDiv = document.getElementById("contact");
                        myDiv.appendChild(fieldset);

                        var selectList = document.createElement("select");
                        selectList.id = 'contact-' + contactNumber;
                        selectList.name = "contactNameId-" + contactNumber;
                        selectList.setAttribute("onchange", "newContact(this)");

                        var option = document.createElement("option");
                        option.value = 0;
                        option.text = 'select contact';
                        selectList.appendChild(option);

                        var option = document.createElement("option");
                        option.value = -1;
                        option.text = 'new contact';
                        selectList.appendChild(option);

                        var contactsArray = JSON.parse(req2.responseText).typeContacts;
                        for (var a = 0; a < contactsArray.length; a++) {
                            var option = document.createElement("option");
                            option.value = contactsArray[a].id;
                            option.text = contactsArray[a].name;
                            selectList.appendChild(option);
                        }
                        fieldset.appendChild(selectList);

                        document.getElementById('contact-' + contactNumber).value = ownContactsArray[i].id;

                        var typeContactValue = document.createElement("input");
                        typeContactValue.type = 'text';
                        typeContactValue.name = 'contactValueName-' + contactNumber;
                        typeContactValue.id = 'contactValueId-' + contactNumber;
                        typeContactValue.value = ownContactsArray[i].value;
                        var selectList = document.getElementById("contactFieldsetId-" + contactNumber);
                        selectList.appendChild(typeContactValue);

                        //document.getElementById('skillValueId-' + skillNumber).value = ownSkillsArray[i].rating;

                        var buttonDelete = document.createElement("input");
                        buttonDelete.type = "button";
                        buttonDelete.name = contactNumber;
                        buttonDelete.value = "Delete contact";
                        buttonDelete.setAttribute("onclick", "deleteContact(" + contactNumber + ")");
                        selectList.appendChild(buttonDelete);
                    }
                }
            }
        }
        req2.open('GET', '/getTypeContacts', true);
        req2.send(null);

}