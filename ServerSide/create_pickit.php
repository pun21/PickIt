<?php
//Turn off deprecated warnings
error_reporting(E_ALL ^ E_DEPRECATED);

$response = array();
if (isset($_GET["UserID"]) && isset($_GET["EndTime"])){
			
	$userID = $_GET["UserID"];
	$timestamp = date('Y-m-d h:i:s');
	$endTime = date('Y-m-d h:i:s', time() + $_GET["EndTime"]);
		
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("INSERT INTO PICKITS (UserID, Timestamp, EndTime) VALUES('$userID', '$timestamp', '$endTime')");
	
	if (!$result) {
		echo 'Could not run query: ' . mysql_error();
		exit;
	}
	
	$result = mysql_query("SELECT PickItID FROM PICKITS WHERE UserID='$userID' AND Timestamp='$timestamp'");
	
	if (!$result) {
		echo 'Could not get PickItID: ' . mysql_error();
	}
	
	$row = mysql_fetch_assoc($result);
	
	$pickItID = $row['PickItID'];
	
	$response["success"] = 1;
    $response["message"] = json_encode(array('PickItID' => $pickItID), JSON_FORCE_OBJECT);
		
	echo json_encode($response);
}else{
	$response["success"] = 0;
    $response["message"] = "Incorrect parameters passed in!";
		
	echo json_encode($response);
}
?>