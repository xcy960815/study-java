#!/usr/bin/expect -f

set timeout 30
set host "100.109.41.26"
set user "root"
set password "xuchongyu87v5"

spawn ssh -o StrictHostKeyChecking=no $user@$host

expect {
    "password:" {
        send "$password\r"
    }
}

expect "# "
send "docker network ls\r"
send "exit\r"

expect eof
