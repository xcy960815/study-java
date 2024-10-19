记录自己使用java搭建一个后端的过程
    
### 10月13日

  - 升级了兼容jdk23版本的 spring boot的版本
  - 自己在controller层创建了两个接口，分别是root/getSuccess和root/getError
  - 在utils文件夹中创建了ResponseResult类和ResponseGenerator类
  - 在controller层中了解了 RequestMapping和 ResponseBody两个注解的用法

### 10月14日

 - 自己在resources文件夹中创建了多个环境的配置文件,如dev、pre、prod,以及主配置文件application.properties
 - 自己在controller层成功引用service层的实现类，并了解了@Resource注解的用法
 - 并给实现类添加接口，并了解了@Service注解在实现类中的用法
 - 最后就是在倒腾idea的语言配置，浪费了很多时间

### 10月16日

 - 将之前的HelloWordController类改名为StudyJavaUserController
 - 将之前的HelloWordService改名为StudyJavaUserService
 - 打通数据库的连接，并生成了对应的实体类

### 10月17日

 - 今天学习了在表类使用 TableField 注解 代表给这个类上添加一个字段，且不是这个表中的原生字段
 - 还有就是@JSONignore注解，用于往接口输出的时候忽略这个字段，但是仍可以在service层中使用
 - 在一个就是使用DTO的方式处理接口返回数据，这个相对比较灵活，可以生成自己想要的字段名字，以及想要哪些字段，缺点就是麻烦点

### 10月20日
 
- 用hashmap完成了接口的返回值，之前封装了一个util类型，结果不知道为啥service实现层没了一直导致报错，把这个类也删除了
- 已经将StudyJavaUser模块完成增删改查功能