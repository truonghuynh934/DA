<?php

        // Bước 1: kết nối csdl
        include("./connect.php");

        // lấy giá trị để tìm
        $idCertificate = $_GET["id_certificate"];
        $name = $_GET["name"];
        $dateOfBirth = $_GET['date_of_birth'];

        // Bước 2: tìm tổng số records
        $strSQL = "select name, dateOfBirth, hometown, class, modeOfStudy, registerNumber, 
        idCertificate, note from history_certificate 
                        where idCertificate like '%" . $idCertificate . "%' and name like '%" . $name . "%' and dateofbirth like '%" . $dateOfBirth . "%';";
        $result = mysqli_query($connect, $strSQL);
        $total_records = mysqli_num_rows($result);

        echo $total_records;

        mysqli_close($connect);
?>
