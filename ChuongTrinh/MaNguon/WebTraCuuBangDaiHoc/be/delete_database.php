<?php
    include("./connect.php");

    $strSQL = "delete from history_certificate;";
    $result = mysqli_query($connect, $strSQL);
        // or die("<p>Không thể thực thi câu truy vấn xóa tất cả dữ liệu</p>");
    echo "Kết quả xóa dữ liệu cũ: " . $result;

    mysqli_close($connect);
?>