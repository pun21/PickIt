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
	
	$result = mysql_query("SELECT UserID,Username,Demographics FROM Users WHERE UserID='$userID'");
	
	if(mysql_num_rows($result)!=0){
		$result = mysql_fetch_assoc($result);
		
		$filepath = $result["Demographics"];
		
		$dataInfo = file_get_contents("C:/PickIt/data/Demographics/".$filepath);
		$dataJson = json_decode($dataInfo, true);
		
		$result["Birthday"] = $dataJson["Birthday"];
		$result["Gender"] = $dataJson["Gender"];
		$result["Ethnicity"] = $dataJson["Ethnicity"];
		$result["Religion"] = $dataJson["Religion"];
		$result["Political Affiliation"] = $dataJson["Political Affiliation"];
		
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
	
	$result = mysql_query("SELECT UserID,Username,Demographics FROM Users WHERE Username='$username'");
	
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