<?php
mysql_connect("localhost","root","password");
mysql_select_db("test");

$q=mysql_query("SELECT * FROM story WHERE id=".$_REQUEST['id']);
while($e=mysql_fetch_assoc($q))
        $output[]=$e;

print(json_encode($output));

mysql_close();
?>

