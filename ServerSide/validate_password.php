<?php
//Turn off deprecated warnings
error_reporting(E_ALL ^ E_DEPRECATED);
 
//Create response array
$response = array();
 
//If the UserID was passed in...
if (isset($_GET["Username"]) && isset($_GET["Password"])){
	$username = $_GET["Username"];
	$password = $_GET["Password"];
	
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("SELECT Password FROM Users WHERE Username='$username'");
	
	if(mysql_num_rows($result)!=0){
		$result = mysql_fetch_assoc($result);
	}
		
	$comparison = strcmp($result["Password"],$password);
	
	if($comparison == 0){
		$response["success"] = 1;
		$response["message"] = "Verified password for user!";
		
		echo json_encode($response);
	}else{
		$response["success"] = 0;
		$response["message"] = "Incorrect Username or Password provided!";
			
		echo json_encode($response);
	}
}else{
	$response["success"] = 0;
    $response["message"] = "No Username or Password provided!";
		
	echo json_encode($response);
}
?>