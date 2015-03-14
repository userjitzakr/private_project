<?php
mysql_connect("localhost","root","password");
mysql_select_db("test");

$q=mysql_query("SELECT id,title FROM story");
while($e=mysql_fetch_assoc($q))
        $output[]=$e;

print(json_encode($output));

mysql_close();
?>

