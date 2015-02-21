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
	
	echo json_encode($result);
}else{
	$response["success"] = 0;
    $response["message"] = "Incorrect parameters passed in!";
		
	echo json_encode($response);
}
?>