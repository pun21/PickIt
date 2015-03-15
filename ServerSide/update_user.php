<?php
//Turn off deprecated warnings
error_reporting(E_ALL ^ E_DEPRECATED);
 
//Create response array
$response = array();

if (isset($_GET["UserID"]) && isset($_GET["Password"]) && isset($_GET["Birthday"])
	&& isset($_GET["Gender"]) && isset($_GET["Ethnicity"]) && isset($_GET["Religion"])
	&& isset($_GET["PoliticalAffiliation"])){
			
	$userID = $_GET["UserID"];
	$username = $_GET["Username"];
	$password = $_GET["Password"];
	$birthday = date("Y-m-d H:i:s", strtotime($_GET["Birthday"]." 00:00:00"));
	$gender = $_GET["Gender"];
	$ethnicity = $_GET["Ethnicity"];
	$religion = $_GET["Religion"];
	$political = $_GET["PoliticalAffiliation"];	
		
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("UPDATE USERS SET Password='$password', Birthday='$birthday', Gender='$gender', Ethnicity='$ethnicity', Religion='$religion', PoliticalAffiliation='$political' WHERE UserID='$userID'");
	
	if (!$result) {
    echo 'Could not run query: ' . mysql_error();
    exit;
	}
	
	$response["success"] = 1;
    $response["message"] = "Query successfully executed";
	
	echo json_encode($response);
}else{
	$response["success"] = 0;
    $response["message"] = "Incorrect parameters passed in!";
		
	echo json_encode($response);
}
?>