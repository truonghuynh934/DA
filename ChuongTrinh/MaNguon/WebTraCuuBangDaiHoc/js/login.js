const inputs = document.querySelectorAll('.input');

function focusFunc() {
    let parent = this.parentNode.parentNode;
    parent.classList.add('focus');
}

function blurFunc() {
    let parent = this.parentNode.parentNode;
    if (this.value == "") {
        parent.classList.remove('focus');
    }
}

inputs.forEach(input => {
    input.addEventListener('focus', focusFunc);
    input.addEventListener('blur', blurFunc);
});

// handle login
function handleLogin(){
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var announcement = document.getElementById("announcement");
    var resultLogin;

    // alert(username +", " + password);

    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function(){
        if (this.readyState == 4 && this.status == 200){
            resultLogin = this.responseText;
        }
    };

    xmlHttp.open("POST","./be/handle_login.php",false);
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlHttp.send("username="+username+"&password="+password);

    if (parseInt(resultLogin) == 1){
        // announcement.innerHTML = "Success";
        localStorage.setItem("user",username);
        localStorage.setItem("pass",password);
        window.location.replace("./admin.html");
    }else{
        announcement.innerHTML = "Bạn nhập sai username hoặc password!";
    }
}

