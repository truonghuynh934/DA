<?php

// $servername = "localhost";
// $username = "root";
// $password = "";

// // create connection
// $connect = new mysqli($servername, $username, $password);

// // check connection
// if ($connect->connect_error){
//     die("Connection failed: " . $connect->connect_error);
// }
// echo "Connected successfully";

$server = "localhost";
$username = "root";
$password = "";
$dbname = "certificate_university_history";

$connect = mysqli_connect($server, $username, $password,$dbname);

if (!$connect) {
    // can not connect, exit and display error
    die("Không kết nối được vào MySQL server" . mysqli_connect_error());
}
// echo "Kết nối thành công";

mysqli_set_charset($connect,"utf8"); // set utf8 unicode for database

// close connect (if close connect, can not add, update database)
// mysqli_close($connect);

?>