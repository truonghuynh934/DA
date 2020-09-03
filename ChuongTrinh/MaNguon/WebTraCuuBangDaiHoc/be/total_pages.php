<?php

// Bước 1: kết nối csdl
include("./connect.php");

// Bước 2: tìm tổng số records
$strSQL = 'select name, dateOfBirth, hometown, class, modeOfStudy, registerNumber, 
idCertificate, note from history_certificate';
$result = mysqli_query($connect, $strSQL);
$total_records = mysqli_num_rows($result);

// Bước 3: Tìm limit và current_page
$limit = 10;

// Bước 4: tính toán total_page và start
// tổng số trang
$total_pages = ceil($total_records / $limit);

echo $total_pages;

mysqli_close($connect);

?>