<?php

    include("./connect.php");
    $currentDate = getdate();

    $tableName = "backup_".$currentDate[year].$currentDate[mon].$currentDate[mday];
    $strSQLCreateTable = "CREATE TABLE if not exists " . $tableName. "(
        id int(11) NOT NULL auto_increment primary key,
        name varchar(256) DEFAULT NULL,
        dateOfBirth varchar(256) DEFAULT NULL,
        hometown varchar(256) DEFAULT NULL,
        class varchar(256) DEFAULT NULL,
        modeOfStudy varchar(256) DEFAULT NULL,
        registerNumber varchar(256) DEFAULT NULL,
        idCertificate varchar(256) DEFAULT NULL,
        note varchar(256) DEFAULT NULL
      ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
    $result = mysqli_query($connect, $strSQLCreateTable);
    echo "Kết quả tạo: " . $result ."<br />";


    $strSQLInsertData = "INSERT INTO  " . $tableName . " (name, dateOfBirth, hometown, class, modeOfStudy, 
    registerNumber, idCertificate, note)
    select name, dateOfBirth, hometown, class, modeOfStudy, registerNumber,idCertificate, note
    from history_certificate;";
    $result = mysqli_query($connect, $strSQLInsertData);
    echo "Kết quả thêm: " . $result;


    mysqli_close($connect);

?>