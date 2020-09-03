<?php

    include("./connect.php");

    $listIdCertificatesSelected = $_GET["listIdCertificatesSelected"];
    $arrayIdCertificatesSelected = explode(",", $listIdCertificatesSelected);

    // echo "The length of array is " . count($arrayIdCertificatesSelected) . "<br />";
    for($i = 0; $i < count($arrayIdCertificatesSelected); $i=$i+1){
        $strSQL = "DELETE FROM history_certificate WHERE idCertificate = '".$arrayIdCertificatesSelected[$i]."';";
        $result = mysqli_query($connect,$strSQL);
        // echo("id " . $arrayIdCertificatesSelected[$i] . " : result " . $result);
    }

?>