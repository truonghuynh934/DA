/****  input tag ****/
const inputs = document.querySelectorAll('.input');

function focusFunc() {
    let parent = this.parentNode;
    parent.classList.add('focus');
}

function blurFunc() {
    let parent = this.parentNode;
    if (this.value == "") {
        parent.classList.remove('focus');
    }
}

inputs.forEach(input => {
    input.addEventListener('focus', focusFunc);
    input.addEventListener('blur', blurFunc);
});

/**** handler buttons ****/
var containerDialog = document.getElementsByClassName("dialog");
var xmlhttp = new XMLHttpRequest();
var divContainTable = document.getElementsByClassName("list-student")[0];

// function inputFile() {
//     console.info("File on input file: " + fileExcel.value);
// }

function addData() {
    var fileExcel = document.getElementById("add-data");
    // console.info("add data: '" + fileExcel.value + "'");

    // display dialog
    if (fileExcel.value != "") {
        containerDialog[0].style.display = "block";
        containerDialog[0].getElementsByTagName("iframe")[0].setAttribute("src", "./dialog_preview_file.html");
        console.log("Add Data method");
    }
}

function replaceData() {
    var fileExcel = document.getElementById("replace-data");

    // display dialog
    if (fileExcel.value != "") {
        containerDialog[0].style.display = "block";
        containerDialog[0].getElementsByTagName("iframe")[0].setAttribute("src", "./dialog_preview_new_file.html");
        console.log("Replace Data method");
    }
}

sessionStorage.clear();
function search() {
    var informationStudent = document.getElementsByClassName("input");
    var idCertificate = informationStudent[0].value;
    var name = informationStudent[1].value;
    var dateOfBirth = informationStudent[2].value;

    // console.info("ID CERTIFICATE: '" + idCertificate + "'");
    // console.info("NAME: '" + name + "'");
    // console.info("DATE: '" + dateOfBirth + "'");

    // trim() đề phòng trường hợp giá trị nhập vào là "            "
    idCertificate = idCertificate.trim();
    name = name.trim();
    dateOfBirth = dateOfBirth.trim();

    // show information student found
    if (idCertificate != "" || name != "" || dateOfBirth != "") {
        sessionStorage.setItem("idCertificate", idCertificate);
        sessionStorage.setItem("name", name);
        sessionStorage.setItem("dateOfBirth", dateOfBirth);

        xmlhttp.onreadystatechange = function () {
            // console.info("readyState = " + this.readyState + ", status = " + this.status);
            if (this.readyState == 4 && this.status == 200) {
                divContainTable.innerHTML = this.responseText;
            } else {
                // console.info("tìm thông tin");
            }
        };
        xmlhttp.open("GET", "./be/search_student_admin.php?id_certificate=" + idCertificate + "&&name=" + name + "&&date_of_birth=" + dateOfBirth, true);
        xmlhttp.send();
        // console.warn("'" + idCertificate + "' : '" + name + "' : '" + dateOfBirth + "'");
        // console.log(typeof idCertificate + ", " + typeof name + ", " + typeof dateOfBirth);

        // show total pages found
        var xmlhttp2 = new XMLHttpRequest();
        xmlhttp2.onreadystatechange = function (totalPagesFound) {
            if (this.readyState == 4 && this.status == 200) {
                if (this.responseText == 0) {
                    document.getElementsByClassName("total-pages")[0].innerHTML = 1;
                } else {
                    document.getElementsByClassName("total-pages")[0].innerHTML = this.responseText;
                }
            } else {
                // console.info("tìm trang");
            }
        };
        xmlhttp2.open("GET", "./be/total_pages_search.php?id_certificate=" + idCertificate + "&&name=" + name + "&&date_of_birth=" + dateOfBirth, false);
        xmlhttp2.send();
        var totalPagesFound=parseInt(document.getElementsByClassName("total-pages")[0].innerText);
        // console.warn("pages = " + totalPagesFound);

        // show total students found
        var xmlhttp3 = new XMLHttpRequest();
        xmlhttp3.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                document.getElementsByClassName("total-students")[0].innerHTML = this.responseText;
            } else {
                // console.info("tìm so luong isnh vien");
            }
        }
        xmlhttp3.open("GET", "./be/total_students_search.php?id_certificate=" + idCertificate + "&&name=" + name + "&&date_of_birth=" + dateOfBirth, true);
        xmlhttp3.send();

        /**
    * UPDATE status 4 buttons
    */
        // console.info("show total pages take html: " + document.getElementsByClassName("total-pages")[0].innerHTML);
        var firstPage = document.getElementsByClassName("first-page")[0];
        var previousPage = document.getElementsByClassName("previous-page")[0];
        var nextPage = document.getElementsByClassName("next-page")[0];
        var lastPage = document.getElementsByClassName("last-page")[0];
        var pageNumber = document.getElementsByClassName("index-page-current")[0];
        pageNumber.value = 1;
        if (totalPagesFound == 1) {
            firstPage.classList.add("disabled");
            previousPage.classList.add("disabled");
            nextPage.classList.add("disabled");
            lastPage.classList.add("disabled");
            // console.info("TRANG TÌM ĐƯỢC = 1");
        } else {
            // page number = 1 nên disable first page and previous page
            firstPage.classList.add("disabled");
            previousPage.classList.add("disabled");
            nextPage.classList.remove("disabled");
            nextPage.style.cursor = "pointer";
            lastPage.classList.remove("disabled");
            lastPage.style.cursor = "pointer";
            // console.info("TRANG TÌM ĐƯỢC !=1: " + totalPagesFound);
        }

        // alert("Đã tìm!");
    } else {
        alert("Bạn chưa điền thông tin để tìm!");
    }

    console.log("Search method");
}

