<?php

function printQuote() {
	include 'con.php';
	$sql = "SELECT COUNT(*) as total FROM quotes";
	$result = $conn->query("SELECT COUNT(*) as total FROM quotes");
	$data = $result->fetch_assoc();
	$id = rand(1, $data['total']);
	$sql = "SELECT quote FROM quotes where id=".$id;

	$result = $conn->query($sql);

	if ($result->num_rows > 0) {
	    // output data of each row
	    while($row = $result->fetch_assoc()) {
        	echo $row["quote"]. "<br>";
	    }
	}

	$result->close();
	$conn->close();
}
if (isset($_GET['new'])) {
    printQuote();
  }
?>

