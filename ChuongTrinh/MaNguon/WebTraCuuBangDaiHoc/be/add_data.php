<?php
    include("./connect.php");

    $studentPassed = $_GET["student_passed"];

    // translate "and" to "&"
    $studentPassed = str_replace("and","&",$studentPassed);

    $arrayStudentPassed = explode(";", $studentPassed);

    // insert data
    $strSQL = "INSERT INTO history_certificate (name, dateOfBirth, hometown, class, 
        modeOfStudy, registerNumber, idCertificate, note) 
        VALUES ('" . $arrayStudentPassed[0] . "','" . $arrayStudentPassed[1] . "','" .
        $arrayStudentPassed[2] . "','" . $arrayStudentPassed[3] . "','" . $arrayStudentPassed[4] . "','" . 
        $arrayStudentPassed[5] . "','" . $arrayStudentPassed[6] . "','" . $arrayStudentPassed[7] . "');";
    
    $result = mysqli_query($connect, $strSQL);
        // or die("<p>Không thể thực thi câu truy vấn thêm</p>");
        // comment dòng trên để nếu insert không được vì trùng khóa chính thì chương trình vẫn chạy

    // TEST
    // echo "3: " . gettype($arrayStudentPassed[3]) . ", 0: " . gettype($arrayStudentPassed[0])."\n";
    // echo "student_passed: " . $studentPassed . "\n";
    // for($i=0;$i<count($arrayStudentPassed);$i++){
    //     echo $arrayStudentPassed[$i] ."\n";
    // }
    //
    // echo "<h2>Số dòng add ảnh hưởng: result = " . $result . "</h2>";

    echo $result;

    mysqli_close($connect);
?>