#!/usr/bin/expect -f
spawn sudo cp con.php insert.php /var/www/html/php/mobiwiki/

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
