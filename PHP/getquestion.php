<?php
$servername = "127.0.0.1";
$username = "root";
$password = "";
$dbname = "quiz_app";



// Create connection
$connection = new mysqli($servername, $username, $password, $dbname);
// Check connection
$sql = "select * from question_table ORDER BY RAND() LIMIT 50";
$result = mysqli_query($connection, $sql) or die("Error in Selecting " . mysqli_error($connection));




$emparray = array();
    while($row =mysqli_fetch_assoc($result))
    {
        $emparray[] = $row;
    }
	
echo json_encode($emparray);	
mysqli_close($connection);	

?> 
