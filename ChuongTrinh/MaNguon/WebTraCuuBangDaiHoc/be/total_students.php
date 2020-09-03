<?php

    // Bước 1: kết nối csdl
    include("./connect.php");

    // Bước 2: tìm tổng số records
    $strSQL = 'select name, dateOfBirth, hometown, class, modeOfStudy, registerNumber, 
    idCertificate, note from history_certificate';
    $result = mysqli_query($connect, $strSQL);
    $total_records = mysqli_num_rows($result);

    echo $total_records;

    mysqli_close($connect);

?>
