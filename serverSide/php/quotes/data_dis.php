<!DOCTYPE html>
<html>
<body>
<?php

function printTable() {
	include 'con.php';
	$sql = "SELECT name, age FROM test_table";
	$result = $conn->query($sql);

	if ($result->num_rows > 0) {
	    // output data of each row
	    while($row = $result->fetch_assoc()) {
        	echo "id: " . $row["name"]. " - Name: " . $row["age"]. " " . "<br>";
	    }
	} else {
	    echo "0 results";
	}
	$conn->close();
}
printTable();

?>

</body>
</html>
