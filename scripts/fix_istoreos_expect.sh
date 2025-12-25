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
# 使用正确的网络名称
send "NETWORK_NAME=\"study-java-compose_study-java-network\"\r"
send "SUBNET=\$(docker network inspect \$NETWORK_NAME -f '{{(index .IPAM.Config 0).Subnet}}' 2>/dev/null)\r"
send "if \[ -z \"\$SUBNET\" \]; then echo \"ERROR: Network not found\"; exit 1; fi\r"
send "echo \"Detected Subnet: \$SUBNET\"\r"
send "sed -i '/# --- Docker Network Fix Start ---/,/# --- Docker Network Fix End ---/d' /etc/firewall.user\r"
send "cat >> /etc/firewall.user <<EOF\r"
send "\r"
send "# --- Docker Network Fix Start ---\r"
send "iptables -I FORWARD 1 -s \$SUBNET -j ACCEPT\r"
send "iptables -t nat -I POSTROUTING 1 -s \$SUBNET -j MASQUERADE\r"
send "# --- Docker Network Fix End ---\r"
send "EOF\r"
send "service firewall restart\r"
send "echo \"FIX_COMPLETED_SUCCESSFULLY\"\r"
send "exit\r"

expect eof
