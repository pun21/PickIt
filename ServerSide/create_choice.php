<?php
//Turn off deprecated warnings
error_reporting(E_ALL ^ E_DEPRECATED);
 
//Create response array
$response = array();

if (isset($_GET["PickItID"]) && isset($_GET["Slot"])){
			
	$pickitID = $_GET["PickItID"];
	$slot = $_GET["Slot"];
	$filepath = $pickitID . "_" . $slot;
		
	require_once __DIR__ . '/db_connect.php';
	$db = new DB_CONNECT();
	
	$result = mysql_query("INSERT INTO CHOICES (PickItID,Filepath) VALUES( '$pickitID','$filepath')");
	
	if (!$result) {
    echo 'Could not run query: ' . mysql_error();
	}
}
?>