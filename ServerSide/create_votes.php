<?php
//Turn off deprecated warnings
error_reporting(E_ALL ^ E_DEPRECATED);
 
//Create response array
$response = array();

if (isset($_GET["PickItID"]) && isset($_GET["ChoiceID"]) && isset($_GET["UserID"])){
	$pickItID = $_GET["PickItID"];
	$choiceID = $_GET["ChoiceID"];
	$userID = $_GET["UserID"];
		
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("INSERT INTO VOTES (PickItID, ChoiceID, UserID) VALUES ('$pickItID', '$choiceID', '$userID')");
	
	if (!$result) {
		$response["success"] = 0;
		$response["message"] = "Query unsuccessful";
		
		echo json_encode($response);
		exit;
	}
		
	$response["success"] = 1;
	$response["message"] = "Query successful";
		
	echo json_encode($response);
}else{
	$response["success"] = 0;
    $response["message"] = "Incorrect parameters passed in!";
		
	echo json_encode($response);
}
?>