<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
    <?php
    /* PHẦN XỬ LÝ PHP */
    // Bước 1: kết nối csdl
    include("./connect.php");

    // Bước 2: tìm tổng số records
    $strSQL = 'select name, dateOfBirth, hometown, class, modeOfStudy, registerNumber, 
    idCertificate, note from history_certificate';
    $result = mysqli_query($connect, $strSQL);
    $total_records = mysqli_num_rows($result);

    // Bước 3: Tìm limit và current_page
    $current_page = isset($_GET['page']) ? $_GET['page'] : 1;
    $limit = 10;

    // echo "<h2>".$current_page."</h2>";

    // Bước 4: tính toán total_page và start
    // tổng số trang
    $total_page = ceil($total_records / $limit);

    // giới hạn current_page trong khoảng 1 đến total_page
    if ($current_page > $total_page) {
        $current_page = $total_page;
    } else if ($current_page < 1) {
        $current_page = 1;
    }

    // tìm start
    $start = ($current_page - 1) * $limit;

    // Bước 5: truy cấn lấy danh sách sinh viên
    // có limit và start rồi thì truy vấn csdl lấy danh sách sinh viên
    $strSQL = "select name, dateOfBirth, hometown, class, modeOfStudy, registerNumber, 
    idCertificate, note from history_certificate limit " . $start .  ", " . $limit;
    $result = mysqli_query($connect, $strSQL);

    echo "<table>
<tr class='title-column'>
<th>Chọn</th>
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
    $start++;
    while ($row = mysqli_fetch_row($result)) {
        echo "<tr>";
        echo "<td><input type='checkbox' class='selectStudent' /></td>";
        echo "<td class='title-column'>" . $start . "</td>";
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

    echo "</table>";

    mysqli_close($connect);
    ?>

</body>

</html>