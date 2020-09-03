<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
    <?php
    // Bước 1: kết nối csdl
    include("./connect.php");

    $idCertificate = $_GET["id_certificate"];
    $name = $_GET["name"];
    $dateOfBirth = $_GET['date_of_birth'];
    echo "<pre><i>Kết quả tìm được với \"số hiệu văn bằng\" = \"".$idCertificate."\", 
    \"họ và tên\" = \"".$name."\", \"ngày sinh\" = \"".$dateOfBirth."\" </i></pre>";

    // echo "<br />" .$idStudent .", ";
    // echo $name . ", ";
    // echo $dateOfBirth . "<br />";

    // Bước 2: tìm tổng số records
    // if ($idStudent != "" && $name == "" && $dateOfBirth == ""){
    //     $strSQL = "select * from history_certificate where idStudent like '%".$idStudent."%';";
    //     echo "Tìm theo msv <br />";
    // }else if ($idStudent == "" && $name != "" && $dateOfBirth == ""){
    //     $strSQL = "select * from history_certificate where name like '%".$name."%';";
    //     echo "Tìm theo họ và tên <br />";
    // }else if ($idStudent == "" && $name == "" && $dateOfBirth != ""){
    //     $strSQL = "select * from history_certificate where dateofbirth like '%".$dateOfBirth."%';";
    //     echo "Tìm theo ngày sinh <br />";
    // }else{
    //     $strSQL = "select * from history_certificate 
    //     where idStudent like '%".$idStudent."%' OR name like '%".$name."%' OR dateofbirth like '%".$dateOfBirth."%';";
    //     echo "Tìm tất cả <br />";
    // }
    $strSQL = "select name, dateOfBirth, hometown, class, modeOfStudy, registerNumber, 
    idCertificate, note from history_certificate 
        where idCertificate like '%" . $idCertificate . "%' and name like '%" . $name . "%' and dateofbirth like '%" . $dateOfBirth . "%';";
    // echo "Tìm tất cả <br />";
    $result = mysqli_query($connect, $strSQL)
        or die("<p>Không thể thực thi câu truy cấn tìm sinh viên</p>");
    $total_records = mysqli_num_rows($result);

    // Bước 3: tìm limit và current_page
    $current_page = isset($_GET["page"]) ? $_GET["page"] : 1;
    $limit = 10;

    // Bước 4: tính toán total_page và start
    $total_page = ceil($total_records / $limit);
    if ($current_page > $total_page) {
        $current_page = $total_page;
    } else if ($current_page < 1) {
        $current_page = 1;
    }

    $start = ($current_page - 1) * $limit;

    // Bước 5: truy vấn lấy danh sách sinh viên
    $strSQL = "select name, dateOfBirth, hometown, class, modeOfStudy, registerNumber, 
    idCertificate, note from history_certificate 
    where idCertificate like '%" . $idCertificate . "%' and name like '%" . $name . "%' 
    and dateofbirth like '%" . $dateOfBirth . "%' limit " . $start .  ", " . $limit . ";";
    $result = mysqli_query($connect, $strSQL);

    // create table
    echo "<table>
<tr class='title-column'>
<th class='id-column'>STT</th>
<th class='name-column'>Họ và tên</th>
<th class='date-of-birth-column'>Ngày sinh</th>
<th class='hometown-column'> Nơi sinh</th>
<th class='class-column'>Lớp</th>
<th class='mode-of-study-column'>Hệ</th>
<th class='register-number-column'>Số vào sổ đăng ký</th>
<th class='id-certificate-column'>Số hiệu văn bằng</th>
<th class='note-column'>Ghi chú</th>
</tr>";

    /* PHẦN HIỂN THỊ SINH VIÊN */
    // Bước 6: Hiển thị danh sách sinh viên
    if ($result != null) { // phòng trường hợp tìm với id, name hoặc date = "                   "
        $start++;
        while ($row = mysqli_fetch_row($result)) {
            echo "<tr>";
            echo "<td class='id-column'>" . $start . "</td>";
            echo "<td class='name-column'>" . $row[0] . "</td>";
            // $date = date_create($row[1]);
            // echo "<td>" . date_format($date, 'd/m/Y') . "</td>";
            echo "<td class='date-of-birth-column'>" . $row[1] . "</td>";
            echo "<td class='hometown-column'>" . $row[2] . "</td>";
            echo "<td class='class-column'>" . $row[3] . "</td>";
            echo "<td class='mode-of-study-column'>" . $row[4] . "</td>";
            echo "<td class='register-number-column'>" . $row[5] . "</td>";
            echo "<td class='id-certificate-column'>" . $row[6] . "</td>";
            echo "<td class='note-column'>" . $row[7] . "</td>";
            echo "</tr>";
            $start++;
        }
    }
    echo "</table>";

    mysqli_close($connect);
    ?>
</body>

</html>