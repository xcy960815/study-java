# Role ID 字段集成测试 - 联表查询版本

## 已完成的修改

### 1. 数据库层面
- 创建了 `add_role_id_field.sql` 脚本来添加 `role_id` 字段
- 字段类型：BIGINT
- 位置：在 `id` 字段之后

### 2. 实体类层面
- `StudyJavaSysUserDao`: 添加了 `roleId` 字段
- `StudyJavaSysUserDto`: 添加了 `roleId` 字段  
- `StudyJavaSysUserVo`: 添加了 `roleId`、`roleName`、`roleCode` 字段

### 3. MyBatis映射层面
- `StudyJavaSysUserMapper.xml`: 
  - 创建了新的 `UserWithRoleResultMap` 来映射联表查询结果
  - 更新了 `getUserList` 查询为联表查询：`study_java_sys_user` LEFT JOIN `study_java_sys_role`
  - 更新了 `getUserInfo` 查询为联表查询
  - 在所有查询条件中添加了 `roleId` 支持
  - 在插入语句中添加了 `role_id` 字段
  - 在更新语句中添加了 `roleId` 字段更新

### 4. Service层面
- `StudyJavaSysUserServiceImpl`:
  - 在 `makeDto2Dao` 方法中添加了 `roleId` 映射
  - 简化了 `getUserList` 和 `getUserInfo` 方法，直接返回VO对象
  - 删除了不再使用的 `makeDaoToVo` 方法

### 5. Mapper接口层面
- `StudyJavaSysUserMapper`:
  - 更新了 `getUserList` 和 `getUserInfo` 方法的返回类型为 `StudyJavaSysUserVo`

## 联表查询说明

### 查询结构
```sql
SELECT 
    u.id, u.role_id, u.nick_name, u.login_name,
    u.password_md5, u.introduce_sign, u.address,
    u.is_deleted, u.locked_flag, u.create_time, u.avatar,
    r.role_name, r.role_code
FROM study_java_sys_user u
LEFT JOIN study_java_sys_role r ON u.role_id = r.id
WHERE u.is_deleted = 0
```

### 返回数据包含
- 用户信息：id, roleId, nickName, loginName, passwordMd5, introduceSign, address, isDeleted, lockedFlag, createTime, avatar
- 角色信息：roleName, roleCode

## 测试用例

### 1. 创建用户时包含角色ID
```json
POST /user/insertUserInfo
{
  "nickName": "测试用户",
  "loginName": "testuser",
  "passwordMd5": "password123",
  "introduceSign": "测试签名",
  "address": "测试地址",
  "roleId": 1
}
```

### 2. 根据角色ID查询用户（现在会返回角色信息）
```
GET /user/getUserList?roleId=1
```
返回结果示例：
```json
{
  "records": [
    {
      "id": 1,
      "roleId": 1,
      "roleName": "管理员",
      "roleCode": "ADMIN",
      "nickName": "测试用户",
      "loginName": "testuser",
      "introduceSign": "测试签名",
      "address": "测试地址",
      "createTime": "2024-01-01",
      "avatar": "data:image/...",
      "age": 25
    }
  ],
  "total": 1
}
```

### 3. 更新用户角色
```json
POST /user/updateUserInfo
{
  "userId": 1,
  "roleId": 2
}
```

### 4. 获取用户信息（包含角色信息）
```
GET /user/getUserInfo
```
返回结果示例：
```json
{
  "id": 1,
  "roleId": 1,
  "roleName": "管理员",
  "roleCode": "ADMIN",
  "nickName": "测试用户",
  "loginName": "testuser",
  "introduceSign": "测试签名",
  "address": "测试地址",
  "createTime": "2024-01-01",
  "avatar": "data:image/...",
  "age": 25
}
```

## 注意事项
1. 需要先执行 `add_role_id_field.sql` 脚本来添加数据库字段
2. 确保 `study_java_sys_role` 表中有对应的角色数据
3. 如果现有数据需要设置默认角色ID，可以执行：
   ```sql
   UPDATE study_java_sys_user SET role_id = 1 WHERE role_id IS NULL;
   ```
4. 联表查询使用LEFT JOIN，即使角色不存在也不会影响用户数据的查询 