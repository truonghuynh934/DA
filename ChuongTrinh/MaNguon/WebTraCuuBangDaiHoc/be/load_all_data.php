<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>

    <?php
    
    include("./create_table_paging.php");
//    include("./connect.php");

//     $strSQL = "select * from history_certificate;";
//     $result = mysqli_query($connect, $strSQL)
//         or die("<p>Không thể thực thi câu truy vấn</p>");

//     // create table
//     echo "<table>
// <tr class='title-column'>
//     <th>STT</th>
//     <th>Mã sinh viên</th>
//     <th>Họ và tên</th>
//     <th>Giới tính</th>
//     <th>Ngày sinh</th>
//     <th>Quê quán</th>
//     <th>ĐTBC</th>
//     <th>Xếp loại</th>
//     <th>Ngày cấp bằng</th>
//     <th>Năm tốt nghiệp</th>
//     <th>Số vào sổ đăng ký</th>
//     <th>Ngành đào tạo</th>
//     <th>Hình thức đào tạo</th>
//     <th>Danh hiệu</th>
//     <th>Khóa</th>
//     <th>Ghi chú</th>
// </tr>";

//     $index = 0;
//     while ($row = mysqli_fetch_row($result)) {
//         $index++;
//         echo "<tr>";
//         echo "<td>" . $index . "</td>";
//         echo "<td>" . $row[0] . "</td>";
//         echo "<td>" . $row[1] . "</td>";
//         echo "<td>" . $row[2] . "</td>";
//         echo "<td>" . $row[3] . "</td>";
//         echo "<td>" . $row[4] . "</td>";
//         echo "<td>" . $row[5] . "</td>";
//         echo "<td>" . $row[6] . "</td>";
//         $date = date_create($row[7]);
//         echo "<td>" . date_format($date, 'd/m/Y') . "</td>";
//         echo "<td>" . $row[8] . "</td>";
//         echo "<td>" . $row[9] . "</td>";
//         echo "<td>" . $row[10] . "</td>";
//         echo "<td>" . $row[11] . "</td>";
//         echo "<td>" . $row[12] . "</td>";
//         echo "<td>" . $row[13] . "</td>";
//         echo "<td>" . $row[14] . "</td>";
//         echo "</tr>";
//     }

//     echo "</table>";

//    mysqli_close($connect);
    ?>

</body>

</html>