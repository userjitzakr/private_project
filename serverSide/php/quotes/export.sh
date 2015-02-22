#!/usr/bin/expect -f
spawn sudo cp con.php  data_dis.php  get_quote.php  quotes.php insert.php /var/www/html/php/

#######################
expect {
  -re ".*es.*o.*" {
    exp_send "yes\r"
    exp_continue
  }
  -re ".*sword.*" {
    exp_send "supermaN\r"
  }
}
interact
