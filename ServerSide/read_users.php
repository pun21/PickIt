<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_GET["UserID"])){
	$userID = $_GET['UserID'];
	
	$result = musql_query("SELECT userID,username,birthday,gender
		,ethnicity,religion,politicalAffiliation FROM Users");
	
	if (!empty($result)){
	
		$result = mysql_fetch_array($result);
		echo $result;
		$user = array();
		$user["userID"] = $result["userID"];
		$user["username"] = $result["username"];
		$user["birthday"] = $result["birthday"];
		$user["gender"] = $result["gender"];
		$user["ethnicity"] = $result["ethnicity"];
		$user["religion"] = $result["religion"];
		$user["politicalAffiliation"] = $result["politicalAffiliation"];
	}
}
else{
	echo "Fucked up";
}
?>