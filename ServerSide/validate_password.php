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
	
	$result = mysql_query("SELECT UserID, Username, Password, Demographics FROM Users WHERE Username='$username'");
	
	if(mysql_num_rows($result)!=0){
		$result = mysql_fetch_assoc($result);
	}
		
	$comparison = strcmp($result["Password"],$password);
	
	if($comparison == 0){
		$filepath = $result["Demographics"];
		
		$dataInfo = file_get_contents("C:/PickIt/data/Demographics/".$filepath);
		$dataJson = json_decode($dataInfo, true);
		
		$result["Birthday"] = $dataJson["Birthday"];
		$result["Gender"] = $dataJson["Gender"];
		$result["Ethnicity"] = $dataJson["Ethnicity"];
		$result["Religion"] = $dataJson["Religion"];
		$result["Political Affiliation"] = $dataJson["Political Affiliation"];
	}else{
		$response["success"] = 0;
        $response["message"] = "User unsuccessfully queried!!";
		
		echo json_encode($response);
	}
	
	$result["Password"] = "";
	$result["Demographics"] = "";
	
	$response["success"] = 1;
	$response["message"] = json_encode($result);
		
	echo json_encode($response);
}else{
	$response["success"] = 0;
	$response["message"] = "Incorrect Username or Password provided!";
			
	echo json_encode($response);
}
?>