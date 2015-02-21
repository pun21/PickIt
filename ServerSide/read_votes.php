<?php
//Turn off deprecated warnings
error_reporting(E_ALL ^ E_DEPRECATED);
 
$response = array();

if (isset($_GET["UserID"])){
	$userID = $_GET["UserID"];
	
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("SELECT VoteID, PickItID, ChoiceID, UserID, Timestamp FROM votes WHERE UserID='$userID'");
	
	if(mysql_num_rows($result)!=0){
		$result = mysql_fetch_assoc($result);
		
		echo json_encode($result);
	}else{
		$response["success"] = 0;
        $response["message"] = "No Votes for the given User";
		
		echo json_encode($response);
	}
}else if (isset($_GET["PickItID"])){
	$pickItID = $_GET["PickItID"];
	
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("SELECT VoteID, PickItID, ChoiceID, UserID, Timestamp FROM votes WHERE PickItID='$pickItID'");
	
	if(mysql_num_rows($result)!=0){
		$result = mysql_fetch_assoc($result);
		
		echo json_encode($result);
	}else{
		$response["success"] = 0;
        $response["message"] = "No Votes for the given PickIt";
		
		echo json_encode($response);
	}
}else if(isset($_GET["ChoiceID"])){
	$choiceID = $_GET["ChoiceID"];
	
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("SELECT VoteID, PickItID, ChoiceID, UserID, Timestamp FROM votes WHERE ChoiceID='$choiceID'");
	
	if(mysql_num_rows($result)!=0){
		$result = mysql_fetch_assoc($result);
		
		echo json_encode($result);
	}else{
		$response["success"] = 0;
        $response["message"] = "No Votes for the given PickIt";
		
		echo json_encode($response);
	}
}else{
	$response["success"] = 0;
    $response["message"] = "Incorrect input parameters";
		
	echo json_encode($response);
}
?>