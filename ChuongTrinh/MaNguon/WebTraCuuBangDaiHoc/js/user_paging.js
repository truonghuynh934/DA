function totalStudents() {
    // console.info("------------------------");
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            // console.info("Success");
            document.getElementsByClassName("total-students")[0].innerHTML = this.responseText;
        }
    };
    xmlhttp.open("GET", "./be/total_students.php", true);
    xmlhttp.send();
    // console.info("------------ Total studetns ------------");
}

function totalPages() {
    // console.info("------------------------");
    var xmlhttp = new XMLHttpRequest();
    var inputPageNumber = document.getElementsByClassName("index-page-current")[0];
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            // console.info("Success");
            document.getElementsByClassName("total-pages")[0].innerHTML = this.responseText;
            inputPageNumber.setAttribute("max", this.responseText);
        }
    };
    xmlhttp.open("GET", "./be/total_pages.php", true);
    xmlhttp.send();
    // console.info("------------ Total pages method ------------");
}

function checkValue() {
    // console.info("------------------------");
    var inputPageNumber = document.getElementsByClassName("index-page-current")[0];
    var totalPages = document.getElementsByClassName("total-pages")[0];
    if (parseInt(inputPageNumber.value) > parseInt(totalPages.innerText)) {
        inputPageNumber.value = parseInt(totalPages.innerText);
        // console.info("Lớn hơn");
        setEnableDisableButton();
    }
    // console.info("--------- check value method --------");
}

function change(indexPage) {
    var xmlhttpSearch = new XMLHttpRequest();
    var idCertificate = sessionStorage.getItem("idCertificate");
    var name = sessionStorage.getItem("name");
    var dateOfBirth = sessionStorage.getItem("dateOfBirth");
    // console.info("id change: '"+ idCertificate+"'");
    // console.info("name change: '"+ name+"'");
    // console.info("date change: '"+ dateOfBirth+"'");

    if (idCertificate != "" || name != "" || dateOfBirth != "") {
        xmlhttpSearch.onreadystatechange = function(){
            if(this.readyState == 4 && this.status == 200){
                document.getElementsByClassName("list-student")[0].innerHTML = this.responseText;
            }
        };
        xmlhttpSearch.open("GET","./be/search_student_user.php?page="+indexPage+"&&id_certificate="+idCertificate+"&&name="+name+"&&date_of_birth="+dateOfBirth,true);
        xmlhttpSearch.send();
        // console.info("KHÔNG RỖNG, id = '"+idCertificate+"', name = '"+name+"', date = '"+dateOfBirth+"'");
    }
}

function changePage() {
    // console.info("------------------------");
    var pageNumber = document.getElementsByClassName("index-page-current")[0];
    // console.info("type = " + typeof pageNumber.value + ", page number: " + pageNumber.value);

    change(pageNumber.value);
    setEnableDisableButton();
    // console.info("------------ CHANGE PAGE METHOD! ------------");
}

function goFirstPage() {
    // console.info("------------------------");
    var pageNumber = document.getElementsByClassName("index-page-current")[0];

    if (parseInt(pageNumber.value) != 1) {
        pageNumber.value = 1;
        // console.info("type 1 = " + typeof 1);
        // console.info("type = " + typeof pageNumber.value + ", page number = " + pageNumber.value);

        change(1);
        setEnableDisableButton();
    }
    // console.info("------------Go page first method!------------");
}

function goLastPage() {
    // console.info("------------------------");
    var pageNumber = document.getElementsByClassName("index-page-current")[0];
    var lastPageNumber = document.getElementsByClassName("total-pages")[0];

    if (parseInt(pageNumber.value) != parseInt(lastPageNumber.innerText)) {
        pageNumber.value = parseInt(lastPageNumber.innerText);
        // console.info("type = " + typeof pageNumber.value + ", page number = " + pageNumber.value);

        change(pageNumber.value);
        setEnableDisableButton();
    }
    // console.info("------------Go page last method!------------");
}

function goPreviousPage() {
    // console.info("------------------------");
    var pageNumber = document.getElementsByClassName("index-page-current")[0];

    if (pageNumber.value > 1) {
        pageNumber.value = parseInt(pageNumber.value) - 1;
        // console.info("type = " + typeof pageNumber.value + ", page number = " + pageNumber.value);

        change(pageNumber.value);
        setEnableDisableButton();
    }
    // console.info("------------Go previous page method!------------");
}

function goNextPage() {
    // console.info("------------------------");
    var pageNumber = document.getElementsByClassName("index-page-current")[0];
    var lastPageNumber = document.getElementsByClassName("total-pages")[0];

    if (parseInt(pageNumber.value) < parseInt(lastPageNumber.innerText)) {
        pageNumber.value = parseInt(pageNumber.value) + 1;
        // console.info("type = " + typeof pageNumber.value + ", page number = " + pageNumber.value);

        change(pageNumber.value);
        setEnableDisableButton();
    }
    // console.info("------------Go next page method!------------");
}

function setEnableDisableButton() {
    // console.info("-----------------------");
    var firstPage = document.getElementsByClassName("first-page")[0];
    var previousPage = document.getElementsByClassName("previous-page")[0];
    var lastPage = document.getElementsByClassName("last-page")[0];
    var nextPage = document.getElementsByClassName("next-page")[0];
    var pageNumber = document.getElementsByClassName("index-page-current")[0];
    var totalPages = document.getElementsByClassName("total-pages")[0];

    // console.info("max: " + pageNumber.getAttribute("max"));
    // console.info("page number: " + pageNumber.value);
    // console.info("total pages: " + totalPages.innerHTML);

    if (parseInt(totalPages.innerHTML) == 1) {
        // trường hợp chỉ có 1 page
        lastPage.style.cursor = "default";
        nextPage.style.cursor = "default";
        lastPage.classList.add('disabled');
        nextPage.classList.add('disabled');

        firstPage.style.cursor = "default";
        previousPage.style.cursor = "default";
        firstPage.classList.add('disabled');
        previousPage.classList.add('disabled');
        // console.info("Trang duy nhất");

    } else if (parseInt(pageNumber.value) == 1) {
        firstPage.style.cursor = "default";
        previousPage.style.cursor = "default";
        firstPage.classList.add('disabled');
        previousPage.classList.add('disabled');

        lastPage.style.cursor = "pointer";
        nextPage.style.cursor = "pointer";
        lastPage.classList.remove('disabled');
        nextPage.classList.remove('disabled');
        // console.info("Trang đầu");

    } else if (parseInt(pageNumber.value) == parseInt(totalPages.innerText)) {
        lastPage.style.cursor = "default";
        nextPage.style.cursor = "default";
        lastPage.classList.add('disabled');
        nextPage.classList.add('disabled');

        firstPage.style.cursor = "pointer";
        previousPage.style.cursor = "pointer";
        firstPage.classList.remove('disabled');
        previousPage.classList.remove('disabled');
        // console.info("Trang cuối");

    } else {
        firstPage.style.cursor = "pointer";
        previousPage.style.cursor = "pointer";
        firstPage.classList.remove('disabled');
        previousPage.classList.remove('disabled');

        lastPage.style.cursor = "pointer";
        nextPage.style.cursor = "pointer";
        lastPage.classList.remove('disabled');
        nextPage.classList.remove('disabled');
        // console.info("Trang giữa");
    }

    // console.info("Set enable, page  = " + pageNumber.value + ", type = " + typeof pageNumber.value);
    // console.info("------- set enable disable button -------");
}