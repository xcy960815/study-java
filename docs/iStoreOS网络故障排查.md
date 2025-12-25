# iStoreOS (OpenWrt) Docker 网络故障排查与修复指南

## 问题背景

在 iStoreOS (OpenWrt) 环境下使用 Docker Compose 部署应用时，容器内无法连接外部网络（如 DeepSeek API），导致业务报错。

### 故障现象
1.  **业务报错**：调用外部 API 时返回 `400 Bad Request`（实际是 Java 捕获异常后转换的错误）。
2.  **网络测试**：
    *   容器内 `ping api.deepseek.com` 提示 `bad address`（DNS 解析失败）。
    *   容器内 `curl -v https://api.deepseek.com` 提示 `Connection refused`（连接被拒绝）。
3.  **宿主机状况**：宿主机本身可能也存在 DNS 解析问题，或者防火墙规则限制。

---

## 排查流程

### 1. 检查日志与权限
*   **现象**：应用启动报错 `Permission denied`，无法写入日志。
*   **原因**：Docker 挂载的宿主机目录权限不足，容器内用户（UID 1000）无权写入。
*   **修复**：
    *   在 dPanel/宿主机中将 `logs` 目录权限设置为 `777`。
    *   或者修改 `docker-compose.yml` 指定 `user: root`（不推荐）。

### 2. 检查 DNS 解析
*   **现象**：容器内 `ping` 域名提示 `bad address`。
*   **修复**：
    *   修改 `docker-compose.yml`，显式指定公共 DNS：
        ```yaml
        dns:
          - 223.5.5.5
          - 8.8.8.8
        ```
    *   或者修复 iStoreOS 宿主机的 WAN 口 DNS 设置。

### 3. 检查网络连接 (关键)
*   **现象**：DNS 解析成功（能获取 IP），但 `curl` 提示 `Connection refused`。
*   **原因**：iStoreOS (OpenWrt) 的防火墙默认拦截了 Docker 自定义 Bridge 网络的流量，且未开启 NAT (Masquerade)。
*   **验证命令**：
    ```bash
    # 在容器内执行
    curl -v https://api.deepseek.com
    ```

---

## 最终解决方案

通过在 iStoreOS 宿主机添加 `iptables` 规则，放行 Docker 网段流量并开启 NAT。

### 步骤 1：获取 Docker 网络信息
在宿主机终端执行：
```bash
docker network ls
# 找到对应的网络名称，例如 study-java-network

docker network inspect study-java-network
# 记录 Subnet (例如 192.168.32.0/20)
# 记录 Id 前12位对应的网桥名称 (例如 br-7ba44b0e2de0)
```

### 步骤 2：添加防火墙规则 (持久化)
将以下脚本写入 `/etc/firewall.user`，确保重启后生效。

```bash
# 1. 定义变量 (请根据实际情况修改 SUBNET)
SUBNET="192.168.32.0/20"

# 2. 写入规则
cat >> /etc/firewall.user <<EOF

# --- Docker Network Fix Start ---
# 放行 Docker 网段的所有转发流量
iptables -I FORWARD 1 -s $SUBNET -j ACCEPT

# 为 Docker 网段开启 NAT (MASQUERADE)
iptables -t nat -I POSTROUTING 1 -s $SUBNET -j MASQUERADE
# --- Docker Network Fix End ---
EOF

# 3. 重启防火墙
service firewall restart
```

### 步骤 3：验证修复
再次进入容器测试：
```bash
curl -v https://api.deepseek.com
```
应返回 `Connected to api.deepseek.com` 及 HTTP 响应（如 401 Unauthorized，说明网络已通）。

---

## 附录：常用排查命令

```bash
# 查看容器日志
docker logs -f <container_name> --tail 100

# 查看防火墙规则
iptables -L FORWARD -n --line-numbers
iptables -t nat -L POSTROUTING -n --line-numbers

# 容器内网络测试
docker exec -it <container_name> /bin/sh
ping -c 4 223.5.5.5
nslookup api.deepseek.com
curl -v https://api.deepseek.com
```
