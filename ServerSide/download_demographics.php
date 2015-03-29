<?php
$file = basename($_GET['file']);
$directory = '/PickIt/data/Demographics/';
$filePath = $directory.$file;
if(!$file){ // file does not exist
	echo "there is no file";
    die('file not found');
} else {
	
	$filesize = filesize($filePath);
    header("Cache-Control: public");
    header("Content-Description: File Transfer");
    header("Content-Disposition: attachment; filename=$file");
    header("Content-Type: application/zip");
    header("Content-Transfer-Encoding: binary");
    header('FileSize:'.$filesize);

    // read the file from disk
    readfile($filePath);
}

?>