function logOut() {
    localStorage.removeItem("user");
    localStorage.removeItem("pass");
    // window.location.replace("./user.html");
    window.location.replace("./home.html");
    console.log("Logout method");
}

function deleteRows(){
    var listIdCertificatesSelected = new Array();
    var xmlhttp = new XMLHttpRequest();
    var selectedStudents = document.getElementsByClassName("selectStudent");
    
    for(var i = 0; i<selectedStudents.length;i++){
        var idCertificateSlected = selectedStudents[i].parentNode.parentNode.getElementsByTagName("td")[8].innerHTML;
        if (selectedStudents[i].checked){    
            listIdCertificatesSelected.push(idCertificateSlected);
            // alert(idCertificateSlected + " được chọn");
        }else{
            // alert(idCertificateSlected + " không được chọn");
        }
    }

    xmlhttp.onreadystatechange = function(){
        if (this.readyState == 4 && this.status == 200){
            document.getElementsByClassName("title")[0].innerHTML = this.responseText;
            // console.info("DELETE successfully!")
        }
    };
    xmlhttp.open("GET","./be/delete_student.php?listIdCertificatesSelected="+listIdCertificatesSelected,false);
    xmlhttp.send();

    // announcement
    if (listIdCertificatesSelected.length == 0){
        alert("Bạn chưa chọn bằng để xóa!");
    }else{
        var idCertificatesDeleted = "";
        for(var j = 0;j<listIdCertificatesSelected.length; j++){
            idCertificatesDeleted = idCertificatesDeleted + listIdCertificatesSelected[j];
            if (j != listIdCertificatesSelected.length-1){
                idCertificatesDeleted = idCertificatesDeleted + ", ";
            }
        }
        alert("Đã xóa các bằng có số hiệu:\n" + idCertificatesDeleted);
    }

    // refresh page after delete
    window.location.reload();
}

// const inputExcleFileReplace = document.getElementById("replace-data");
// inputExcleFileReplace.addEventListener("change", handleExcleFile, false);
// function replaceData(){
//     let file;
//     if (inputExcleFileReplace.value == null){
//         console.log("This is null");
//     }else{
//         file = inputExcleFileReplace.file;
//         console.log(inputExcleFileReplace.value);
//     }

//     // window.open("file:///");
//     console.log("Replace Data method");
// }


/**** pagination ****/
const pagination = document.querySelectorAll('.pagination');

/**** others ****/
function loadAllData() {
    xmlhttp.onreadystatechange = function () {
        // console.info("readyState = " + this.readyState + ", status = " + this.status);

        if (this.readyState == 4 && this.status == 200) {
            divContainTable.innerHTML = this.responseText;
        }
    };
    xmlhttp.open("GET", "./be/load_all_data.php", true);
    xmlhttp.send();

    console.info("Load all data method");
}