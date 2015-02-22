<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="refresh" content="15" />
  </head>
  <style>
    div {
    }
    div.col {
        margin-top: 50%;;
        margin-bottom: 0px;
        margin-right: 50px;
        margin-left: 50px;
    }
  </style>
  <body>
    <div>
      <div align=center class=col>
        <font size=36>
	<?php
	include 'get_quote.php';
	printQuote();

	?>
        </font>
      </div>
    </div>
  </body>
</html>
