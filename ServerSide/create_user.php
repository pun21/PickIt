<?php
//Turn off deprecated warnings
error_reporting(E_ALL ^ E_DEPRECATED);
 
//Create response array
$response = array();

if (isset($_GET["Username"]) && isset($_GET["Password"]) && isset($_GET["Birthday"])
	&& isset($_GET["Gender"]) && isset($_GET["Ethnicity"]) && isset($_GET["Religion"])
	&& isset($_GET["PoliticalAffiliation"])){
			
	$username = $_GET["Username"];
	$password = $_GET["Password"];
	$birthday = date("Y-m-d H:i:s", strtotime($_GET["Birthday"]." 00:00:00"));
	$gender = $_GET["Gender"];
	$ethnicity = $_GET["Ethnicity"];
	$religion = $_GET["Religion"];
	$political = $_GET["PoliticalAffiliation"];	
		
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("INSERT INTO USERS (Username, Password, Birthday, Gender, Ethnicity, Religion, PoliticalAffiliation) 
							VALUES('$username','$password','$birthday','$gender','$ethnicity','$religion','$political')");
	
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
	$query_result = mysql_query("SELECT UserID,Username,Gender,Religion,PoliticalAffiliation,Birthday,Ethnicity FROM Users WHERE Username='$username'");
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