<?php

    include("./connect.php");

    $username = $_POST["username"];
    $password = $_POST["password"];

    $strSQL = "select * from login where username = '" . $username . "' and password = '" . $password . "';";
    $result = mysqli_query($connect, $strSQL);
    $numberRow = mysqli_num_rows($result);

    if ($numberRow > 0) {
        // echo "Đăng nhập thành công";
        // header("location: ../FE/admin.html", true, 301);
        echo("1");
    } else {
        // echo "không tìm thấy trong csdl";
        // header("location: ../FE/dialog_login_fail.html",true,301);
        echo("0");
    }

    mysqli_close($connect);

?>