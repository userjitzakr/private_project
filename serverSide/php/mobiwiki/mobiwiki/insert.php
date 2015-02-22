<!DOCTYPE HTML> 
<html>
<head>
<style>
.error {color: #FF0000;}
</style>
</head>
<body> 

<?php
// define variables and set to empty values
$quoteErr = "";
$quote = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
   if (empty($_POST["quote"])) {
     $quoteErr = "Name is required";
   } else {
     $quote = test_input($_POST["quote"]);
     // check if quote only contains letters and whitespace
     if (!preg_match("/^[a-zA-Z ,.']*$/",$quote)) {
       $quoteErr = "Only letters and white space allowed"; 
     }
   }
}

function test_input($data) {
   $data = trim($data);
   $data = stripslashes($data);
   $data = htmlspecialchars($data);
   return $data;
}
?>

<h2>ADD NEW QUOTE</h2>
<form method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>"> 
   QUOTE: <input type="text" name="quote" value="<?php echo $quote;?>">
   <span class="error">* <?php echo $quoteErr;?></span>
   <br><br>
   <br><br>
   <input type="submit" name="submit" value="Submit"> 
</form>

<?php
echo "<h2>Your Input:</h2>";
echo $quote;
echo "<br>";
include 'con.php';
        $sql = "INSERT INTO quotes (quote,visited,category) VALUES ("."\"".$quote. "\"".",0,0)";
	$quote = "";
        $result = $conn->query($sql);
	$conn->close();
	$result->close();


?>

</body>
</html>
