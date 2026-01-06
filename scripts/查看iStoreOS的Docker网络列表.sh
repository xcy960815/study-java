#!/usr/bin/expect -f

set timeout 30
set host "100.109.41.26"
set user "root"

# 交互式获取密码
stty -echo
send_user "请输入 iStoreOS 密码: "
expect_user -re "(.*)\n"
set password $expect_out(1,string)
send_user "\n"
stty echo

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
