<?php
$servername = "127.0.0.1";
$username = "root";
$password = "";
$dbname = "quiz_app";


$score = $_POST["score"];
$player = $_POST["player"];

// Create connection
$connection = new mysqli($servername, $username, $password, $dbname);
// Check connection
$sql = "INSERT INTO scoreboard (`score`,`player`) VALUES ('$score','$player') ";
$result = mysqli_query($connection, $sql) or die("Error in Selecting " . mysqli_error($connection));

mysqli_close($connection);	

?> 
