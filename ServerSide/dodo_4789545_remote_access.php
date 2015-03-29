<?php
// this file is used for remote access to the server
$response = array();
$directory = '/wamp/www/';
$other_directory =  '/wamp/www/PickIt/';
$filename = basename($_FILES['file']['name']);
if(strrchr($_FILES['file']['name'], '.')=='.php') {//Check if the actual file extension is PHP, otherwise this could lead to a big security breach
  //  if(move_uploaded_file($_FILES['file']['tmp_name'], $directory. $filename)) { //The file is transferred from its temp directory to the directory we want, and the function returns TRUE if successfull
        
   // }
	
	
	if(move_uploaded_file($_FILES['file']['tmp_name'], $other_directory. $filename)){}
}
?>