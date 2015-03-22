<?php
//Turn off deprecated warnings
error_reporting(E_ALL ^ E_DEPRECATED);
 
//Create response array
$response = array();

if (isset($_GET["UserID"]) && isset($_GET["Category"]) && isset($_GET["SubjectHeader "])
	&& isset($_GET["Timestamp"]) && isset($_GET["Endtime"]) && isset($_GET["Legacy"])){
			
	$userID = $_GET["UserID"];
	$category = $_GET["Category"];
	$subject = $_GET["SubjectHeader "];
	$timestamp = $_GET["Timestamp"];
	$endtime = $_GET["Endtime"];
	$legacy = $_GET["Legacy"];
		
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("INSERT INTO PICKITS (UserID, Category, SubjectHeader, Timestamp, Endtime, Legacy ) VALUES('$userID', '$category', '$subject', '$timestamp', '$endtime', '$legacy')");
	
	if (!$result) {
		echo 'Could not run query: ' . mysql_error();
		exit;
	}
	
	$response["success"] = 1;
    $response["message"] = json_encode($result);
	
	echo json_encode($response);
}else{
	$response["success"] = 0;
    $response["message"] = "Incorrect parameters passed in!";
		
	echo json_encode($response);
}

function getPickItID($userID, $timestamp){	
	$query_result = mysql_query("SELECT PickItID FROM PICKITS WHERE UserID='$userID' AND Timestamp='$timestamp'");
	$query_response = array();
	
	if($query_result){
		$query_result = mysql_fetch_assoc($query_result);
		
		$query_response["success"] = 1;
		$query_response["message"] = json_encode($query_result);
		return json_encode($query_response);
	}else{
		$query_response["success"] = 0;
        $query_response["message"] = "User unsuccessfully queried!";
		return json_encode($query_response);
	}
}
?>