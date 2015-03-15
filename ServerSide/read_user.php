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
	
	$result = mysql_query("SELECT UserID,Username,Gender,Religion,PoliticalAffiliation,Birthday,Ethnicity FROM Users WHERE UserID='$userID'");
	
	if(mysql_num_rows($result)!=0){
		$result = mysql_fetch_assoc($result);
		
		echo json_encode($result);
	}else{
		$response["success"] = 0;
        $response["message"] = "User unsuccessfully queried!!";
		
		echo json_encode($response);
	}
}else if (isset($_GET["Username"])){
	$username = $_GET['Username'];
	
	require_once __DIR__ . '/db_connect.php';
	
	$db = new DB_CONNECT();
	
	$result = mysql_query("SELECT UserID,Username,Gender,Religion,PoliticalAffiliation,Birthday,Ethnicity FROM Users WHERE Username='$username'");
	
	if($result){
		$result = mysql_fetch_assoc($result);
		
		$response["success"] = 1;
		$response["message"] = json_encode($result);
		echo json_encode($response);
	}else{
		$response["success"] = 0;
        $response["message"] = "User unsuccessfully queried!!";
		
		echo json_encode($response);
	}
}else{
	$response["success"] = 0;
    $response["message"] = "No Username or UserID provided!";
		
	echo json_encode($response);
}
?>