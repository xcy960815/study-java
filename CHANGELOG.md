# 变更日志

## [未发布]

### 新增
- 添加了Docker配置、Redis配置、Ollama模块、大文件切片上传、AI模块功能开发等。

### 修改
- 在userController层添加必填项的校验
- 在adminUserController层添加必填项的校验

### 删除
- 删除了之前封装的util类型，导致service实现层报错

## [10月22日]

### 新增
- 完成adminUser模块接口的增删改查

### 修改
- 在userController层添加必填项的校验
- 在adminUserController层添加必填项的校验

## [10月20日]

### 新增
- 已经将StudyJavaUser模块完成增删改查功能

### 修改
- 用hashmap完成了接口的返回值

## [10月17日]

### 新增
- 学习了在表类使用 TableField 注解，代表给这个类上添加一个字段，且不是这个表中的原生字段
- 了解了@JSONignore注解，用于往接口输出的时候忽略这个字段，但是仍可以在service层中使用
- 使用DTO的方式处理接口返回数据，这个相对比较灵活，可以生成自己想要的字段名字，以及想要哪些字段，缺点就是麻烦点

## [10月16日]

### 新增
- 将之前的HelloWordController类改名为StudyJavaUserController
- 将之前的HelloWordService改名为StudyJavaUserService
- 打通数据库的连接，并生成了对应的实体类

## [10月14日]

### 新增
- 在resources文件夹中创建了多个环境的配置文件,如dev、pre、prod,以及主配置文件application.properties
- 在controller层成功引用service层的实现类，并了解了@Resource注解的用法
- 给实现类添加接口，并了解了@Service注解在实现类中的用法

## [10月13日]

### 新增
- 升级了兼容jdk23版本的 spring boot的版本
- 在controller层创建了两个接口，分别是root/getSuccess和root/getError
- 在utils文件夹中创建了ResponseResult类和ResponseGenerator类
- 在controller层中了解了 RequestMapping和 ResponseBody两个注解的用法

## [其他提交]

### 新增
- 添加了Docker配置、Redis配置、Ollama模块、大文件切片上传、AI模块功能开发等。
- 添加了用户信息模块、商品列表、全局错误捕获、表单验证、登录接口添加验证码等功能。

### 修改
- 修改了密码、杂项配置、动态配置、代码优化等。

### 删除
- 删除了JPA依赖、无用代码等。 