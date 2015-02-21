<?php
//Turn off deprecated warnings
error_reporting(E_ALL ^ E_DEPRECATED);
 
//Create response array
$response = array();
 
//If the UserID was passed in...
if (isset($_GET["UserID"])){
	$userID = $_GET['UserID'];
	
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("SELECT Leader FROM Following WHERE Follower='$userID'");
	
	if (!$result) {
    echo 'Could not run query: ' . mysql_error();
    exit;
	}
	
	if(mysql_num_rows($result)!=0){
		$result = mysql_fetch_assoc($result);
		
		echo json_encode($result);
	}else{
		$response["success"] = 0;
        $response["message"] = "User is not following anyone";
		
		echo json_encode($response);
	}
}else{
	$response["success"] = 0;
    $response["message"] = "Incorrect input parameters provided";
		
	echo json_encode($response);
}
?>