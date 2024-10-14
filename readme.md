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