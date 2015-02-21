<?php
//Turn off deprecated warnings
error_reporting(E_ALL ^ E_DEPRECATED);
 
//Create response array
$response = array();
 
//If the UserID was passed in... else if, Username passed in
if (isset($_GET["Follower"]) && isset($_GET["Leader"])){
	$follower = $_GET["Follower"];
	$leader = $_GET["Leader"];
	
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("SELECT * FROM FOLLOWING WHERE Follower='$follower' AND Leader='$leader'");
	
	if (!$result) {
    echo 'Could not run query: ' . mysql_error();
    exit;
	}
	
	if(mysql_num_rows($result)!=0){
		$response["success"] = 1;
		$response["message"] = "True";
		echo json_encode($response);
	}else{
		$response["success"] = 0;
        $response["message"] = "False";
		
		echo json_encode($response);
	}
}else if (isset($_GET["Username"])){
	$username = $_GET['Username'];
	
	require_once __DIR__ . '/db_connect.php';
	
	$db = new DB_CONNECT();
	
	$result = mysql_query("SELECT UserID,Username,Gender,Religion,PoliticalAffiliation,Birthday,Ethnicity FROM Users WHERE Username='$username'");
	
	if(mysql_num_rows($result)!=0){
		$result = mysql_fetch_assoc($result);
		
		echo json_encode($result);
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