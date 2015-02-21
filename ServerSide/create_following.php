<?php
//Turn off deprecated warnings
error_reporting(E_ALL ^ E_DEPRECATED);
 
//Create response array
$response = array();

if (isset($_GET["Follower"]) && isset($_GET["Leader"])){
	$follower = $_GET["Follower"];
	$leader = $_GET["Leader"];
		
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("INSERT INTO FOLLOWING (Follower, Leader) VALUES ('$follower','$leader')");
	
	if (!$result) {
    echo 'Could not run query: ' . mysql_error();
    exit;
	}
	
	$response["success"] = 1;
    $response["message"] = "Query ran successfully";
	
	echo json_encode($response);
}else{
	$response["success"] = 0;
    $response["message"] = "Incorrect parameters passed in!";
		
	echo json_encode($response);
}
?>