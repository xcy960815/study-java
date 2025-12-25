#!/bin/bash

# iStoreOS SSH 信息
HOST="100.109.41.26"
USER="root"

echo "正在尝试连接 iStoreOS ($HOST)..."
echo "提示：请在下方提示时输入 SSH 密码。"

# 远程执行的指令
REMOTE_SCRIPT=$(cat << 'EOF'
# 1. 获取 Docker 网络子网
NETWORK_NAME="study-java-network"
SUBNET=$(docker network inspect $NETWORK_NAME -f '{{(index .IPAM.Config 0).Subnet}}' 2>/dev/null)

if [ -z "$SUBNET" ]; then
    echo "错误: 未找到 Docker 网络 $NETWORK_NAME，请确保容器已启动。"
    exit 1
fi

echo "检测到子网: $SUBNET"

# 2. 检查是否已经存在规则，避免重复写入
if grep -q "Docker Network Fix Start" /etc/firewall.user; then
    echo "配置已存在，正在更新子网信息..."
    # 删除旧规则块
    sed -i '/# --- Docker Network Fix Start ---/,/# --- Docker Network Fix End ---/d' /etc/firewall.user
fi

# 3. 写入新规则
cat >> /etc/firewall.user <<INNER_EOF

# --- Docker Network Fix Start ---
# 放行 Docker 网段的所有转发流量
iptables -I FORWARD 1 -s $SUBNET -j ACCEPT
# 为 Docker 网段开启 NAT (MASQUERADE)
iptables -t nat -I POSTROUTING 1 -s $SUBNET -j MASQUERADE
# --- Docker Network Fix End ---
INNER_EOF

echo "规则已写入 /etc/firewall.user"

# 4. 重启防火墙
echo "正在重启防火墙..."
service firewall restart
echo "修复完成！"
EOF
)

# 使用 ssh 登录并执行
ssh -o StrictHostKeyChecking=no $USER@$HOST "$REMOTE_SCRIPT"
