<?php
//Turn off deprecated warnings
error_reporting(E_ALL ^ E_DEPRECATED);
 
//Create response array
$response = array();

if (isset($_GET["Username"]) && isset($_GET["Password"])){
			
	$username = $_GET["Username"];
	$password = $_GET["Password"];
	$demographics = $username."_demographics.json";
		
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("INSERT INTO USERS (Username, Password, Demographics) VALUES('$username','$password','$demographics')");
	
	if (!$result) {
    echo 'Could not run query: ' . mysql_error();
    exit;
	}
	
	$result = getUserByUsername($username);
	
	echo $result;
}else{
	$response["success"] = 0;
    $response["message"] = "Incorrect parameters passed in!";
		
	echo json_encode($response);
}

function getUserByUsername($username){	
	$query_result = mysql_query("SELECT UserID FROM Users WHERE Username='$username'");
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